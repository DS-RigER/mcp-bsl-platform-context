# Virtual table filters should be in parameters

## 📋 Общая информация

| Свойство | Значение |
|----------|----------|
| **ID проверки** | `ql-virtual-table-filters` |
| **Категория** | Query Language |
| **Серьёзность** | Major |
| **Тип** | Performance |
| **Стандарт** | 1C:Enterprise Development Standards |

## 🎯 Что проверяет

Проверяет, что условия фильтрации виртуальных таблиц переданы в параметры таблицы, а не в секцию WHERE. При использовании виртуальных таблиц в запросах передавайте все условия, относящиеся к виртуальной таблице, в параметры таблицы. Не рекомендуется обращаться к виртуальным таблицам с использованием условий секции WHERE.

Такой запрос возвращает правильный результат, но СУБД сложнее выбрать оптимальный метод выполнения запроса. В некоторых случаях это может привести к ошибкам оптимизатора СУБД и значительному замедлению выполнения запроса.

## ❌ Примеры ошибок

```bsl
// Incorrect - filter in WHERE section
Query.Text = "SELECT
    | Products
    |FROM
    | AccumulationRegister.Stock.Balance()
    |WHERE
    | Warehouse = &Warehouse";

// Incorrect - multiple filters in WHERE
Query.Text = "SELECT
    | Goods,
    | QuantityBalance
    |FROM
    | AccumulationRegister.GoodsInWarehouses.Balance()
    |WHERE
    | Warehouse = &Warehouse
    | AND Goods.Category = &Category";

// Incorrect - virtual table without parameters
Query.Text = "SELECT
    | Period,
    | QuantityTurnover
    |FROM
    | AccumulationRegister.Sales.Turnovers()
    |WHERE
    | Product = &Product
    | AND Period >= &StartDate";
```

## ✅ Правильное решение

```bsl
// Correct - filter in table parameters
Query.Text = "SELECT
    | Products
    |FROM
    | AccumulationRegister.Stock.Balance(, Warehouse = &Warehouse)";

// Correct - all conditions in parameters
Query.Text = "SELECT
    | Goods,
    | QuantityBalance
    |FROM
    | AccumulationRegister.GoodsInWarehouses.Balance(
    |   ,
    |   Warehouse = &Warehouse
    |   AND Goods.Category = &Category)";

// Correct - parameters with date range
Query.Text = "SELECT
    | Period,
    | QuantityTurnover
    |FROM
    | AccumulationRegister.Sales.Turnovers(
    |   &StartDate,
    |   &EndDate,
    |   Auto,
    |   Product = &Product)";
```

## 🔧 Как исправить

1. Определите условия, относящиеся к виртуальной таблице
2. Перенесите эти условия из секции WHERE в параметры виртуальной таблицы
3. Используйте параметры периода для ограничения по дате
4. Оставьте в WHERE только условия, не относящиеся к виртуальной таблице

## 🔍 Технические детали

- **Класс проверки**: `com.e1c.v8codestyle.ql.check.VirtualTableFiltersCheck`
- **Плагин**: `com.e1c.v8codestyle.ql`

## 📚 Ссылки

- [Accessing virtual table](https://support.1ci.com/hc/en-us/articles/360011121039-Accessing-virtual-table)
- [Using filters in queries with virtual tables](https://1c-dn.com/library/using_filters_in_queries_with_virtual_tables/)
- [Virtual table parameters](https://1c-dn.com/library/tutorials/practical_developer_guide_virtual_table_parameters3/)
