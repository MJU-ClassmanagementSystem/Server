package mju.capstone.cms.domain.teacher.Repository;

import mju.capstone.cms.domain.teacher.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeacherRepository extends JpaRepository<Teacher, String> {
}