package com.choi.api.biz.guest.dao;

import com.choi.api.biz.guest.model.Page;
import com.choi.api.biz.guest.model.QPage;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.jpa.impl.JPAUpdateClause;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;

import static com.choi.api.biz.guest.model.QPage.page;

@Repository
@RequiredArgsConstructor
public class PageRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;
    private final JPAQueryFactory queryFactory;
    private final QPage qPage = QPage.page;

    public int savePage(Page param){

        JPAUpdateClause updateClause = new JPAUpdateClause(entityManager, qPage);
        updateClause.set(qPage.content, param.getContent());
        if (param.getTitle() != null) {
            updateClause.set(qPage.title, param.getTitle());
        }
        return (int) updateClause.where(qPage.id.eq(param.getId()).and(qPage.seq.eq(param.getSeq()))).execute();
    }

}
