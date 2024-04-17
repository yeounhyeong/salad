
/*
    설명 : 상품관리의 목록, 수정, 삭제, 조회로 이동하는 페이지 영역
    입력값 : /cart/list, /cart/insert, /cart/update, /cart/delete, /cart/detail
    출력값 : cart/list, cart/insert, cart/update, cart/detail
    작성일 : 24.02.22
    작성자 : 정아름
    수정사항 : 상품관리의 전체 목록 페이지는 list 처리 하기로 함
 */

package com.example.basic.Controller;

import com.example.basic.DTO.CartDTO;
import com.example.basic.DTO.CartItemDTO;
import com.example.basic.DTO.MemberDTO;
import com.example.basic.DTO.ProductDTO;
import com.example.basic.Service.CartService;
import com.example.basic.Service.MemberService;
import com.example.basic.Service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class CartController {
    //주입
    private final CartService cartService;
    private final MemberService memberService;
    private final ProductService productService;

    //전체 조회
    @GetMapping("/cart/list")
    public String listForm(@AuthenticationPrincipal User user, Model model,
                           RedirectAttributes redirectAttributes) throws NullPointerException {
        //로그인 정보로 회원 조회
        MemberDTO memberDTO = memberService.detail(user.getUsername());

        CartDTO cartDTO = cartService.cartDetail(memberDTO.getMemberId());
        //회원의 장바구니 조회
        List<CartItemDTO> cartItemDTO = cartService.cartList(cartDTO.getCartId());

        //회원 정보가 없으면 상품 목록 페이지로 이동
        if (memberDTO == null) {
            return "redirect:/product/list";
        }

        if (cartItemDTO.isEmpty()) {
            redirectAttributes.addFlashAttribute("successMessage",
                    "장바구니가 비어있습니다.");
        }

        model.addAttribute("user", memberDTO);
        model.addAttribute("list", cartItemDTO);

        return "cart/list";
    }

    //삽입
    //get -> post로 변경 fixme 03-19 --> 04-03 다시수정
    @PostMapping("/cart/insert")
    public String insertProc(@RequestParam(name = "productId") Integer productId, Integer quantity,
                             @AuthenticationPrincipal User user,
                             RedirectAttributes redirectAttributes) {
        try {
            // 상품 상세정보 조회
            ProductDTO productDTO = productService.productDetail(productId);

            // 상품이 존재하지 않는 경우 처리
            if (productDTO == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "상품을 찾을 수 없습니다.");
                return "redirect:/product/list";
            }

            // 유저 정보 조회
            String memberEmail = user.getUsername();
            MemberDTO memberDTO = memberService.detail(memberEmail);

            // 회원 정보가 없는 경우 처리
            if (memberDTO == null) {
                redirectAttributes.addFlashAttribute("errorMessage", "회원 정보를 찾을 수 없습니다.");
                return "redirect:/product/list";
            }

            // 장바구니에 상품 추가
            cartService.cartInsert(memberDTO.getMemberId(), productDTO, quantity);

            redirectAttributes.addFlashAttribute("successMessage", "장바구니에 상품이 추가되었습니다.");
            return "redirect:/cart/list";
        } catch (Exception e) {
            // 예외 처리 로직 추가
            redirectAttributes.addFlashAttribute("errorMessage", "상품을 추가하는 중에 오류가 발생했습니다.");
            return "redirect:/product/list";
        }
    }

    //수정
    @GetMapping("/cart/update")
    public String updateForm(Integer cartItemId, Model model) {

        CartItemDTO cartItemDTO = cartService.cartItemDetail(cartItemId);

        model.addAttribute("data", cartItemDTO);

        return "cart/update";
    }

    @PostMapping("/cart/update")
    public String updateProc(CartItemDTO cartItemDTO, Integer cartId,
                             RedirectAttributes redirectAttributes) {
        cartService.cartUpdate(cartItemDTO, cartId);

        redirectAttributes.addFlashAttribute("successMessage",
                "장바구니가 수정되었습니다.");

        return "redirect:/cart/list";
    }

    //삭제
    @GetMapping("/cart/delete")
    public String deleteProc(Integer cartItemId, RedirectAttributes redirectAttributes) {
        cartService.cartDelete(cartItemId);

        redirectAttributes.addFlashAttribute("successMessage",
                "장바구니 상품이 삭제되었습니다.");

        return "redirect:/cart/list";
    }
}
