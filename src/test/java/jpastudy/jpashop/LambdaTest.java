package jpastudy.jpashop;

import org.junit.Test;

import java.util.List;

import static java.util.stream.Collectors.toList;

public class LambdaTest {
    @Test
    public void stream() {
        //List<User> ->  List<String> 이름만 추출해서 반환해라 age > 20
        //List<User> ->Stream<User> -> Stream<String>-> List<String>
        List<User> userList = List.of(new User("aa",10),
                new User("bb",20),new User("cc",30));

        List<String> strList = userList.stream()  //Stream<User>
                .filter(user -> user.getAge() >= 20) //Stream<User>
                .map(user -> user.getName()) //Stream<String>
                .collect(toList());//List<String>
        //ctrl + alt + v, shift + alt + l (이클립스)
        strList.forEach(str -> System.out.println(str));
        //Method Reference
        strList.forEach(System.out::println);

    }

    static class User {
        String name;
        int age;

        public User(String name, int age) {
            this.name = name;
            this.age = age;
        }
        public String getName() {
            return name;
        }
        public int getAge() {
            return age;
        }
    }

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
