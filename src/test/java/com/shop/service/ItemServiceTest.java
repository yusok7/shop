package com.shop.service;

import com.shop.constant.ItemSellStatus;
import com.shop.dto.ItemFormDto;
import com.shop.entity.Item;
import com.shop.entity.ItemImg;
import com.shop.repository.ItemImgRepository;
import com.shop.repository.ItemRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
@SpringBootTest
class ItemServiceTest {

    @Autowired
    ItemService itemService;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    ItemImgRepository itemImgRepository;

    /**
     * MockMultipartFile의 활용
     * MockMultipartFile 클래스를 이용하여 가짜 MultipartFile 리스트를 만들어서 반환해주는 메소드
     */
    List<MultipartFile> createMultipartFiles() throws Exception {

        List<MultipartFile> multipartFileList = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            String path = "C:/shop/item/";
            String imageName = "image" + i + ".jpg";
            MockMultipartFile multipartFile = new MockMultipartFile(path, imageName,
                    "image/jpg", new byte[] {1, 2, 3, 4});
            multipartFileList.add(multipartFile);
        }

        return multipartFileList;
    }

    @Test
    @DisplayName("상품 등록 테스트")
    @WithMockUser(username = "admin", roles = "ADMIN")
    void saveItem() throws Exception {
        ItemFormDto itemFormDto = ItemFormDto.builder()
                .itemNm("테스트 상품")
                .itemSellStatus(ItemSellStatus.SELL)
                .itemDetail("테스트 상품 입니다.")
                .price(1000)
                .stockNumber(100)
                .build();

        List<MultipartFile> multipartFileList = this.createMultipartFiles();
        Long itemId = itemService.saveItem(itemFormDto, multipartFileList);

        List<ItemImg> itemImgList = itemImgRepository.findByItemIdOrderByIdAsc(itemId);
        Item item = itemRepository.findById(itemId)
                .orElseThrow(EntityNotFoundException::new);

        // 상품 관련 확인
        assertThat(itemFormDto.getItemNm()).isEqualTo(item.getItemNm());
        assertThat(itemFormDto.getItemSellStatus()).isEqualTo(item.getItemSellStatus());
        assertThat(itemFormDto.getItemDetail()).isEqualTo(item.getItemDetail());
        assertThat(itemFormDto.getPrice()).isEqualTo(item.getPrice());
        assertThat(itemFormDto.getStockNumber()).isEqualTo(item.getStockNumber());

        // 상품 이미지 관련 확인
        assertThat(multipartFileList.get(0).getOriginalFilename()).isEqualTo(itemImgList.get(0).getOriImgName());

    }
}