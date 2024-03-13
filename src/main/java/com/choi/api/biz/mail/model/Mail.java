package com.choi.api.biz.mail.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "TB_MAIL_BASIC")
public class Mail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int seq; // 요청시퀀스
    private String email; // 유저아이디
    private String subject; // 제목
    private String content; // 메시지
    private String code; // 인증코드
    private String type; // 타입
}
