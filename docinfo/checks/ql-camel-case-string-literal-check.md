# Query string literal contains non CamelCase content

## 📋 Общая информация

| Свойство | Значение |
|----------|----------|
| **ID проверки** | `ql-camel-case-string-literal` |
| **Категория** | Query Language |
| **Серьёзность** | Minor |
| **Тип** | Code smell |
| **Стандарт** | 1C:Enterprise Development Standards |

## 🎯 Что проверяет

Проверяет, что строковые литералы в запросах соответствуют стилю CamelCase. Это относится к псевдонимам полей и таблиц.

## ❌ Примеры ошибок

```bsl
// Incorrect - lowercase alias
Query.Text = "SELECT
    | Products.Name AS product_name
    |FROM
    | Catalog.Products AS Products";

// Incorrect - mixed case with underscore
Query.Text = "SELECT
    | Products.Code AS Product_Code
    |FROM
    | Catalog.Products AS products";
```

## ✅ Правильное решение

```bsl
// Correct - CamelCase alias
Query.Text = "SELECT
    | Products.Name AS ProductName
    |FROM
    | Catalog.Products AS Products";

// Correct - proper CamelCase
Query.Text = "SELECT
    | Products.Code AS ProductCode
    |FROM
    | Catalog.Products AS Products";
```

## 🔧 Как исправить

1. Найдите все строковые литералы в запросе
2. Преобразуйте псевдонимы полей в формат CamelCase
3. Удалите подчёркивания между словами
4. Начинайте каждое слово с заглавной буквы

## 🔍 Технические детали

- **Класс проверки**: `com.e1c.v8codestyle.ql.check.CamelCaseStringLiteral`
- **Плагин**: `com.e1c.v8codestyle.ql`

## 📚 Ссылки

- [1C:Enterprise Development Standards](https://its.1c.ru/db/v8std)
- [Query Language Documentation](https://1c-dn.com/library/query_language/)
