package com.karmanchik.chtotib_bot_rest_service.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "teacher")
@EqualsAndHashCode(callSuper = true)
@Builder(builderMethodName = "hiddenBuilder")
public class Teacher extends BaseEntity {

    @Column(name = "name")
    @NotNull
    private String name;

    @JsonBackReference
    @OneToOne(mappedBy = "teacher", fetch = FetchType.LAZY)
    private ChatUser chatUser;

    @ManyToMany(cascade = CascadeType.REFRESH, mappedBy = "teachers")
    @OrderBy("day, pairNumber ASC")
    private List<Lesson> lessons;

    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "teachers")
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
