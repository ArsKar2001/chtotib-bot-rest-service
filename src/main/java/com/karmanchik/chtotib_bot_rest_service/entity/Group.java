package com.karmanchik.chtotib_bot_rest_service.entity;

import com.karmanchik.chtotib_bot_rest_service.converter.JpaConverterJson;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "groups", uniqueConstraints = {@UniqueConstraint(columnNames = "group_name", name = "schedule_group_name_uindex")})
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Group extends AbstractBaseEntity {
    @Column(name = "group_name", nullable = false, unique = true)
    @NotNull private String groupName;

    @Column(name = "timetable")
    @Convert(converter = JpaConverterJson.class)
    @NotNull private JpaConverterJson timetable;

    public Group(@NotNull String groupName) {
        this.groupName = groupName;
    }
}
