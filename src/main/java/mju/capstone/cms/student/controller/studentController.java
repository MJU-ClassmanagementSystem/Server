package mju.capstone.cms.student.controller;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/student")
public class studentController {

    @Getter
    public static class testDto {
        private String time;
    }

    @PostMapping("/test")
    public ResponseEntity<String> test(@RequestBody testDto test) {
        System.out.println("!!! time : " + test.getTime());

        // 파이썬에서 post 방식으로 json 보내면 스프링에서 저장하면 됨.
        // 리액트는 파이썬이랑 연동하면 됨?

        return ResponseEntity.ok("success");
    }
}
