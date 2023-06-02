package mju.capstone.cms.domain.subject.Repository;

import mju.capstone.cms.domain.subject.entity.Subject;
import mju.capstone.cms.domain.teacher.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
    @Query("SELECT s FROM Subject s WHERE s.subjectName <> '쉬는시간' And s.teacher = :teacher")
    List<Subject> findByTeacher(Teacher teacher);
}