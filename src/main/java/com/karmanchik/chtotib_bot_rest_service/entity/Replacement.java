package com.karmanchik.chtotib_bot_rest_service.entity;

import com.karmanchik.chtotib_bot_rest_service.converter.JSONObjectConverter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.sql.Date;

@Entity
@Table(name = "replacement")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Replacement extends AbstractBaseEntity {
    @Column(name = "group_id")
    @NotNull
    private Integer groupId;

    @Column(name = "timetable")
    @Convert(converter = JSONObjectConverter.class)
    @NotNull
    private JSONObjectConverter timetable;

    @Column(name = "date")
    @NotNull
    private Date date;
}
