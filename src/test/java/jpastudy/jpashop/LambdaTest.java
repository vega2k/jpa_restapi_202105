package jpastudy.jpashop;

import org.junit.Test;

public class LambdaTest {

    @Test
    public void lambda() {
        //Anonymous Inner 클래스로 Runnable를 구현
        Thread thread  = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("익명클래스");
            }
        });
        thread.start();

        //Lambda 로 Runnable 구현
        Thread thread2 = new Thread(() -> System.out.println("람다"));
        thread2.start();

    }
}
