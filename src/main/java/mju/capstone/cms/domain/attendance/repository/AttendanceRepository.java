package mju.capstone.cms.domain.attendance.repository;

import mju.capstone.cms.domain.attendance.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    @Query("SELECT a FROM Attendance a JOIN a.student st WHERE  a.date BETWEEN :startDate AND :endDate AND st.id = :studentId")
    Attendance findByStudentIdAndDateBetween(@Param("studentId") String studentId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}