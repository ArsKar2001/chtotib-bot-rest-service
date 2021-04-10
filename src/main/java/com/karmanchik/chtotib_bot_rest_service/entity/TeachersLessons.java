package com.karmanchik.chtotib_bot_rest_service.entity;

import javax.persistence.*;

@Entity
@Table(name = "lessons_teachers")
public class TeachersLessons extends BaseEntity {
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "lessons_id", referencedColumnName = "id")
    private Lesson lesson;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "teachers_id", referencedColumnName = "id")
    private Teacher teacher;
}
