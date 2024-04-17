package com.example.basic.Service;

import com.example.basic.DTO.ProductDTO;
import com.example.basic.Entity.ProductEntity;
import com.example.basic.Repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {
    //파일이 저장될경로
    @Value("${imgUploadLocation}")
    private String imgUploadLocation;


    private final ProductRepository productRepository;

    //이 두가지는 동일하지만 new ModelMapper();는 변수선언과 동시에 초기화를 하는것.
//    private final ModelMapper modelMapper;
    private final ModelMapper modelMapper = new ModelMapper();

    private final FileService fileService;

    //삽입 fixme 04-03 추가
    public void productInsert(ProductDTO productDTO,
                              MultipartFile imgFile) throws Exception {
        String originalFileName = imgFile.getOriginalFilename(); //저장할 파일명
        String newFileName = ""; //새로 만든 파일명
        if(originalFileName != null ) { //파일이 존재하면
            newFileName = fileService.uploadFile(
                    imgUploadLocation,
                    originalFileName,
                    imgFile.getBytes()
            );
        }
        productDTO.setProductImage(newFileName); //새로운 파일명을 재등록
        //변환
        ProductEntity productEntity = modelMapper.map(productDTO, ProductEntity.class);

        productRepository.save(productEntity); //저장

    }

//    //삽입 fixme 04-03 기존꺼
//    public void productInsert(ProductDTO productDTO) {
//
//        ProductEntity productEntity = modelMapper.map(productDTO,
//                ProductEntity.class);
//        productRepository.save(productEntity);
//    }


    //수정 fixme 04-03
    public void productUpdate(ProductDTO productDTO, MultipartFile imgFile) throws Exception{
        //기존파일삭제
        ProductEntity productEntity = productRepository
                .findById(productDTO.getProductId())
                .orElseThrow();
        String deleteFile = productEntity.getProductImage();

        String originalFileName = imgFile.getOriginalFilename(); //저장할파일명
        String newFileName = ""; //새로만든 파일명
        if(originalFileName.length() != 0){ //파일이 존재하면
            //바꿀파일이있다면
            if(deleteFile.length() != 0){//기존파일이 있다면
                fileService.deleteFile(imgUploadLocation, deleteFile);
            }
            newFileName = fileService.uploadFile(
                    imgUploadLocation,
                    originalFileName,
                    imgFile.getBytes()
            );
            productDTO.setProductImage(newFileName); //새로운 파일명을 재등록
        }
        productDTO.setProductId(productEntity.getProductId());

        //변환
        ProductEntity data = modelMapper.map(productDTO, ProductEntity.class);
        productRepository.save(data); //저장

    }

//    //수정 fixme 04-03 기존꺼
//    public void productUpdate(ProductDTO productDTO) {
//
//        Integer curcount = productDTO.getQuantityCount();
//        Integer outquantity = productDTO.getQuantity();
//        Integer curquantity = productDTO.getQuantityIn();
//        Integer quantity = curcount - outquantity;
//
//        //재고 수량 업데이트
//        productDTO.setQuantityCount((curquantity != null ? curquantity : 0) + quantity);
//
//        //데이터베이스 저장
//        ProductEntity product = modelMapper.map(productDTO,
//                ProductEntity.class);
//        productRepository.save(product);
//    }

    //삭제
    public void productDelete(Integer productId) throws Exception {

        //fixme 04-03 수정
        ProductEntity productEntity = productRepository
                .findById(productId)
                .orElseThrow(); //조회 > 저장

        fileService.deleteFile(imgUploadLocation, productEntity.getProductImage());
        productRepository.deleteById(productId);


//        //상품 조회 fixme 04-03 기존에 쓰던것들
//  public void productDelete(Integer productId)  {
//        Optional<ProductEntity> productEntity = productRepository.findById(productId);
        //읽어온 레코드에서 파일명을 읽는다.
//        String delFile = productEntity.get().getProductImage();
//        //저장된 경로와 파일명을 전달하여 파일을 삭제
//        fileService.deleteFile(imgUploadLocation, delFile);
//        productRepository.deleteById(productId);


    }

    //전체 조회
    //fixme 04-03 controller 전체조회쪽에 문제생김 일단 만드는중이라 보류
    public List<ProductDTO> productList() throws Exception{

        //fixme 04-03 추가
        List<ProductEntity> productEntities =productRepository.findAll(); //조회

        List<ProductDTO> productDTOS = Arrays.asList(
                modelMapper.map(productEntities, ProductDTO[].class));

        return productDTOS;

        //fixme 04-03 기존꺼
        // public List<ProductDTO> productList(String keyword, String type) {
//        List<ProductEntity> productEntities;
//
//        if (keyword != null) {
//            //검색어가 존재하면 상품명에서 검색
//            productEntities = productRepository.findByProductNameContaining(keyword);
//            //검색어도 없으면
//        } else {
//            productEntities = productRepository.findAll();
//        }
//
//        if (type.equals("n")) {
//            productEntities = productRepository.findProductEntitiesBy();
//        }
//
//        List<ProductDTO> productDTOS = Arrays.
//                asList(modelMapper.map(productEntities, ProductDTO[].class));
//
//        return productDTOS;
    }

    //개별 조회
    public ProductDTO productDetail(Integer productId) throws Exception{
        //fixme 04-03
        ProductEntity productEntity = productRepository
                .findById(productId)
                .orElseThrow();
        ProductDTO productDTO = modelMapper.map(productEntity,ProductDTO.class);
        return productDTO;

        //fixme 04-03 기존에 쓰던것들
//        Optional<ProductEntity> productEntity = productRepository.findById(productId);
//        ProductDTO productDTO = modelMapper.map(productEntity, ProductDTO.class);
//        return productDTO;
    }
}
