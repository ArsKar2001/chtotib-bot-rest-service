package com.karmanchik.chtotib_bot_rest_service.jpa;

import com.karmanchik.chtotib_bot_rest_service.entity.Lesson;
import com.karmanchik.chtotib_bot_rest_service.entity.Replacement;
import com.karmanchik.chtotib_bot_rest_service.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface JpaTeacherRepository extends JpaRepository<Teacher, Integer> {
    Optional<Teacher> findByName(@NotNull String name);

    @Query("SELECT g.name FROM Group g")
    Optional<List<String>> getAllNames();

    @Query("SELECT t.lessons FROM Teacher t WHERE t.id = :id")
    List<Lesson> getLessonsById(@Param("id") @NotNull Integer id);

    @Query("SELECT t.replacements FROM Teacher t WHERE t.id = :id")
    List<Replacement> getReplacementsById(@Param("id") @NotNull Integer id);
}
