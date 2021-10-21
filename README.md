# chtotib-telegram-bot-rest-service
Rest API для панели администратора чат-бота.

## Использование Rest API

Приложение определяет следующие запросы к API (описание неполное)

### Group
| Метод | Url | Описание | Образец запроса | Образец ответа |
| ----- | --- | -------- | --------------- | --------------- |
| GET | /api/groups/ | Вернет массив всех групп | | |
| GET | /api/groups/{`id`} | Вернет группу по `id` | | [JSON](#get_group_by_id) |
| GET | /api/groups/{`id`}/replacements | Вернет замену для группы с разбивкой по дате | | [JSON](#get_replacement_by_group_id) |
| GET | /api/groups/{`id`}/lessons | Вернет расписание для группы с разбивкой по дням недели | | [JSON](#get_lessons_by_group_id) |
| POST | /api/groups | Создание новой группы | [JSON](#create_group) |  |
| PUT | /api/groups/{`id`} | Обновление группы по `id` | [JSON](#put_group_by_id) | |
| DELETE | /api/groups/{`id`} | Удаление группы по `id` | | |


## Примеры допустимых JSON запросов

###Groups

#### <a id="create_group">Создание `Group` (`lessons` и `replacements` необязательны) -> /api/groups</a>
```json
{
    "name": "ТЕСТ-01-02",
    "lessons": [],
    "replacements": []
}
```

#### <a id="put_group_by_id">Изменение `Group` (`lessons` и `replacements` необязательны) -> /api/groups/`id`</a>
```json
{
    "name": "ТЕСТ-01-02",
    "lessons": [],
    "replacements": []
}
```

## Примеры JSON оветов

###Groups

#### <a id="get_group_by_id">/api/groups/`120519`</a>
```json
{
    "id": 120519,
    "name": "ТЕСТ-01-1",
    "_links": {
        "self": {
            "href": "http://localhost:8080/api/groups/120519"
        },
        "groups": {
            "href": "http://localhost:8080/api/groups/"
        },
        "lessons": {
            "href": "http://localhost:8080/api/groups/120519/lessons"
        },
        "replacements": {
            "href": "http://localhost:8080/api/groups/120519/replacements"
        }
    }
}
```

#### <a id="get_replacement_by_group_id">/api/group/`120519`/replacements</a>
```json
[
    {
        "group_id": 111849,
        "replacements": []
    }
]
```

#### <a id="get_lessons_by_group_id">/api/group/`120519`/lessons</a>
```json
[
    {
        "group_id": 120519,
        "lessons": []
    },
    {
        "group_id": 120519,
        "lessons": []
    },
    {
        "group_id": 120519,
        "lessons": []
    }
]
```
