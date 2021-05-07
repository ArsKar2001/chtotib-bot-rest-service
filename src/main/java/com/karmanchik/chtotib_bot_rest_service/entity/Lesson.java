package com.karmanchik.chtotib_bot_rest_service.entity;

import com.karmanchik.chtotib_bot_rest_service.entity.enums.WeekType;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "lessons")
@EqualsAndHashCode(callSuper = true)
public class Lesson extends BaseEntity {

    @Column(name = "pair_number")
    @NotNull
    private Integer pairNumber;

    @Column(name = "day")
    @NotNull
    private Integer day;

    @Column(name = "discipline", nullable = false)
    private String discipline;

    @Column(name = "auditorium", nullable = false)
    private String auditorium;

    @Column(name = "week_type")
    @NotNull
    private WeekType weekType;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "group_id")
    private Group group;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Teacher> teachers;

    @Override
    public String toString() {
        return "Lesson{" +
                "pairNumber=" + pairNumber +
                ", day=" + day +
                ", discipline='" + discipline + '\'' +
                ", auditorium='" + auditorium + '\'' +
                ", weekType=" + weekType +
                ", id=" + id +
                '}';
    }
}
