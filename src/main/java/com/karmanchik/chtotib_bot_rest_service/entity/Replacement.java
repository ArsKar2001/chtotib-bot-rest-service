package com.karmanchik.chtotib_bot_rest_service.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.*;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "replacement")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Replacement extends BaseEntity {

    @Column(name = "pair_number")
    @NotNull
    private String pairNumber;

    @Column(name = "discipline")
    @NotNull
    private String discipline;

    @Column(name = "auditorium")
    @NotNull
    private String auditorium;

    @Column(name = "date_value")
    @NotNull
    private LocalDate date;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    @JsonBackReference
    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "lessons")
    private List<Teacher> teachers;

    @Override
    public String toString() {
        return "Replacement{" +
                "pairNumber='" + pairNumber + '\'' +
                ", discipline='" + discipline + '\'' +
                ", auditorium='" + auditorium + '\'' +
                ", date=" + date +
                ", group=" + group +
                ", teachers=" + teachers +
                ", id=" + id +
                '}';
    }
}
