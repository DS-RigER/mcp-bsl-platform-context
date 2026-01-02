# Query cast to max number

## 📋 Общая информация

| Свойство | Значение |
|----------|----------|
| **ID проверки** | `ql-cast-to-max-number` |
| **Категория** | Query Language |
| **Серьёзность** | Major |
| **Тип** | Code smell |
| **Стандарт** | 1C:Enterprise Development Standards |

## 🎯 Что проверяет

Проверяет корректность приведения числовых значений в запросах. Не рекомендуется приводить к максимальной длине числа, так как это может привести к переполнению или неожиданным результатам.

## ❌ Примеры ошибок

```bsl
// Incorrect - casting to maximum precision
Query.Text = "SELECT
    | CAST(Products.Quantity AS NUMBER(31, 15))
    |FROM
    | Catalog.Products AS Products";

// Incorrect - excessive number length
Query.Text = "SELECT
    | CAST(Document.Amount AS NUMBER(25, 10))
    |FROM
    | Document.Invoice AS Document";
```

## ✅ Правильное решение

```bsl
// Correct - appropriate precision for the data type
Query.Text = "SELECT
    | CAST(Products.Quantity AS NUMBER(15, 3))
    |FROM
    | Catalog.Products AS Products";

// Correct - reasonable number length
Query.Text = "SELECT
    | CAST(Document.Amount AS NUMBER(15, 2))
    |FROM
    | Document.Invoice AS Document";
```

## 🔧 Как исправить

1. Проанализируйте фактический диапазон значений в поле
2. Определите оптимальную точность и разрядность
3. Используйте минимально достаточную длину числа
4. Учитывайте ограничения СУБД

## 🔍 Технические детали

- **Класс проверки**: `com.e1c.v8codestyle.ql.check.CastToMaxNumber`
- **Плагин**: `com.e1c.v8codestyle.ql`

## 📚 Ссылки

- [1C:Enterprise Development Standards](https://its.1c.ru/db/v8std)
- [Query Language - Data Types](https://1c-dn.com/library/query_language/)
