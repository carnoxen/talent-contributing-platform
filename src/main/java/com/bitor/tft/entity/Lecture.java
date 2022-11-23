package com.bitor.tft.entity;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Comment;

import com.bitor.tft.form.LectureForm;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(force = true, access = AccessLevel.PROTECTED)
public class Lecture implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(length = 120, nullable = false)
    @Comment("")
    private String name;

    @Column(nullable = false)
    @Comment("")
    private Date startDate;

    @Column(nullable = false)
    @Comment("")
    private Date endDate;

    @Column(nullable = false)
    @Comment("")
    private Double latitude;

    @Column(nullable = false)
    @Comment("")
    private Double longitude;

    @Column(length = 255, nullable = false)
    @Comment("")
    private String address;

    @Column(length = 255, nullable = false)
    @Comment("")
    private String addressInternal;

    @Column(nullable = false)
    @Comment("")
    private Long studentLimit;

    @Column(nullable = false)
    @Comment("")
    private Long teacherLimit;
    
    @Column(nullable = false)
    @Lob
    @Comment("")
    private String description;

    @Column(nullable = false)
    @Comment("")
    private Integer attendLimitStart;

    @Column(nullable = false)
    @Comment("")
    private Integer attendLimitEnd;

    @Builder
    private Lecture(Member member, String name, Date startdate, Date enddate, Double latitude, Double longitude, String address, String addressInternal, Long studentLimit, Long teacherLimit, String description, Integer attendLimitStart, Integer attendLimitEnd) {
        this.member = member;
        this.name = name;
        this.startDate = startdate;
        this.endDate = enddate;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
        this.addressInternal = addressInternal;
        this.studentLimit = studentLimit;
        this.teacherLimit = teacherLimit;
        this.description = description;
        this.attendLimitStart = attendLimitStart;
        this.attendLimitEnd = attendLimitEnd;
    }

    public void updateAll(LectureForm lectureForm) {
        this.name = lectureForm.getName();
        this.startDate = lectureForm.getStartdate();
        this.endDate = lectureForm.getEnddate();
        this.latitude = lectureForm.getLatitude();
        this.longitude = lectureForm.getLongitude();
        this.address = lectureForm.getAddress();
        this.studentLimit = lectureForm.getStudentlimit();
        this.teacherLimit = lectureForm.getTeacherlimit();
        this.description = lectureForm.getDescription();
        this.attendLimitStart = lectureForm.getAttendLimitStart();
        this.attendLimitEnd = lectureForm.getAttendLimitEnd();
    }

    public void updateId(Long id) {
        this.id = id;
    }
}
