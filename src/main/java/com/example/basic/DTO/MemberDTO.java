/*
작성자 : 정아름
작성일 : 24.02.19
작성내용 : 회원 구현
확인사항 : 테스트 해야함
 */

package com.example.basic.DTO;

import com.example.basic.Constant.RoleType;
import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.aspectj.bridge.Message;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberDTO {
    //회원 번호
    private Integer memberId;

    //회원 이메일
    @NotBlank (message = "이메일은 필수입니다.")
    private String memberEmail;

    //회원 비밀번호
    @NotBlank (message = "비밀번호는 필수입니다.")
    private String memberPassword;

    //회원 이름
    @NotBlank (message = "이름은 필수입니다.")
    private String memberName;

    //회원 전화번호
    private String memberPhone;

    //회원 주소
    @NotBlank (message = "주소는 필수입니다.")
    private String memberAddress;

    //회원 분류
    private RoleType roleType;

    //등록일
    private LocalDateTime regDate;

    //수정일
    private LocalDateTime modDate;
}
