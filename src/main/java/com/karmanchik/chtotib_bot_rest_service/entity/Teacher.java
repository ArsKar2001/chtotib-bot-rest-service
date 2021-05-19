package com.karmanchik.chtotib_bot_rest_service.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Entity
@AllArgsConstructor
@Table(name = "teacher")
@EqualsAndHashCode(callSuper = true)
@Builder(builderMethodName = "hiddenBuilder")
public class Teacher extends BaseEntity {

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "teacher", fetch = FetchType.LAZY)
    private List<ChatUser> chatUsers;

    @ManyToMany(mappedBy = "teachers", cascade = CascadeType.ALL)
    private List<Lesson> lessons;

    @ManyToMany(cascade = CascadeType.ALL, mappedBy = "teachers")
    private List<Replacement> replacements;

    public Teacher() {

    }

    @Override
    public String toString() {
        return "Teacher{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
