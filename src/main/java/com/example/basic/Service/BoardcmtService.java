
/*
    작성자 : 정아름
    작성일 : 24.02.21
    수정사항 : 리뷰 댓글이랑 게시판 댓글 repository & service 다시 확인해라!!!!!!!!!!
 */

package com.example.basic.Service;

import com.example.basic.DTO.BoardcmtDTO;
import com.example.basic.Entity.BoardEntity;
import com.example.basic.Entity.BoardcmtEntity;
import com.example.basic.Repository.BoardRepository;
import com.example.basic.Repository.BoardcmtRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardcmtService {
    private final BoardcmtRepository boardcmtRepository;
    private final BoardRepository boardRepository;
    private final ModelMapper modelMapper;

    //삽입
    public void boardcmtInsert(BoardcmtDTO boardcmtDTO, Integer boardId) {

        Optional<BoardEntity> data = boardRepository.findById(boardId);

        BoardEntity boardEntity = data.orElseThrow();

        BoardcmtEntity boardcmtEntity = modelMapper.map(boardcmtDTO, BoardcmtEntity.class);

        boardcmtEntity.setBoardEntity(boardEntity);
        boardcmtRepository.save(boardcmtEntity);
    }

    //수정
    public void boardcmtUpdate(BoardcmtDTO boardcmtDTO, Integer boardId) {

        Optional<BoardEntity> data = boardRepository.findById(boardId);

        BoardEntity boardEntity = data.orElseThrow();

        BoardcmtEntity boardcmtEntity = modelMapper.map(boardcmtDTO, BoardcmtEntity.class);

        boardcmtEntity.setBoardEntity(boardEntity);
        boardcmtRepository.save(boardcmtEntity);
    }

    //삭제
    public void boardcmtDelete(Integer boardcmtId) {
        boardcmtRepository.deleteById(boardcmtId);
    }

    //전체 조회
    public List<BoardcmtDTO> boardcmtlist(Integer boardId) {
        List<BoardcmtEntity> boardcmtEntities = boardcmtRepository.findByBoardId(boardId);

        List<BoardcmtDTO> boardcmtDTOS = Arrays.asList(modelMapper.
                map(boardcmtEntities, BoardcmtDTO[].class));

        return boardcmtDTOS;
    }

    //개별조회
    public BoardcmtDTO boardcmtDetail(Integer boardcmtId) {
        Optional<BoardcmtEntity> boardcmtEntity = boardcmtRepository.findById(boardcmtId);
        BoardcmtDTO boardcmtDTO = modelMapper.map(boardcmtEntity, BoardcmtDTO.class);

        return boardcmtDTO;
    }
}
