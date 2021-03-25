package com.karmanchik.chtotib_bot_rest_service.jpa;

import com.karmanchik.chtotib_bot_rest_service.jpa.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.Optional;

@Repository
@Transactional
public interface JpaTeacherRepository extends JpaRepository<Teacher, Integer> {
    Optional<Teacher> getByName(@NotNull String name);
}
