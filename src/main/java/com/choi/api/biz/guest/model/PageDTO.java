package com.choi.api.biz.guest.model;

import lombok.Builder;
import lombok.Data;

public class PageDTO {

    @Data
    public static class PageSaveReq {
        private int seq;
        private String title;
        private String content;
    }

    @Builder
    @Data
    public static class PageListRes {
        private int seq;
        private String title;
    }

    @Builder
    @Data
    public static class PageDetailRes {
        private int seq;
        private String title;
        private String content;
    }
}
