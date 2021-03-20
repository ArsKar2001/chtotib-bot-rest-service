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
    @Setter
    @Getter
    @Column(name = "lessons", columnDefinition = "jsonb", nullable = false)
    @Type(type = "jsonb")
    private String lessons;

    @Getter
    @Setter
    @Column(name = "date_value")
    @NotNull
    private LocalDate date;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "group_id")
    private Group group;

    @Override
    public String toString() {
        return "Replacement{" +
                "timetable='" + lessons + '\'' +
                ", date=" + date +
                ", group=" + group +
                ", id=" + id +
                '}';
    }
}
