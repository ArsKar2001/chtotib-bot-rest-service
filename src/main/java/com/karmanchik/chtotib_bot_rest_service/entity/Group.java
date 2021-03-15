package com.karmanchik.chtotib_bot_rest_service.entity;

import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;

@Entity
@Table(
        name = "groups",
        uniqueConstraints = {
                @UniqueConstraint(
                        columnNames = "group_name",
                        name = "schedule_group_name_uindex")
        })
@Getter
@Setter
@TypeDef(name = "json", typeClass = JsonStringType.class)
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Group extends AbstractBaseEntity {

    @Column(name = "group_name", nullable = false, unique = true)
    @NotNull
    private String groupName;

    @Column(name = "timetable", columnDefinition = "json")
    @Type(type = "json")
    @NotNull
    private String lessons = "[]";

    public Group(String groupName) {
        this.groupName = groupName;
    }
}
