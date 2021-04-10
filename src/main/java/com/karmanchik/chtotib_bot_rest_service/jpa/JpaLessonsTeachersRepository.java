package com.karmanchik.chtotib_bot_rest_service.jpa;

import com.karmanchik.chtotib_bot_rest_service.entity.TeachersLessons;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface JpaLessonsTeachersRepository extends JpaRepository<TeachersLessons, Integer> {
}
