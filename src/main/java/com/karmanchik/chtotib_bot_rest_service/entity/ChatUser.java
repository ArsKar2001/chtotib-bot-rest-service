package com.karmanchik.chtotib_bot_rest_service.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.karmanchik.chtotib_bot_rest_service.entity.enums.BotState;
import com.karmanchik.chtotib_bot_rest_service.entity.enums.Role;
import com.karmanchik.chtotib_bot_rest_service.entity.enums.UserState;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.*;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;


@Data
@Entity
@Builder
@AllArgsConstructor
@Table(name = "chat_users", uniqueConstraints = {
        @UniqueConstraint(name = "chat_id_unique", columnNames = "chat_id"),
        @UniqueConstraint(name = "user_name_unique", columnNames = "user_name")
})
@EqualsAndHashCode(callSuper = true)
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class ChatUser extends BaseEntity {
    @Column(name = "chat_id")
    private Long chatId;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "bot_state_id")
    private BotState botState;

    @Column(name = "user_state_id")
    private UserState userState;

    @Column(name = "role_id")
    private Role role;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(name = "user_group",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)},
            inverseJoinColumns = {@JoinColumn(name = "group_id", referencedColumnName = "id", nullable = false)})
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinTable(name = "user_teacher",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)},
            inverseJoinColumns = {@JoinColumn(name = "teacher_id", referencedColumnName = "id", nullable = false)})
    private Teacher teacher;

    public ChatUser() {}

    @Override
    public String toString() {
        return "ChatUser{" +
                "id=" + id +
                ", chatId=" + chatId +
                ", userName='" + userName + '\'' +
                ", botState=" + botState +
                ", userState=" + userState +
                ", role=" + role +
                '}';
    }
}

