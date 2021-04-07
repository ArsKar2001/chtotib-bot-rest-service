package com.karmanchik.chtotib_bot_rest_service.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.*;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Table(
        name = "groups",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = "name",
                        name = "schedule_group_name_uindex")})
@Getter
@Setter
@Builder(builderMethodName = "hiddenBuilder")
@AllArgsConstructor
@NoArgsConstructor
public class Group extends BaseEntity {
    @Column(name = "name", unique = true)
    @NotNull
    private String name;

    @JsonBackReference
    @OneToOne(mappedBy = "group")
    private User user;

    @JsonManagedReference
    @LazyCollection(LazyCollectionOption.TRUE)
    @ManyToMany(mappedBy = "groups", cascade = CascadeType.ALL)
    @JoinTable(
            name = "group_lesson",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "lesson_id"))
    @OrderBy("day, pairNumber ASC")
    private List<Lesson> lessons;

    @JsonManagedReference
    @LazyCollection(LazyCollectionOption.TRUE)
    @ManyToMany(mappedBy = "groups", cascade = CascadeType.ALL)
    @JoinTable(
            name = "group_replacement",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "replacement_id"))
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

