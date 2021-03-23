package com.karmanchik.chtotib_bot_rest_service.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.*;
import org.hibernate.annotations.Type;
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
@Getter
@Setter
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Group extends AbstractBaseEntity {
    @Column(name = "name", unique = true)
    @NotNull
    private String name;

    @Getter
    @JsonManagedReference
    @OneToMany(mappedBy = "group", fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @OrderBy("day, pairNumber ASC")
    private List<Lesson> lessons;

    @Getter
    @JsonManagedReference
    @OneToMany(mappedBy = "group", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy(value = "date ASC")
    private List<Replacement> replacements;

    public Group(String name) {
        this.name = name;
    }

}
