package mju.capstone.cms.domain.emotion.dto;


import lombok.*;
import mju.capstone.cms.domain.student.entity.Student;
import mju.capstone.cms.domain.subject.entity.Subject;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDate;

@Builder
@Getter
@NoArgsConstructor
@ToString
@AllArgsConstructor
public class WebSocketEmotionDto {

    private int count = 0;

    private Double angry = 0.0;

    private Double disgust = 0.0;

    private Double fear = 0.0;

    private Double happy = 0.0;

    private Double sad = 0.0;

    private Double surprise = 0.0;

    private Double neutral = 0.0;

    private String studentId;

    public WebSocketEmotionDto(String studentId) {
        this.studentId = studentId;
    }

    //count 증가, 감정들도 더하기, 쓸때 emotions/count로 해서 db 넣기
    public void updateEmotion(WebSocketEmotionDto emotionDto) {
        count += 1;
        angry += emotionDto.getAngry();
        disgust += emotionDto.getDisgust();
        fear += emotionDto.getFear();
        happy += emotionDto.getHappy();
        sad += emotionDto.getSad();
        surprise += emotionDto.getSurprise();
        neutral += emotionDto.getNeutral();
    }
}
