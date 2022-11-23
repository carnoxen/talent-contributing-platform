package com.bitor.tft.form;

import java.sql.Time;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AttendanceForm {
    @NotNull
    public Long id;
    
    @NotNull
    public Time starttime;

    @NotNull
    public Time endtime;

    @NotBlank
    public String reason;
}
