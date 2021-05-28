package jpastudy.jpashop.repository;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jpastudy.jpashop.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {
    private final EntityManager em;

    public void save(Order order) {
        em.persist(order);
    }

    public Order findOne(Long id) {
        return em.find(Order.class, id);
    }

    //Order, Member, Delivery, OrderItem, Item 를 FetchJoin
    public List<Order> findAllWithItems() {
        return em.createQuery(
                "select distinct o from Order o " +
                        "join fetch o.member m " +
                        "join fetch o.delivery d " +
                        "join fetch o.orderItems oi " +
                        "join fetch oi.item i", Order.class)
                .setFirstResult(1)
                .setMaxResults(100)
                .getResultList();
    }

    //Order, Member, Delivery를 Fetch Join
    public List<Order> findAllWithMemberDelivery() {
        return em.createQuery(
                "select o from Order o " +
                    "join fetch o.member m " +
                    "join fetch o.delivery d", Order.class)
                .getResultList();
    }

    //JPQL을 사용해서 동적쿼리 생성
    public List<Order> findAllByString(OrderSearch orderSearch) {
        String jpql = "select o From Order o join o.member m";
        boolean isFirstCondition = true;

        //주문 상태 검색
        if (orderSearch.getOrderStatus() != null) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else { jpql += " and"; }
            jpql += " o.status = :status";
        }
        //회원 이름 검색
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else { jpql += " and"; }
            jpql += " m.name like :name";
        }

        TypedQuery<Order> query = em.createQuery(jpql, Order.class).setMaxResults(1000);
        if (orderSearch.getOrderStatus() != null) {
            query = query.setParameter("status", orderSearch.getOrderStatus());
        }
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            query = query.setParameter("name", orderSearch.getMemberName());
        }
        return query.getResultList();
    }
    //querydsl을 사용한 동적쿼리 생성
    public List<Order> findAllByQuerydsl(OrderSearch orderSearch) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QOrder order = QOrder.order;
        QMember member = QMember.member;
        return queryFactory
                .select(order)
                .from(order)
                .join(order.member, member)
                .where(statusEq(orderSearch.getOrderStatus()),
                       nameLike(orderSearch.getMemberName()))
                .limit(1000)
                .fetch();
    }

    private BooleanExpression statusEq(OrderStatus statusCond) {
        if(statusCond == null) return null;
        return QOrder.order.status.eq(statusCond);
    }
    private BooleanExpression nameLike(String nameCond) {
        if(!StringUtils.hasText(nameCond)) return null;
        return QMember.member.name.like(nameCond);
    }

}