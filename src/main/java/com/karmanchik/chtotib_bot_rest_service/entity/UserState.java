package com.karmanchik.chtotib_bot_rest_service.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "user_state")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserState {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_user_state", unique = true)
    @NotNull
    private Integer id;

    @Column(name = "code_user_state", unique = true, nullable = false)
    @NotBlank
    private String code;

    @Column(name = "description_user_state")
    private String description;

    @Override
    public String toString() {
        return "UserState{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public enum Instance {
        NONE(200),
        SELECT_COURSE(201),
        SELECT_GROUP(202),
        SELECT_ROLE(203),
        SELECT_OPTION(204),
        ENTER_NAME(205),
        SELECT_TEACHER(206);

        private final int id;

        Instance(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }
}
