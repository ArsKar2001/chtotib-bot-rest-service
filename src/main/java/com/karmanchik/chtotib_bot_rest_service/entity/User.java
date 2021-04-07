package com.karmanchik.chtotib_bot_rest_service.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.karmanchik.chtotib_bot_rest_service.jpa.enums.BotState;
import com.karmanchik.chtotib_bot_rest_service.jpa.enums.Role;
import com.karmanchik.chtotib_bot_rest_service.jpa.enums.UserState;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.*;
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
}

