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
@NoArgsConstructor
@Table(name = "chat_users", uniqueConstraints = {
        @UniqueConstraint(name = "chat_id_unique", columnNames = "chat_id"),
        @UniqueConstraint(name = "user_name_unique", columnNames = "user_name")
})
@EqualsAndHashCode(callSuper = true)
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class ChatUser extends BaseEntity {
    @Column(name = "chat_id", unique = true)
    @NotNull
    private Integer chatId;

    @Column(name = "user_name", unique = true)
    @NotNull
    private String userName;

    @JsonManagedReference
    @OneToOne(cascade = CascadeType.ALL)
    @JoinTable(name = "user_teacher",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)},
            inverseJoinColumns = {@JoinColumn(name = "teacher_id", referencedColumnName = "id", nullable = false)})
    private Teacher teacher;

    @JsonManagedReference
    @OneToOne(cascade = CascadeType.ALL)
    @JoinTable(name = "user_group",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)},
            inverseJoinColumns = {@JoinColumn(name = "group_id", referencedColumnName = "id", nullable = false)})
    private Group group;


    @Column(name = "role_id")
    private Role role;

    @Column(name = "user_state_id")
    @NotNull
    private UserState userState;

    @Column(name = "bot_state_id")
    @NotNull
    private BotState botState;

    @Override
    public String toString() {
        return "User{" +
                "chatId=" + chatId +
                ", userName='" + userName + '\'' +
                ", teacher=" + teacher +
                ", group=" + group +
                ", role=" + role +
                ", userState=" + userState +
                ", botState=" + botState +
                ", id=" + id +
                '}';
    }
}

