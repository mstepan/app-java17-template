package ds;

public class ListMain {

    public static void main(String[] args) throws Exception {

        UnrolledLinkedList<Integer> list = new UnrolledLinkedList<>();
        for (int i = 0; i < 13; ++i) {
            list.add(i);
        }

        while (!list.isEmpty()) {
            System.out.println(list.dequee());
        }

//        for (int i = 0; i < 13; ++i) {
//            list.push(i);
//        }
//
//        while (!list.isEmpty()) {
//            System.out.printf("value: %d%n", list.pop());
//        }

        System.out.printf("list size: %d%n", list.size());

        System.out.printf("list: %s%n", list);

        System.out.println("UnrolledLinkedList done...");
    }

}

