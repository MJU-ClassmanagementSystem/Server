package mju.capstone.cms.domain.subject.Repository;

import mju.capstone.cms.domain.subject.entity.Subject;
import mju.capstone.cms.domain.teacher.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
    List<Subject> findByTeacher(Teacher teacher);
}