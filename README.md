# Java Tasks (KFU)

Репозиторий с выполненными заданиями по Java (КФУ).

## Проекты

### 1) java_Tasks
- `Java/java_Tasks/List_realization` — абстрактный `List` + `ArrayList/LinkedList` + итераторы
- `Java/java_Tasks/Integral_MonteKarlo` — вычисление интеграла методом Монте‑Карло
- `Java/java_Tasks/Weather_request` — запрос к Yandex Weather API и вывод текущей погоды

### 2) Test: Java Basics
Публичное API → кэш в памяти + файл → собственный HTTP‑сервер.

**Функциональность:**
- загрузка пользователей с `https://jsonplaceholder.typicode.com/users`
- сохранение на диск: `data/users.csv`
- потокобезопасный репозиторий + `refresh()` в фоне (пул потоков)
- HTTP сервер (JDK `HttpServer`) с эндпоинтами:
  - `GET /users`
  - `GET /users/{id}`
  - `POST /refresh`
  - `GET /stats`

## Скриншоты
- `learninggitbranching/` — скриншоты прохождения Learn Git Branching
- `stepik/` — скриншоты/папки по пройденным шагам Stepik
- `portswigger/` - — скриншоты прохождения курсов PortSwigger

### Мой сервер
Папка `server/` — простой backend для тестирования через Postman:
- `POST /login` → токен
- `GET /image` → png/jpg
- `POST /game` → win/draw/loss
- `DELETE /delete` → удаление/выход

---

## Требования
- Java 17+

Проверка версии:
```bash
java -version
javac -version
```

---

## Сборка и запуск

### macOS / Linux

#### java_Tasks
Перейти в папку:
```bash
cd Java/java_Tasks
```

**1) List_realization**
```bash
javac -d out List_realization/src/*.java
java -cp out list_realization.Demo
```

**2) Integral_MonteKarlo**
```bash
javac -d out Integral_MonteKarlo/src/*.java
java -cp out integral_montekarlo.Main square 0 1 1000000 42
```

**3) Weather_request**
Ключ передаётся через переменную окружения `YANDEX_WEATHER_KEY`.
```bash
export YANDEX_WEATHER_KEY="ВАШ_КЛЮЧ"
javac -d out Weather_request/src/*.java
java -cp out weather_request.Main 55.7558 37.6173
```

#### Test: Java Basics
Перейти в папку проекта (важно: кавычки из‑за пробелов/двоеточия):
```bash
cd "Test: Java Basics"
```

Скомпилировать:
```bash
mkdir -p out
javac -d out $(find src -name "*.java")
```

Запустить сервер:
```bash
java -cp out app.Main
```

Проверка эндпоинтов (в новом окне терминала):
```bash
curl -i http://localhost:8080/users; echo
curl -i http://localhost:8080/users/3; echo
curl -i http://localhost:8080/stats; echo
curl -i -X POST http://localhost:8080/refresh; echo
```

Файл с данными после обновления:
```bash
ls -l data/users.csv
head -n 5 data/users.csv
```
