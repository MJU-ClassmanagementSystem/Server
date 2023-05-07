package mju.capstone.cms.domain.student.dto;

import lombok.Data;

@Data
public class StudentDto {
    private String id;
    private String name;
    private String teacherId;
    private String parentId;
}
