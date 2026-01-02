# Right set: Administration

## 📋 Общая информация

| Свойство | Значение |
|----------|----------|
| **ID проверки** | `right-administration` |
| **Категория** | Role Rights |
| **Серьёзность** | Critical |
| **Тип** | Security |
| **Стандарт** | 1C:Enterprise Development Standards |

## 🎯 Что проверяет

Проверяет назначение права `Administration` (Администрирование) в ролях. Это одно из самых важных системных прав, дающее доступ к административным функциям: управление пользователями, резервное копирование, обновление конфигурации. Должно быть доступно только для роли Администратор.

## ❌ Примеры ошибок

```xml
<!-- Incorrect - Administration right for non-admin role -->
<Rights>
  <Right>
    <Name>Administration</Name>
    <Value>true</Value>
  </Right>
</Rights>

<!-- Incorrect - Multiple roles with Administration -->
<!-- Role: Manager -->
<Rights>
  <Right>
    <Name>Administration</Name>
    <Value>true</Value>
  </Right>
</Rights>
```

## ✅ Правильное решение

```xml
<!-- Correct - Administration only for Administrator role -->
<!-- Role: Administrator -->
<Rights>
  <Right>
    <Name>Administration</Name>
    <Value>true</Value>
  </Right>
</Rights>

<!-- Correct - Other roles without Administration -->
<!-- Role: User -->
<Rights>
  <Right>
    <Name>Administration</Name>
    <Value>false</Value>
  </Right>
</Rights>
```

## 🔧 Как исправить

1. Проверьте все роли с правом Administration
2. Снимите право для всех ролей, кроме Администратор
3. Создайте отдельные роли для специфических административных задач
4. Используйте право DataAdministration для управления данными

## 🔍 Технические детали

- **Класс проверки**: `com.e1c.v8codestyle.right.check.RightAdministration`
- **Плагин**: `com.e1c.v8codestyle.right`

## 📚 Ссылки

- [System Rights](https://1c-dn.com/library/system_rights/)
- [Role-based access restriction](https://its.1c.ru/db/v8std/content/689/hdoc)
