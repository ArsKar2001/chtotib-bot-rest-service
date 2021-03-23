package com.karmanchik.chtotib_bot_rest_service.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "lessons")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Lesson extends AbstractBaseEntity {

    @Setter
    @Getter
    @Column(name = "group_id")
    @NotNull
    private Integer groupId;

    @Setter
    @Getter
    @Column(name = "teacher_id")
    @NotNull
    private Integer teacherId;

    @Setter
    @Getter
    @Column(name = "pair_number")
    @NotNull
    private Integer pairNumber;

    @Setter
    @Getter
    @Column(name = "day")
    @NotNull
    private Integer day;

    @Setter
    @Getter
    @Column(name = "discipline", nullable = false)
    private String discipline;

    @Setter
    @Getter
    @Column(name = "auditorium", nullable = false)
    @NotNull
    private String auditorium;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "group_id")
    private Group group;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;
}
