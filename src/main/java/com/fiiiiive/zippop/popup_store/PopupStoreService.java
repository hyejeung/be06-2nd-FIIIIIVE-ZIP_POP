package com.fiiiiive.zippop.popup_store;

import com.fiiiiive.zippop.common.exception.BaseException;
import com.fiiiiive.zippop.common.responses.BaseResponseMessage;
import com.fiiiiive.zippop.member.CompanyRepository;
import com.fiiiiive.zippop.member.model.Company;
import com.fiiiiive.zippop.member.model.CustomUserDetails;
import com.fiiiiive.zippop.popup_goods.model.PopupGoods;
import com.fiiiiive.zippop.popup_goods.model.response.GetPopupGoodsRes;
import com.fiiiiive.zippop.popup_review.model.PopupReview;
import com.fiiiiive.zippop.popup_review.model.response.GetPopupReviewRes;
import com.fiiiiive.zippop.popup_store.model.PopupStore;
import com.fiiiiive.zippop.popup_store.model.request.CreatePopupStoreReq;
import com.fiiiiive.zippop.popup_store.model.response.GetPopupStoreRes;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PopupStoreService {
    private final PopupStoreRepository popupStoreRepository;
    private final CompanyRepository companyRepository;

    public void register(CustomUserDetails customUserDetails, CreatePopupStoreReq createPopupStoreReq) throws BaseException {
        Optional<Company> company = companyRepository.findById(customUserDetails.getIdx());

        if (company.isPresent()) {
            PopupStore popupStore = PopupStore.builder()
                    .storeName(createPopupStoreReq.getStoreName())
                    .storeAddr(createPopupStoreReq.getStoreAddr())
                    .storeDate(createPopupStoreReq.getStoreDate())
                    .category(createPopupStoreReq.getCategory())
                    .totalPeople(createPopupStoreReq.getTotalPeople())
                    .company(company.get())
                    .build();
            System.out.println(popupStore);
            popupStoreRepository.save(popupStore);
        } else{
            throw new BaseException(BaseResponseMessage.POPUP_STORE_REGISTER_FAIL_DUPLICATION);
        }
    }
    public Page<GetPopupStoreRes> findAll(Pageable pageable) throws BaseException {
        Page<PopupStore> result = popupStoreRepository.findAll(pageable);
        if (result.hasContent()) {
            List<GetPopupStoreRes> getPopupStoreResList = result.getContent().stream().map(popupStore -> {
                GetPopupStoreRes getPopupStoreRes = GetPopupStoreRes.builder()
                        .storeName(popupStore.getStoreName())
                        .storeAddr(popupStore.getStoreAddr())
                        .storeDate(popupStore.getStoreDate())
                        .category(popupStore.getCategory())
                        .build();

                List<GetPopupGoodsRes> getPopupGoodsResList = popupStore.getPopupGoodsList().stream().map(popupGoods -> {
                    return GetPopupGoodsRes.builder()
                            .productIdx(popupGoods.getProductIdx())
                            .productName(popupGoods.getProductName())
                            .productPrice(popupGoods.getProductPrice())
                            .productContent(popupGoods.getProductContent())
                            .productImg(popupGoods.getProductImg())
                            .productAmount(popupGoods.getProductAmount())
                            .storeName(popupGoods.getStoreName())
                            .build();
                }).collect(Collectors.toList());
                getPopupStoreRes.setPopupGoodsList(getPopupGoodsResList);

                List<GetPopupReviewRes> reviewResList = popupStore.getReviews().stream().map(popupReview -> {
                    return GetPopupReviewRes.builder()
                            .reviewTitle(popupReview.getReviewTitle())
                            .reviewContent(popupReview.getReviewContent())
                            .rating(popupReview.getRating())
                            .reviewDate(popupReview.getReviewDate())
                            .storeName(popupReview.getStoreName())
                            .build();
                }).collect(Collectors.toList());
                getPopupStoreRes.setReviews(reviewResList);

                return getPopupStoreRes;
            }).collect(Collectors.toList());

            return new PageImpl<>(getPopupStoreResList, pageable, result.getTotalElements());
        } else {
            throw new BaseException(BaseResponseMessage.POPUP_STORE_SEARCH_FAIL_NOT_EXIST);
        }
    }

    public Page<GetPopupStoreRes> findByCategory(String category, Pageable pageable) throws BaseException {
        Page<PopupStore> result = popupStoreRepository.findByCategory(category, pageable);
        if (result.hasContent()) {
            List<GetPopupStoreRes> getPopupStoreResList = result.getContent().stream().map(popupStore -> {
                GetPopupStoreRes getPopupStoreRes = GetPopupStoreRes.builder()
                        .storeName(popupStore.getStoreName())
                        .storeAddr(popupStore.getStoreAddr())
                        .storeDate(popupStore.getStoreDate())
                        .category(popupStore.getCategory())
                        .build();

                List<GetPopupGoodsRes> getPopupGoodsResList = popupStore.getPopupGoodsList().stream().map(popupGoods -> {
                    return GetPopupGoodsRes.builder()
                            .productIdx(popupGoods.getProductIdx())
                            .productName(popupGoods.getProductName())
                            .productPrice(popupGoods.getProductPrice())
                            .productContent(popupGoods.getProductContent())
                            .productImg(popupGoods.getProductImg())
                            .productAmount(popupGoods.getProductAmount())
                            .storeName(popupGoods.getStoreName())
                            .build();
                }).collect(Collectors.toList());
                getPopupStoreRes.setPopupGoodsList(getPopupGoodsResList);

                List<GetPopupReviewRes> popupReviewResList = popupStore.getReviews().stream().map(popupReview -> {
                    return GetPopupReviewRes.builder()
                            .reviewTitle(popupReview.getReviewTitle())
                            .reviewContent(popupReview.getReviewContent())
                            .rating(popupReview.getRating())
                            .reviewDate(popupReview.getReviewDate())
                            .storeName(popupReview.getStoreName())
                            .build();
                }).collect(Collectors.toList());
                getPopupStoreRes.setReviews(popupReviewResList);

                return getPopupStoreRes;
            }).collect(Collectors.toList());
            result = popupStoreRepository.findByCategoryFetchJoin(category, pageable);
            return new PageImpl<>(getPopupStoreResList, pageable, result.getTotalElements());
        } else {
            throw new BaseException(BaseResponseMessage.POPUP_STORE_SEARCH_FAIL_NOT_EXIST);
        }
    }

    public GetPopupStoreRes findByStoreName(String storeName) throws BaseException{
        Long start = System.currentTimeMillis();
        Optional<PopupStore> result = popupStoreRepository.findByStoreName(storeName);
        Long end = System.currentTimeMillis();
        Long diff = end - start;
        if (result.isPresent()) {
            GetPopupStoreRes getPopupStoreRes = GetPopupStoreRes.builder()
                    .storeName(result.get().getStoreName())
                    .storeAddr(result.get().getStoreAddr())
                    .storeDate(result.get().getStoreDate())
                    .category(result.get().getCategory())
                    .build();
            List<GetPopupGoodsRes> getPopupGoodsResList = new ArrayList<>();
            for (PopupGoods popupGoods : result.get().getPopupGoodsList()) {
                GetPopupGoodsRes getPopupGoodsRes = GetPopupGoodsRes.builder()
                        .productIdx(popupGoods.getProductIdx())
                        .productName(popupGoods.getProductName())
                        .productPrice(popupGoods.getProductPrice())
                        .productContent(popupGoods.getProductContent())
                        .productImg(popupGoods.getProductImg())
                        .productAmount(popupGoods.getProductAmount())
                        .storeName(popupGoods.getStoreName())
                        .build();
                getPopupGoodsResList.add(getPopupGoodsRes);
            }
            getPopupStoreRes.setPopupGoodsList(getPopupGoodsResList);

            List<GetPopupReviewRes> reviewResList = new ArrayList<>();
            for (PopupReview review : result.get().getReviews()) {
                GetPopupReviewRes reviewRes = GetPopupReviewRes.builder()
                        .reviewTitle(review.getReviewTitle())
                        .reviewContent(review.getReviewContent())
                        .rating(review.getRating())
                        .reviewDate(review.getReviewDate())
                        .storeName(review.getPopupStore().getStoreName())
                        .build();
                reviewResList.add(reviewRes);
            }
            getPopupStoreRes.setReviews(reviewResList);
            result = popupStoreRepository.findByStoreNameFetchJoin(storeName);
            return getPopupStoreRes;
        } else {
            throw new BaseException(BaseResponseMessage.POPUP_STORE_SEARCH_FAIL_NOT_EXIST);
        }
    }

    public Page<GetPopupStoreRes> findByStoreAddr(String storeAddr, Pageable pageable) throws BaseException{
        Page<PopupStore> result = popupStoreRepository.findByCategory(storeAddr, pageable);
        if (result.hasContent()) {
            List<GetPopupStoreRes> getPopupStoreResList = result.getContent().stream().map(popupStore -> {
                GetPopupStoreRes getPopupStoreRes = GetPopupStoreRes.builder()
                        .storeName(popupStore.getStoreName())
                        .storeAddr(popupStore.getStoreAddr())
                        .storeDate(popupStore.getStoreDate())
                        .category(popupStore.getCategory())
                        .build();

                List<GetPopupGoodsRes> getPopupGoodsResList = popupStore.getPopupGoodsList().stream().map(popupGoods -> {
                    return GetPopupGoodsRes.builder()
                            .productIdx(popupGoods.getProductIdx())
                            .productName(popupGoods.getProductName())
                            .productPrice(popupGoods.getProductPrice())
                            .productContent(popupGoods.getProductContent())
                            .productImg(popupGoods.getProductImg())
                            .productAmount(popupGoods.getProductAmount())
                            .storeName(popupGoods.getStoreName())
                            .build();
                }).collect(Collectors.toList());
                getPopupStoreRes.setPopupGoodsList(getPopupGoodsResList);

                List<GetPopupReviewRes> popupReviewResList = popupStore.getReviews().stream().map(popupReview -> {
                    return GetPopupReviewRes.builder()
                            .reviewTitle(popupReview.getReviewTitle())
                            .reviewContent(popupReview.getReviewContent())
                            .rating(popupReview.getRating())
                            .reviewDate(popupReview.getReviewDate())
                            .storeName(popupReview.getStoreName())
                            .build();
                }).collect(Collectors.toList());
                getPopupStoreRes.setReviews(popupReviewResList);

                return getPopupStoreRes;
            }).collect(Collectors.toList());
            result = popupStoreRepository.findByStoreAddrFetchJoin(storeAddr, pageable);
            return new PageImpl<>(getPopupStoreResList, pageable, result.getTotalElements());
        } else {
            throw new BaseException(BaseResponseMessage.POPUP_STORE_SEARCH_FAIL_NOT_EXIST);
        }
    }

    public Page<GetPopupStoreRes> findByCompanyIdx(Long companyIdx, Pageable pageable) throws BaseException{
        Page<PopupStore> result = popupStoreRepository.findByCompanyCompanyIdx(companyIdx, pageable);
        if (result.hasContent()) {
            List<GetPopupStoreRes> getPopupStoreResList = result.getContent().stream().map(popupStore -> {
                GetPopupStoreRes getPopupStoreRes = GetPopupStoreRes.builder()
                        .storeName(popupStore.getStoreName())
                        .storeAddr(popupStore.getStoreAddr())
                        .storeDate(popupStore.getStoreDate())
                        .category(popupStore.getCategory())
                        .build();

                List<GetPopupGoodsRes> getPopupGoodsResList = popupStore.getPopupGoodsList().stream().map(popupGoods -> {
                    return GetPopupGoodsRes.builder()
                            .productIdx(popupGoods.getProductIdx())
                            .productName(popupGoods.getProductName())
                            .productPrice(popupGoods.getProductPrice())
                            .productContent(popupGoods.getProductContent())
                            .productImg(popupGoods.getProductImg())
                            .productAmount(popupGoods.getProductAmount())
                            .storeName(popupGoods.getStoreName())
                            .build();
                }).collect(Collectors.toList());
                getPopupStoreRes.setPopupGoodsList(getPopupGoodsResList);

                List<GetPopupReviewRes> popupReviewResList = popupStore.getReviews().stream().map(popupReview -> {
                    return GetPopupReviewRes.builder()
                            .reviewTitle(popupReview.getReviewTitle())
                            .reviewContent(popupReview.getReviewContent())
                            .rating(popupReview.getRating())
                            .reviewDate(popupReview.getReviewDate())
                            .storeName(popupReview.getStoreName())
                            .build();
                }).collect(Collectors.toList());
                getPopupStoreRes.setReviews(popupReviewResList);

                return getPopupStoreRes;
            }).collect(Collectors.toList());
            result = popupStoreRepository.findByCompanyIdxFetchJoin(companyIdx, pageable);
            return new PageImpl<>(getPopupStoreResList, pageable, result.getTotalElements());
        } else {
            throw new BaseException(BaseResponseMessage.POPUP_STORE_SEARCH_FAIL_NOT_EXIST);
        }
    }

    public Page<GetPopupStoreRes> findByStoreDate(String storeDate, Pageable pageable) throws BaseException{
        Page<PopupStore> result = popupStoreRepository.findByCategory(storeDate, pageable);
        if (result.hasContent()) {
            List<GetPopupStoreRes> getPopupStoreResList = result.getContent().stream().map(popupStore -> {
                GetPopupStoreRes getPopupStoreRes = GetPopupStoreRes.builder()
                        .storeName(popupStore.getStoreName())
                        .storeAddr(popupStore.getStoreAddr())
                        .storeDate(popupStore.getStoreDate())
                        .category(popupStore.getCategory())
                        .build();

                List<GetPopupGoodsRes> getPopupGoodsResList = popupStore.getPopupGoodsList().stream().map(popupGoods -> {
                    return GetPopupGoodsRes.builder()
                            .productIdx(popupGoods.getProductIdx())
                            .productName(popupGoods.getProductName())
                            .productPrice(popupGoods.getProductPrice())
                            .productContent(popupGoods.getProductContent())
                            .productImg(popupGoods.getProductImg())
                            .productAmount(popupGoods.getProductAmount())
                            .storeName(popupGoods.getStoreName())
                            .build();
                }).collect(Collectors.toList());
                getPopupStoreRes.setPopupGoodsList(getPopupGoodsResList);

                List<GetPopupReviewRes> popupReviewResList = popupStore.getReviews().stream().map(popupReview -> {
                    return GetPopupReviewRes.builder()
                            .reviewTitle(popupReview.getReviewTitle())
                            .reviewContent(popupReview.getReviewContent())
                            .rating(popupReview.getRating())
                            .reviewDate(popupReview.getReviewDate())
                            .storeName(popupReview.getStoreName())
                            .build();
                }).collect(Collectors.toList());
                getPopupStoreRes.setReviews(popupReviewResList);

                return getPopupStoreRes;
            }).collect(Collectors.toList());

            result = popupStoreRepository.findByStoreDateFetchJoin(storeDate, pageable);
            return new PageImpl<>(getPopupStoreResList, pageable, result.getTotalElements());
        } else {
            throw new BaseException(BaseResponseMessage.POPUP_STORE_SEARCH_FAIL_NOT_EXIST);
        }
    }
}