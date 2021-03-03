package com.karmanchik.chtotib_bot_rest_service.models;

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
@NoArgsConstructor
public class Replacement extends AbstractBaseEntity {
    @Column(name = "group_id")
    @NotNull
    private Integer groupId;

    @Column(name = "timetable", columnDefinition = "json")
    @Type(type = "json")
    @NotNull
    private String timetable;

    @Column(name = "date")
    @NotNull
    private LocalDate date;

    public Replacement(ReplacementBuilder replacementBuilder) {
        groupId = replacementBuilder.groupId;
        timetable = replacementBuilder.timetable;
        date = replacementBuilder.date;
    }

    public static class ReplacementBuilder {
        private Integer groupId;
        private String timetable;
        private LocalDate date;

        public ReplacementBuilder setGroupId(Integer groupId) {
            this.groupId = groupId;
            return this;
        }

        public ReplacementBuilder setTimetable(String timetable) {
            this.timetable = timetable;
            return this;
        }

        public ReplacementBuilder setDate(LocalDate date) {
            this.date = date;
            return this;
        }

        public Replacement build() {
            return new Replacement(this);
        }
    }
}
