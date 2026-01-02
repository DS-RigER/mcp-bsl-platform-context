# Query join with sub query

## 📋 Общая информация

| Свойство | Значение |
|----------|----------|
| **ID проверки** | `ql-join-to-sub-query` |
| **Категория** | Query Language |
| **Серьёзность** | Major |
| **Тип** | Performance |
| **Стандарт** | 1C:Enterprise Development Standards |

## 🎯 Что проверяет

Проверяет использование соединений с подзапросами. Соединения с подзапросами могут приводить к неоптимальному плану выполнения запроса и снижению производительности. Рекомендуется использовать временные таблицы вместо подзапросов в соединениях.

## ❌ Примеры ошибок

```bsl
// Incorrect - join with subquery
Query.Text = "SELECT
    | Products.Name,
    | Prices.Price
    |FROM
    | Catalog.Products AS Products
    |LEFT JOIN (
    |    SELECT
    |        ProductPrices.Product AS Product,
    |        MAX(ProductPrices.Price) AS Price
    |    FROM
    |        InformationRegister.ProductPrices AS ProductPrices
    |    GROUP BY
    |        ProductPrices.Product
    |) AS Prices
    |ON Products.Ref = Prices.Product";

// Incorrect - multiple subquery joins
Query.Text = "SELECT
    | Orders.Ref,
    | Balances.Balance
    |FROM
    | Document.SalesOrder AS Orders
    |LEFT JOIN (
    |    SELECT
    |        Goods.GoodsRef,
    |        SUM(Goods.Quantity) AS Balance
    |    FROM
    |        AccumulationRegister.GoodsInWarehouses AS Goods
    |    GROUP BY
    |        Goods.GoodsRef
    |) AS Balances
    |ON Orders.Product = Balances.GoodsRef";
```

## ✅ Правильное решение

```bsl
// Correct - use temporary tables
Query.Text = "SELECT
    | ProductPrices.Product AS Product,
    | MAX(ProductPrices.Price) AS Price
    |INTO TempPrices
    |FROM
    | InformationRegister.ProductPrices AS ProductPrices
    |GROUP BY
    | ProductPrices.Product
    |;
    |SELECT
    | Products.Name,
    | Prices.Price
    |FROM
    | Catalog.Products AS Products
    |LEFT JOIN TempPrices AS Prices
    |ON Products.Ref = Prices.Product";

// Correct - separate batch queries
Query.Text = "SELECT
    | Goods.GoodsRef AS GoodsRef,
    | SUM(Goods.Quantity) AS Balance
    |INTO TempBalances
    |FROM
    | AccumulationRegister.GoodsInWarehouses AS Goods
    |GROUP BY
    | Goods.GoodsRef
    |;
    |SELECT
    | Orders.Ref,
    | Balances.Balance
    |FROM
    | Document.SalesOrder AS Orders
    |LEFT JOIN TempBalances AS Balances
    |ON Orders.Product = Balances.GoodsRef";
```

## 🔧 Как исправить

1. Выделите подзапрос во временную таблицу
2. Используйте конструкцию `INTO TempTableName`
3. Замените соединение с подзапросом на соединение с временной таблицей
4. При необходимости добавьте индексы к временной таблице

## 🔍 Технические детали

- **Класс проверки**: `com.e1c.v8codestyle.ql.check.JoinToSubQuery`
- **Плагин**: `com.e1c.v8codestyle.ql`

## 📚 Ссылки

- [Using temporary tables](https://kb.1ci.com/1C_Enterprise_Platform/Guides/Developer_Guides/1C_Enterprise_Development_Standards/Data_processing/Optimizing_queries/Using_temporary_tables/)
- [Query Optimization](https://1c-dn.com/library/query_optimization/)
