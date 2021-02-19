package com.karmanchik.chtotib_bot_rest_service.exeption;

public class StringReadException extends RuntimeException {
    /**
     * Constructs a new runtime exception with {@code null} as its
     * detail message.  The cause is not initialized, and may subsequently be
     * initialized by a call to {@link #initCause}.
     */
    public StringReadException() {
        super();
    }

    /**
     * Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param message the detail message. The detail message is saved for
     *                later retrieval by the {@link #getMessage()} method.
     */
    public StringReadException(String message, String split, Integer size) {
        super(String.format("Ошибка в строке - \"%s\"; split - [%s]; size - %s; Превышенно кол-во столбцов [Дни недели, № за-нятия, Предмет, Ауд., Ф.И.О. преподавателя]", message, split, size));
    }


}
