# Right set: Interactive Delete Marked Predefined Data

## 📋 Общая информация

| Свойство | Значение |
|----------|----------|
| **ID проверки** | `right-interactive-delete-marked-predefined-data` |
| **Категория** | Role Rights |
| **Серьёзность** | Critical |
| **Тип** | Security |
| **Стандарт** | 1C:Enterprise Development Standards |

## 🎯 Что проверяет

Проверяет назначение права `InteractiveDeleteMarkedPredefinedData` (Интерактивное удаление помеченных предопределённых данных) в ролях. Это право позволяет удалять помеченные на удаление предопределённые элементы. Должно быть ограничено только системными ролями.

## ❌ Примеры ошибок

```xml
<!-- Incorrect - Right enabled for regular role -->
<Rights>
  <Right>
    <Name>InteractiveDeleteMarkedPredefinedData</Name>
    <Value>true</Value>
    <Object>Catalog.Countries</Object>
  </Right>
</Rights>

<!-- Incorrect - Manager can delete marked predefined data -->
<Rights>
  <Right>
    <Name>InteractiveDeleteMarkedPredefinedData</Name>
    <Value>true</Value>
    <Object>Catalog.Currencies</Object>
  </Right>
</Rights>
```

## ✅ Правильное решение

```xml
<!-- Correct - Right disabled for all roles -->
<Rights>
  <Right>
    <Name>InteractiveDeleteMarkedPredefinedData</Name>
    <Value>false</Value>
    <Object>Catalog.Countries</Object>
  </Right>
</Rights>

<!-- Correct - Only for FullRights/Administrator role -->
<!-- Role: FullRights -->
<Rights>
  <Right>
    <Name>InteractiveDeleteMarkedPredefinedData</Name>
    <Value>true</Value>
    <Object>Catalog.Countries</Object>
  </Right>
</Rights>
```

## 🔧 Как исправить

1. Снимите право InteractiveDeleteMarkedPredefinedData для пользовательских ролей
2. Оставьте право только для служебных ролей (ПолныеПрава)
3. Контролируйте удаление предопределённых данных через администратора
4. Используйте регламентные задания для очистки помеченных объектов

## 🔍 Технические детали

- **Класс проверки**: `com.e1c.v8codestyle.right.check.RightInteractiveDeleteMarkedPredefinedData`
- **Плагин**: `com.e1c.v8codestyle.right`

## 📚 Ссылки

- [Predefined data](https://1c-dn.com/library/predefined_items/)
- [Role-based access restriction](https://its.1c.ru/db/v8std/content/689/hdoc)
