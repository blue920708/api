package com.choi.api.biz.mail.model;

import lombok.*;

public class MailDTO {

    @Getter
    public static class SendMailReq {
        private String email;
    }

    @Getter
    public static class VerifyCodeReq {
        private String seq;
        private String email;
        private String code;
    }

}
