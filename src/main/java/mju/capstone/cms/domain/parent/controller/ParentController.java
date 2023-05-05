package mju.capstone.cms.domain.parent.controller;

import lombok.RequiredArgsConstructor;
import mju.capstone.cms.domain.parent.dto.ParentSignupRequestDto;
import mju.capstone.cms.domain.parent.dto.ParentSignupResponseDto;
import mju.capstone.cms.domain.parent.entity.Parent;
import mju.capstone.cms.domain.parent.service.ParentService;
import mju.capstone.cms.global.BaseResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ParentController {

    private final ParentService parentService;

    //회원가입-부모
    @PostMapping("/signup/parent")
    public BaseResponse<ParentSignupResponseDto> signup(@RequestBody ParentSignupRequestDto parentSignupDto) {
        Parent parent = parentService.signup(parentSignupDto.getId(), parentSignupDto.getPassword());

        //자녀 등록 로직 필요.

        return new BaseResponse<>(
            200,
            "학부모 회원가입 성공",
            new ParentSignupResponseDto(parent.getId())
        );
    }

}
