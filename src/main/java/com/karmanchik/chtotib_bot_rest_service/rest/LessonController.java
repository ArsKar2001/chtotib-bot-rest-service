package com.karmanchik.chtotib_bot_rest_service.rest;

import com.karmanchik.chtotib_bot_rest_service.exception.ResourceNotFoundException;
import com.karmanchik.chtotib_bot_rest_service.jpa.entity.Lesson;
import com.karmanchik.chtotib_bot_rest_service.jpa.service.LessonService;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Log4j2
@RestController
@RequestMapping("/api/")
public class LessonController implements EntityRestControllerInterface<Lesson> {
    private final LessonService lessonService;

    public LessonController(LessonService lessonService) {
        this.lessonService = lessonService;
    }

    /**
     * Возвращает один элемент из таблицы БД
     *
     * @param id индентификатор эелемента
     * @return елемент
     */
    @Override
    @GetMapping("/lessons/{id}")
    public Lesson get(@PathVariable @NotNull Integer id) {
        return lessonService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id, Lesson.class));
    }

    /**
     * Вернет коллекцию всех элементов из таблицы БД
     *
     * @return коллекция элементов
     */
    @Override
    @GetMapping("/lessons/")
    public List<Lesson> getAll() {
        return lessonService.findAll();
    }

    /**
     * Добавит новый элемент в таблицу БД
     *
     * @param lesson тело элемент
     * @return сохраненный элемент
     */
    @Override
    @PostMapping("/lessons/")
    public Lesson post(@RequestBody @Valid Lesson lesson) {
        return lessonService.save(lesson);
    }

    /**
     * Изменит элемент таблицы БД
     *
     * @param id     индентификатор элемента БД
     * @param lesson тело элемента
     * @return измененный элемент
     */
    @Override
    @PutMapping("/lessons/{id}")
    public Lesson put(@PathVariable @NotNull Integer id,
                      @RequestBody @Valid Lesson lesson) {
        return lessonService.findById(id)
                .map(l -> {
                    l.setAuditorium(lesson.getAuditorium());
                    l.setDay(lesson.getDay());
                    l.setDiscipline(lesson.getDiscipline());
                    l.setPairNumber(lesson.getPairNumber());
                    l.setWeekType(lesson.getWeekType());
                    l.setGroup(lesson.getGroup());
                    l.setTeacher(lesson.getTeacher());
                    return lessonService.save(l);
                }).orElseThrow(() -> new ResourceNotFoundException(id, Lesson.class));
    }

    /**
     * Удалит элемент из таблицы БД
     *
     * @param id индентификатор элемента таблицы БД
     */
    @Override
    @DeleteMapping("/lessons/{id}")
    public void delete(@PathVariable @NotNull Integer id) {
        lessonService.deleteById(id);
    }

    /**
     * Удалит все элементы из таблицы БД
     */
    @Override
    @DeleteMapping("/lessons/")
    public void deleteAll() {
        lessonService.deleteAll();
    }
}
