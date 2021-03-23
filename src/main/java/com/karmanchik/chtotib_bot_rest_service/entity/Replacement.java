package com.karmanchik.chtotib_bot_rest_service.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "replacement")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Replacement extends AbstractBaseEntity {

    @Getter
    @Setter
    @Column(name = "pair_number")
    @NotNull
    private Integer pairNumber;

    @Getter
    @Setter
    @Column(name = "discipline")
    @NotNull
    private String discipline;

    @Getter
    @Setter
    @Column(name = "auditorium")
    @NotNull
    private String auditorium;

    @Getter
    @Setter
    @Column(name = "date_value")
    @NotNull
    private LocalDate date;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "group_id")
    private Group group;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

}
