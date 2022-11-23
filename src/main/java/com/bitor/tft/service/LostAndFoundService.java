package com.bitor.tft.service;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Optional;

import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.bitor.tft.entity.Member;
import com.bitor.tft.repository.MemberRepository;
// import com.twilio.Twilio;
// import com.twilio.rest.api.v2010.account.Message;
// import com.twilio.type.PhoneNumber;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LostAndFoundService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailSender javaMailSender;
    
    // private final String ACCOUNT_SID = "AC49bf0cf71f2b49146f54ab1e2718a944";
    // private final String AUTH_TOKEN = "2f637a020adf8e4ccbb3502059e8c006";

    public String getRandomString() throws NoSuchAlgorithmException {
        SecureRandom sec = new SecureRandom();
        return String.format("%06d",sec.nextInt(1000000));
    }

    public Optional<Member> getMemberByUsername(String username) {
        return memberRepository.findByUsername(username);
    }

    public Optional<Member> getMemberByPhone(String phone) {
        return memberRepository.findByPhone(phone);
    }

    public void applyRandom(Member member, String random) {
        member.updatePassword(random, passwordEncoder);
        memberRepository.save(member);
    }

    public void sendUsernameByEmail(String username) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("tft.bitor@gmail.com");
        message.setTo(username);
        message.setSubject("This is a message from bitor-group");
        message.setText(String.format("Your username found: \n%s", username));

        javaMailSender.send(message);
    }

    public void sendRandomByEmail(String username, String random) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("tft.bitor@gmail.com");
        message.setTo(username);
        message.setSubject(String.format("Temporal Password: %s", username));
        message.setText(String.format("Your password is currently:\n %s", random));

        javaMailSender.send(message);
    }

    // public void sendUsernameByPhone(String phone, String username) {
    //     Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    //     PhoneNumber from = new PhoneNumber("+");
    //     PhoneNumber to = new PhoneNumber(phone);
    //     String alert_email = String.format("Your Email is: %s", username);

    //     Message message = Message.creator(from, to, alert_email).create();

    //     System.out.println(message.getSid());
    // }

}
