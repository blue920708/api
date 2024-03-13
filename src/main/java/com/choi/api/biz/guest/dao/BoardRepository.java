package com.choi.api.biz.guest.dao;

import com.choi.api.biz.guest.model.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BoardRepository extends JpaRepository<Board, Integer> {

    @Modifying
    @Query(nativeQuery = true,
            value = "UPDATE TB_GUEST_BOARD " +
                    "SET TITLE = :#{#board.title}, " +
                    "CONTENT = :#{#board.content}, " +
                    "EVENT_DATE = :#{#board.eventDate},  " +
                    "EVENT_TIME = :#{#board.eventTime}, " +
                    "EVENT_TYPE = :#{#board.eventType}," +
                    "SYS_UPDATE_DATE = NOW() WHERE ID = :#{#board.id} AND SEQ = :#{#board.seq}"
    )
    int saveBoard(@Param("board")Board board);

    Optional<Board> findByIdAndSeq(String id, int seq);

    Page<Board> findAllByIdAndTitleContainingOrderBySeqDesc(String id, String title, Pageable pageable);

}
