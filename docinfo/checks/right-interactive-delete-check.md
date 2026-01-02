# Right set: Interactive Delete

## 📋 Общая информация

| Свойство | Значение |
|----------|----------|
| **ID проверки** | `right-interactive-delete` |
| **Категория** | Role Rights |
| **Серьёзность** | Critical |
| **Тип** | Security |
| **Стандарт** | 1C:Enterprise Development Standards |

## 🎯 Что проверяет

Проверяет назначение права `InteractiveDelete` (Интерактивное удаление) в ролях. Это право позволяет пользователю удалять объекты непосредственно через интерфейс. Должно быть ограничено только административными ролями.

## ❌ Примеры ошибок

```xml
<!-- Incorrect - InteractiveDelete right enabled for regular role -->
<Rights>
  <Right>
    <Name>InteractiveDelete</Name>
    <Value>true</Value>
    <Object>Catalog.Products</Object>
  </Right>
</Rights>

<!-- Incorrect - InteractiveDelete for documents -->
<Rights>
  <Right>
    <Name>InteractiveDelete</Name>
    <Value>true</Value>
    <Object>Document.Invoice</Object>
  </Right>
</Rights>
```

## ✅ Правильное решение

```xml
<!-- Correct - InteractiveDelete only for Administrator role -->
<!-- Role: Administrator -->
<Rights>
  <Right>
    <Name>InteractiveDelete</Name>
    <Value>true</Value>
    <Object>Catalog.Products</Object>
  </Right>
</Rights>

<!-- Correct - Use InteractiveSetDeletionMark for users -->
<!-- Role: User -->
<Rights>
  <Right>
    <Name>InteractiveSetDeletionMark</Name>
    <Value>true</Value>
    <Object>Catalog.Products</Object>
  </Right>
  <Right>
    <Name>InteractiveDelete</Name>
    <Value>false</Value>
    <Object>Catalog.Products</Object>
  </Right>
</Rights>
```

## 🔧 Как исправить

1. Снимите право InteractiveDelete для обычных ролей
2. Разрешите InteractiveDelete только для роли Администратор
3. Для пользователей используйте право InteractiveSetDeletionMark
4. Настройте регламентную процедуру удаления помеченных объектов

## 🔍 Технические детали

- **Класс проверки**: `com.e1c.v8codestyle.right.check.RightInteractiveDelete`
- **Плагин**: `com.e1c.v8codestyle.right`

## 📚 Ссылки

- [Role-based access restriction](https://its.1c.ru/db/v8std/content/689/hdoc)
- [User Rights](https://1c-dn.com/library/user_rights/)
