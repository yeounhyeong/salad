package com.example.basic.Service;

import com.example.basic.DTO.BoardDTO;
import com.example.basic.Entity.BoardEntity;
import com.example.basic.Repository.BoardRepository;
import com.example.basic.Repository.BoardcmtRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.internal.bytebuddy.implementation.bytecode.Throw;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BoardService {
    private final BoardRepository boardRepository;
    private final ModelMapper modelMapper;


    //삽입
    public void boardInsert(BoardDTO boardDTO) {
        BoardEntity boardEntity = modelMapper.map(boardDTO, BoardEntity.class);

        boardRepository.save(boardEntity);
    }

    //수정
    public void boardUpdate(BoardDTO boardDTO) {
        Integer id = boardDTO.getBoardId();
        Optional<BoardEntity> read = boardRepository.findById(id);

        if (read != null) {
            BoardEntity boardEntity = modelMapper.map(boardDTO, BoardEntity.class);
            boardRepository.save(boardEntity);
        }
    }

    //삭제
    public void boardDelete(Integer boardId) {
        boardRepository.deleteById(boardId);
    }

    //전체 조회
    public Page<BoardDTO> boardlist(String type, String keyword, Pageable pageable) {
        int cntPage = pageable.getPageNumber() - 1; //현재페이지
        int pageLimit = 10;
        Pageable page = PageRequest.of(cntPage, pageLimit,
                Sort.by(Sort.Direction.DESC, "boardId"));

        Page<BoardEntity> boardEntities;

        if (type.equals("s") && keyword != null) {
            //분류가 제목에 검색어가 존재하면
            boardEntities = boardRepository.findByBoardSubjectContaining(keyword, page);
        } else if (type.equals("c") && keyword != null) {
            //분류가 내용에 검색어가 존재하면
            boardEntities = boardRepository.findByBoardContentContaining(keyword, page);
        } else if (type.equals("w") && keyword != null) {
            //분류가 작성자에 검색어가 존재하면
            boardEntities = boardRepository.findByBoardWriterContaining(keyword, page);
        } else if (type.equals("sc") && keyword != null) {
            //분류가 제목과 내용에 검색어가 존재하면
            boardEntities = boardRepository.findByBoardSCContaining(keyword, page);
        } else {
            //분류 및 검색어가 없는 경우
            boardEntities = boardRepository.findAll(page);
        }

        Page<BoardDTO> boardDTOS = boardEntities.map(
                data -> modelMapper.map(data, BoardDTO.class));

        return boardDTOS;
    }

    //개별 조회
    public BoardDTO boardDetail(Integer boardId) {

        Optional<BoardEntity> boardEntity = boardRepository.findById(boardId);
        BoardDTO boardDTO = modelMapper.map(boardEntity, BoardDTO.class);

        return boardDTO;
    }

    //비밀글 개별조회
    public BoardDTO boardDetail(Integer boardId, String password) {

        Optional<BoardEntity> boardEntity = boardRepository.findById(boardId);
        //게시글이 있으면
        if (boardEntity.isPresent()) {
            //비밀글 여부 확인
            if (boardEntity.get().isSecret()) {
                //입력한 비밀번호와 저장된 비밀번호가 같으면
                if (password.equals(boardEntity.get().getBoardPassword())) {
                    BoardDTO boardDTO = modelMapper.map(boardEntity, BoardDTO.class);
                    return boardDTO;
                }
            }
        }
        return null;
    }
}