package reflection;

public class ReflectionMain {


    public static void main(String[] args) throws Throwable {

        UserService service = new UserService("user-1");

        System.out.printf("res: %s\n", (String)service.getNameHandle().invokeExact(service));

//        System.out.println(service.getName());

        System.out.println("ReflectionMain done...");
    }
}
