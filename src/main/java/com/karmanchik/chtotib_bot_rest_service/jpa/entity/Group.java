package com.karmanchik.chtotib_bot_rest_service.jpa.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.*;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

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
    @OneToOne(mappedBy = "group")
    private User user;

    @Setter
    @JsonManagedReference
    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    @OrderBy("day, pairNumber ASC")
    private List<Lesson> lessons;

    @Setter
    @JsonManagedReference
    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Fetch(FetchMode.SUBSELECT)
    @OrderBy(value = "date ASC")
    private List<Replacement> replacements;

    private static GroupBuilder builder() {
        return new GroupBuilder();
    }

    public static GroupBuilder builder(String name) {
        return hiddenBuilder().name(name);
    }

    @Override
    public String toString() {
        return "Group{" +
                "name='" + name + '\'' +
                ", lessons=" + lessons +
                ", replacements=" + replacements +
                ", id=" + id +
                '}';
    }
}

