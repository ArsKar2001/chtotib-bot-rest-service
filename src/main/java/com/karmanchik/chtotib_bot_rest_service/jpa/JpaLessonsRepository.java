package com.karmanchik.chtotib_bot_rest_service.jpa;

import com.karmanchik.chtotib_bot_rest_service.entity.Group;
import com.karmanchik.chtotib_bot_rest_service.entity.Lesson;
import com.karmanchik.chtotib_bot_rest_service.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.OrderBy;
import javax.validation.constraints.NotNull;
import java.util.List;

@Repository
@Transactional
public interface JpaLessonsRepository extends JpaRepository<Lesson, Integer> {
    List<Lesson> findAllByGroupsOrderByDayPairNumberAsc(List<Group> groups);
    List<Lesson> findAllByTeachersOOrderByDayPairNumberAsc(List<Teacher> teachers);

    List<Lesson> findAllByDayOOrderByPairNumber(@NotNull Integer day);
}
