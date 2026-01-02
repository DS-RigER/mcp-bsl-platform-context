# Right set: Interactive Delete Predefined Data

## 📋 Общая информация

| Свойство | Значение |
|----------|----------|
| **ID проверки** | `right-interactive-delete-predefined-data` |
| **Категория** | Role Rights |
| **Серьёзность** | Critical |
| **Тип** | Security |
| **Стандарт** | 1C:Enterprise Development Standards |

## 🎯 Что проверяет

Проверяет назначение права `InteractiveDeletePredefinedData` (Интерактивное удаление предопределённых данных) в ролях. Это право крайне опасно, так как позволяет удалять данные, заложенные в конфигурацию разработчиком. Должно быть запрещено для всех ролей, кроме полных прав.

## ❌ Примеры ошибок

```xml
<!-- Incorrect - InteractiveDeletePredefinedData enabled -->
<Rights>
  <Right>
    <Name>InteractiveDeletePredefinedData</Name>
    <Value>true</Value>
    <Object>Catalog.Countries</Object>
  </Right>
</Rights>

<!-- Incorrect - Predefined data deletion for manager role -->
<Rights>
  <Right>
    <Name>InteractiveDeletePredefinedData</Name>
    <Value>true</Value>
    <Object>Catalog.PaymentMethods</Object>
  </Right>
</Rights>
```

## ✅ Правильное решение

```xml
<!-- Correct - Predefined data deletion disabled -->
<Rights>
  <Right>
    <Name>InteractiveDeletePredefinedData</Name>
    <Value>false</Value>
    <Object>Catalog.Countries</Object>
  </Right>
</Rights>

<!-- Correct - Only FullRights role may have this -->
<!-- Role: FullRights (for maintenance purposes only) -->
<Rights>
  <Right>
    <Name>InteractiveDeletePredefinedData</Name>
    <Value>true</Value>
    <Object>Catalog.Countries</Object>
  </Right>
</Rights>
```

## 🔧 Как исправить

1. Снимите право InteractiveDeletePredefinedData для всех ролей
2. Если необходимо, разрешите только для роли ПолныеПрава
3. Предопределённые данные должны управляться разработчиком
4. Для изменения предопределённых данных используйте обновление конфигурации

## 🔍 Технические детали

- **Класс проверки**: `com.e1c.v8codestyle.right.check.RightInteractiveDeletePredefinedData`
- **Плагин**: `com.e1c.v8codestyle.right`

## 📚 Ссылки

- [Predefined data](https://1c-dn.com/library/predefined_items/)
- [Role-based access restriction](https://its.1c.ru/db/v8std/content/689/hdoc)
