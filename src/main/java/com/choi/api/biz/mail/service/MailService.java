package com.choi.api.biz.mail.service;

import com.choi.api.biz.mail.dao.MailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import com.choi.api.biz.mail.model.Mail;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.internet.MimeMessage;
import java.util.Random;

@Service
@Slf4j
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;
    private final SpringTemplateEngine templateEngine;

    @Autowired
    private MailRepository mailRepository;

    public String sendMail(Mail mail) throws Exception {
        // 인증코드
        String code = createCode();

        // 메일발송
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
        mimeMessageHelper.setTo(mail.getEmail()); // 메일 수신자
        mimeMessageHelper.setSubject(mail.getSubject()); // 메일 제목
        mimeMessageHelper.setText(setContext(code, mail.getType()), true); // 메일 본문 내용, HTML 여부
        //mailSender.send(mimeMessage);

        // DB
        mail.setCode(code);
        mail = mailRepository.saveAndFlush(mail);

        log.debug(this.getClass().getName() + " 디버그 -> 메일 : {}", mail.toString());

        return mail.getSeq();
    }

    public String verifyCode(Mail mail) throws Exception {
        String code = mailRepository.selectCodeBySeq(mail);
        return code;
    }

    // 인증번호 및 임시 비밀번호 생성 메서드
    public String createCode() {
        Random random = new Random();
        StringBuffer key = new StringBuffer();

        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(4);

            switch (index) {
                case 0: key.append((char) ((int) random.nextInt(26) + 97)); break;
                case 1: key.append((char) ((int) random.nextInt(26) + 65)); break;
                default: key.append(random.nextInt(9));
            }
        }
        return key.toString();
    }

    // thymeleaf를 통한 html 적용
    public String setContext(String code, String type) {
        Context context = new Context();
        context.setVariable("code", code);
        return templateEngine.process(type, context);
    }
}
