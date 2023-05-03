package mju.capstone.cms.domain.parent.controller;

import lombok.RequiredArgsConstructor;
import mju.capstone.cms.domain.parent.dto.ParentSignupDto;
import mju.capstone.cms.domain.parent.service.ParentService;
import mju.capstone.cms.global.BaseResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ParentController {

    private final ParentService parentService;

    //회원가입-부모
    @PostMapping("/signup/parent")
    public BaseResponse<String> signup(@RequestBody ParentSignupDto parentSignupDto) {
        String id = parentService.signup(parentSignupDto.getId(), parentSignupDto.getPassword());

        //자녀 등록 로직 필요.

        return new BaseResponse<>(
            200,
            "회원가입 완료",
            id
        );
    }
}
