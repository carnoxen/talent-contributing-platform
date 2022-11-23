package com.bitor.tft.service;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Optional;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.bitor.tft.entity.Member;
import com.bitor.tft.entity.Verification;
import com.bitor.tft.repository.MemberRepository;
import com.bitor.tft.repository.VerificationRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@AllArgsConstructor
public class VerifyService {
    private final MemberRepository memberRepository;
    private final VerificationRepository verificationRepository;
    private final JavaMailSender javaMailSender;

    public String upsertVerification(String username) throws NoSuchAlgorithmException {
        SecureRandom sec = new SecureRandom();
        String code = String.format("%06d", sec.nextInt(1000000));
        log.info("random code made");

        Optional<Verification> verExists = verificationRepository.findByUsername(username);
        Verification newVer = Verification.builder().username(username).code(code).build();
        if (verExists.isPresent()) {
            newVer = verExists.get();
            newVer.updateCode(code);
        }
        log.info("verification tuple made");

        verificationRepository.save(newVer);
        log.info("verification save ended");

        return code;
    }

    public void send_code_by_email(String username, String random) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("tft.bitor@gmail.com");
        message.setTo(username);
        message.setSubject(String.format("Temporal Code to %s", username));
        message.setText(String.format("Your code is currently:\n %s", random));

        log.info("message sent");
        javaMailSender.send(message);
    }

    public Optional<Verification> getVerification(String username, String code) {
        return verificationRepository.findByUsernameAndCode(username, code);
    }

    public Boolean verify(String originalCode, String code) {
        return originalCode.equals(code);
    }

    public void deleteVerification(Verification verification) {
        verificationRepository.delete(verification);
    }

    public Optional<Member> getMember(String username) {
        return memberRepository.findByUsername(username);
    }
}
