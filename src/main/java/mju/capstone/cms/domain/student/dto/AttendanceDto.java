package mju.capstone.cms.domain.student.dto;

import lombok.Data;

import java.util.List;

@Data
public class AttendanceDto {
    private String id;
    private String name;
    private List<Long> attend;
}