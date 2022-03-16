package com.shop.repository;

import com.shop.entity.ItemImg;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * 상품의 이미지 정보를 저장하는 Repository
 */

public interface ItemImgRepository extends JpaRepository<ItemImg, Long> {

    // 이미지가 잘 저장됐는지 테스트 코드를 작성하기 위한 메소드
    // 매개변수로 넘겨준 상품 id를 가지며, 상품 이미지 아이디의 오름차순으로 가져오는 쿼리 메소드
    List<ItemImg> findByItemIdOrderByIdAsc(Long itemId);
}
