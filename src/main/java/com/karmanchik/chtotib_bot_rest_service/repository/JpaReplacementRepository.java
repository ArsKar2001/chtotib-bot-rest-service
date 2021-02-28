package com.karmanchik.chtotib_bot_rest_service.repository;

import com.karmanchik.chtotib_bot_rest_service.models.Replacement;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface JpaReplacementRepository extends JpaRepository<Replacement, Integer> {
    Optional<List<Replacement>> findByGroupId(@NotNull Integer groupId);

    Optional<List<Replacement>> findByGroupIdAndDateValue(@NotNull Integer groupId, @NotNull LocalDate dateValue);
}
