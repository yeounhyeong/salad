package com.example.basic.Service;

import com.example.basic.DTO.ReviewDTO;
import com.example.basic.Entity.ReviewEntity;
import com.example.basic.Repository.ReviewRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ModelMapper modelMapper;

    //삽입
//    @Transactional
    public void reviewInsert(ReviewDTO reviewDTO) {
        reviewDTO.setReviewCount(0);
        ReviewEntity reviewEntity = modelMapper.map(reviewDTO,
                ReviewEntity.class);
        reviewRepository.save(reviewEntity);
    }

    //수정
//    @Transactional
    public void reviewUpdate(ReviewDTO reviewDTO) {

        ReviewEntity reviewEntity = modelMapper.map(reviewDTO, ReviewEntity.class);
        reviewRepository.save(reviewEntity);

    }

    //삭제
    public void reviewDelete(Integer reviewId) {
        reviewRepository.deleteById(reviewId);
    }

    //전체 조회
    public Page<ReviewDTO> reviewList(Pageable pageable) {
        int currentPage = pageable.getPageNumber() - 1;
        int pageLimit = 10;

        Pageable page;
        page = PageRequest.of(currentPage, pageLimit,
                Sort.by(Sort.Direction.DESC, "reviewId"));

        Page<ReviewEntity> reviewEntities = reviewRepository.findAll(page);
        Page<ReviewDTO> reviewDTOS = reviewEntities.map(data -> modelMapper.
                map(data, ReviewDTO.class));

        return reviewDTOS;
    }

    //개별 조회
    public ReviewDTO reviewDetail(Integer reviewId, String pandan) {
        //조회수 증가
        if (pandan.equals("R")) {
            reviewRepository.reviewCount(reviewId);
        }

        Optional<ReviewEntity> reviewEntity = reviewRepository.findById(reviewId);
        ReviewDTO reviewDTO = modelMapper.map(reviewEntity, ReviewDTO.class);

        return reviewDTO;
    }
}