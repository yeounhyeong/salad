/*
작성자 : 정아름
작성일 : 24.02.19
작성내용 : 고객센터(1:1)게시판 댓글 구현
확인사항 : 테스트 해야함
 */

package com.example.basic.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BoardcmtDTO {
    //고객센터 댓글 번호
    private Integer boardcmtId;

    //고객센터 댓글 내용
    @NotBlank(message = "내용은 필수입니다.")
    private String boardcmtBody;

    //고객센터 댓글 작성자
    private String boardcmtWriter;

    //등록일
    private LocalDateTime regDate;

    //수정일
    private LocalDateTime modDate;

    //고객센터 게시판 번호
    private Integer boardId;

}
