package com.karmanchik.chtotib_bot_rest_service.entity;

import com.karmanchik.chtotib_bot_rest_service.model.GroupNone;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Entity
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = "chat_id", name = "users_unique_chatid_idx")})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User extends AbstractBaseEntity {
    private static final Integer GROUP_NONE_ID = 100;

    @Column(name = "chat_id", unique = true, nullable = false)
    @NotNull
    private Integer chatId;

    @Column(name = "name")
    @NotBlank
    private String name;

    @Column(name = "bot_lat_message_id", nullable = false)
    @NotBlank
    private Integer botLastMessageId;

    @Column(name = "bot_state_id", nullable = false)
    @NotBlank
    private Integer botStateId;

    @Column(name = "user_state_id", nullable = false)
    @NotBlank
    private Integer userStateId;

    @Column(name = "role_id", nullable = false)
    @NotBlank
    private Integer roleId;

    @Column(name = "group_id", nullable = false)
    @NotBlank
    private Integer groupId;


    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "group_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Group group = GroupNone.getInstance();

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_state_id", referencedColumnName = "id_user_state", insertable = false, updatable = false)
    private UserState userState;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "bot_state_id", referencedColumnName = "id_bot_state", insertable = false, updatable = false)
    private BotState botState;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "role_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Role role;
}
