/**
 * Author: Alex Cheng 5/17/2021 9:31 PM
 */
public class InnerClassTest {

    public String name;
    public static  String category = "DCCP";

    public InnerClass innerClass = new InnerClass();
    public InnerStaticClass innerStaticClass = new InnerStaticClass();

    InnerClassTest(String name){
        this.name = name;
    }

    public class InnerClass{
        public void innerPrinter(){
            System.out.println("outer class instance field " + name + ", outer class static field " + category);
        }
    }

    public static class InnerStaticClass{
        public void innerPrinter(){
            System.out.println("outer class " + category);
        }
    }

    public static void main(String[] args) {

    }
}
