package jpastudy.jpashop;

import jpastudy.jpashop.domain.*;
import jpastudy.jpashop.domain.item.Book;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.time.LocalDateTime;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class EntityTest {
    @Autowired
    EntityManager em;

    @Test
    @Rollback(false)
    public void member_order_address_delivery() throws Exception {
        //Member생성
        Member member = new Member();
        member.setName("부트1");
        Address address = new Address("서울","강동","1234");
        member.setAddress(address);
        em.persist(member);

        //Order생성
        Order order = new Order();
        order.setMember(member);

        //Delivery 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());
        delivery.setStatus(DeliveryStatus.READY);
        //em.persist(delivery);
        order.setDelivery(delivery);

        //OrderItem 생성
        OrderItem orderItem = new OrderItem();
        orderItem.setOrderPrice(1000);
        orderItem.setCount(10);
        
        //Book 생성
        Book book = new Book();
        book.setStockQuantity(100);
        book.setPrice(1000);
        book.setName("자바");
        book.setAuthor("김저자");
        book.setIsbn("1234-5678");
        em.persist(book);

        orderItem.setItem(book);
        order.addOrderItem(orderItem);

        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.ORDER);
        em.persist(order);

    }
}
