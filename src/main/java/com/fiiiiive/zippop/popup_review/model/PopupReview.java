package com.fiiiiive.zippop.popup_review.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fiiiiive.zippop.popup_store.model.PopupStore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PopupReview {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long reviewIdx;

    private String reviewTitle;
    private String reviewContent;
    private Integer rating;
    private String reviewDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "storeIdx")
    @JsonBackReference
    private PopupStore popupStore;
    private String storeName;
}
