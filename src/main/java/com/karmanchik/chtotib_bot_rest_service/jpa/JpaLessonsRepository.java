package com.karmanchik.chtotib_bot_rest_service.jpa;

import com.karmanchik.chtotib_bot_rest_service.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.List;

@Repository
@Transactional
public interface JpaLessonsRepository extends JpaRepository<Lesson, Integer> {
    List<Lesson> findAllByDayOrderByPairNumber(@NotNull Integer day);
}
