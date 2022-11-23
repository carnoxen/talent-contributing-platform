package com.bitor.tft.form;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;

import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AppraisalForm {
    @NotNull
    @Digits(integer = 1, fraction = 1)
    public Double score;
    
    @NotBlank
    public String reason;
}
