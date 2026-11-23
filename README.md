# Patterns — заказ доставки карты

Автотест для задачи №1 занятия «2.3. Patterns» (Netology).

## Стек

- Java 11
- Gradle 8.8 (Gradle Wrapper)
- Selenide 6.19.1
- JUnit 5
- JavaFaker
- Lombok

## Структура

```text
patterns-delivery/
├── artifacts/
│   └── app-replan-delivery.jar
├── gradle/wrapper/
├── src/test/java/ru/netology/delivery/
│   ├── DeliveryTest.java
│   └── data/
│       ├── DataGenerator.java
│       └── DeliveryData.java
├── build.gradle
├── settings.gradle
├── gradlew
└── gradlew.bat
```

## Запуск

1. Установить и использовать JDK 11.
2. В первом терминале из корня проекта запустить приложение:

   ```powershell
   java -jar artifacts/app-replan-delivery.jar
   ```

3. Во втором терминале запустить автотест:

   ```powershell
   .\gradlew.bat clean test
   ```

Для запуска браузера в headless-режиме:

```powershell
.\gradlew.bat clean test -Dselenide.headless=true
```

Тест создаёт заявку на одну дату, затем выбирает другую дату через календарь,
повторно отправляет форму, подтверждает перепланирование и проверяет дату
в уведомлении об успешной операции.
