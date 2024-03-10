package com.choi.api.biz.mail.dao;

import com.choi.api.biz.mail.model.Mail;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MailRepository extends JpaRepository<Mail, String>, CrudRepository<Mail, String> {
    Mail saveAndFlush(Mail mail);

    @Query("SELECT m.code FROM TB_MAIL_BASIC m WHERE m.seq = :#{#mail.seq} AND m.email = :#{#mail.email}")
    String selectCodeBySeq(Mail mail);
}
