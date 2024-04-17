package com.example.basic.Repository;

import com.example.basic.Entity.BoardEntity;
import com.example.basic.Entity.BoardcmtEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardcmtRepository extends JpaRepository<BoardcmtEntity, Integer> {
    //@Query(value = "SELECT u.board.boardId FROM BoardcmtEntity u where u.boardID=:boardId", nativeQuery = true)

    //@Query(value="SELECT s FROM BoardEntity s, BoardcmtEntity w WHERE s.boardId=w.boardId", nativeQuery = true)
    @Query("SELECT s FROM BoardcmtEntity s WHERE s.boardEntity.boardId = :boardId")
    List<BoardcmtEntity> findByBoardId(@Param("boardId") Integer boardId);
}
