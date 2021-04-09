package com.karmanchik.chtotib_bot_rest_service.jpa.service;

import com.karmanchik.chtotib_bot_rest_service.entity.Replacement;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

public interface ReplacementService extends BaseService<Replacement> {
    List<Replacement> findAllByDate(@NotNull LocalDate date);
}
