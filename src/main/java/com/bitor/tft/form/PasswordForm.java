package com.bitor.tft.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.bitor.tft.entity.Member;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class PasswordForm {
    @NotBlank
    public final String old_password;

    @NotBlank
    @Size(min = 5)
    public final String password;

    @NotBlank
    public final String password_confirm;

    public Boolean old_is_correct(Member member, PasswordEncoder passwordEncoder) {
        return passwordEncoder.matches(old_password, member.getPassword());
    }

    public Boolean is_same() {
        return password.equals(password_confirm);
    }
}
