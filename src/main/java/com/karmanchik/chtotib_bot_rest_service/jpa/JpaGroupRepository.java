package com.karmanchik.chtotib_bot_rest_service.jpa;

import com.karmanchik.chtotib_bot_rest_service.jpa.entity.Group;
import com.karmanchik.chtotib_bot_rest_service.jpa.entity.Lesson;
import com.karmanchik.chtotib_bot_rest_service.jpa.entity.Replacement;
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
public interface JpaGroupRepository extends JpaRepository<Group, Integer> {
    Optional<Group> getByName(@NotNull String groupName);

    @Query("SELECT g.name FROM Group g")
    Optional<List<String>> getAllGroupName();

    @Query("SELECT g.lessons FROM Group g WHERE g.id = :id")
    List<Lesson> getLessonsById(@Param("id") @NotNull Integer id);

    @Query("SELECT g.replacements FROM Group g " +
            "WHERE g.id = :id")
    List<Replacement> getReplacementsById(@Param("id") @NotNull Integer id);
}
