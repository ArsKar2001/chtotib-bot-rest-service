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

    @Override
    public String toString() {
        return "BotState{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public enum Instance {
        START(100),
        REG(101),
        AUTHORIZED(102);

        private final int id;

        Instance(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }
    }
}
