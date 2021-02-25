package com.karmanchik.chtotib_bot_rest_service.repository;

import com.karmanchik.chtotib_bot_rest_service.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public interface JpaUserRepository extends JpaRepository<User, Integer> {
}
