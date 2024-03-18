package com.choi.api.biz.mail.dao;

import com.choi.api.biz.mail.model.Mail;
import org.apache.tomcat.jni.Local;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface MailRepository extends JpaRepository<Mail, String>, CrudRepository<Mail, String> {
    Mail saveAndFlush(Mail mail);

    @Query(nativeQuery = true, value = "SELECT COUNT(*) FROM TB_MAIL_BASIC M WHERE M.CLIENT_IP = :clientIp AND M.SYS_CREATION_DATE >= :time ")
    int countByClientIpAndSysCreationDateBefore(@Param("clientIp") String clientIp, @Param("time") String time);

    @Query("SELECT m.code FROM TB_MAIL_BASIC m WHERE m.seq = :#{#mail.seq} AND m.email = :#{#mail.email}")
    String selectCodeBySeq(@Param("mail") Mail mail);
}
