package jpastudy.jpashop;

import org.junit.Test;

import java.util.List;

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

        List<String> stringList = List.of("aa","bb","cc");
        stringList.forEach(str -> System.out.println(str));
        //Method Reference - 람다식을 좀 더 간단하게
        stringList.forEach(System.out::println);
    }
}
