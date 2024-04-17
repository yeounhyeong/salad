
/*
    설명 : 상품관리의 목록, 수정, 삭제, 조회로 이동하는 페이지 영역
    입력값 : /product/list, /product/insert, /product/update, /product/delete, /product/detail
    출력값 : product/list, product/insert, product/update, product/detail
    작성일 : 24.02.22
    작성자 : 정아름
    수정사항 : 상품관리의 전체 목록 페이지는 list 처리 하기로 함
 */

package com.example.basic.Controller;

import com.example.basic.DTO.ProductDTO;
import com.example.basic.Service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ProductController {
    //주입
    private final ProductService productService;

    //카테고리 별 정렬 추가
    //전체 조회 fixme 04-03
    @GetMapping(value = {"/product/list", "/salad/list", "/product/admin"})
    public String listForm(@RequestParam(value = "keyword", defaultValue = "") String keyword,
                           @RequestParam(value = "type", defaultValue = "") String type,
                           Model model) throws Exception{

        List<ProductDTO> productDTOS = productService.productList();

        model.addAttribute("productDTOS", productDTOS);

        return "product/list";
    }

    //삽입 fixme 04-03
    @GetMapping("/product/insert")
    public String insertForm(Model model, RedirectAttributes redirectAttributes)throws Exception {

        //검증처리시 빈 DTO를 전달
        ProductDTO productDTO = new ProductDTO();
        model.addAttribute("productDTO", productDTO);

        return "product/insert";
    }

    @PostMapping("/product/insert")
    public String insertProc(@Valid ProductDTO productDTO,
                             MultipartFile imgFile,
                             BindingResult bindingResult, Model model) throws Exception {
        //상품 검증
        if(bindingResult.hasErrors()) {
            //오류가 있으면 입력페이지로 다시 이동
            return "redirect:/product/insert";
        }

        productService.productInsert(productDTO, imgFile); //신규등록
        return "redirect:/product/list";
    }


    //수정 fixme 04-03
    @GetMapping("/product/update")
    public String updateForm(Integer id,
                             Model model,
                             RedirectAttributes redirectAttributes) throws Exception {
        ProductDTO productDTO = productService.productDetail(id); //1개조회
        model.addAttribute("productDTO", productDTO);  //값전달

        return "product/update";
    }

    @PostMapping("/product/update")
    public String updateProc(@Valid ProductDTO productDTO,
                             MultipartFile  imgFile,
                             Model model,
                             RedirectAttributes redirectAttributes)throws Exception {
       productService.productUpdate(productDTO, imgFile);

        return "redirect:/product/list";
    }

    //삭제
    @GetMapping("/product/delete")
    public String deleteProc(Integer id,
                             Model model,
                             RedirectAttributes redirectAttributes) throws Exception {
        productService.productDelete(id);

        return "redirect:/product/list";
    }

    //개별 조회 fixme 04-03
    @GetMapping("/product/detail")
    public String readProc(Integer id, Model model, RedirectAttributes redirectAttributes)throws Exception {

        ProductDTO productDTO = new ProductDTO(); //검증을 위한 빈DTO

        model.addAttribute("productDTO", productDTO);
        return "product/detail";
    }

    //fixme 04-03 새로추가한거
    @GetMapping("/product/productlist") //사용자목록
    public String productListPage(Model model,
                                  RedirectAttributes redirectAttributes)throws Exception{
        List<ProductDTO> productDTOS = productService.productList();
        model.addAttribute("productDTOS", productDTOS);

        return "product/productlist";
    }

    @GetMapping("/product/productdetail") //사용자 상세보기
    public String productListDetailPage(Integer id, Model model,
                                        RedirectAttributes redirectAttributes) throws Exception{
        ProductDTO productDTO = productService.productDetail(id);

        model.addAttribute("productDTO", productDTO);

        return "product/productdetail";

    }

}
