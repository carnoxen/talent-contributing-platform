package com.bitor.tft.entity;

import java.io.Serializable;
import java.sql.Time;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Check;
import org.hibernate.annotations.Comment;

import com.bitor.tft.form.WeekdayForm;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Check(constraints = "weekday BETWEEN 1 AND 7")
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
public class Schedule implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "lecture_id", nullable = false)
    @Comment("")
    private Lecture lecture;

    @Column(nullable = false)
    @Comment("")
    private Byte weekday;

    @Column(nullable = false)
    @Comment("")
    private Time startTime;

    @Column(nullable = false)
    @Comment("")
    private Time endTime;

    @Builder
    private Schedule(Lecture lecture, Byte weekday, Time starttime, Time endtime) {
        this.lecture = lecture;
        this.weekday = weekday;
        this.startTime = starttime;
        this.endTime = endtime;
    }

    public void updateAll(WeekdayForm weekdayForm) {
        this.startTime = weekdayForm.getStarttime();
        this.endTime = weekdayForm.getEndtime();
    }
}
