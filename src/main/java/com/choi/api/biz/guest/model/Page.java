package com.choi.api.biz.guest.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;

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
    @CreationTimestamp
    @Column(name = "SYS_CREATION_DATE")
    private LocalDateTime sysCreationDate;
    @UpdateTimestamp
    @Column(name = "SYS_UPDATE_DATE")
    private LocalDateTime sysUpdateDate;
    @PrePersist
    protected void onCreate() {
        sysCreationDate = LocalDateTime.now();
        sysUpdateDate = LocalDateTime.now();
    }
}
