package mju.capstone.cms.domain.student.repository;

import mju.capstone.cms.domain.parent.entity.Parent;
import mju.capstone.cms.domain.student.entity.Student;
import mju.capstone.cms.domain.teacher.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRepository extends JpaRepository<Student, String> {
    List<Student> findByTeacher(Teacher teacher);
    List<Student> findByParent(Parent parent);
}