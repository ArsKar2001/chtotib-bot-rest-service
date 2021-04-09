package com.karmanchik.chtotib_bot_rest_service.jpa;

import com.karmanchik.chtotib_bot_rest_service.entity.Replacement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Repository
@Transactional
public interface JpaReplacementRepository extends JpaRepository<Replacement, Integer> {
    List<Replacement> findAllByDateOrderByPairNumber(@NotNull LocalDate date);
}
