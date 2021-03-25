package com.karmanchik.chtotib_bot_rest_service.jpa.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "teacher")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Teacher extends BaseEntity {
    @Setter
    @Getter
    @Column(name = "name")
    @NotNull
    private String name;

    @Getter
    @JsonManagedReference
    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL)
    private List<Lesson> lessons;

    @Getter
    @JsonManagedReference
    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL)
    private List<Replacement> replacements;

    @Override
    public String toString() {
        return "Teacher{" +
                "name='" + name + '\'' +
                ", lessons=" + lessons +
                ", replacements=" + replacements +
                ", id=" + id +
                '}';
    }
}
