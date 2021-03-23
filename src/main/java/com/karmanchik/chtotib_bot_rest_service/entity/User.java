package com.karmanchik.chtotib_bot_rest_service.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;


@Entity
@Table(name = "users", uniqueConstraints = {@UniqueConstraint(columnNames = "chat_id", name = "users_unique_chatid_idx")})
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User extends AbstractBaseEntity {
    @Getter
    @Column(name = "chat_id", unique = true)
    @NotNull
    private Integer chatId;

    @Getter
    @Column(name = "user_name", unique = true)
    @NotNull
    private String name;

    @Setter
    @Getter
    @Column(name = "bot_lat_message_id")
    @NotNull
    private Integer botLastMessageId;

    @Setter
    @Getter
    @Column(name = "teacher_id")
    private Integer teacherId;

    @Setter
    @Getter
    @Column(name = "group_id")
    private Integer groupId;

    @Setter
    @Getter
    @Column(name = "user_state_id")
    @NotNull
    private Integer userStateId;

    @Setter
    @Getter
    @Column(name = "bot_state_id")
    @NotNull
    private Integer botStateId;

    @Setter
    @Getter
    @Column(name = "role_id")
    @NotNull
    private Integer roleId;

    @Getter
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "teacher_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Teacher teacher;

    @Getter
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "group_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Group group;

    @Getter
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_state_id", referencedColumnName = "id_user_state", insertable = false, updatable = false)
    private UserState userState;

    @Getter
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "bot_state_id", referencedColumnName = "id_bot_state", insertable = false, updatable = false)
    private BotState botState;

    @Getter
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "role_id", referencedColumnName = "id", insertable = false, updatable = false)
    private Role role;

    @Override
    public String toString() {
        return "User{" +
                "chatId=" + chatId +
                ", name='" + name + '\'' +
                ", botLastMessageId=" + botLastMessageId +
                ", teacherId=" + teacherId +
                ", groupId=" + groupId +
                ", userStateId=" + userStateId +
                ", botStateId=" + botStateId +
                ", roleId=" + roleId +
                ", teacher=" + teacher +
                ", group=" + group +
                ", userState=" + userState +
                ", botState=" + botState +
                ", role=" + role +
                ", id=" + id +
                '}';
    }
}
