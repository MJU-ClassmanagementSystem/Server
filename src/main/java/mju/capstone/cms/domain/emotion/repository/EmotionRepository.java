package mju.capstone.cms.domain.emotion.repository;

import mju.capstone.cms.domain.emotion.entity.Emotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface EmotionRepository extends JpaRepository<Emotion, Long> {
    // 한 과목에 대한 감정 객체, 특정 날짜 범위 지정 between
    @Query("SELECT e FROM Emotion e JOIN e.subject s WHERE s.id = :subjectId AND e.date BETWEEN :startDate AND :endDate")
    List<Emotion> findBySubjectIdAndDateBetween(@Param("subjectId") Long subjectId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);


    @Query("SELECT e FROM Emotion e JOIN e.subject s JOIN e.student st WHERE s.id = :subjectId AND e.date BETWEEN :startDate AND :endDate AND st.id = :studentId")
    Emotion findBySubjectIdAndStudentIdAndDateBetween(@Param("subjectId") Long subjectId, @Param("studentId") String studentId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);


    @Query("SELECT e FROM Emotion e  JOIN e.student st WHERE  e.date BETWEEN :startDate AND :endDate AND st.id = :studentId")
    List<Emotion> findByStudentIdAndDateBetween(@Param("studentId") String studentId, @Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}