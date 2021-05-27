package jpastudy.jpashop;

import jpastudy.jpashop.domain.*;
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

        order.setOrderDate(LocalDateTime.now());
        order.setStatus(OrderStatus.ORDER);
        em.persist(order);

    }
}
