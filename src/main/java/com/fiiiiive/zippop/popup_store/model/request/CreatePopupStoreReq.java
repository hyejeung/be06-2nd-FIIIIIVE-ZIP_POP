package com.fiiiiive.zippop.popup_store.model.request;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreatePopupStoreReq {
    private String storeName;
    private String storeAddress;
    private String storeContent;
    private String storeEndDate;
    private String category;
}
