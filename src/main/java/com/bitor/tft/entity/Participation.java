package com.bitor.tft.entity;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Comment;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Participation implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name = "lecture_id", nullable = false)
    private Lecture lecture;

    @Column(nullable = false)
    @Comment("")
    private Byte status = 1;

    @Column(nullable = false)
    @Comment("")
    private Timestamp startDatetime;

    @Builder
    private Participation(Member member, Lecture lecture, Timestamp startDatetime) {
        this.member = member;
        this.lecture = lecture;
        this.startDatetime = startDatetime;
    }

    public void updateStatus(Byte status) {
        this.status = status;
    }
}
