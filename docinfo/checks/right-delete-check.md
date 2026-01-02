# Right set: Delete

## 📋 Общая информация

| Свойство | Значение |
|----------|----------|
| **ID проверки** | `right-delete` |
| **Категория** | Role Rights |
| **Серьёзность** | Critical |
| **Тип** | Security |
| **Стандарт** | 1C:Enterprise Development Standards |

## 🎯 Что проверяет

Проверяет назначение права `Delete` (Удаление) в ролях. Право непосредственного удаления данных должно быть ограничено минимальным количеством ролей по соображениям безопасности. В большинстве случаев следует использовать механизм пометки на удаление вместо прямого удаления.

## ❌ Примеры ошибок

```xml
<!-- Incorrect - Delete right enabled for regular user role -->
<Rights>
  <Right>
    <Name>Delete</Name>
    <Value>true</Value>
    <Object>Catalog.Products</Object>
  </Right>
</Rights>

<!-- Incorrect - Delete right without proper consideration -->
<Rights>
  <Right>
    <Name>Delete</Name>
    <Value>true</Value>
    <Object>Document.SalesOrder</Object>
  </Right>
</Rights>
```

## ✅ Правильное решение

```xml
<!-- Correct - Delete right only for administrative role -->
<!-- Role: Administrator -->
<Rights>
  <Right>
    <Name>Delete</Name>
    <Value>true</Value>
    <Object>Catalog.Products</Object>
  </Right>
</Rights>

<!-- Correct - Use deletion mark instead of direct delete -->
<!-- Role: User -->
<Rights>
  <Right>
    <Name>InteractiveSetDeletionMark</Name>
    <Value>true</Value>
    <Object>Catalog.Products</Object>
  </Right>
</Rights>
```

## 🔧 Как исправить

1. Снимите право Delete для обычных пользовательских ролей
2. Оставьте право Delete только для административных ролей
3. Используйте механизм пометки на удаление для обычных операций
4. Реализуйте регламентное удаление помеченных объектов

## 🔍 Технические детали

- **Класс проверки**: `com.e1c.v8codestyle.right.check.RightDelete`
- **Плагин**: `com.e1c.v8codestyle.right`

## 📚 Ссылки

- [Role-based access restriction](https://its.1c.ru/db/v8std/content/689/hdoc)
- [Deletion mark mechanism](https://1c-dn.com/library/deletion_mark/)
