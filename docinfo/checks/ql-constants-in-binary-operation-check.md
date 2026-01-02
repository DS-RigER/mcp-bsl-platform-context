# Using binary operations with constants or parameters in queries

## 📋 Общая информация

| Свойство | Значение |
|----------|----------|
| **ID проверки** | `ql-constants-in-binary-operation` |
| **Категория** | Query Language |
| **Серьёзность** | Major |
| **Тип** | Code smell |
| **Стандарт** | 1C:Enterprise Development Standards |

## 🎯 Что проверяет

Проверяет использование бинарных операций с константами или параметрами в запросах. Не рекомендуется генерировать шаблонную строку с использованием вычислений или конкатенации строк в языке запросов. Это требование основано на особенностях миграции приложений на различные СУБД.

## ❌ Примеры ошибок

```bsl
// Incorrect - string concatenation in query
Query.Text = "SELECT
    | Products.Name AS Name,
    | ""My"" + ""Goods"" AS Code
    |FROM
    | Catalog.Products AS Products";

// Incorrect - concatenation with parameter
Query.Text = "SELECT
    | Products.Name AS Name,
    | ""My"" + &Parameter AS Code
    |FROM
    | Catalog.Products AS Products";

// Incorrect - concatenation in LIKE clause
Query.Text = "SELECT
    | Products.Name AS Name,
    | Products.Code AS Code
    |FROM
    | Catalog.Products AS Products
    |WHERE
    | Products.Code LIKE ""123"" + ""%""";
```

## ✅ Правильное решение

```bsl
// Correct - single string literal
Query.Text = "SELECT
    | Products.Name AS Name,
    | ""MyGoods"" AS Code
    |FROM
    | Catalog.Products AS Products";

// Correct - parameter without concatenation
Query.Text = "SELECT
    | Products.Name AS Name,
    | &Parameter AS Code
    |FROM
    | Catalog.Products AS Products";

// Correct - complete pattern in LIKE
Query.Text = "SELECT
    | Products.Name AS Name,
    | Products.Code AS Code
    |FROM
    | Catalog.Products AS Products
    |WHERE
    | Products.Code LIKE ""123%""";
```

## 🔧 Как исправить

1. Объедините строковые литералы в одну строку
2. Формируйте параметры запроса заранее в коде BSL
3. Используйте полные шаблоны в выражениях LIKE
4. Избегайте вычислений и конкатенации внутри текста запроса

## 🔍 Технические детали

- **Класс проверки**: `com.e1c.v8codestyle.ql.check.ConstantsInBinaryOperationCheck`
- **Плагин**: `com.e1c.v8codestyle.ql`

## 📚 Ссылки

- [Effective query conditions](https://support.1ci.com/hc/en-us/articles/360011121019-Effective-query-conditions)
- [Specifics of using LIKE operator in queries](https://support.1ci.com/hc/en-us/articles/360011001500-Specifics-of-using-LIKE-operator-in-queries)
