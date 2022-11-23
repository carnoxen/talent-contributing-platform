package com.bitor.tft.entity;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Comment;

import com.bitor.tft.form.CurriculumForm;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Curriculum implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    @Comment("")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "lecture_id", nullable = false)
    @Comment("")
    private Lecture lecture;

    @Column(nullable = false)
    @Comment("")
    private Date startDate;

    @Column(nullable = false)
    @Comment("")
    private Time startTime;

    @Column(nullable = false)
    @Comment("")
    private Time endTime;

    @Column(length = 256, nullable = false)
    @Comment("")
    private String title;

    @Builder
    private Curriculum(Member member, Lecture lecture, Date startdate, Time starttime, Time endtime, String title) {
        this.member = member;
        this.lecture = lecture;
        this.startDate = startdate;
        this.startTime = starttime;
        this.endTime = endtime;
        this.title = title;
    }

    public void updateByOwner(CurriculumForm curriculumForm) {
        this.startDate = curriculumForm.startdate;
        this.startTime = curriculumForm.starttime;
        this.endTime = curriculumForm.endtime;
        this.title = curriculumForm.title;
    }

    public void updateByManager(Member member, CurriculumForm curriculumForm) {
        this.member = member;
        this.startDate = curriculumForm.startdate;
        this.startTime = curriculumForm.starttime;
        this.endTime = curriculumForm.endtime;
        this.title = curriculumForm.title;
    }
}
