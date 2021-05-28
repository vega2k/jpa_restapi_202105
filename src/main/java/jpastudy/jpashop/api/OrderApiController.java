package jpastudy.jpashop.api;

import jpastudy.jpashop.domain.*;
import jpastudy.jpashop.repository.OrderRepository;
import jpastudy.jpashop.repository.order.query.OrderQueryDto;
import jpastudy.jpashop.repository.order.query.OrderQueryRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@RestController
@RequiredArgsConstructor
public class OrderApiController {
    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;
    
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

    /**
     * V2. 엔티티를 DTO로 변환해서 노출
     * 여러번 쿼리가 수행 성능이슈는 있음
     */
    @GetMapping("api/v2/orders")
    public List<OrderDto> ordersV2() {
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());
//        return orders.stream() //Stream<Order>
//                    .map(order -> new OrderDto(order)) //Stream<OrderDto>
//                    .collect(toList());  //List<OrderDto>
        return orders.stream().map(OrderDto::new).collect(toList());
    }

    /**
     * V3. 엔티티를 DTO로 변환하고, Fetch Join 으로 성능이슈 해결
     */
    @GetMapping("/api/v3/orders")
    public List<OrderDto> ordersV3() {
        List<Order> orders = orderRepository.findAllWithItems();
        return orders.stream()
                .map(order -> new OrderDto(order))
                .collect(toList());
    }

    /**
     * V3.1 엔티티를 DTO 변환, toOne관계는 Fetch Join으로 페이징처리
     * toMandy관계는 hibernate.defaut_batch_fetch_size: 1000 설정으로 페이징처리
     */
    @GetMapping("/api/v3.1/orders")
    public List<OrderDto> ordersV3_page(
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue= "100") int limit) {
        List<Order> orders = orderRepository.findAllWithMemberDelivery(offset,limit);
        List<OrderDto> result = orders.stream()
                .map(o -> new OrderDto(o))
                .collect(Collectors.toList());
        return result;
    }

    /**
     * V4 Query한 결과를 OrderQueryDto, OrderItemQueryDto 직접 저장
     */
    @GetMapping("/api/v4/orders")
    public List<OrderQueryDto> ordersV4() {
        return orderQueryRepository.findOrderQueryDtos();
    }

    /**
     * V5 Query한 결과를 OrderQueryDto, OrderItemQueryDto 직접 저장
     * Java Stream groupingby 기능 사용
     */
    @GetMapping("/api/v5/orders")
    public List<OrderQueryDto> ordersV5() {
        return orderQueryRepository.findAllByDto_optimization();
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
