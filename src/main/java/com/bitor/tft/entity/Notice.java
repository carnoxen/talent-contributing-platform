package com.bitor.tft.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Comment;

import com.bitor.tft.form.NoticeForm;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
public class Notice implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "lecture_id", nullable = false)
    @Comment("")
    private Lecture lecture;

    @Column(nullable = false)
    @Comment("")
    private Timestamp startDatetime;
    
    @Column(length = 120, nullable = false)
    @Comment("")
    private String title;
    
    @Column(nullable = false)
    @Lob
    @Comment("")
    private String description;

    @Builder
    private Notice(Lecture lecture, String title, String description, Timestamp startDatetime) {
        this.lecture = lecture;
        this.title = title;
        this.description = description;
        this.startDatetime = startDatetime;
    }

    public void updateAll(NoticeForm noticeForm) {
        this.title = noticeForm.title;
        this.description = noticeForm.description;
    }
}
