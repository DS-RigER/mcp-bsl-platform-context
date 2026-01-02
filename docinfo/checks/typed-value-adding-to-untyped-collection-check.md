# Добавление типизированного значения в нетипизированную коллекцию

## 📋 Общая информация

| Параметр | Значение |
|----------|----------|
| **ID проверки** | `typed-value-adding-to-untyped-collection` |
| **Severity** | Major |
| **Тип** | Code style |
| **Категория** | Strict Types |

## 🎯 Что проверяет

Система строгой типизации проверяет, что типизированное значение не добавляется в нетипизированную коллекцию. Коллекции должны иметь объявленный тип элементов.

## ❌ Примеры ошибок

### Example 1 - Adding to untyped array

```bsl
// @strict-types

// Returns:
//  Array  ← ERROR: Array type not specified
//
Function GetProducts()
    Result = New Array;
    Result.Add(Product);  // ← Adding typed value to untyped collection
    Return Result;
EndFunction
```

### Example 2 - Untyped ValueList

```bsl
// @strict-types

// Parameters:
//  Items - ValueList  ← ERROR: ValueList item type not specified
//
Procedure ProcessItems(Items)
    Items.Add(NewItem);  // ← Adding to untyped collection
EndProcedure
```

## ✅ Правильные решения

### Example 1 - Typed array

```bsl
// @strict-types

// Returns:
//  Array of CatalogRef.Products - list of products
//
Function GetProducts()
    Result = New Array;
    Result.Add(Product);
    Return Result;
EndFunction
```

### Example 2 - Typed ValueList

```bsl
// @strict-types

// Parameters:
//  Items - ValueList of CatalogRef.Products - product list
//
Procedure ProcessItems(Items)
    Items.Add(NewItem);
EndProcedure
```

### Example 3 - Typed Map

```bsl
// @strict-types

// Returns:
//  Map of KeyAndValue:
//      * Key - String - setting name
//      * Value - Arbitrary - setting value
//
Function GetSettings()
    Result = New Map;
    Result.Insert(Key, Value);
    Return Result;
EndFunction
```

## 🔧 Как исправить

1. Add item type specification to collection
2. For `Array` - add `of <ItemType>`
3. For `ValueList` - add `of <ValueType>`
4. For `Map` - add `of KeyAndValue` with nested structure

### Collection typing formats:
```
Array of CatalogRef.Products
ValueList of String
Map of KeyAndValue:
    * Key - Type
    * Value - Type
```

## 🔍 Технические детали

- **Java class**: `TypedValueAddingToUntypedCollectionCheck`
- **Location**: `com.e1c.v8codestyle.bsl.strict.check`
- **Applies when**: Module has `@strict-types` annotation

## 📚 Ссылки

- [Code typification](https://its.1c.ru/db/metod8dev#content:5930:hdoc)
