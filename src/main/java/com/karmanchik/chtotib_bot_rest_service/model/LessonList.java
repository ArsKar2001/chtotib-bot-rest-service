package com.karmanchik.chtotib_bot_rest_service.model;

import com.karmanchik.chtotib_bot_rest_service.jpa.entity.Lesson;

import java.util.List;

public class LessonList {
    private List<Lesson> lessons;

    public List<Lesson> getLessons() {
        return lessons;
    }

    public void setLessons(List<Lesson> lessons) {
        this.lessons = lessons;
    }
}
