package com.bitor.tft.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Comment;

import com.bitor.tft.form.AppraisalForm;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Appraisal implements Serializable {
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
    private Double score;
    
    @Column(nullable = false)
    @Lob
    @Comment("")
    private String reason;

    @Builder
    private Appraisal(Member member, Lecture lecture, Double score, String reason) {
        this.member = member;
        this.lecture = lecture;
        this.score = score;
        this.reason = reason;
    }

    public void updateAll(AppraisalForm appraisalForm) {
        this.score = appraisalForm.score;
        this.reason = appraisalForm.reason;
    }
}
