package com.bitor.tft.form;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;

import com.bitor.tft.entity.Category;
import com.bitor.tft.entity.Lecture;
import com.bitor.tft.entity.Member;
import com.bitor.tft.entity.Schedule;

import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LectureForm {
    @NotBlank
    private String name;

    @NotNull
    private Date startdate;

    @NotNull
    private Date enddate;

    @NotBlank
    private String address;

    @NotBlank
    private String address_internal;

    private List<WeekdayForm> weekdayForms = new ArrayList<>() {{
        add(new WeekdayForm(false, (byte) 1, null, null));
        add(new WeekdayForm(false, (byte) 2, null, null));
        add(new WeekdayForm(false, (byte) 3, null, null));
        add(new WeekdayForm(false, (byte) 4, null, null));
        add(new WeekdayForm(false, (byte) 5, null, null));
        add(new WeekdayForm(false, (byte) 6, null, null));
        add(new WeekdayForm(false, (byte) 7, null, null));
    }};

    @NotBlank
    private String categories;

    @NotNull
    private Long studentlimit;

    @NotNull
    private Long teacherlimit;

    @NotBlank
    private String description;

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;

    @NotNull
    @Max(60)
    private Integer attendLimitStart;

    @NotNull
    @Max(60)
    private Integer attendLimitEnd;

    public Lecture toLecture(Member member) {
        return Lecture.builder()
        .member(member)
        .name(name)
        .startdate(startdate)
        .enddate(enddate)
        .address(address)
        .addressInternal(address_internal)
        .studentLimit(studentlimit)
        .teacherLimit(teacherlimit)
        .description(description)
        .latitude(latitude)
        .longitude(longitude)
        .attendLimitStart(attendLimitStart)
        .attendLimitEnd(attendLimitEnd).build();
    }

    public static LectureForm from(Lecture lecture, List<Schedule> schedules, List<Category> category_list){
        LectureForm lectureForm = new LectureForm();

        lectureForm.name = lecture.getName();
        lectureForm.startdate = lecture.getStartDate();
        lectureForm.enddate = lecture.getEndDate();
        lectureForm.address = lecture.getAddress();
        lectureForm.address_internal = lecture.getAddressInternal();
        lectureForm.latitude = lecture.getLatitude();
        lectureForm.longitude = lecture.getLongitude();
        lectureForm.categories = String.join(",", category_list.stream().map(Category::getName).toList());
        lectureForm.studentlimit = lecture.getStudentLimit();
        lectureForm.teacherlimit = lecture.getTeacherLimit();
        lectureForm.description = lecture.getDescription();
        lectureForm.attendLimitStart = lecture.getAttendLimitStart();
        lectureForm.attendLimitEnd = lecture.getAttendLimitEnd();

        for (Schedule schedule : schedules) {
            WeekdayForm  weekdayForm = WeekdayForm.from(schedule);
            lectureForm.weekdayForms.set(schedule.getWeekday() - 1, weekdayForm);
        }

        return lectureForm;
    }
}
