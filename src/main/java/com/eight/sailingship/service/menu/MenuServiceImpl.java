package com.eight.sailingship.service.menu;

import com.eight.sailingship.auth.user.UserDetailsImpl;
import com.eight.sailingship.dto.menu.MenuRequestDto;
import com.eight.sailingship.entity.Menu;
import com.eight.sailingship.entity.Store;
import com.eight.sailingship.repository.MenuRepository;
import com.eight.sailingship.repository.StoreRepository;
import com.eight.sailingship.util.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {

    private final MenuRepository menuRepository;
    private final StoreRepository storeRepository;

    @Autowired
    private S3Uploader s3Uploader;


    @Transactional
    @Override
    public long createMenu(MenuRequestDto requestDto, UserDetailsImpl userDetails, MultipartFile images) throws IllegalArgumentException, IOException {
        Store store = storeRepository.findByOwner_UserId(userDetails.getUser().getUserId())
                .orElseThrow(() -> new IllegalArgumentException("상점이 생성되어 있지 않습니다."));

        String storedFileName = s3Uploader.upload(images, "image");

        Menu menu = new Menu(requestDto, store, storedFileName);
        menuRepository.save(menu);
        return store.getStoreId();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Menu> listMenu(UserDetailsImpl userDetails, Long storeId) throws IllegalArgumentException{
        checkListMenu(userDetails, storeId);
        return menuRepository.findByStore_StoreId(userDetails.getUser().getUserId());
    }

    @Transactional
    @Override
    public Menu editMenu(Long menuId, UserDetailsImpl userDetails) throws IllegalArgumentException {
        Menu menu = menuRepository.findById(menuId).orElseThrow(() -> new IllegalArgumentException("유효하지 않은 메뉴 번호 입니다."));
        long menuOwnerId = menu.getStore().getStoreId();
        Store store = storeRepository.findByOwner_UserId(userDetails.getUser().getUserId())
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 상점 번호입니다."));
        if(store.getStoreId() != menuOwnerId) {
            throw new IllegalArgumentException("매장이 사장님의 소유가 아닙니다.");
        }
        return menu;
    }

    @Transactional
    @Override
    public long editSaveMenu(MenuRequestDto requestDto, Long id, MultipartFile images, UserDetailsImpl userDetails) throws IOException {
        Menu menu = menuRepository.findById(id).get();
        if (!images.isEmpty()) {
            String storedFileName = s3Uploader.upload(images, "image");
            menu.update(requestDto, storedFileName);
        } else {
            menu.update(requestDto);
        }
        menuRepository.save(menu);
        Store store = storeRepository.findByOwner_UserId(userDetails.getUser().getUserId()).orElseThrow(() -> new IllegalArgumentException("상점 번호가 유효하지 않습니다."));
        return store.getStoreId();
    }

    @Transactional
    @Override
    public ResponseEntity<String> deleteMenu(Long menuId, UserDetailsImpl userDetails) throws IllegalArgumentException {
        Menu menu = menuRepository.findById(menuId).orElseThrow(() -> new IllegalArgumentException("유효하지 않은 메뉴 번호 입니다."));
        Long menuOwnerId = menu.getStore().getStoreId();
        Store store = storeRepository.findByOwner_UserId(userDetails.getUser().getUserId())
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 상점 번호입니다."));
        if(store.getStoreId() != menuOwnerId) {
            throw new IllegalArgumentException("사장님의 매장이 아닙니다.");
        }
        menuRepository.delete(menu);
        return ResponseEntity.ok().body("삭제 되었습니다.");
    }

    @Transactional
    @Override
    public void checkListMenu(UserDetailsImpl userDetails, Long storeId) throws IllegalArgumentException {
        Store store = storeRepository.findByOwner_UserId(userDetails.getUser().getUserId()).orElseThrow(() -> new IllegalArgumentException("매장을 아직 생성하지 않았습니다."));
        if (store.getStoreId() != storeId) {
            throw new IllegalArgumentException("사장님의 매장이 아닙니다.");
        }

    }
}
