# Тип коллекции в документирующем комментарии

## 📋 Общая информация

| Параметр | Значение |
|----------|----------|
| **ID проверки** | `doc-comment-collection-item-type` |
| **Severity** | Minor |
| **Тип** | Code style |
| **Стандарт** | № 453 |

## 🎯 Что проверяет

Проверяет, что тип коллекции (массив, соответствие и т.д.) в документирующем комментарии содержит описание типа элемента коллекции.

## ❌ Примеры ошибок

### Example 1 - Collection without item type

```bsl
// Returns list of products
//
// Returns:
//  Array - list of products ← ERROR: Collection type without item type
//
Function GetProducts()
    Result = New Array;
    // ...
    Return Result;
EndFunction
```

### Example 2 - Map without key/value types

```bsl
// Gets settings
//
// Parameters:
//  Settings - Map - user settings ← ERROR: Map without item types
//
Procedure LoadSettings(Settings)
```

## ✅ Правильные решения

### Example 1 - Array with item type

```bsl
// Returns list of products
//
// Returns:
//  Array of CatalogRef.Products - list of products
//
Function GetProducts()
    Result = New Array;
    // ...
    Return Result;
EndFunction
```

### Example 2 - Map with key-value types

```bsl
// Gets settings
//
// Parameters:
//  Settings - Map of KeyAndValue:
//      * Key - String - setting name
//      * Value - Arbitrary - setting value
//
Procedure LoadSettings(Settings)
```

### Example 3 - ValueList with item type

```bsl
// Returns available values
//
// Returns:
//  ValueList of EnumRef.PaymentTypes - payment methods
//
Function GetPaymentTypes()
```

## 🔧 Как исправить

1. Find collection types in documentation comments
2. Add item type specification:
   - For `Array` - add `of <ItemType>`
   - For `Map` - add `of KeyAndValue` with nested description
   - For `ValueList` - add `of <ValueType>`
   - For `FixedArray` - add `of <ItemType>`
3. Use concrete types instead of `Arbitrary` where possible

### Collection types requiring item specification:
- Array / FixedArray
- Map / FixedMap
- ValueList
- FixedCollection

## 🔍 Технические детали

- **Java class**: `CollectionTypeDefinitionCheck`
- **Location**: `com.e1c.v8codestyle.bsl.comment.check`
- **Configurable parameter**: `collectionTypes` - list of collection type names

## 📚 Ссылки

- [Standard #453 - Commenting code](https://its.1c.ru/db/v8std#content:453:hdoc)
- [Documentation comment format](https://its.1c.ru/db/v8std#content:453:hdoc)
