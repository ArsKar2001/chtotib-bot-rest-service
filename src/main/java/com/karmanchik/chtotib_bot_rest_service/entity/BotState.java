package com.karmanchik.chtotib_bot_rest_service.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "bot_state")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class BotState {
    public static final Integer START = 100;
    public static final Integer REG = 101;
    public static final Integer AUTHORIZED = 102;


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id_bot_state", unique = true)
    @NotNull
    private Integer id;

    @Column(name = "code_bot_state", unique = true, nullable = false)
    @NotBlank
    private String code;

    @Column(name = "description_bot_state")
    private String description;

    public BotState(@NotNull Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "BotState{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
