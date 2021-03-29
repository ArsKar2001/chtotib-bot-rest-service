package com.karmanchik.chtotib_bot_rest_service.jpa.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "teacher")
@Builder(builderMethodName = "hiddenBuilder")
@NoArgsConstructor
@AllArgsConstructor
public class Teacher extends BaseEntity {
    @Setter
    @Getter
    @Column(name = "name")
    @NotNull
    private String name;

    @Setter
    @OneToOne(mappedBy = "teacher")
    private User user;

    @Getter
    @JsonManagedReference
    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    @OrderBy("day, pairNumber ASC")
    private List<Lesson> lessons;

    @Getter
    @JsonManagedReference
    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    @OrderBy("date, pairNumber ASC")
    private List<Replacement> replacements;

    public static TeacherBuilder builder(String name) {
        return hiddenBuilder().name(name);
    }

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
