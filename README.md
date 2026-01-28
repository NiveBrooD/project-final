## Проделанные работы:
 - Удалить (Easy task) не работающие социальные сети или настроить их (Hard task)
   Удалил авторизацию через vk, т.к. он требует редирект на https протокол.
   Остальные исправил.
   Авторизация через github не удастся, если в профиле публично не указал email.
- Вынес чувствительную информацию в отдельный проперти файл.
- Переделал тесты так, чтоб во время тестов использовался тестконтейнер.
- Написал тесты для всех публичных методов контроллера ProfileRestController.
- Сделал рефакторинг метода com.javarush.jira.bugtracking.attachment.FileUtil#upload чтоб он использовал современный подход для работы с файловой системмой.
- Добавитл новый функционал: добавления тегов к задаче (REST API + реализация на сервисе). Фронт делал.
- Добавил подсчет времени сколько задача находилась в работе и тестировании. Методы в классе com.javarush.jira.bugtracking.task.ActivityService
- Также исправил отправку на почту подтверждения регистрации

P.S. Насчет подтверждения почты: там сделано так, что в default profile используется тестовый email для подтверждения, т.е. если вы введете свлю почту подтверждение всё равно будет приходить не на вашу почту, а на тестовую. Чтобы подтверждение регистрации приходило на вашу почту, нужно указывать Active profile: prod 


## [REST API](http://localhost:8080/doc)

## Концепция:

- Spring Modulith
    - [Spring Modulith: достигли ли мы зрелости модульности](https://habr.com/ru/post/701984/)
    - [Introducing Spring Modulith](https://spring.io/blog/2022/10/21/introducing-spring-modulith)
    - [Spring Modulith - Reference documentation](https://docs.spring.io/spring-modulith/docs/current-SNAPSHOT/reference/html/)

```
  url: jdbc:postgresql://localhost:5432/jira
  username: jira
  password: JiraRush
```

- Есть 2 общие таблицы, на которых не fk
    - _Reference_ - справочник. Связь делаем по _code_ (по id нельзя, тк id привязано к окружению-конкретной базе)
    - _UserBelong_ - привязка юзеров с типом (owner, lead, ...) к объекту (таска, проект, спринт, ...). FK вручную будем
      проверять

## Аналоги

- https://java-source.net/open-source/issue-trackers

## Тестирование

- https://habr.com/ru/articles/259055/

Список выполненных задач:
...
