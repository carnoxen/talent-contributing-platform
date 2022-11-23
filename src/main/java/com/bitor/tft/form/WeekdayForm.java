package com.bitor.tft.form;

import java.sql.Time;

import com.bitor.tft.entity.Schedule;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeekdayForm {
    private Boolean checked;

    private Byte weekday;

    private Time starttime;

    private Time endtime;
    
    public boolean isValid() {
        return checked && weekday != null && starttime != null && endtime != null;
    }

    public static WeekdayForm from(Schedule schedule) {
        return new WeekdayForm(true, schedule.getWeekday(), schedule.getStartTime(), schedule.getEndTime());
    }
}
