package com.bitor.tft.component;

import java.sql.Time;
import java.text.ParseException;
import java.time.LocalTime;
import java.util.Locale;

import org.springframework.format.Formatter;
import org.springframework.stereotype.Component;

/**
 * 스프링이 <input type="time"> 태그의 데이터를 인식하지 못 해 추가한 포매터
 * 초 단위는 물론(00:00:00) 초 단위가 없는 시간(00:00)까지 인식 가능하게 만들어 준다.
 */
@Component
public class TimeFormatter implements Formatter<Time> {

    @Override
    public String print(Time time, Locale locale) {
        return time.toString();
    }

    @Override
    public Time parse(String text, Locale locale) throws ParseException {
        LocalTime ltime = LocalTime.parse(text);
        return Time.valueOf(ltime);
    }
    
}
