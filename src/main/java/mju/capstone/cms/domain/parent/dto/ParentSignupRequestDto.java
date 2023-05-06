package mju.capstone.cms.domain.parent.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParentSignupRequestDto {
    private String id;
    private String password;
    private String studentId;
}
