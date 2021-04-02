package com.karmanchik.chtotib_bot_rest_service.jpa.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.karmanchik.chtotib_bot_rest_service.jpa.enums.WeekType;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "lessons")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Lesson extends BaseEntity {
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
    private String auditorium;

    @Setter
    @Getter
    @Column(name = "group_name", nullable = false)
    private String groupName;

    @Setter
    @Getter
    @Column(name = "teacher_name", nullable = false)
    private String teacherName;

    @Setter
    @Getter
    @Column(name = "week_type")
    @NotNull
    private WeekType weekType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "group_id")
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @Override
    public String toString() {
        return "Lesson{" +
                "pairNumber=" + pairNumber +
                ", day=" + day +
                ", discipline='" + discipline + '\'' +
                ", auditorium='" + auditorium + '\'' +
                ", weekType=" + weekType +
                ", group=" + group.getId() +
                ", teacher=" + teacher.getId() +
                ", id=" + id +
                '}';
    }
}
