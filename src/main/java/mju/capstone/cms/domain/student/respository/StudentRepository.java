package mju.capstone.cms.domain.student.respository;

import mju.capstone.cms.domain.student.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<Student, String> {
}
