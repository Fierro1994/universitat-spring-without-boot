# Проект "Университет"

Этот проект представляет собой crud приложение без использования Lombok, Spring, Hibernate для управления студентами, курсами и преподавателями.
- Между курсами и студентами организована связь ManyToMany
- Между Курсами и преподавателями ManyToOne
- Между преподавателями и курсами OneToMany

## Функциональность

- Добавление, обновление, чтение и удаление студентов
- Добавление, обновление, чтение и удаление преподавателей
- Добавление, обновление, чтение и удаление курсов

## Технологии

- Java
- Servlet
- Mapstruct
- JDBC
- Maven
- TestContainers
- Junit
- Mockito

## Установка и запуск
1. Клонируйте репозиторий:
   git clone
   
3. Запустите   

## Использование

## Для студентов

- Добавление студента:
POST /addStudent

```json
{
  "email": "example@mail.ru",
  "name": "example"
}
```

- Обновление студента:
PUT /updateStudent

```json
{
  "id" : "1"
  "email": "example@mail.ru",
  "name": "example"
}
```

- Удаление студента:
DELETE /removeStudent?id=

- Получение информации о студенте:
GET /getStudent?id= 

## Для курсов

- Добавление курса:
POST /addCourse

```json
{
  "name": "java"
}
```
  
- Обновление курса:
PUT /updateCourse

```json
{
  "id" : "1"
  "name": "java"
}
```

- Удаление курса:
DELETE /removeCourse?id=

- Получение информации о курсе:
GET /getCourse?id=

## Для преподователей

- Добавление преподавателя:
POST /addTeacher


```json
{
  "email": "example@mail.ru",
  "name": "Example"
}
```

- Обновление преподавателя:
PUT /updateTeacher


```json
{
  "id": "1"
  "email": "example@mail.ru",
  "name": "Example"
}
```

- Удаление преподавателя:
DELETE /removeTeacher?id=

- Получение информации о преподователе:
GET /getTeacher?id=


