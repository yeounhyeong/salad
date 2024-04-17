
/*
    설명 : 고객센터 게시판의 목록, 수정, 삭제, 조회로 이동하는 페이지 영역
    입력값 : /board/list, /board/insert, /board/update, /board/delete, /board/detail
    출력값 : board/list, board/insert, board/update, board/detail
    작성일 : 24.02.21
    작성자 : 정아름
    수정사항 : 고객센터 게시판의 전체 목록 페이지는 page 처리 하기로 함
 */

package com.example.basic.Controller;

import com.example.basic.DTO.BoardDTO;
import com.example.basic.DTO.BoardcmtDTO;
import com.example.basic.Entity.BoardEntity;
import com.example.basic.Service.BoardService;
import com.example.basic.Service.BoardcmtService;
import com.example.basic.Util.PaginationUtil;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class BoardController {
    //주입
    private final BoardService boardService;

    private final BoardcmtService boardcmtService;

    //전체 조회
    @GetMapping("/board/list")
    public String listForm(@RequestParam(value = "type", defaultValue = "") String type,
                           @RequestParam(value = "keyword", defaultValue = "") String keyword,
                           @PageableDefault(page = 1) Pageable pageable,
                           Model model) {
        //검색조회, 페이지처리
        Page<BoardDTO> list = boardService.boardlist(type, keyword, pageable);

        Map<String, Integer> page = PaginationUtil.Pagination(list);

        model.addAllAttributes(page);
        model.addAttribute("list", list);

        return "board/list";
    }

    //삽입
    @GetMapping("/board/insert")
    public String insertForm() {

        return "board/insert";
    }

    @PostMapping("/board/insert")
    public String insertProc(BoardDTO boardDTO, RedirectAttributes redirectAttributes) {
        boardService.boardInsert(boardDTO);

        redirectAttributes.addFlashAttribute("successMessage",
                "게시글이 등록되었습니다.");

        return "redirect:/board/list";
    }

    //수정
    @GetMapping("/board/update")
    public String updateForm(Integer id, Model model) {
        BoardDTO boardDTO = boardService.boardDetail(id);
        model.addAttribute("data", boardDTO);

        return "board/update";
    }

    @PostMapping("/board/update")
    public String updateProc(BoardDTO boardDTO, RedirectAttributes redirectAttributes) {
        boardService.boardUpdate(boardDTO);

        redirectAttributes.addFlashAttribute("successMessage",
                "게시글이 수정되었습니다.");

        return "redirect:/board/list";
    }

    //삭제
    @GetMapping("/board/delete")
    public String deleteProc(Integer id, RedirectAttributes redirectAttributes) {
        boardService.boardDelete(id);

        redirectAttributes.addFlashAttribute("successMessage",
                "게시글이 삭제되었습니다.");

        return "redirect:/board/list";
    }

    //개별 조회
    @GetMapping("/board/detail")
    public String detailProc(Integer id, Model model, String password) {

        BoardDTO boardDTO = boardService.boardDetail(id);
        List<BoardcmtDTO> boardcmtDTOS = boardcmtService.boardcmtlist(id);

        //게시글 조회
        //비밀글 일 때
        if (boardDTO.isSecret()) {
            //입력받은 비밀번호와 저장된 비밀번호를 확인
            //비밀번호가 맞지 않을 때
            if (!password.equals(boardDTO.getBoardPassword())) {

                return "redirect:/board/list";
            } else {
                //비밀번호가 일치할 때. 비밀번호를 저장하고 페이지 이동
                boardDTO.setBoardPassword(password);
                model.addAttribute("data", boardDTO);
                model.addAttribute("list", boardcmtDTOS);
            }
        } else  {
            model.addAttribute("data", boardDTO);
            model.addAttribute("list", boardcmtDTOS);
        }

        return "board/detail";
    }
}
