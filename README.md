# Примеры запусков шедулеров

## Сборка
```
./gradlew jar
```

## Поднятие Postgres
```
docker-compose up -d
```

## Запуск
```
java -jar ./build/libs/jobrunr-example-1.0-SNAPSHOT.jar "jdbc:postgresql://localhost:15432/app_db" app_user app_password
```