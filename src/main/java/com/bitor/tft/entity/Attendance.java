package com.bitor.tft.entity;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Comment;

import com.bitor.tft.form.AttendanceForm;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Attendance implements Serializable {
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

    @Comment("")
    private Time startTime = null;

    @Comment("")
    private Time endTime = null;
    
    @Column(nullable = false)
    @Lob
    @Comment("")
    private String reason = "";

    @Builder
    private Attendance(Member member, Lecture lecture, Date startdate) {
        this.member = member;
        this.lecture = lecture;
        this.startDate = startdate;
    }

    public void updateTime(LocalTime localTime) {
        LocalTime lt = localTime.withSecond(0).withNano(0);
        Time time = Time.valueOf(lt);
        
        if (this.startTime == null) {
            this.startTime = time;
        }
        else if (this.endTime == null) {
            this.endTime = time;
        }
    }

    public void updateReason(String reason) {
        this.reason = reason;
    }

    public void updateAll(AttendanceForm attendanceForm) {
        this.startTime = attendanceForm.starttime;
        this.endTime = attendanceForm.endtime;
        this.reason = attendanceForm.reason;
    }
}
