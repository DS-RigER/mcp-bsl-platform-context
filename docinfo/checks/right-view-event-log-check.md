# Right set: View Event Log

## 📋 Общая информация

| Свойство | Значение |
|----------|----------|
| **ID проверки** | `right-view-event-log` |
| **Категория** | Role Rights |
| **Серьёзность** | Major |
| **Тип** | Security |
| **Стандарт** | 1C:Enterprise Development Standards |

## 🎯 Что проверяет

Проверяет назначение права `ViewEventLog` (Просмотр журнала регистрации) в ролях. Журнал регистрации содержит информацию о действиях пользователей и системных событиях. Доступ к нему должен быть ограничен административными ролями.

## ❌ Примеры ошибок

```xml
<!-- Incorrect - ViewEventLog for regular user -->
<Rights>
  <Right>
    <Name>ViewEventLog</Name>
    <Value>true</Value>
  </Right>
</Rights>

<!-- Incorrect - All users can view log -->
<!-- Role: User -->
<Rights>
  <Right>
    <Name>ViewEventLog</Name>
    <Value>true</Value>
  </Right>
</Rights>
```

## ✅ Правильное решение

```xml
<!-- Correct - ViewEventLog for Administrator -->
<!-- Role: Administrator -->
<Rights>
  <Right>
    <Name>ViewEventLog</Name>
    <Value>true</Value>
  </Right>
</Rights>

<!-- Correct - Auditor role for compliance -->
<!-- Role: Auditor -->
<Rights>
  <Right>
    <Name>ViewEventLog</Name>
    <Value>true</Value>
  </Right>
</Rights>

<!-- Correct - Regular users cannot view log -->
<!-- Role: User -->
<Rights>
  <Right>
    <Name>ViewEventLog</Name>
    <Value>false</Value>
  </Right>
</Rights>
```

## 🔧 Как исправить

1. Снимите право ViewEventLog для пользовательских ролей
2. Оставьте право для ролей Администратор и Аудитор
3. Журнал регистрации содержит конфиденциальную информацию
4. Создайте специальные отчёты для ограниченного доступа к логам

## 🔍 Технические детали

- **Класс проверки**: `com.e1c.v8codestyle.right.check.RightViewEventLog`
- **Плагин**: `com.e1c.v8codestyle.right`

## 📚 Ссылки

- [Event Log](https://1c-dn.com/library/event_log/)
- [System Rights](https://1c-dn.com/library/system_rights/)
