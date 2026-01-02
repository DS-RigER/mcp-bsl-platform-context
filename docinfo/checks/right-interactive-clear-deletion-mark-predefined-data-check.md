# Right set: Interactive Clear Deletion Mark Predefined Data

## 📋 Общая информация

| Свойство | Значение |
|----------|----------|
| **ID проверки** | `right-interactive-clear-deletion-mark-predefined-data` |
| **Категория** | Role Rights |
| **Серьёзность** | Major |
| **Тип** | Security |
| **Стандарт** | 1C:Enterprise Development Standards |

## 🎯 Что проверяет

Проверяет назначение права `InteractiveClearDeletionMarkPredefinedData` (Интерактивное снятие пометки на удаление предопределённых данных) в ролях. Это право позволяет снимать пометку на удаление с предопределённых элементов. Должно быть ограничено административными ролями.

## ❌ Примеры ошибок

```xml
<!-- Incorrect - Right enabled for regular user role -->
<Rights>
  <Right>
    <Name>InteractiveClearDeletionMarkPredefinedData</Name>
    <Value>true</Value>
    <Object>Catalog.Countries</Object>
  </Right>
</Rights>

<!-- Incorrect - Operator can clear deletion mark -->
<Rights>
  <Right>
    <Name>InteractiveClearDeletionMarkPredefinedData</Name>
    <Value>true</Value>
    <Object>Catalog.PaymentTypes</Object>
  </Right>
</Rights>
```

## ✅ Правильное решение

```xml
<!-- Correct - Right disabled for regular roles -->
<Rights>
  <Right>
    <Name>InteractiveClearDeletionMarkPredefinedData</Name>
    <Value>false</Value>
    <Object>Catalog.Countries</Object>
  </Right>
</Rights>

<!-- Correct - Only Administrator has this right -->
<!-- Role: Administrator -->
<Rights>
  <Right>
    <Name>InteractiveClearDeletionMarkPredefinedData</Name>
    <Value>true</Value>
    <Object>Catalog.Countries</Object>
  </Right>
</Rights>
```

## 🔧 Как исправить

1. Снимите право для всех пользовательских ролей
2. Разрешите только для роли Администратор
3. Операции с предопределёнными данными должны быть ограничены
4. Документируйте изменения предопределённых данных

## 🔍 Технические детали

- **Класс проверки**: `com.e1c.v8codestyle.right.check.RightInteractiveClearDeletionMarkPredefinedData`
- **Плагин**: `com.e1c.v8codestyle.right`

## 📚 Ссылки

- [Predefined data](https://1c-dn.com/library/predefined_items/)
- [Role-based access restriction](https://its.1c.ru/db/v8std/content/689/hdoc)
