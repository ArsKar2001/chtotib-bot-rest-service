# chtotib-telegram-bot-rest-service
Rest API для панели администратора, с использованием Spring Boot, PostgresSQL, JPA and Hibernate.

## Использование Rest API

Приложение определяет следующие запросы к API

### Users
| Метод | Url | Описание | Образец запроса | 
| ----- | --- | ---------- | --------------------------- |
| GET   | /api/v1/users | Вернет массив всех пользователей | |
| GET   | /api/v1/user/{`user_id`} | Вернет пользователя по `user_id` | [JSON](#get_user_by_id) |
| GET   | /user/{`user_id`}/user_state | Вернет статус пользователя | [JSON](#get_user_by_id) |
| GET   | /user/{`user_id`}/bot_state | Вернет статус чат-бота для этого пользователя | [JSON](#get_bot_state_by_id) |
| GET   | /user/{`user_id`}/role | Вернет роль пользователя | [JSON](#get_bot_state_by_id) |
| GET   | /user/{`user_id`}/group | Вернет группу пользователя |  |
| GET   | /user/{`user_id`}/group/lessons | Вернет расписание группы пользователя |  |
| GET   | /user/{`user_id`}/group/replacement | Вернет замену для группы пользователя |  |
| PUT   | /api/v1/user/{`user_id`} | Обновление пользователя по `user_id` | [JSON](#put_user_by_id) |
| DELETE    | /api/v1/user/{`user_id`} | Удаление пользователя по `user_id` | |

### Group
| Метод | Url | Описание | Образец запроса | 
| ----- | --- | ---------- | --------------------------- |
| GET | /api/v1/groups | Вернет массив всех групп | |
| GET | /api/v1/group/{`id`} | Вернет групп по `group_id` | [JSON](#get_group_by_id) |
| GET | /api/v1/group/{`id`}/replacement | Вернет замену для группы | [JSON](#get_group_replacement_by_id) |
| GET | /api/v1/group/{`id`}/lessons | Вернет расписание группы | [JSON](#get_group_replacement_by_id) |
| POST | /api/v1/groups | Создание новой группы | [JSON](#create_group_by_id) |
| PUT | /api/v1/group/{`id`} | Обновление группы | [JSON](#put_group_by_id) |
| DELETE | /api/v1/group/{`id`} | Удаление группы |  |

### Replacement

| Метод | Url | Описание | Образец запроса | 
| ----- | --- | ---------- | --------------------------- |
| GET | /api/v1/replacements | Вернет массив всех замен | |
| GET | /api/v1/replacements/{`id`} | Вернет замену по `replacement_id` | [JSON](#get_replacement_by_id) |
| POST | /api/v1/replacements | Создание новой замены |  |
| PUT | /api/v1/replacements/{`id`} | Обновление замены по `replacement_id` |  |
| DELETE | /api/v1/replacements/{`id`} | Удаление замены по `replacement_id` |  |

## Примеры допустимых JSON запросов

##### <a id="get_users">Get Users -> /api/v1/users</a>
```json
[
    {
        "id": 100215,
        "chatId": 977606227,
        "name": "l_karmanchik_l",
        "teacher": "",
        "botLastMessageId": 5814,
        "groupId": 100120,
        "userStateId": 200,
        "botStateId": 102,
        "roleId": 102,
        "group": {
            "id": 100120,
            "groupName": "СЭЗС-20-2",
            "lessons": "[{\"number\": \"I\", \"teacher\": \"Насибулин С.А.\", \"week_type\": \"NONE\", \"auditorium\": \"312\", \"discipline\": \"История\", \"day_of_week\": \"0\"}, {\"number\": \"II\", \"teacher\": \"Журавлева Л.Б.\", \"week_type\": \"NONE\", \"auditorium\": \"312\", \"discipline\": \"Русский язык\", \"day_of_week\": \"0\"}, {\"number\": \"III\", \"teacher\": \"Назарова А.А., Туркова С.В.\", \"week_type\": \"NONE\", \"auditorium\": \"312\", \"discipline\": \"Иностр. язык\", \"day_of_week\": \"0\"}, {\"number\": \"IV\", \"teacher\": \"Селин А.И.\", \"week_type\": \"NONE\", \"auditorium\": \"-\", \"discipline\": \"Физ.культура\", \"day_of_week\": \"0\"}, {\"number\": \"I\", \"teacher\": \"Насибулин С.А.\", \"week_type\": \"NONE\", \"auditorium\": \"312\", \"discipline\": \"Обществознание\", \"day_of_week\": \"1\"}, {\"number\": \"II\", \"teacher\": \"Кисель С.Д.\", \"week_type\": \"NONE\", \"auditorium\": \"312\", \"discipline\": \"Физика\", \"day_of_week\": \"1\"}, {\"number\": \"III\", \"teacher\": \"Гильфанова Л.А.\", \"week_type\": \"NONE\", \"auditorium\": \"312\", \"discipline\": \"Математика\", \"day_of_week\": \"1\"}, {\"number\": \"IV\", \"teacher\": \"Ведерников И.К., Самойлова Н.Г.\", \"week_type\": \"NONE\", \"auditorium\": \"26,29\", \"discipline\": \"Информатика\", \"day_of_week\": \"1\"}, {\"number\": \"V\", \"teacher\": \"-\", \"week_type\": \"NONE\", \"auditorium\": \"-\", \"discipline\": \"-\", \"day_of_week\": \"1\"}, {\"number\": \"I\", \"teacher\": \"-\", \"week_type\": \"DOWN\", \"auditorium\": \"-\", \"discipline\": \"-\", \"day_of_week\": \"2\"}, {\"number\": \"I\", \"teacher\": \"Селин А.И.\", \"week_type\": \"UP\", \"auditorium\": \"\", \"discipline\": \"Физ.культура\", \"day_of_week\": \"2\"}, {\"number\": \"II\", \"teacher\": \"Журавлева Л.Б.\", \"week_type\": \"NONE\", \"auditorium\": \"312\", \"discipline\": \"Литература\", \"day_of_week\": \"2\"}, {\"number\": \"III\", \"teacher\": \"Воронецкая А.И.\", \"week_type\": \"NONE\", \"auditorium\": \"312\", \"discipline\": \"Биология\", \"day_of_week\": \"2\"}, {\"number\": \"IV\", \"teacher\": \"Кисель С.Д, Ковалева В.В.\", \"week_type\": \"DOWN\", \"auditorium\": \"312\", \"discipline\": \"Физика\", \"day_of_week\": \"2\"}, {\"number\": \"IV\", \"teacher\": \"-\", \"week_type\": \"UP\", \"auditorium\": \"-\", \"discipline\": \"-\", \"day_of_week\": \"2\"}, {\"number\": \"I\", \"teacher\": \"Гильфанова Л.А.\", \"week_type\": \"NONE\", \"auditorium\": \"312\", \"discipline\": \"Математика\", \"day_of_week\": \"3\"}, {\"number\": \"II\", \"teacher\": \"Швецова Д.О.\", \"week_type\": \"NONE\", \"auditorium\": \"306\", \"discipline\": \"Химия\", \"day_of_week\": \"3\"}, {\"number\": \"III\", \"teacher\": \"Насибулин С.А.\", \"week_type\": \"DOWN\", \"auditorium\": \"312\", \"discipline\": \"Обществознание\", \"day_of_week\": \"3\"}, {\"number\": \"III\", \"teacher\": \"Насибулин С.А.\", \"week_type\": \"UP\", \"auditorium\": \"312\", \"discipline\": \"История\", \"day_of_week\": \"3\"}, {\"number\": \"IV\", \"teacher\": \"Лебедева Т.Ю.\", \"week_type\": \"NONE\", \"auditorium\": \"312\", \"discipline\": \"Классный час\", \"day_of_week\": \"3\"}, {\"number\": \"I\", \"teacher\": \"Назарова А.А., Туркова С.В.\", \"week_type\": \"DOWN\", \"auditorium\": \"312\", \"discipline\": \"Иностр. язык\", \"day_of_week\": \"4\"}, {\"number\": \"I\", \"teacher\": \"-\", \"week_type\": \"UP\", \"auditorium\": \"-\", \"discipline\": \"-\", \"day_of_week\": \"4\"}, {\"number\": \"II\", \"teacher\": \"Бабикова Н.А.\", \"week_type\": \"NONE\", \"auditorium\": \"312\", \"discipline\": \"ОБЖ\", \"day_of_week\": \"4\"}, {\"number\": \"III\", \"teacher\": \"Журавлева Л.Б.\", \"week_type\": \"NONE\", \"auditorium\": \"312\", \"discipline\": \"Родная литература\", \"day_of_week\": \"4\"}, {\"number\": \"IV\", \"teacher\": \"Кисель С.Д.\", \"week_type\": \"DOWN\", \"auditorium\": \"312\", \"discipline\": \"Физика\", \"day_of_week\": \"4\"}, {\"number\": \"IV\", \"teacher\": \"Гильфанова Л.А.\", \"week_type\": \"UP\", \"auditorium\": \"312\", \"discipline\": \"Математика\", \"day_of_week\": \"4\"}, {\"number\": \"V\", \"teacher\": \"-\", \"week_type\": \"DOWN\", \"auditorium\": \"-\", \"discipline\": \"-\", \"day_of_week\": \"4\"}, {\"number\": \"V\", \"teacher\": \"Ведерников И.К.\", \"week_type\": \"UP\", \"auditorium\": \"26\", \"discipline\": \"Информатика\", \"day_of_week\": \"4\"}]",
            "replacements": [
                {
                    "id": 100196,
                    "lessons": "[{\"number\": \"IV\", \"teacher\": \"Швецова Д.О.\", \"auditorium\": \"\", \"discipline\": \"Химия I и II п.\"}]",
                    "date": "2021-12-17"
                },
                {
                    "id": 100218,
                    "lessons": "[{\"number\": \"II, IV\", \"teacher\": \"Деньченко А.В.\", \"auditorium\": \"\", \"discipline\": \"Физ. культура\"}]",
                    "date": "2021-12-18"
                }
            ]
        },
        "userState": {
            "id": 200,
            "code": "NONE",
            "description": ""
        },
        "botState": {
            "id": 102,
            "code": "AUTHORIZED",
            "description": "Присваевается при завершении изменения/создания настроек"
        },
        "role": {
            "id": 102,
            "nameRole": "STUDENT",
            "description": "студент"
        }
    }
]
```
##### <a id="get_user_by_id">Get User by user_id -> /api/v1/user/{`user_id`}</a>
```json
{
    "id": 100215,
    "chatId": 977606227,
    "name": "l_karmanchik_l",
    "teacher": "",
    "botLastMessageId": 5814,
    "groupId": 100120,
    "userStateId": 200,
    "botStateId": 102,
    "roleId": 102,
    "group": {
        "id": 100120,
        "groupName": "СЭЗС-20-2",
        "lessons": "[{\"number\": \"I\", \"teacher\": \"Насибулин С.А.\", \"week_type\": \"NONE\", \"auditorium\": \"312\", \"discipline\": \"История\", \"day_of_week\": \"0\"}, {\"number\": \"II\", \"teacher\": \"Журавлева Л.Б.\", \"week_type\": \"NONE\", \"auditorium\": \"312\", \"discipline\": \"Русский язык\", \"day_of_week\": \"0\"}, {\"number\": \"III\", \"teacher\": \"Назарова А.А., Туркова С.В.\", \"week_type\": \"NONE\", \"auditorium\": \"312\", \"discipline\": \"Иностр. язык\", \"day_of_week\": \"0\"}, {\"number\": \"IV\", \"teacher\": \"Селин А.И.\", \"week_type\": \"NONE\", \"auditorium\": \"-\", \"discipline\": \"Физ.культура\", \"day_of_week\": \"0\"}, {\"number\": \"I\", \"teacher\": \"Насибулин С.А.\", \"week_type\": \"NONE\", \"auditorium\": \"312\", \"discipline\": \"Обществознание\", \"day_of_week\": \"1\"}, {\"number\": \"II\", \"teacher\": \"Кисель С.Д.\", \"week_type\": \"NONE\", \"auditorium\": \"312\", \"discipline\": \"Физика\", \"day_of_week\": \"1\"}, {\"number\": \"III\", \"teacher\": \"Гильфанова Л.А.\", \"week_type\": \"NONE\", \"auditorium\": \"312\", \"discipline\": \"Математика\", \"day_of_week\": \"1\"}, {\"number\": \"IV\", \"teacher\": \"Ведерников И.К., Самойлова Н.Г.\", \"week_type\": \"NONE\", \"auditorium\": \"26,29\", \"discipline\": \"Информатика\", \"day_of_week\": \"1\"}, {\"number\": \"V\", \"teacher\": \"-\", \"week_type\": \"NONE\", \"auditorium\": \"-\", \"discipline\": \"-\", \"day_of_week\": \"1\"}, {\"number\": \"I\", \"teacher\": \"-\", \"week_type\": \"DOWN\", \"auditorium\": \"-\", \"discipline\": \"-\", \"day_of_week\": \"2\"}, {\"number\": \"I\", \"teacher\": \"Селин А.И.\", \"week_type\": \"UP\", \"auditorium\": \"\", \"discipline\": \"Физ.культура\", \"day_of_week\": \"2\"}, {\"number\": \"II\", \"teacher\": \"Журавлева Л.Б.\", \"week_type\": \"NONE\", \"auditorium\": \"312\", \"discipline\": \"Литература\", \"day_of_week\": \"2\"}, {\"number\": \"III\", \"teacher\": \"Воронецкая А.И.\", \"week_type\": \"NONE\", \"auditorium\": \"312\", \"discipline\": \"Биология\", \"day_of_week\": \"2\"}, {\"number\": \"IV\", \"teacher\": \"Кисель С.Д, Ковалева В.В.\", \"week_type\": \"DOWN\", \"auditorium\": \"312\", \"discipline\": \"Физика\", \"day_of_week\": \"2\"}, {\"number\": \"IV\", \"teacher\": \"-\", \"week_type\": \"UP\", \"auditorium\": \"-\", \"discipline\": \"-\", \"day_of_week\": \"2\"}, {\"number\": \"I\", \"teacher\": \"Гильфанова Л.А.\", \"week_type\": \"NONE\", \"auditorium\": \"312\", \"discipline\": \"Математика\", \"day_of_week\": \"3\"}, {\"number\": \"II\", \"teacher\": \"Швецова Д.О.\", \"week_type\": \"NONE\", \"auditorium\": \"306\", \"discipline\": \"Химия\", \"day_of_week\": \"3\"}, {\"number\": \"III\", \"teacher\": \"Насибулин С.А.\", \"week_type\": \"DOWN\", \"auditorium\": \"312\", \"discipline\": \"Обществознание\", \"day_of_week\": \"3\"}, {\"number\": \"III\", \"teacher\": \"Насибулин С.А.\", \"week_type\": \"UP\", \"auditorium\": \"312\", \"discipline\": \"История\", \"day_of_week\": \"3\"}, {\"number\": \"IV\", \"teacher\": \"Лебедева Т.Ю.\", \"week_type\": \"NONE\", \"auditorium\": \"312\", \"discipline\": \"Классный час\", \"day_of_week\": \"3\"}, {\"number\": \"I\", \"teacher\": \"Назарова А.А., Туркова С.В.\", \"week_type\": \"DOWN\", \"auditorium\": \"312\", \"discipline\": \"Иностр. язык\", \"day_of_week\": \"4\"}, {\"number\": \"I\", \"teacher\": \"-\", \"week_type\": \"UP\", \"auditorium\": \"-\", \"discipline\": \"-\", \"day_of_week\": \"4\"}, {\"number\": \"II\", \"teacher\": \"Бабикова Н.А.\", \"week_type\": \"NONE\", \"auditorium\": \"312\", \"discipline\": \"ОБЖ\", \"day_of_week\": \"4\"}, {\"number\": \"III\", \"teacher\": \"Журавлева Л.Б.\", \"week_type\": \"NONE\", \"auditorium\": \"312\", \"discipline\": \"Родная литература\", \"day_of_week\": \"4\"}, {\"number\": \"IV\", \"teacher\": \"Кисель С.Д.\", \"week_type\": \"DOWN\", \"auditorium\": \"312\", \"discipline\": \"Физика\", \"day_of_week\": \"4\"}, {\"number\": \"IV\", \"teacher\": \"Гильфанова Л.А.\", \"week_type\": \"UP\", \"auditorium\": \"312\", \"discipline\": \"Математика\", \"day_of_week\": \"4\"}, {\"number\": \"V\", \"teacher\": \"-\", \"week_type\": \"DOWN\", \"auditorium\": \"-\", \"discipline\": \"-\", \"day_of_week\": \"4\"}, {\"number\": \"V\", \"teacher\": \"Ведерников И.К.\", \"week_type\": \"UP\", \"auditorium\": \"26\", \"discipline\": \"Информатика\", \"day_of_week\": \"4\"}]",
        "replacements": [
            {
                "id": 100196,
                "lessons": "[{\"number\": \"IV\", \"teacher\": \"Швецова Д.О.\", \"auditorium\": \"\", \"discipline\": \"Химия I и II п.\"}]",
                "date": "2021-12-17"
            },
            {
                "id": 100218,
                "lessons": "[{\"number\": \"II, IV\", \"teacher\": \"Деньченко А.В.\", \"auditorium\": \"\", \"discipline\": \"Физ. культура\"}]",
                "date": "2021-12-18"
            }
        ]
    },
    "userState": {
        "id": 200,
        "code": "NONE",
        "description": ""
    },
    "botState": {
        "id": 102,
        "code": "AUTHORIZED",
        "description": "Присваевается при завершении изменения/создания настроек"
    },
    "role": {
        "id": 102,
        "nameRole": "STUDENT",
        "description": "студент"
    }
}
```

##### <a id="get_user_state_by_id">Get User State by user_id -> /api/v1/user/{`user_id`}/user_state</a>
```json
{
    "id": 200,
    "code": "NONE",
    "description": ""
}
```

##### <a id="get_bot_state_by_id">Get Bot State by user_id -> /api/v1/user/{`user_id`}/bot_state</a>
```json
{
    "id": 102,
    "code": "AUTHORIZED",
    "description": "Присваевается при завершении изменения/создания настроек"
}
```

##### <a id="get_role_by_id">Get Role by user_id -> /api/v1/user/{`user_id`}/role</a>
```json
{
    "id": 102,
    "nameRole": "STUDENT",
    "description": "студент"
}
```

##### <a id="put_user_by_id">Put User by user_id -> /api/v1/user/{`user_id`}</a>
```json
{
    "id": 100215,
    "chatId": 977606227,
    "name": "l_karmanchik_l",
    "teacher": "",
    "botLastMessageId": 5814,
    "groupId": 100120,
    "userStateId": 200,
    "botStateId": 102,
    "roleId": 102
}
```

#### <a id="get_group_by_id">Get Group by a group_id -> /api/v1/group/{`group_id`}</a>
```json
{
    "id": 100121,
    "groupName": "СЭЗС-20-3к",
    "lessons": "[{\"number\": \"I\", \"teacher\": \"Павлов А.М., Гурулева Н.Ю.\", \"week_type\": \"NONE\", \"auditorium\": \"308\", \"discipline\": \"Иностр. язык\", \"day_of_week\": \"0\"}, {\"number\": \"II\", \"teacher\": \"Швецова Д.О.\", \"week_type\": \"NONE\", \"auditorium\": \"306\", \"discipline\": \"Химия\", \"day_of_week\": \"0\"}, {\"number\": \"III\", \"teacher\": \"Раздобреев И.С.\", \"week_type\": \"NONE\", \"auditorium\": \"-\", \"discipline\": \"Физ.культура\", \"day_of_week\": \"0\"}, {\"number\": \"IV\", \"teacher\": \"Никифорова Е.С., Самойлова Н.Г.\", \"week_type\": \"NONE\", \"auditorium\": \"23,29\", \"discipline\": \"Информатика\", \"day_of_week\": \"0\"}, {\"number\": \"I\", \"teacher\": \"Кисель С.Д.\", \"week_type\": \"NONE\", \"auditorium\": \"308\", \"discipline\": \"Физика\", \"day_of_week\": \"1\"}, {\"number\": \"II\", \"teacher\": \"Литвинцева Е.А\", \"week_type\": \"NONE\", \"auditorium\": \"308\", \"discipline\": \"Русский язык\", \"day_of_week\": \"1\"}, {\"number\": \"III\", \"teacher\": \"Насибулин С.А.\", \"week_type\": \"NONE\", \"auditorium\": \"308\", \"discipline\": \"История\", \"day_of_week\": \"1\"}, {\"number\": \"IV\", \"teacher\": \"Гильфанова Л.А.\", \"week_type\": \"NONE\", \"auditorium\": \"308\", \"discipline\": \"Математика\", \"day_of_week\": \"1\"}, {\"number\": \"V\", \"teacher\": \"-\", \"week_type\": \"NONE\", \"auditorium\": \"-\", \"discipline\": \"-\", \"day_of_week\": \"1\"}, {\"number\": \"I\", \"teacher\": \"Никифорова Е.С\", \"week_type\": \"DOWN\", \"auditorium\": \"23\", \"discipline\": \"Информатика\", \"day_of_week\": \"2\"}, {\"number\": \"I\", \"teacher\": \"-\", \"week_type\": \"UP\", \"auditorium\": \"-\", \"discipline\": \"-\", \"day_of_week\": \"2\"}, {\"number\": \"II\", \"teacher\": \"Насибулин С.А.\", \"week_type\": \"DOWN\", \"auditorium\": \"308\", \"discipline\": \"Обществознание\", \"day_of_week\": \"2\"}, {\"number\": \"II\", \"teacher\": \"Кисель С.Д.\", \"week_type\": \"UP\", \"auditorium\": \"308\", \"discipline\": \"Физика\", \"day_of_week\": \"2\"}, {\"number\": \"III\", \"teacher\": \"Гильфанова Л.А.\", \"week_type\": \"DOWN\", \"auditorium\": \"308\", \"discipline\": \"Математика\", \"day_of_week\": \"2\"}, {\"number\": \"III\", \"teacher\": \"Павлов А.М., Гурулева Н.Ю..\", \"week_type\": \"UP\", \"auditorium\": \"308\", \"discipline\": \"Иностр. язык\", \"day_of_week\": \"2\"}, {\"number\": \"IV\", \"teacher\": \"-\", \"week_type\": \"DOWN\", \"auditorium\": \"-\", \"discipline\": \"-\", \"day_of_week\": \"2\"}, {\"number\": \"IV\", \"teacher\": \"Насибулин С.А.\", \"week_type\": \"UP\", \"auditorium\": \"308\", \"discipline\": \"История\", \"day_of_week\": \"2\"}, {\"number\": \"I\", \"teacher\": \"Насибулин С.А.\", \"week_type\": \"NONE\", \"auditorium\": \"308\", \"discipline\": \"Обществознание\", \"day_of_week\": \"3\"}, {\"number\": \"II\", \"teacher\": \"Гильфанова Л.А.\", \"week_type\": \"NONE\", \"auditorium\": \"308\", \"discipline\": \"Математика\", \"day_of_week\": \"3\"}, {\"number\": \"III\", \"teacher\": \"Воронецкая А.И.\", \"week_type\": \"NONE\", \"auditorium\": \"308\", \"discipline\": \"Биология\", \"day_of_week\": \"3\"}, {\"number\": \"IV\", \"teacher\": \"Мадияров Т.А.\", \"week_type\": \"NONE\", \"auditorium\": \"308\", \"discipline\": \"Классный час\", \"day_of_week\": \"3\"}, {\"number\": \"I\", \"teacher\": \"Литвинцева Е.А\", \"week_type\": \"NONE\", \"auditorium\": \"308\", \"discipline\": \"Литература\", \"day_of_week\": \"4\"}, {\"number\": \"II\", \"teacher\": \"Литвинцева Е.А.\", \"week_type\": \"NONE\", \"auditorium\": \"308\", \"discipline\": \"Родная литература\", \"day_of_week\": \"4\"}, {\"number\": \"III\", \"teacher\": \"Воронецкая А.И.\", \"week_type\": \"NONE\", \"auditorium\": \"308\", \"discipline\": \"ОБЖ\", \"day_of_week\": \"4\"}, {\"number\": \"IV\", \"teacher\": \"Раздобреев И.С.\", \"week_type\": \"DOWN\", \"auditorium\": \"\", \"discipline\": \"Физ.культура\", \"day_of_week\": \"4\"}, {\"number\": \"IV\", \"teacher\": \"Кисель С.Д, Ковалева В.В.\", \"week_type\": \"UP\", \"auditorium\": \"308\", \"discipline\": \"Физика\", \"day_of_week\": \"4\"}, {\"number\": \"V\", \"teacher\": \"-\", \"week_type\": \"NONE\", \"auditorium\": \"-\", \"discipline\": \"-\", \"day_of_week\": \"4\"}]",
    "replacements": [
        {
            "id": 100260,
            "lessons": "[{\"number\": \"I\", \"teacher\": \"Никифорова Е.С.\", \"auditorium\": \"\", \"discipline\": \"Информатика Iп\"}, {\"number\": \"V\", \"teacher\": \"Никифорова Е.С.\", \"auditorium\": \"\", \"discipline\": \"Информатика IIп\"}]",
            "date": "2021-12-16"
        },
        {
            "id": 100197,
            "lessons": "[{\"number\": \"II\", \"teacher\": \"Насибулин С.А.\", \"auditorium\": \"\", \"discipline\": \"История\"}, {\"number\": \"IV\", \"teacher\": \"Гильфанова Л.А.\", \"auditorium\": \"\", \"discipline\": \"Математика\"}, {\"number\": \"V\", \"teacher\": \"Швецова Д.О.\", \"auditorium\": \"\", \"discipline\": \"Химия I и II п.\"}]",
            "date": "2021-12-17"
        },
        {
            "id": 100219,
            "lessons": "[{\"number\": \"II\", \"teacher\": \"Гильфанова Л.А.\", \"auditorium\": \"\", \"discipline\": \"Математика\"}, {\"number\": \"IV\", \"teacher\": \"Насибулин С.А.\", \"auditorium\": \"\", \"discipline\": \"История\"}]",
            "date": "2021-12-18"
        }
    ]
}
```

#### <a id="get_group_replacement_by_id">Get Group Replacement by group_id -> /api/v1/group/{`group_id`}/replacement</a>
```json
[
    {
        "id": 100260,
        "lessons": "[{\"number\": \"I\", \"teacher\": \"Никифорова Е.С.\", \"auditorium\": \"\", \"discipline\": \"Информатика Iп\"}, {\"number\": \"V\", \"teacher\": \"Никифорова Е.С.\", \"auditorium\": \"\", \"discipline\": \"Информатика IIп\"}]",
        "date": "2021-12-16"
    },
    {
        "id": 100197,
        "lessons": "[{\"number\": \"II\", \"teacher\": \"Насибулин С.А.\", \"auditorium\": \"\", \"discipline\": \"История\"}, {\"number\": \"IV\", \"teacher\": \"Гильфанова Л.А.\", \"auditorium\": \"\", \"discipline\": \"Математика\"}, {\"number\": \"V\", \"teacher\": \"Швецова Д.О.\", \"auditorium\": \"\", \"discipline\": \"Химия I и II п.\"}]",
        "date": "2021-12-17"
    },
    {
        "id": 100219,
        "lessons": "[{\"number\": \"II\", \"teacher\": \"Гильфанова Л.А.\", \"auditorium\": \"\", \"discipline\": \"Математика\"}, {\"number\": \"IV\", \"teacher\": \"Насибулин С.А.\", \"auditorium\": \"\", \"discipline\": \"История\"}]",
        "date": "2021-12-18"
    }
]
```

#### <a id="create_group_by_id">Create Group-> /api/v1/groups</a>
```json
{
    "id": 100121,
    "groupName": "СЭЗС-20-3к",
    "lessons": "[{\"number\": \"I\", \"teacher\": \"Павлов А.М., Гурулева Н.Ю.\", \"week_type\": \"NONE\", \"auditorium\": \"308\", \"discipline\": \"Иностр. язык\", \"day_of_week\": \"0\"}, {\"number\": \"II\", \"teacher\": \"Швецова Д.О.\", \"week_type\": \"NONE\", \"auditorium\": \"306\", \"discipline\": \"Химия\", \"day_of_week\": \"0\"}, {\"number\": \"III\", \"teacher\": \"Раздобреев И.С.\", \"week_type\": \"NONE\", \"auditorium\": \"-\", \"discipline\": \"Физ.культура\", \"day_of_week\": \"0\"}, {\"number\": \"IV\", \"teacher\": \"Никифорова Е.С., Самойлова Н.Г.\", \"week_type\": \"NONE\", \"auditorium\": \"23,29\", \"discipline\": \"Информатика\", \"day_of_week\": \"0\"}, {\"number\": \"I\", \"teacher\": \"Кисель С.Д.\", \"week_type\": \"NONE\", \"auditorium\": \"308\", \"discipline\": \"Физика\", \"day_of_week\": \"1\"}, {\"number\": \"II\", \"teacher\": \"Литвинцева Е.А\", \"week_type\": \"NONE\", \"auditorium\": \"308\", \"discipline\": \"Русский язык\", \"day_of_week\": \"1\"}, {\"number\": \"III\", \"teacher\": \"Насибулин С.А.\", \"week_type\": \"NONE\", \"auditorium\": \"308\", \"discipline\": \"История\", \"day_of_week\": \"1\"}, {\"number\": \"IV\", \"teacher\": \"Гильфанова Л.А.\", \"week_type\": \"NONE\", \"auditorium\": \"308\", \"discipline\": \"Математика\", \"day_of_week\": \"1\"}, {\"number\": \"V\", \"teacher\": \"-\", \"week_type\": \"NONE\", \"auditorium\": \"-\", \"discipline\": \"-\", \"day_of_week\": \"1\"}, {\"number\": \"I\", \"teacher\": \"Никифорова Е.С\", \"week_type\": \"DOWN\", \"auditorium\": \"23\", \"discipline\": \"Информатика\", \"day_of_week\": \"2\"}, {\"number\": \"I\", \"teacher\": \"-\", \"week_type\": \"UP\", \"auditorium\": \"-\", \"discipline\": \"-\", \"day_of_week\": \"2\"}, {\"number\": \"II\", \"teacher\": \"Насибулин С.А.\", \"week_type\": \"DOWN\", \"auditorium\": \"308\", \"discipline\": \"Обществознание\", \"day_of_week\": \"2\"}, {\"number\": \"II\", \"teacher\": \"Кисель С.Д.\", \"week_type\": \"UP\", \"auditorium\": \"308\", \"discipline\": \"Физика\", \"day_of_week\": \"2\"}, {\"number\": \"III\", \"teacher\": \"Гильфанова Л.А.\", \"week_type\": \"DOWN\", \"auditorium\": \"308\", \"discipline\": \"Математика\", \"day_of_week\": \"2\"}, {\"number\": \"III\", \"teacher\": \"Павлов А.М., Гурулева Н.Ю..\", \"week_type\": \"UP\", \"auditorium\": \"308\", \"discipline\": \"Иностр. язык\", \"day_of_week\": \"2\"}, {\"number\": \"IV\", \"teacher\": \"-\", \"week_type\": \"DOWN\", \"auditorium\": \"-\", \"discipline\": \"-\", \"day_of_week\": \"2\"}, {\"number\": \"IV\", \"teacher\": \"Насибулин С.А.\", \"week_type\": \"UP\", \"auditorium\": \"308\", \"discipline\": \"История\", \"day_of_week\": \"2\"}, {\"number\": \"I\", \"teacher\": \"Насибулин С.А.\", \"week_type\": \"NONE\", \"auditorium\": \"308\", \"discipline\": \"Обществознание\", \"day_of_week\": \"3\"}, {\"number\": \"II\", \"teacher\": \"Гильфанова Л.А.\", \"week_type\": \"NONE\", \"auditorium\": \"308\", \"discipline\": \"Математика\", \"day_of_week\": \"3\"}, {\"number\": \"III\", \"teacher\": \"Воронецкая А.И.\", \"week_type\": \"NONE\", \"auditorium\": \"308\", \"discipline\": \"Биология\", \"day_of_week\": \"3\"}, {\"number\": \"IV\", \"teacher\": \"Мадияров Т.А.\", \"week_type\": \"NONE\", \"auditorium\": \"308\", \"discipline\": \"Классный час\", \"day_of_week\": \"3\"}, {\"number\": \"I\", \"teacher\": \"Литвинцева Е.А\", \"week_type\": \"NONE\", \"auditorium\": \"308\", \"discipline\": \"Литература\", \"day_of_week\": \"4\"}, {\"number\": \"II\", \"teacher\": \"Литвинцева Е.А.\", \"week_type\": \"NONE\", \"auditorium\": \"308\", \"discipline\": \"Родная литература\", \"day_of_week\": \"4\"}, {\"number\": \"III\", \"teacher\": \"Воронецкая А.И.\", \"week_type\": \"NONE\", \"auditorium\": \"308\", \"discipline\": \"ОБЖ\", \"day_of_week\": \"4\"}, {\"number\": \"IV\", \"teacher\": \"Раздобреев И.С.\", \"week_type\": \"DOWN\", \"auditorium\": \"\", \"discipline\": \"Физ.культура\", \"day_of_week\": \"4\"}, {\"number\": \"IV\", \"teacher\": \"Кисель С.Д, Ковалева В.В.\", \"week_type\": \"UP\", \"auditorium\": \"308\", \"discipline\": \"Физика\", \"day_of_week\": \"4\"}, {\"number\": \"V\", \"teacher\": \"-\", \"week_type\": \"NONE\", \"auditorium\": \"-\", \"discipline\": \"-\", \"day_of_week\": \"4\"}]",
    "replacements": [
        {
            "id": 100260,
            "lessons": "[{\"number\": \"I\", \"teacher\": \"Никифорова Е.С.\", \"auditorium\": \"\", \"discipline\": \"Информатика Iп\"}, {\"number\": \"V\", \"teacher\": \"Никифорова Е.С.\", \"auditorium\": \"\", \"discipline\": \"Информатика IIп\"}]",
            "date": "2021-12-16"
        },
        {
            "id": 100197,
            "lessons": "[{\"number\": \"II\", \"teacher\": \"Насибулин С.А.\", \"auditorium\": \"\", \"discipline\": \"История\"}, {\"number\": \"IV\", \"teacher\": \"Гильфанова Л.А.\", \"auditorium\": \"\", \"discipline\": \"Математика\"}, {\"number\": \"V\", \"teacher\": \"Швецова Д.О.\", \"auditorium\": \"\", \"discipline\": \"Химия I и II п.\"}]",
            "date": "2021-12-17"
        },
        {
            "id": 100219,
            "lessons": "[{\"number\": \"II\", \"teacher\": \"Гильфанова Л.А.\", \"auditorium\": \"\", \"discipline\": \"Математика\"}, {\"number\": \"IV\", \"teacher\": \"Насибулин С.А.\", \"auditorium\": \"\", \"discipline\": \"История\"}]",
            "date": "2021-12-18"
        }
    ]
}
```

#### <a id="put_group_by_id">Put Group-> /api/v1/groups</a>
```json
{
    "id": 100121,
    "groupName": "СЭЗС-20-3к",
    "lessons": "[{\"number\": \"I\", \"teacher\": \"Павлов А.М., Гурулева Н.Ю.\", \"week_type\": \"NONE\", \"auditorium\": \"308\", \"discipline\": \"Иностр. язык\", \"day_of_week\": \"0\"}, {\"number\": \"II\", \"teacher\": \"Швецова Д.О.\", \"week_type\": \"NONE\", \"auditorium\": \"306\", \"discipline\": \"Химия\", \"day_of_week\": \"0\"}, {\"number\": \"III\", \"teacher\": \"Раздобреев И.С.\", \"week_type\": \"NONE\", \"auditorium\": \"-\", \"discipline\": \"Физ.культура\", \"day_of_week\": \"0\"}, {\"number\": \"IV\", \"teacher\": \"Никифорова Е.С., Самойлова Н.Г.\", \"week_type\": \"NONE\", \"auditorium\": \"23,29\", \"discipline\": \"Информатика\", \"day_of_week\": \"0\"}, {\"number\": \"I\", \"teacher\": \"Кисель С.Д.\", \"week_type\": \"NONE\", \"auditorium\": \"308\", \"discipline\": \"Физика\", \"day_of_week\": \"1\"}, {\"number\": \"II\", \"teacher\": \"Литвинцева Е.А\", \"week_type\": \"NONE\", \"auditorium\": \"308\", \"discipline\": \"Русский язык\", \"day_of_week\": \"1\"}, {\"number\": \"III\", \"teacher\": \"Насибулин С.А.\", \"week_type\": \"NONE\", \"auditorium\": \"308\", \"discipline\": \"История\", \"day_of_week\": \"1\"}, {\"number\": \"IV\", \"teacher\": \"Гильфанова Л.А.\", \"week_type\": \"NONE\", \"auditorium\": \"308\", \"discipline\": \"Математика\", \"day_of_week\": \"1\"}, {\"number\": \"V\", \"teacher\": \"-\", \"week_type\": \"NONE\", \"auditorium\": \"-\", \"discipline\": \"-\", \"day_of_week\": \"1\"}, {\"number\": \"I\", \"teacher\": \"Никифорова Е.С\", \"week_type\": \"DOWN\", \"auditorium\": \"23\", \"discipline\": \"Информатика\", \"day_of_week\": \"2\"}, {\"number\": \"I\", \"teacher\": \"-\", \"week_type\": \"UP\", \"auditorium\": \"-\", \"discipline\": \"-\", \"day_of_week\": \"2\"}, {\"number\": \"II\", \"teacher\": \"Насибулин С.А.\", \"week_type\": \"DOWN\", \"auditorium\": \"308\", \"discipline\": \"Обществознание\", \"day_of_week\": \"2\"}, {\"number\": \"II\", \"teacher\": \"Кисель С.Д.\", \"week_type\": \"UP\", \"auditorium\": \"308\", \"discipline\": \"Физика\", \"day_of_week\": \"2\"}, {\"number\": \"III\", \"teacher\": \"Гильфанова Л.А.\", \"week_type\": \"DOWN\", \"auditorium\": \"308\", \"discipline\": \"Математика\", \"day_of_week\": \"2\"}, {\"number\": \"III\", \"teacher\": \"Павлов А.М., Гурулева Н.Ю..\", \"week_type\": \"UP\", \"auditorium\": \"308\", \"discipline\": \"Иностр. язык\", \"day_of_week\": \"2\"}, {\"number\": \"IV\", \"teacher\": \"-\", \"week_type\": \"DOWN\", \"auditorium\": \"-\", \"discipline\": \"-\", \"day_of_week\": \"2\"}, {\"number\": \"IV\", \"teacher\": \"Насибулин С.А.\", \"week_type\": \"UP\", \"auditorium\": \"308\", \"discipline\": \"История\", \"day_of_week\": \"2\"}, {\"number\": \"I\", \"teacher\": \"Насибулин С.А.\", \"week_type\": \"NONE\", \"auditorium\": \"308\", \"discipline\": \"Обществознание\", \"day_of_week\": \"3\"}, {\"number\": \"II\", \"teacher\": \"Гильфанова Л.А.\", \"week_type\": \"NONE\", \"auditorium\": \"308\", \"discipline\": \"Математика\", \"day_of_week\": \"3\"}, {\"number\": \"III\", \"teacher\": \"Воронецкая А.И.\", \"week_type\": \"NONE\", \"auditorium\": \"308\", \"discipline\": \"Биология\", \"day_of_week\": \"3\"}, {\"number\": \"IV\", \"teacher\": \"Мадияров Т.А.\", \"week_type\": \"NONE\", \"auditorium\": \"308\", \"discipline\": \"Классный час\", \"day_of_week\": \"3\"}, {\"number\": \"I\", \"teacher\": \"Литвинцева Е.А\", \"week_type\": \"NONE\", \"auditorium\": \"308\", \"discipline\": \"Литература\", \"day_of_week\": \"4\"}, {\"number\": \"II\", \"teacher\": \"Литвинцева Е.А.\", \"week_type\": \"NONE\", \"auditorium\": \"308\", \"discipline\": \"Родная литература\", \"day_of_week\": \"4\"}, {\"number\": \"III\", \"teacher\": \"Воронецкая А.И.\", \"week_type\": \"NONE\", \"auditorium\": \"308\", \"discipline\": \"ОБЖ\", \"day_of_week\": \"4\"}, {\"number\": \"IV\", \"teacher\": \"Раздобреев И.С.\", \"week_type\": \"DOWN\", \"auditorium\": \"\", \"discipline\": \"Физ.культура\", \"day_of_week\": \"4\"}, {\"number\": \"IV\", \"teacher\": \"Кисель С.Д, Ковалева В.В.\", \"week_type\": \"UP\", \"auditorium\": \"308\", \"discipline\": \"Физика\", \"day_of_week\": \"4\"}, {\"number\": \"V\", \"teacher\": \"-\", \"week_type\": \"NONE\", \"auditorium\": \"-\", \"discipline\": \"-\", \"day_of_week\": \"4\"}]"
}
```


#### <a id="get_replacement_by_id">Get Replacement-> /api/v1/replacements/{`id`}</a>
```json
{
    "id": 100196,
    "groupId": 100120,
    "lessons": "[{\"number\": \"IV\", \"teacher\": \"Швецова Д.О.\", \"auditorium\": \"\", \"discipline\": \"Химия I и II п.\"}]",
    "date": "2021-12-17"
}
```