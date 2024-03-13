package com.choi.api.biz.guest.model;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

public class BoardDTO {

    @Getter
    public static class BoardAddReq {
        private String title;
        private String content;
        private String eventDate;
        private String eventTime;
        private String eventType;
    }

    @Getter
    public static class BoardSaveReq {
        private int seq;
        private String title;
        private String content;
        private String eventDate;
        private String eventTime;
        private String eventType;
    }

    @Data
    @Builder
    public static class BoardDetailRes {
        private int seq;
        private String title;
        private String content;
        private String eventDate;
        private String eventTime;
        private String eventType;
        private String insdate;
        private String update;
    }

    @Data
    @Builder
    public static class BoardListRes {
        private int seq;
        private String title;
        private String eventDate;
        private String eventTime;
        private String eventType;
        private String insdate;
    }
}
