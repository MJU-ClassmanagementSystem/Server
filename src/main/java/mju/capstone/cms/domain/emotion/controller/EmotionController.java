package mju.capstone.cms.domain.emotion.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class EmotionController {
    @GetMapping("/emotions")
    public String emotions() {
        return "emotions";
    }
}
