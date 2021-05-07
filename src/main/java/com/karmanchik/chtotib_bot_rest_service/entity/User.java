package com.karmanchik.chtotib_bot_rest_service.entity;

import com.karmanchik.chtotib_bot_rest_service.entity.converter.PasswordDecodeEncodeConverter;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(name = "login_unique", columnNames = "login"),
                @UniqueConstraint(name = "password_unique", columnNames = "password")
        })
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity {
        @Column(name = "login")
        @NotNull
        private String login;

        @Column(name = "password")
        @Convert(converter = PasswordDecodeEncodeConverter.class)
        @NotNull
        private String password;
}
