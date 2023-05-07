package mju.capstone.cms.domain.focus.repository;

import mju.capstone.cms.domain.focus.entity.Focus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;

public interface FocusRepository extends JpaRepository<Focus, Long> {

    // 한 과목에 대한 집중 객체, 특정 날짜 범위 지정 between
    @Query("SELECT f FROM Focus f JOIN f.subject s WHERE s.id = :subjectId AND f.date BETWEEN :startDate AND :endDate")
    List<Focus> findBySubjectIdAndDateBetween(@Param("subjectId") Long subjectId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT f FROM Focus f JOIN f.subject s JOIN f.student st WHERE s.id = :subjectId AND f.date BETWEEN :startDate AND :endDate AND st.id = :studentId")
    Focus findBySubjectIdAndStudentIdAndDateBetween(@Param("subjectId") Long subjectId, @Param("studentId") String studentId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}