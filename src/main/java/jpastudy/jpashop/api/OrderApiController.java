package jpastudy.jpashop.api;

import jpastudy.jpashop.domain.Order;
import jpastudy.jpashop.domain.OrderItem;
import jpastudy.jpashop.domain.OrderSearch;
import jpastudy.jpashop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class OrderApiController {
    private final OrderRepository orderRepository;

    /**
     * V1. 엔티티 직접 노출
     * 엔티티가 변하면 API 스펙이 변한다.
     * Hibernate5Module 모듈 등록, LAZY=null 처리
     * 양방향 관계 문제 발생 -> @JsonIgnore
     */
    @GetMapping("/api/v1/orders")
    public List<Order> ordersV1()  {
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        for (Order order : all) {
            order.getMember().getName(); //Lazy 강제초기화
            order.getDelivery().getAddress();
            List<OrderItem> orderItems = order.getOrderItems();
            orderItems.stream().forEach(orderItem -> orderItem.getItem().getName());
        }
        return all;
    }


}
