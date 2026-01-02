# Role Right has RLS

## 📋 Общая информация

| Свойство | Значение |
|----------|----------|
| **ID проверки** | `role-right-has-rls` |
| **Категория** | Role Rights |
| **Серьёзность** | Minor |
| **Тип** | Code smell |
| **Стандарт** | 1C:Enterprise Development Standards |

## 🎯 Что проверяет

Проверяет наличие RLS (Row Level Security - ограничение на уровне записей) для прав в ролях. Если для роли установлены права на объект, проверяется необходимость ограничения доступа к отдельным записям. RLS позволяет ограничивать доступ пользователей к данным на уровне отдельных записей.

## ❌ Примеры ошибок

```xml
<!-- Incorrect - Read right without RLS when separation is needed -->
<Rights>
  <Right>
    <Name>Read</Name>
    <Value>true</Value>
    <Object>Document.Invoice</Object>
    <!-- Missing RLS restriction -->
  </Right>
</Rights>

<!-- Incorrect - Update right without organization restriction -->
<Rights>
  <Right>
    <Name>Update</Name>
    <Value>true</Value>
    <Object>Catalog.Partners</Object>
    <!-- Should have RLS by organization -->
  </Right>
</Rights>
```

## ✅ Правильное решение

```xml
<!-- Correct - Read right with RLS restriction -->
<Rights>
  <Right>
    <Name>Read</Name>
    <Value>true</Value>
    <Object>Document.Invoice</Object>
    <Restriction>
      Organization IN (&AvailableOrganizations)
    </Restriction>
  </Right>
</Rights>

<!-- Correct - Update right with RLS -->
<Rights>
  <Right>
    <Name>Update</Name>
    <Value>true</Value>
    <Object>Catalog.Partners</Object>
    <Restriction>
      Organization IN (&AvailableOrganizations) AND
      Department IN (&AvailableDepartments)
    </Restriction>
  </Right>
</Rights>
```

## 🔧 Как исправить

1. Определите, требуется ли разграничение данных по записям
2. Определите измерения разграничения (организация, подразделение и т.д.)
3. Добавьте ограничения RLS к соответствующим правам
4. Используйте параметры сессии для хранения доступных значений

## 🔍 Технические детали

- **Класс проверки**: `com.e1c.v8codestyle.right.check.RoleRightHasRls`
- **Плагин**: `com.e1c.v8codestyle.right`

## 📚 Ссылки

- [Row Level Security (RLS)](https://1c-dn.com/library/row_level_security/)
- [Access restriction at record level](https://its.1c.ru/db/v8std/content/689/hdoc)
