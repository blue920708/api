package com.choi.api.biz.mail.model;

import lombok.*;

public class MailDTO {

    @Getter
    public static class SendMailReq {
        private String username;
        private String email;
        private String type;
    }

    @Getter
    public static class VerifyCodeReq {
        private int seq;
        private String email;
        private String code;
    }

}
