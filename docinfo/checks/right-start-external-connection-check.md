# Right set: Start External Connection

## 📋 Общая информация

| Свойство | Значение |
|----------|----------|
| **ID проверки** | `right-start-external-connection` |
| **Категория** | Role Rights |
| **Серьёзность** | Critical |
| **Тип** | Security |
| **Стандарт** | 1C:Enterprise Development Standards |

## 🎯 Что проверяет

Проверяет назначение права `StartExternalConnection` (Внешнее соединение) в ролях. Это право позволяет подключаться к информационной базе через внешнее COM-соединение. Используется для интеграции, но должно быть ограничено служебными учётными записями.

## ❌ Примеры ошибок

```xml
<!-- Incorrect - ExternalConnection for regular user -->
<Rights>
  <Right>
    <Name>StartExternalConnection</Name>
    <Value>true</Value>
  </Right>
</Rights>

<!-- Incorrect - All users can connect externally -->
<!-- Role: User -->
<Rights>
  <Right>
    <Name>StartExternalConnection</Name>
    <Value>true</Value>
  </Right>
</Rights>
```

## ✅ Правильное решение

```xml
<!-- Correct - ExternalConnection for integration service only -->
<!-- Role: IntegrationService -->
<Rights>
  <Right>
    <Name>StartExternalConnection</Name>
    <Value>true</Value>
  </Right>
</Rights>

<!-- Correct - Regular users cannot connect externally -->
<!-- Role: User -->
<Rights>
  <Right>
    <Name>StartExternalConnection</Name>
    <Value>false</Value>
  </Right>
</Rights>

<!-- Correct - Administrator for maintenance -->
<!-- Role: Administrator -->
<Rights>
  <Right>
    <Name>StartExternalConnection</Name>
    <Value>true</Value>
  </Right>
</Rights>
```

## 🔧 Как исправить

1. Снимите право для всех пользовательских ролей
2. Оставьте право для служебных учётных записей
3. Используйте специальные роли для интеграционных подключений
4. Ведите журнал внешних подключений

## 🔍 Технические детали

- **Класс проверки**: `com.e1c.v8codestyle.right.check.RightStartExternalConnection`
- **Плагин**: `com.e1c.v8codestyle.right`

## 📚 Ссылки

- [External Connection](https://1c-dn.com/library/external_connection/)
- [System Rights](https://1c-dn.com/library/system_rights/)
