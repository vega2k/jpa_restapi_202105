package jpastudy.jpashop.api;

import jpastudy.jpashop.domain.*;
import jpastudy.jpashop.repository.OrderRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

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

    @Data
    static class OrderDto {
        private Long orderId;   //주문번호
        private String name;    //회원이름
        private LocalDateTime orderDate;    //주문날짜
        private OrderStatus orderStatus;    //주문현황
        private Address address;    //주소
        private List<OrderItemDto> orderItems;  //주문Item

        public OrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName();
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();
            orderItems = order.getOrderItems().stream() //Stream<OrderItem>
                    .map(orderItem -> new OrderItemDto(orderItem)) //Stream<OrderItemDto>
                    .collect(toList()); //List<OrderItemDto>
        }
    } //static class OrderDto

    @Data
    static class OrderItemDto {
        private String itemName; //상품명
        private int orderPrice; //주문 가격
        private int count; //주문 수량

        public OrderItemDto(OrderItem orderItem) {
            itemName = orderItem.getItem().getName();
            orderPrice = orderItem.getOrderPrice();
            count = orderItem.getCount();
        }
    } //static class OrderItemDt

}
