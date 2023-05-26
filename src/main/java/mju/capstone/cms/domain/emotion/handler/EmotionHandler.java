package mju.capstone.cms.domain.emotion.handler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mju.capstone.cms.domain.auth.jwt.provider.JwtProvider;
import mju.capstone.cms.domain.emotion.dto.WebSocketEmotionDto;
import mju.capstone.cms.domain.emotion.entity.Emotion;
import mju.capstone.cms.domain.emotion.repository.EmotionRepository;
import mju.capstone.cms.domain.student.entity.Student;
import mju.capstone.cms.domain.student.repository.StudentRepository;
import mju.capstone.cms.domain.teacher.Repository.TeacherRepository;
import mju.capstone.cms.domain.teacher.entity.Teacher;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmotionHandler extends TextWebSocketHandler {

    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final EmotionRepository emotionRepository;
    private final JwtProvider jwtProvider;
    private final ObjectMapper objectMapper = new ObjectMapper();

    //연결된 클라이언트
    private WebSocketSession socketSession;
    //연결된 클라이언트의 학생들 감정을 저장할 객체
    private Map<String, WebSocketEmotionDto> emotions = new HashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        //연결
        socketSession = session;
//        jwt parsing -> teacherId에 해당하는 students 찾기
//        헤더가 websocket에서는 안된다는데 ?
//        log.info("session : {}", session.getHandshakeHeaders().);
//        String token = session.getHandshakeHeaders().getFirst("Authorization");
        String authorization = session.getUri().getQuery().split("=")[1];
        initEmotions("Bearer " + authorization);
    }

    //감정들 초기화
    private void initEmotions(String token) {
        emotions.clear();
        String teacherId = jwtProvider.extractId(token);
        Teacher teacher = teacherRepository.findById(teacherId).orElseThrow(() -> new IllegalArgumentException("teacherId 없습니다."));
        List<Student> students = studentRepository.findByTeacher(teacher);
        // key : studentId, value = WebSocketEmotionDto 의 Map에 저장.
        for (Student student : students) {
            emotions.put(student.getId(), new WebSocketEmotionDto(student.getId()));
        }
        log.info("teacherId({}) connected.", teacherId);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        //json 데이터 WebSocketEmotionDto로 매핑
        String payload = message.getPayload();
        List<WebSocketEmotionDto> webSocketEmotionDtos = objectMapper.readValue(payload, new TypeReference<List<WebSocketEmotionDto>>() {
        });
        //매핑된 데이터 업데이트하기.
        for (WebSocketEmotionDto webSocketEmotionDto : webSocketEmotionDtos) {
            log.info("webSocketEmotionDto : {}", webSocketEmotionDto);
            emotions.get(webSocketEmotionDto.getStudentId()).updateEmotion(webSocketEmotionDto);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        //db에 저장하기.
        for (WebSocketEmotionDto value : emotions.values()) {
            int count = value.getCount();
            Student student = studentRepository.findById(value.getStudentId())
                .orElseThrow(() -> new IllegalArgumentException("student dne"));
            Emotion emotion = Emotion.builder()
                .student(student)
                .angry(value.getAngry() / count)
                .disgust(value.getDisgust() / count)
                .fear(value.getFear() / count)
                .happy(value.getHappy() / count)
                .sad(value.getSad() / count)
                .surprise(value.getSurprise() / count)
                .neutral(value.getNeutral() / count)
                .build();
            emotionRepository.save(emotion);
        }
        session = null;
        emotions.clear();
        log.info("disconnected.");
    }


}
