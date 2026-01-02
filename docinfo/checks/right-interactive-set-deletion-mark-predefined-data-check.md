# Right set: Interactive Set Deletion Mark Predefined Data

## 📋 Общая информация

| Свойство | Значение |
|----------|----------|
| **ID проверки** | `right-interactive-set-deletion-mark-predefined-data` |
| **Категория** | Role Rights |
| **Серьёзность** | Major |
| **Тип** | Security |
| **Стандарт** | 1C:Enterprise Development Standards |

## 🎯 Что проверяет

Проверяет назначение права `InteractiveSetDeletionMarkPredefinedData` (Интерактивная пометка на удаление предопределённых данных) в ролях. Это право позволяет помечать на удаление предопределённые элементы справочников. Должно быть ограничено административными ролями.

## ❌ Примеры ошибок

```xml
<!-- Incorrect - Right enabled for regular role -->
<Rights>
  <Right>
    <Name>InteractiveSetDeletionMarkPredefinedData</Name>
    <Value>true</Value>
    <Object>Catalog.Countries</Object>
  </Right>
</Rights>

<!-- Incorrect - User can mark predefined data for deletion -->
<Rights>
  <Right>
    <Name>InteractiveSetDeletionMarkPredefinedData</Name>
    <Value>true</Value>
    <Object>Catalog.Units</Object>
  </Right>
</Rights>
```

## ✅ Правильное решение

```xml
<!-- Correct - Right disabled for user roles -->
<Rights>
  <Right>
    <Name>InteractiveSetDeletionMarkPredefinedData</Name>
    <Value>false</Value>
    <Object>Catalog.Countries</Object>
  </Right>
</Rights>

<!-- Correct - Only for Administrator role -->
<!-- Role: Administrator -->
<Rights>
  <Right>
    <Name>InteractiveSetDeletionMarkPredefinedData</Name>
    <Value>true</Value>
    <Object>Catalog.Countries</Object>
  </Right>
</Rights>
```

## 🔧 Как исправить

1. Запретите право для всех обычных ролей
2. Разрешите только для роли Администратор
3. Предопределённые данные защищают целостность конфигурации
4. Изменение их состояния требует административного контроля

## 🔍 Технические детали

- **Класс проверки**: `com.e1c.v8codestyle.right.check.RightInteractiveSetDeletionMarkPredefinedData`
- **Плагин**: `com.e1c.v8codestyle.right`

## 📚 Ссылки

- [Predefined data](https://1c-dn.com/library/predefined_items/)
- [Role-based access restriction](https://its.1c.ru/db/v8std/content/689/hdoc)
