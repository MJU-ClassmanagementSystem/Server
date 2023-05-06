package mju.capstone.cms.domain.student.service;

import lombok.RequiredArgsConstructor;
import mju.capstone.cms.domain.parent.repository.ParentRepository;
import mju.capstone.cms.domain.student.dto.StudentRegisterResponseDto;
import mju.capstone.cms.domain.student.entity.Student;
import mju.capstone.cms.domain.student.respository.StudentRepository;
import mju.capstone.cms.domain.teacher.Repository.TeacherRepository;
import mju.capstone.cms.domain.teacher.entity.Teacher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final ParentRepository parentRepository;

    public StudentRegisterResponseDto register(String id, String name, String teacherId) {
        Teacher teacher = teacherRepository.findById(teacherId)
            .orElseThrow(() -> new IllegalStateException("Teacher이 없습니다"));
        Student student = Student.builder()
            .id(id)
            .name(name)
            .teacher(teacher)
            .build();
        studentRepository.save(student);
        return new StudentRegisterResponseDto(id, name, teacherId);
    }

}
