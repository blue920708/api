package com.choi.api.biz.guest.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.sql.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity(name = "TB_GUEST_PAGE")
@Builder
public class Page {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer seq;
    private String id;
    private String title;
    @Column(columnDefinition="TEXT")
    private String content;
    @Column(name = "SYS_CREATION_DATE")
    private Date sysCreationDate;
    @Column(name = "SYS_UPDATE_DATE")
    private Date sysUpdateDate;
}
