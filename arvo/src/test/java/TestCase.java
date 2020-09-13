import com.tanknavy.avro.User;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Author: Alex Cheng 9/4/2020 12:43 PM
 */


public class TestCase {

    @Test
    public void testUser() {
        User user1 = new User();
        user1.setName("Alyssa");
        user1.setTitle("Sr Director");
        user1.setDisplayName("Bob Cheng");
        // Leave favorite color null

        // Alternate constructor
        User user2 = new User("Ben", "CTO","Ben Cheng");

        // Construct via builder
        User user3 = User.newBuilder()
                .setName("Charlie")
                .setTitle("blue")
                .setDisplayName("Charlie Cheng")
                .build();
    }

    @Test
    public void testRefer() {
        int a = 2020;
        int b = a;
        //System.out.println(a + ":" + b);//相等
        a = 20;
        System.out.println("number type:" +a + ":" + b);//基本类型，不相等

        List<String> ar1 = new ArrayList<>();
        ar1.add("hello");
        List<String> ar2 = ar1;
        ar1.set(0, "world");
        System.out.println(ar1.toString() + ":" + ar2.toString());//引用类型，相等

        String c = "hello";
        String d = c;
        String cc = "hello";
        System.out.println(c == cc);
        System.out.println(c == d);
        System.out.println("string eq with ==:" + Boolean.toString(c == cc));//相等
        System.out.println("string eq with ==:" + Boolean.toString(c == d));//相等
        System.out.println("string eq with equals: " + c.equals(d));//相等
        c = "java"; //注意：字符串是引用类型，但是这里是按照值引用
        System.out.println(c == d);
        System.out.println("string type:" + c + ":" + d);//字符串基本类型，不相等

        int[] aa = {1,2,3};
        int[] bb = aa;
        //System.out.println(aa.getClass().getSimpleName()); //数组类型
        System.out.println(Arrays.toString(aa) + ":" +Arrays.toString(bb));//相等
        aa[1] = 11;
        //引用类型,相等
        System.out.println(Arrays.toString(aa) + ":" +Arrays.toString(bb));
    }

}
