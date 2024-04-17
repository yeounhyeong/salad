
/*
    설명 : 구매후기 게시판의 목록, 수정, 삭제, 조회로 이동하는 페이지 영역
    입력값 : /review/list, /review/insert, /review/update, /review/delete, /review/detail
    출력값 : review/list, review/insert, review/update, review/detail
    작성일 : 24.02.21
    작성자 : 정아름
    수정사항 : 구매후기 게시판의 전체 목록 페이지는 page 처리 하기로 함
 */

package com.example.basic.Controller;

import com.example.basic.DTO.ReviewDTO;
import com.example.basic.DTO.ReviewcmtDTO;
import com.example.basic.Service.ReviewService;
import com.example.basic.Service.ReviewcmtService;
import com.example.basic.Util.PaginationUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ReviewController {
    //주입
    private final ReviewService reviewService;

    private final ReviewcmtService reviewcmtService;

    //전체 조회
    @GetMapping("/review/list")
    public String listForm(@PageableDefault(page = 1) Pageable pageable,
                           Model model) {
        //페이지처리
        Page<ReviewDTO> reviewDTOS = reviewService.reviewList(pageable);

        Map<String, Integer> page = PaginationUtil.Pagination(reviewDTOS);

        model.addAllAttributes(page);
        model.addAttribute("list", reviewDTOS);

        return "review/list";
    }

    //삽입
    @GetMapping("/review/insert")
    public String insertForm() {

        return "review/insert";
    }

    @PostMapping("/review/insert")
    public String insertProc(ReviewDTO reviewDTO, RedirectAttributes redirectAttributes) {
        reviewService.reviewInsert(reviewDTO);

        redirectAttributes.addFlashAttribute("successMessage",
                "게시글이 등록되었습니다.");

        return "redirect:/review/list";
    }

    //수정
    @GetMapping("/review/update")
    public String updateForm(Integer id, Model model) {
        ReviewDTO reviewDTO = reviewService.reviewDetail(id, "M");
        model.addAttribute("data", reviewDTO);

        return "review/update";
    }

    @PostMapping("/review/update")
    public String updateProc(ReviewDTO reviewDTO, RedirectAttributes redirectAttributes) {
        reviewService.reviewUpdate(reviewDTO);

        redirectAttributes.addFlashAttribute("successMessage",
                "게시글이 수정되었습니다.");

        return "redirect:/review/list";
    }

    //삭제
    @GetMapping("/review/delete")
    public String deleteProc(Integer id, RedirectAttributes redirectAttributes) {
        reviewService.reviewDelete(id);

        redirectAttributes.addFlashAttribute("successMessage",
                "게시글이 삭제되었습니다.");

        return "redirect:/review/list";
    }

    //개별 조회
    @GetMapping("/review/detail")
    public String detailProc(Integer id, Model model) {
        ReviewDTO reviewDTO = reviewService.reviewDetail(id, "R");
        List<ReviewcmtDTO> reviewcmtDTOS = reviewcmtService.reviewcmtlist(id);

        model.addAttribute("data", reviewDTO);
        model.addAttribute("list", reviewcmtDTOS);

        return "review/detail";
    }
    //확인해라!!
}
