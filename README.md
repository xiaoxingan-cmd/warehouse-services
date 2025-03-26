# WarehouseServices
> * В сервисе должно быть организовано логирование процессов -> Большинство из них на уровне DEBUG, поэтому часть может быть не видно.
> * Обеспечить кэширование необходимых запросов -> Сделал отдельный метод для наглядности в сервисе "warehouse-service/src/main/java/com/xiaoxingan/resources/TestCacheResource.java", остальные итак грузятся за 5мс
> * Обеспечить возможность доступа к API сервисов через Swagger -> /swagger
> * Необходимо предусмотреть возможность запуска на БД с нуля, например средствами liquibase -> Добавил первоначальные таблицы и данные
> * Выбор Базы Данных -> Изначально рассматривал MariaDB, поскольку использует намного меньше ресурсов, но в итоге остановился на Postgres
> * Структуру хранения данных в БД -> Представлена на схеме [click](https://dbdiagram.io/d/WarehoseandShop-67e01e3b75d75cc84422a8af), но возможно что-то изменилось
> * Внутреннюю архитектуру микросервисов -> Разобраться в Consul и Stork пока не смог, а из Springа оно не поддерживает, поэтому без SD И LoadBalancer
> * Обработку ошибок и взаимодействие с клиентским приложением -> Добавил исключения и валидацию полей

## Installation
Для быстрого запуска можно использовать docker-compose.

```yml
services:

  warehouse-service:
    image: shayakum/warehouse-service:1.0.0
    depends_on:
      - postgres
    environment:
      QUARKUS_DATASOURCE_JDBC_URL: jdbc:postgresql://postgres:5432/warehouse_db
      QUARKUS_DATASOURCE_USERNAME: admin
      QUARKUS_DATASOURCE_PASSWORD: admin
    ports:
      - 8080:8080
    networks:
      - warehouse_network

  order-service:
    image: shayakum/warehouse-order:1.0.0
    depends_on:
      - postgres
    environment:
      QUARKUS_DATASOURCE_JDBC_URL: jdbc:postgresql://postgres:5432/warehouse_db
      QUARKUS_DATASOURCE_USERNAME: admin
      QUARKUS_DATASOURCE_PASSWORD: admin
    ports:
      - 8081:8081
    networks:
      - warehouse_network

  postgres:
    image: postgres:latest
    environment:
      POSTGRES_USER: admin
      POSTGRES_PASSWORD: admin
      POSTGRES_DB: warehouse_db
    networks:
      - warehouse_network

networks:
  warehouse_network:
    driver: bridge
```
>[!NOTE]
> Сервисы расположены на портах 8080 / 8081
>
> * http://localhost:8080/swagger
> * http://localhost:8081/swagger
