package com.bitor.tft.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bitor.tft.entity.Member;
import com.bitor.tft.entity.Verification;
import com.bitor.tft.service.VerifyService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j
@RestController
@RequestMapping("/verify")
@AllArgsConstructor
public class VerifyController {
    private final VerifyService verifyService;

    @GetMapping("/username")
    public ResponseEntity<String> post_username(@RequestParam String username) throws NoSuchAlgorithmException {
        log.info("post message start");
        Optional<Member> memberExists = verifyService.getMember(username);

        if (memberExists.isPresent()) {
            return ResponseEntity.badRequest().body("member already exists: " + username);
        }

        String code = verifyService.upsertVerification(username);
        log.info("create verification service ended");

        verifyService.send_code_by_email(username, code);
        log.info("mail service ended");

        return ResponseEntity.accepted().body("verification success");
    }

    @GetMapping("/code")
    public ResponseEntity<String> post_code(@RequestParam String username, @RequestParam String code) {
        Optional<Verification> opVer = verifyService.getVerification(username, code);

        if (opVer.isEmpty()) {
            return ResponseEntity.badRequest().body("no code exists: " + username);
        }

        verifyService.deleteVerification(opVer.get());

        return ResponseEntity.ok(username);
    }
}
