package com.karmanchik.chtotib_bot_rest_service.rest;

import com.karmanchik.chtotib_bot_rest_service.entity.BaseEntity;
import com.karmanchik.chtotib_bot_rest_service.service.BaseService;
import org.springframework.http.ResponseEntity;

import javax.validation.constraints.NotNull;
import java.util.List;

public interface Controller<T extends BaseEntity, S extends BaseService<T>> {
    /**
     * Возвращает один элемент из таблицы БД
     *
     * @param id индентификатор эелемента
     * @return елемент
     */
    ResponseEntity<?> get(@NotNull Integer id);

    /**
     * Вернет коллекцию всех элементов из таблицы БД
     * @return коллекция элементов
     */
    ResponseEntity<?> getAll();

    /**
     * Добавит новый элемент в таблицу БД
     * @param t тело элемент
     * @return сохраненный элемент
     */
    ResponseEntity<?> post(T t);

    /**
     * Изменит элемент таблицы БД
     * @param id индентификатор элемента БД
     * @param t тело элемента
     * @return измененный элемент
     */
    ResponseEntity<?> put(@NotNull Integer id, T t);

    /**
     * Удалит элемент из таблицы БД
     * @param id индентификатор элемента таблицы БД
     * @return
     */
    ResponseEntity<?> delete(@NotNull Integer id);

    /**
     * Удалит все элементы из таблицы БД
     * @return
     * @param values
     */
    ResponseEntity<?> deleteAll(List<Integer> values);

    ResponseEntity<?> deleteAll();

}
