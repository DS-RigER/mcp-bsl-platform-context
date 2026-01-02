# Right set: Active Users

## 📋 Общая информация

| Свойство | Значение |
|----------|----------|
| **ID проверки** | `right-active-users` |
| **Категория** | Role Rights |
| **Серьёзность** | Major |
| **Тип** | Security |
| **Стандарт** | 1C:Enterprise Development Standards |

## 🎯 Что проверяет

Проверяет назначение права `ActiveUsers` (Активные пользователи) в ролях. Это право позволяет просматривать список активных пользователей системы. Должно быть ограничено административными ролями по соображениям безопасности.

## ❌ Примеры ошибок

```xml
<!-- Incorrect - ActiveUsers right for regular user -->
<Rights>
  <Right>
    <Name>ActiveUsers</Name>
    <Value>true</Value>
  </Right>
</Rights>

<!-- Incorrect - Every role has ActiveUsers -->
<!-- Role: Operator -->
<Rights>
  <Right>
    <Name>ActiveUsers</Name>
    <Value>true</Value>
  </Right>
</Rights>
```

## ✅ Правильное решение

```xml
<!-- Correct - ActiveUsers only for admin roles -->
<!-- Role: Administrator -->
<Rights>
  <Right>
    <Name>ActiveUsers</Name>
    <Value>true</Value>
  </Right>
</Rights>

<!-- Correct - Regular roles have no ActiveUsers right -->
<!-- Role: User -->
<Rights>
  <Right>
    <Name>ActiveUsers</Name>
    <Value>false</Value>
  </Right>
</Rights>
```

## 🔧 Как исправить

1. Снимите право ActiveUsers для обычных пользовательских ролей
2. Оставьте право только для административных ролей
3. Список активных пользователей — административная информация
4. При необходимости создайте отдельный отчёт с ограниченными данными

## 🔍 Технические детали

- **Класс проверки**: `com.e1c.v8codestyle.right.check.RightActiveUsers`
- **Плагин**: `com.e1c.v8codestyle.right`

## 📚 Ссылки

- [System Rights](https://1c-dn.com/library/system_rights/)
- [Role-based access restriction](https://its.1c.ru/db/v8std/content/689/hdoc)
