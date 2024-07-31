package com.fiiiiive.zippop.popup_store;

import com.fiiiiive.zippop.popup_store.model.PopupStoreImage;
import com.fiiiiive.zippop.post.model.PostImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PopupStoreImageRepository extends JpaRepository<PopupStoreImage, Long> {
    @Query("SELECT psi FROM PopupStoreImage psi WHERE psi.popupStore.storeIdx = :storeIdx")
    Optional<List<PopupStoreImage>> findByStoreIdx(Long storeIdx);
}
