package com.karmanchik.chtotib_bot_rest_service.jpa.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(name = "teacher")
@Getter
@Setter
@Builder(builderMethodName = "hiddenBuilder")
@NoArgsConstructor
@AllArgsConstructor
public class Teacher extends BaseEntity {
    @Column(name = "name")
    @NotNull
    private String name;

    @JsonBackReference
    @OneToOne(mappedBy = "teacher")
    private User user;

    @JsonIgnore
    @OneToMany(mappedBy = "teacher")
    @OrderBy("day, pairNumber ASC")
    private List<Lesson> lessons;

    @JsonIgnore
    @OneToMany(mappedBy = "teacher")
    @OrderBy("date, pairNumber ASC")
    private List<Replacement> replacements;

    public static TeacherBuilder builder(String name) {
        return hiddenBuilder().name(name);
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}
