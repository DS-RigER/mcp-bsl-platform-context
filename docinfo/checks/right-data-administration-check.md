# Right set: Data Administration

## 📋 Общая информация

| Свойство | Значение |
|----------|----------|
| **ID проверки** | `right-data-administration` |
| **Категория** | Role Rights |
| **Серьёзность** | Critical |
| **Тип** | Security |
| **Стандарт** | 1C:Enterprise Development Standards |

## 🎯 Что проверяет

Проверяет назначение права `DataAdministration` (Администрирование данных) в ролях. Это право позволяет выполнять операции с данными: удаление помеченных объектов, реструктуризация, тестирование и исправление. Должно быть ограничено административными ролями.

## ❌ Примеры ошибок

```xml
<!-- Incorrect - DataAdministration for regular user -->
<Rights>
  <Right>
    <Name>DataAdministration</Name>
    <Value>true</Value>
  </Right>
</Rights>

<!-- Incorrect - Manager with data administration -->
<!-- Role: Manager -->
<Rights>
  <Right>
    <Name>DataAdministration</Name>
    <Value>true</Value>
  </Right>
</Rights>
```

## ✅ Правильное решение

```xml
<!-- Correct - DataAdministration for Administrator only -->
<!-- Role: Administrator -->
<Rights>
  <Right>
    <Name>DataAdministration</Name>
    <Value>true</Value>
  </Right>
</Rights>

<!-- Correct - Regular roles without DataAdministration -->
<!-- Role: User -->
<Rights>
  <Right>
    <Name>DataAdministration</Name>
    <Value>false</Value>
  </Right>
</Rights>
```

## 🔧 Как исправить

1. Снимите право DataAdministration для пользовательских ролей
2. Оставьте право только для роли Администратор
3. Используйте регламентные задания для автоматических операций
4. Документируйте операции администрирования данных

## 🔍 Технические детали

- **Класс проверки**: `com.e1c.v8codestyle.right.check.RightDataAdministration`
- **Плагин**: `com.e1c.v8codestyle.right`

## 📚 Ссылки

- [System Rights](https://1c-dn.com/library/system_rights/)
- [Data Administration](https://1c-dn.com/library/data_administration/)
