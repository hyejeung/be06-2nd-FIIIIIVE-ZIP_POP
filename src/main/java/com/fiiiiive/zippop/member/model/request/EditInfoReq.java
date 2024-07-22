package com.fiiiiive.zippop.member.model.request;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EditInfoReq {
    private String name;
    private String crn; // 사업자 등록 번호(Company Registration Number
    private String phoneNumber;
    private String address;
}
