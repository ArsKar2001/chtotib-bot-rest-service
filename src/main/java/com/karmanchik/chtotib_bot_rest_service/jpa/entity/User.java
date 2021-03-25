package com.karmanchik.chtotib_bot_rest_service.jpa.entity;

import com.karmanchik.chtotib_bot_rest_service.jpa.enums.BotState;
import com.karmanchik.chtotib_bot_rest_service.jpa.enums.Role;
import com.karmanchik.chtotib_bot_rest_service.jpa.enums.UserState;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import javax.validation.constraints.NotNull;


@Entity
@Table(name = "users")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User extends BaseEntity {
    @Column(name = "chat_id", unique = true)
    @NotNull
    private Integer chatId;

    @Column(name = "user_name", unique = true)
    @NotNull
    private String userName;

    @Column(name = "user_state_id", insertable = false, updatable = false)
    @NotNull
    private Integer userStateId;

    @Column(name = "bot_state_id", insertable = false, updatable = false)
    @NotNull
    private Integer botStateId;

    @Column(name = "role_id", insertable = false, updatable = false)
    @NotNull
    private Integer roleId;

    @Column(name = "data", nullable = false)
    @Type(type = "jsonb")
    private BaseEntity data;


    @Column(name = "role_id")
    private Role role;

    @Column(name = "user_state_id")
    @NotNull
    private UserState userState;

    @Column(name = "bot_state_id")
    @NotNull
    private BotState botState;
}

