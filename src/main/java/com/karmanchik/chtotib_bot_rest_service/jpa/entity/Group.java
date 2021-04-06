package com.karmanchik.chtotib_bot_rest_service.jpa.entity;

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
                        name = "schedule_group_name_uindex")
        })
@EqualsAndHashCode(callSuper = true)
@Getter
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@Builder(builderMethodName = "hiddenBuilder")
@AllArgsConstructor
@NoArgsConstructor
public class Group extends BaseEntity {
    @Setter
    @Column(name = "name", unique = true)
    @NotNull
    private String name;

    @Setter
    @JsonBackReference
    @OneToOne(mappedBy = "group")
    private User user;

    @Setter
    @JsonIgnore
    @OneToMany(mappedBy = "group")
    @OrderBy("day, pairNumber ASC")
    private List<Lesson> lessons;

    @Setter
    @JsonIgnore
    @OneToMany(mappedBy = "group")
    @OrderBy(value = "date ASC")
    private List<Replacement> replacements;

    public static GroupBuilder builder(String name) {
        return hiddenBuilder().name(name);
    }

    public List<Replacement> getReplacements() {
        return replacements;
    }

    @Override
    public String toString() {
        return "Group{" +
                "name='" + name + '\'' +
                ", id=" + id +
                '}';
    }
}

