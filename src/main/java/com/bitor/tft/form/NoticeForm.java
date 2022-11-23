package com.bitor.tft.form;

import javax.validation.constraints.NotBlank;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class NoticeForm {
    @NotBlank
    public final String title;
    
    @NotBlank
    public final String description;
}
