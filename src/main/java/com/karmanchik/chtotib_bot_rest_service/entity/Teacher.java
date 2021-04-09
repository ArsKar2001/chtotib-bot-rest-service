package com.karmanchik.chtotib_bot_rest_service.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

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
    @LazyCollection(LazyCollectionOption.TRUE)
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "teacher_lesson",
            joinColumns = {@JoinColumn(name = "teacher_id")},
            inverseJoinColumns = {@JoinColumn(name = "lesson_id")})
    @OrderBy("day, pairNumber ASC")
    private List<Lesson> lessons;

    @JsonIgnore
    @LazyCollection(LazyCollectionOption.TRUE)
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "teacher_replacement",
            joinColumns = @JoinColumn(name = "teacher_id"),
            inverseJoinColumns = @JoinColumn(name = "replacement_id"))
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
