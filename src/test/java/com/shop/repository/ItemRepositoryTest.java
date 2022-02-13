package com.shop.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shop.constant.ItemSellStatus;
import com.shop.entity.Item;
import com.shop.entity.QItem;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;

    @PersistenceContext
    EntityManager em;

    @BeforeEach
    void createItemList() {
        for (int i = 1; i <= 10; i++) {
            Item newItem = Item.builder()
                    .itemNm("테스트 상품" + i)
                    .price(10000 + i)
                    .itemDetail("테스트 상품 상세 설명" + i)
                    .itemSellStatus(ItemSellStatus.SELL)
                    .stockNumber(100 + i)
                    .build();

            itemRepository.save(newItem);
        }
    }

    @Test
    @DisplayName("상품 저장 테스트")
    void createItemTest() {

        // given
        Item item = Item.builder()
                .itemNm("테스트 상품")
                .price(10000)
                .itemDetail("테스트 상품 상세 설명")
                .itemSellStatus(ItemSellStatus.SELL)
                .stockNumber(100)
                .build();

        // when
        itemRepository.save(item);

        // then
        List<Item> items = itemRepository.findAll();
        Item savedItem = items.get(items.size() - 1);

        Assertions.assertThat(savedItem.getItemNm()).isEqualTo("테스트 상품");
    }

    @Test
    @DisplayName("상품명 조회 테스트")
    void findByItemNmTest() {
        List<Item> items = itemRepository.findByItemNm("테스트 상품1");
        for (Item item : items) {
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("@Query를 이용한 상품 조회 테스트")
    void findByItemDetailTest() {
        List<Item> items = itemRepository.findByItemDetail("테스트 상품 상세 설명");
        for (Item item : items) {
            System.out.println(item.toString());
        }
    }

    @Test
    @DisplayName("Querydsl 조회 테스트1")
    void queryDslTest() {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QItem qItem = QItem.item;
        JPAQuery<Item> query = queryFactory.selectFrom(qItem)
                .where(qItem.itemSellStatus.eq(ItemSellStatus.SELL))
                .where(qItem.itemDetail.like("%" + "테스트 상품 상세 설명" + "%"))
                .orderBy(qItem.price.desc());

        List<Item> itemList = query.fetch();

        for (Item item : itemList) {
            System.out.println(item.toString());
        }
    }

}