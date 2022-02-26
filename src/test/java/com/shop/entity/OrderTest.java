package com.shop.entity;

import com.shop.constant.ItemSellStatus;
import com.shop.repository.ItemRepository;
import com.shop.repository.MemberRepository;
import com.shop.repository.OrderItemRepository;
import com.shop.repository.OrderRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;

import static org.junit.jupiter.api.Assertions.*;

@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
@SpringBootTest
class OrderTest {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    ItemRepository itemRepository;

    @PersistenceContext
    EntityManager em;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    OrderItemRepository orderItemRepository;

    private Item createItem() {
        Item item = Item.builder()
                .itemNm("테스트 상품")
                .price(10000)
                .itemDetail("상세 설명")
                .itemSellStatus(ItemSellStatus.SELL)
                .stockNumber(100)
                .build();
        return item;
    }

    private Order createOrder() {
        Order order = new Order();

        for (int i = 0; i < 3; i++) {
            Item item = createItem();
            itemRepository.save(item);

            OrderItem orderItem = OrderItem.builder()
                    .item(item)
                    .count(10)
                    .orderPrice(1000)
                    .order(order)
                    .build();
            order.getOrderItems().add(orderItem);
        }

        Member member = new Member();
        memberRepository.save(member);

        order.setMember(member);
        orderRepository.save(order);
        return order;
    }

    @Test
    @DisplayName("영속성 전이 테스트")
    void cascadeTest() {

        Order order = new Order();

        for (int i = 0; i < 3; i++) {
            Item item = this.createItem();
            // 바로 아래의 코드는 꼭 필요한 코드이다.
            // 왜 꼭 필요한지 생각해보기!
            itemRepository.save(item);

            OrderItem orderItem = OrderItem.builder()
                    .item(item)
                    .count(10)
                    .orderPrice(10000)
                    .order(order)
                    .build();
            order.getOrderItems().add(orderItem);
        }

        orderRepository.saveAndFlush(order);
        em.clear();

        Order savedOrder = orderRepository.findById(order.getId())
                .orElseThrow(EntityNotFoundException::new);
        Assertions.assertThat(savedOrder.getOrderItems().size()).isEqualTo(3);
    }


    @Test
    @DisplayName("고아객체 제거 테스트")
    void orphanRemovalTest() {
        Order order = this.createOrder();
        order.getOrderItems().remove(0);
        em.flush();
    }

    @Test
    @DisplayName("즉시로딩 - 지연로딩 쿼리확인 테스트")
    void lazyLoadingTest() {
        Order order = this.createOrder();
        Long orderItemId = order.getOrderItems().get(0).getId();
        em.flush();
        em.clear();

        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(EntityNotFoundException::new);
        System.out.println("Order class : " + orderItem.getOrder().getClass());
    }
}