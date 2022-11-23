package com.bitor.tft.search;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bitor.tft.entity.Lecture;
import com.bitor.tft.entity.Member;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {
    private final SearchService searchService;
    private final Map<String, String> sortMap = new HashMap<>(){{
        put("distance", "거리 순");
    }};

    @GetMapping
    String get_search(Model model, @AuthenticationPrincipal Member you, @RequestParam Optional<String> q, @RequestParam Optional<String> s) {
        String query = q.orElse("");
        List<Lecture> searched = searchService.getLecturesByKeyword(query);

        if (s.isPresent()) {
            String sortion = s.get();

            if (sortion.equals("distance")) {
                searched = searchService.getLecturesByKeywordAndDistance(query, you);
            }
        }

        model.addAttribute("you", you);
        model.addAttribute("searched", searched);
        model.addAttribute("sortEntry", sortMap.entrySet());

        return "search";
    }
}
