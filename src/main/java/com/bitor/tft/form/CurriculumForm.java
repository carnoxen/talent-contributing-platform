package com.bitor.tft.form;

import java.sql.Date;
import java.sql.Time;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class CurriculumForm {
    @NotNull
    public final Date startdate;
    
    @NotNull
    public final Time starttime;
    
    @NotNull
    public final Time endtime;

    @NotBlank
    public final String title;
}
