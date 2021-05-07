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
@Table(name = "groups")
@EqualsAndHashCode(callSuper = true)
@Builder(builderMethodName = "hiddenBuilder")
public class Group extends BaseEntity {
    @Column(name = "name", unique = true)
    @NotNull
    private String name;

    @JsonBackReference
    @OneToOne(mappedBy = "group", fetch = FetchType.LAZY)
    private ChatUser chatUser;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    @OrderBy("day, pairNumber ASC")
    private List<Lesson> lessons;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL)
    @OrderBy(value = "date, pairNumber ASC")
    private List<Replacement> replacements;

    public static GroupBuilder builder(String name) {
        return hiddenBuilder().name(name);
    }

    @Override
    public String toString() {
        return "Group{" +
                "name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}

