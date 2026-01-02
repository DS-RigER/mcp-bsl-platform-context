# The right operand of the LIKE comparison operation is a table field

## 📋 Общая информация

| Свойство | Значение |
|----------|----------|
| **ID проверки** | `ql-like-expression-with-field` |
| **Категория** | Query Language |
| **Серьёзность** | Major |
| **Тип** | Code smell |
| **Стандарт** | 1C:Enterprise Development Standards |

## 🎯 Что проверяет

Проверяет, что правый операнд операции сравнения LIKE является литералом (параметром) или выражением над литералами. Использование поля таблицы в качестве правого операнда LIKE и ESCAPE запрещено. Маскирующие символы: `_` (любой символ) и `%` (последовательность любых символов).

## ❌ Примеры ошибок

```bsl
// Incorrect - table field as right operand of LIKE
Query.Text = "SELECT
    | StocksBalanceAndTurnovers.Warehouse
    |FROM
    | AccumulationRegister.Stocks.BalanceAndTurnovers AS StocksBalanceAndTurnovers
    |WHERE
    | StocksBalanceAndTurnovers.Warehouse LIKE Table.Field";

// Incorrect - using another field in LIKE pattern
Query.Text = "SELECT
    | Products.Name
    |FROM
    | Catalog.Products AS Products
    |WHERE
    | Products.Code LIKE Products.SearchPattern";

// Incorrect - field reference with wildcard
Query.Text = "SELECT
    | Documents.Number
    |FROM
    | Document.Invoice AS Documents
    |WHERE
    | Documents.Number LIKE Patterns.NumberMask";
```

## ✅ Правильное решение

```bsl
// Correct - string literal with escape character
Query.Text = "SELECT
    | StocksBalanceAndTurnovers.Warehouse
    |FROM
    | AccumulationRegister.Stocks.BalanceAndTurnovers AS StocksBalanceAndTurnovers
    |WHERE
    | StocksBalanceAndTurnovers.Warehouse LIKE ""123%!%"" ESCAPE ""!""";

// Correct - parameter as pattern
Query.Text = "SELECT
    | Products.Name
    |FROM
    | Catalog.Products AS Products
    |WHERE
    | Products.Code LIKE &SearchPattern";

// Correct - literal pattern
Query.Text = "SELECT
    | Documents.Number
    |FROM
    | Document.Invoice AS Documents
    |WHERE
    | Documents.Number LIKE ""INV-%""";
```

## 🔧 Как исправить

1. Замените поле таблицы на строковый литерал или параметр
2. Сформируйте шаблон поиска в коде BSL и передайте как параметр
3. Используйте константные строковые литералы
4. При необходимости экранирования используйте ESCAPE с литералом

## 🔍 Технические детали

- **Класс проверки**: `com.e1c.v8codestyle.ql.check.LikeExpressionWithFieldCheck`
- **Плагин**: `com.e1c.v8codestyle.ql`

## 📚 Ссылки

- [Appendix 8. Features of operating with different DBMS](https://support.1ci.com/hc/en-us/articles/6347699838098-8-3-IBM-Db2)
- [Specifics of using LIKE operator in queries](https://support.1ci.com/hc/en-us/articles/360011001500-Specifics-of-using-LIKE-operator-in-queries)
