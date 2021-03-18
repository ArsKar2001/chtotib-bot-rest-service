package com.karmanchik.chtotib_bot_rest_service.entity;

import com.vladmihalcea.hibernate.type.json.JsonStringType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "replacement")
@TypeDef(name = "json", typeClass = JsonStringType.class)
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Replacement extends AbstractBaseEntity {
    @Column(name = "group_id")
    @NotNull
    private Integer groupId;

    @Column(name = "timetable", columnDefinition = "json")
    @Type(type = "json")
    @NotNull
    private String timetable = "[]";

    @Column(name = "date_value")
    @NotNull
    private LocalDate date;
}
