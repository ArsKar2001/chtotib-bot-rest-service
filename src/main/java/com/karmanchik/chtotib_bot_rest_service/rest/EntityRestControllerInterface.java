package com.karmanchik.chtotib_bot_rest_service.rest;

import com.karmanchik.chtotib_bot_rest_service.jpa.entity.BaseEntity;
import com.karmanchik.chtotib_bot_rest_service.jpa.service.BaseService;
import org.springframework.http.ResponseEntity;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface EntityRestControllerInterface<T extends BaseEntity> {
    /**
     * Возвращает один элемент из таблицы БД
     * @param id индентификатор эелемента
     * @return елемент
     */
    T get(@NotNull Integer id);

    /**
     * Вернет коллекцию всех элементов из таблицы БД
     * @return коллекция элементов
     */
    List<T> getAll();

    /**
     * Добавит новый элемент в таблицу БД
     * @param t тело элемент
     * @return сохраненный элемент
     */
    T post(T t);

    /**
     * Изменит элемент таблицы БД
     * @param id индентификатор элемента БД
     * @param t тело элемента
     * @return измененный элемент
     */
    T put(@NotNull Integer id, T t);

    /**
     * Удалит элемент из таблицы БД
     * @param id индентификатор элемента таблицы БД
     */
    void delete(@NotNull Integer id);

    /**
     * Удалит все элементы из таблицы БД
     */
    void deleteAll();
}
