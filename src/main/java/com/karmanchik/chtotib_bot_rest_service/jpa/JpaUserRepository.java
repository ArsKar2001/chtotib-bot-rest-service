package com.karmanchik.chtotib_bot_rest_service.jpa;

import com.karmanchik.chtotib_bot_rest_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.Optional;

@Repository
@Transactional
public interface JpaUserRepository extends JpaRepository<User, Integer> {
    Optional<User> getByLoginAndPassword(@NotNull String login, @NotNull String password);
}
