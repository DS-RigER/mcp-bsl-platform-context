# Using "FOR UPDATE" clause

## 📋 Общая информация

| Свойство | Значение |
|----------|----------|
| **ID проверки** | `ql-using-for-update` |
| **Категория** | Query Language |
| **Серьёзность** | Minor |
| **Тип** | Code smell |
| **Стандарт** | 1C:Enterprise Development Standards |

## 🎯 Что проверяет

Проверяет использование конструкции FOR UPDATE в запросах. Конструкция FOR UPDATE предназначена для заблаговременной блокировки определённых данных (доступных для чтения из транзакции, принадлежащей другому соединению) во время их чтения, чтобы избежать взаимоблокировок позже, при записи. Не рекомендуется использовать FOR UPDATE, так как она неактуальна в режиме управляемых блокировок.

## ❌ Примеры ошибок

```bsl
// Incorrect - using FOR UPDATE clause
Query.Text = "SELECT
    | Doc.Ref
    |FROM
    | Document.RetailSale AS Doc
    |WHERE
    | Doc.Ref = &DocumentRef
    |FOR UPDATE AccumulationRegister.MutualSettlementsByAgreement.Balance";

// Incorrect - FOR UPDATE with document
Query.Text = "SELECT
    | Invoice.Ref,
    | Invoice.Number
    |FROM
    | Document.Invoice AS Invoice
    |WHERE
    | Invoice.Posted = TRUE
    |FOR UPDATE Document.Invoice";

// Incorrect - FOR UPDATE with catalog
Query.Text = "SELECT
    | Products.Ref,
    | Products.Code
    |FROM
    | Catalog.Products AS Products
    |FOR UPDATE Catalog.Products";
```

## ✅ Правильное решение

```bsl
// Correct - without FOR UPDATE (use managed locks)
Query.Text = "SELECT
    | Doc.Ref
    |FROM
    | Document.RetailSale AS Doc
    |WHERE
    | Doc.Ref = &DocumentRef";

// Correct - use managed lock mode in code
Query.Text = "SELECT
    | Invoice.Ref,
    | Invoice.Number
    |FROM
    | Document.Invoice AS Invoice
    |WHERE
    | Invoice.Posted = TRUE";

// Use DataLock for explicit locking
DataLock = New DataLock;
LockItem = DataLock.Add("AccumulationRegister.MutualSettlements");
LockItem.Mode = DataLockMode.Exclusive;
LockItem.SetValue("Document", DocumentRef);
DataLock.Lock();
```

## 🔧 Как исправить

1. Удалите конструкцию FOR UPDATE из текста запроса
2. Переведите конфигурацию на режим управляемых блокировок
3. Используйте объект `DataLock` для явной блокировки данных
4. Устанавливайте блокировки программно перед чтением данных

## 🔍 Технические детали

- **Класс проверки**: `com.e1c.v8codestyle.ql.check.UsingForUpdateCheck`
- **Плагин**: `com.e1c.v8codestyle.ql`

## 📚 Ссылки

- [Using managed lock mode](https://support.1ci.com/hc/en-us/articles/360011002120-Using-managed-lock-mode)
