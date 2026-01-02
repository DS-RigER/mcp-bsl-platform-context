# Temporary table should have indexes

## 📋 Общая информация

| Свойство | Значение |
|----------|----------|
| **ID проверки** | `ql-temp-table-index` |
| **Категория** | Query Language |
| **Серьёзность** | Major |
| **Тип** | Performance |
| **Стандарт** | 1C:Enterprise Development Standards |

## 🎯 Что проверяет

Проверяет наличие индексов для временных таблиц. Индексирование целесообразно если:

1. Большая временная таблица участвует в соединении (независимо от стороны). Добавьте в индекс поля, участвующие в условии BY.
2. Временная таблица вызывается в подзапросе конструкции логического оператора IN (...). Добавьте в индекс поля временной таблицы из списка выборки, соответствующие полям слева от оператора IN(...).

**Примечание**: Не нужно индексировать маленькие временные таблицы, состоящие менее чем из 1000 записей.

## ❌ Примеры ошибок

```bsl
// Incorrect - large temp table without index used in join
Query.Text = "SELECT
    | Orders.Product AS Product,
    | Orders.Quantity AS Quantity
    |INTO TempOrders
    |FROM
    | Document.SalesOrder.Products AS Orders
    |;
    |SELECT
    | Products.Name,
    | TempOrders.Quantity
    |FROM
    | Catalog.Products AS Products
    |LEFT JOIN TempOrders AS TempOrders
    |ON Products.Ref = TempOrders.Product";

// Incorrect - temp table in IN clause without index
Query.Text = "SELECT
    | Products.Ref AS Product
    |INTO SelectedProducts
    |FROM
    | Catalog.Products AS Products
    |WHERE
    | Products.Category = &Category
    |;
    |SELECT
    | Prices.Price
    |FROM
    | InformationRegister.Prices AS Prices
    |WHERE
    | Prices.Product IN (SELECT Product FROM SelectedProducts)";
```

## ✅ Правильное решение

```bsl
// Correct - temp table with index for join
Query.Text = "SELECT
    | Orders.Product AS Product,
    | Orders.Quantity AS Quantity
    |INTO TempOrders
    |FROM
    | Document.SalesOrder.Products AS Orders
    |
    |INDEX BY
    | Product
    |;
    |SELECT
    | Products.Name,
    | TempOrders.Quantity
    |FROM
    | Catalog.Products AS Products
    |LEFT JOIN TempOrders AS TempOrders
    |ON Products.Ref = TempOrders.Product";

// Correct - temp table with index for IN clause
Query.Text = "SELECT
    | Products.Ref AS Product
    |INTO SelectedProducts
    |FROM
    | Catalog.Products AS Products
    |WHERE
    | Products.Category = &Category
    |
    |INDEX BY
    | Product
    |;
    |SELECT
    | Prices.Price
    |FROM
    | InformationRegister.Prices AS Prices
    |WHERE
    | Prices.Product IN (SELECT Product FROM SelectedProducts)";
```

## 🔧 Как исправить

1. Определите, участвует ли временная таблица в соединениях или IN операторе
2. Добавьте конструкцию `INDEX BY` после определения временной таблицы
3. Включите в индекс поля, используемые в условиях соединения
4. Для IN-операторов индексируйте поля из списка выборки подзапроса

## 🔍 Технические детали

- **Класс проверки**: `com.e1c.v8codestyle.ql.check.TempTableHasIndex`
- **Плагин**: `com.e1c.v8codestyle.ql`

## 📚 Ссылки

- [Using temporary tables](https://kb.1ci.com/1C_Enterprise_Platform/Guides/Developer_Guides/1C_Enterprise_Development_Standards/Data_processing/Optimizing_queries/Using_temporary_tables/)
