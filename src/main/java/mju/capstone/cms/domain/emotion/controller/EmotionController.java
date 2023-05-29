package mju.capstone.cms.domain.emotion.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;

//@CrossOrigin(originPatterns = "http://127.0.0.1:5174")
@Controller
public class EmotionController {
    @GetMapping("/emotions")
    public String emotions() {
        return "emotions";
    }
}
