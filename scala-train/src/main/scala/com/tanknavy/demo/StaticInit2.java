package com.tanknavy.demo;

/**
 * Author: Alex Cheng 6/14/2020 12:32 PM
 */
public class StaticInit2 {

    public static void main(String[] args) {
        Runnable r  = () ->{ //lambda表达式，因为Runnable接口中只有一个方法需要重写(此时要重写的方法名可以省略)
            System.out.println(Thread.currentThread().getName()+ "启动线程");
            DemoThread demoThread = new DemoThread(); //一个类的多个对象,static代码块只会被执行一次！
            System.out.println(Thread.currentThread().getName() + "停止线程");
        };

        System.out.println(r.getClass());
        r.run();//此时接口实现类中的run方法会被主线程main执行，

        //public Thread(Runnable target,String name), the object whose run method is invoked when this thread is started
        Thread t1 = new Thread(r,"线程1");
        Thread t2 = new Thread(r,"线程2");

        System.out.println(t1.getClass());

        t1.start();
        t2.start();

        System.out.println("---------------------");
        String a = new String("hello");
        a = "best";
        System.out.println("String a is: " + a);

    }
}

class DemoThread{ //静态方法只会被执行一次
    static {
        if(true){
            System.out.println(Thread.currentThread().getName() + "初始化当前类");
//            while(true){
//
//            }
        }
    }
}
