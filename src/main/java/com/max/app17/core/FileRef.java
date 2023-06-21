package com.max.app17.core;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.nio.file.Path;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

/**
 * PhantomReference usage as an alternative finalizer.
 */
public class FileRef extends PhantomReference<File> implements AutoCloseable {

    private static final ReferenceQueue<? super File> REF_QUEUE = new ReferenceQueue<>();

    static {
        Thread th = new Thread(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    FileRef ref = (FileRef) REF_QUEUE.remove(1000L);
                    if (ref != null) {
                        ref.close();
                    }
                }
                catch (InterruptedException interEx) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        th.setDaemon(true);
        th.start();
    }

    private final RandomAccessFile randFile;
    private final Path filePath;

    public FileRef(File file) {
        super(makeReferenceCopy(Objects.requireNonNull(file, "null 'file' reference detected")), REF_QUEUE);
        this.filePath = file.toPath();

        try {
            this.randFile = new RandomAccessFile(file, "rw");
        }
        catch (FileNotFoundException fileNotFoundEx) {
            throw new ExceptionInInitializerError(fileNotFoundEx);
        }
    }

    private static File makeReferenceCopy(File file) {
        return file.toPath().toFile();
    }

    @Override
    public void close() {
        try {
            System.out.printf("Closing random access file: %s\n", filePath.toFile());
            randFile.close();
        }
        catch (IOException ioEx) {
            throw new IllegalStateException(ioEx);
        }
    }

    @SuppressWarnings("all")
    public static void main(String[] args) throws Exception {

        File file = new File("/Users/mstepan/repo/app-java17-template/src/main/resources/test.txt");

        FileRef fileRef1 = new FileRef(file);
        FileRef fileRef2 = new FileRef(new File("/Users/mstepan/repo/app-java17-template/profile.sh"));

        System.gc();
        TimeUnit.SECONDS.sleep(1);

        System.out.println("FileRef main done...");
    }
}
