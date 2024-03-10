package com.choi.api.biz.guest.dao;

import com.choi.api.biz.guest.model.Page;
import com.choi.api.biz.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PageRepository extends JpaRepository<Page, Integer> {

    List<Page> findById(String userId);

    @Query(nativeQuery = true, value = "SELECT a.SEQ, a.TITLE, a.CONTENT, a.ID FROM TB_GUEST_PAGE a WHERE a.ID = :#{#page.id} AND a.SEQ = :#{#page.seq}")
    Page selectPageByIdAndSeq(@Param("page") Page page);

    @Modifying
    @Query(nativeQuery = true, value = "UPDATE TB_GUEST_PAGE a SET a.TITLE = :#{#page.title}, a.CONTENT = :#{#page.content}, a.SYS_UPDATE_DATE = CURRENT_TIMESTAMP WHERE a.ID = :#{#page.id} AND a.SEQ = :#{#page.seq}")
    int savePage(Page page);

}