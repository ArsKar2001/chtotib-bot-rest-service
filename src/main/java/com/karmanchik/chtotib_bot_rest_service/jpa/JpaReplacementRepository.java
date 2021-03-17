package com.karmanchik.chtotib_bot_rest_service.jpa;

import com.karmanchik.chtotib_bot_rest_service.entity.Replacement;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Optional;

public interface JpaReplacementRepository extends JpaRepository<Replacement, Integer> {
    Optional<Replacement> findByGroupId(@NotNull Integer groupId);

    Optional<Replacement> findByGroupIdAndDate(@NotNull Integer groupId, @NotNull LocalDate date);
}
