package com.karmanchik.chtotib_bot_rest_service.repository;

import com.karmanchik.chtotib_bot_rest_service.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotNull;
import java.util.Optional;


@Repository
@Transactional(readOnly = true)
public interface JpaGroupRepository extends JpaRepository<Group, Integer> {
    Optional<Group> findByGroupName(@NotNull String groupName);
    @Query("SELECT g.id FROM Group g WHERE g.groupName = :groupName")
    Integer getGroupIdByGroupName(@Param("groupName") @NotNull String groupName);
}
