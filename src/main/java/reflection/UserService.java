package reflection;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

public class UserService {

    private final String name;

    private static final String GET_NAME = "getName";
    private static final MethodType GET_NAME_TYPE = MethodType.methodType(String.class);

    public UserService(String name) {
        this.name = name;
    }

    public MethodHandle getNameHandle() {
        try {
            return MethodHandles.lookup().findVirtual(UserService.class, GET_NAME, GET_NAME_TYPE);
        }
        catch (ReflectiveOperationException ex) {
            throw new IllegalStateException("Can't find method '%s'".formatted(GET_NAME));
        }
    }

    public String getName() {
        return name;
    }
}
