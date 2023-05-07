package mju.capstone.cms.domain.parent.service;

import lombok.RequiredArgsConstructor;
import mju.capstone.cms.domain.parent.entity.Parent;
import mju.capstone.cms.domain.parent.repository.ParentRepository;
import mju.capstone.cms.domain.student.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class ParentService {

    private final ParentRepository parentRepository;
    private final StudentRepository studentRepository;

    //회원가입
    public Parent signup(String id, String password, String studentId) {

        //이미 있는 아이디 -> 예외
        parentRepository.findById(id)
            .ifPresent(parent -> {
                throw new IllegalStateException("parent already exists");
            });

        Parent p = Parent.builder()
            .id(id)
            .password(password)
            .build();
        Parent saved = parentRepository.save(p);

        studentRepository.findById(studentId)
            .ifPresent(student -> student.setParent(saved));

        return saved;
    }
}
