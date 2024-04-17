
/*
    설명 : 구매후기 게시판의 목록, 수정, 삭제, 조회로 이동하는 페이지 영역
    입력값 : /board/list, /board/insert, /board/update, /board/delete, /board/detail
    출력값 : board/list, board/insert, board/update, board/detail
    작성일 : 24.02.21
    작성자 : 정아름
    수정사항 : 구매후기 게시판의 전체 목록 페이지는 page 처리 하기로 함
 */

package com.example.basic.Controller;

import com.example.basic.DTO.BoardcmtDTO;
import com.example.basic.Service.BoardcmtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class BoardcmtController {
    //주입
    private final BoardcmtService boardcmtService;

    //삽입
    //댓글 상세보기 하단에 입력폼 존재 (페이지가 따로 존재 하지 않음)
    //게시글번호, 댓글작업 후 상세페이지로 다시출력
    @PostMapping("/boardcmt/insert")
    public String insertProc(Integer id, BoardcmtDTO boardcmtDTO,
                             RedirectAttributes redirectAttributes) {
        boardcmtService.boardcmtInsert(boardcmtDTO, id);

        redirectAttributes.addAttribute("id", id);
        redirectAttributes.addFlashAttribute("successMessage",
                "댓글이 등록되었습니다.");

        return "redirect:/board/detail";
    }

    //수정
    @GetMapping("/boardcmt/update")
    public String updateForm(Integer id, Model model, RedirectAttributes redirectAttributes) {
        BoardcmtDTO boardcmtDTO = boardcmtService.boardcmtDetail(id);

        redirectAttributes.addAttribute("id", id);
        model.addAttribute("data", boardcmtDTO);

        return "boardcmt/update";
    }


    @PostMapping("/boardcmt/update")
    public String updateProc(Integer id, BoardcmtDTO boardcmtDTO,
                             RedirectAttributes redirectAttributes) {
        boardcmtService.boardcmtUpdate(boardcmtDTO, id);

        redirectAttributes.addAttribute("id", id);
        redirectAttributes.addFlashAttribute("successMessage",
                "댓글이 수정되었습니다.");

        return "redirect:/board/detail";
    }

    //삭제
    @GetMapping("/boardcmt/delete")
    public String deleteProc(Integer id, RedirectAttributes redirectAttributes) {
        boardcmtService.boardcmtDelete(id);

        redirectAttributes.addAttribute("id", id);
        redirectAttributes.addFlashAttribute("successMessage",
                "댓글이 삭제되었습니다.");

        return "redirect:/board/detail";
    }
}
