package com.bitor.tft.controller;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bitor.tft.entity.Member;
import com.bitor.tft.service.LostAndFoundService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/lost-and-found")
@RequiredArgsConstructor
public class LostAndFoundController {
    private final LostAndFoundService lostAndFoundService;

    @GetMapping
    public String lostAndFound(@AuthenticationPrincipal Member you) {
        if (you != null) {
            return "redirect:/";
        }
        return "lost-and-found";
    }

    @PostMapping("/username")
    String find_username(@RequestParam String username) {
        Optional<Member> opmem = lostAndFoundService.getMemberByUsername(username);

        if (opmem.isPresent()) {
            Member mem = opmem.get();

            lostAndFoundService.sendUsernameByEmail(mem.getUsername());
        }
        else {
            return "redirect:/lost-and-found";
        }

        return "redirect:/login";
    }
    
    @PostMapping("/password")
    String reset_password(@RequestParam String username) throws NoSuchAlgorithmException {
        Optional<Member> opmem = lostAndFoundService.getMemberByUsername(username);

        if (opmem.isPresent()) {
            Member mem = opmem.get();
            String randomcode = lostAndFoundService.getRandomString();

            lostAndFoundService.applyRandom(mem, randomcode);
            lostAndFoundService.sendRandomByEmail(username, randomcode);
        }
        else {
            return "redirect:/lost-and-found";
        }

        return "redirect:/login";
    }
}
