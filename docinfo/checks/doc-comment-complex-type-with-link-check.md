# Поле документирующего комментария использует сложный тип вместо ссылки

## 📋 Общая информация

| Параметр | Значение |
|----------|----------|
| **ID проверки** | `doc-comment-complex-type-with-link` |
| **Severity** | Minor |
| **Тип** | Code style |

## 🎯 Что проверяет

Проверяет, что поле документирующего комментария использует ссылку на функцию-конструктор вместо полного описания сложного типа. Это улучшает читаемость и позволяет избежать дублирования описаний.

## ❌ Примеры ошибок

### Example 1 - Full structure description instead of link

```bsl
// Gets document data
//
// Returns:
//  Structure:
//      * Date - Date - document date
//      * Number - String - document number
//      * Amount - Number - total amount  ← ERROR: Use link to constructor
//      * Currency - CatalogRef.Currencies - currency
//
Function GetDocumentData()
```

### Example 2 - Inline complex type description

```bsl
// Parameters:
//  Settings - Structure:
//      * User - String
//      * Password - String  ← ERROR: Complex type repeated in multiple places
//      * Server - String
//
Procedure Connect(Settings)
```

## ✅ Правильные решения

### Example 1 - Link to constructor function

```bsl
// Gets document data
//
// Returns:
//  Structure - See NewDocumentData
//
Function GetDocumentData()
    Return NewDocumentData();
EndFunction

// Creates new document data structure
//
// Returns:
//  Structure:
//      * Date - Date - document date
//      * Number - String - document number
//      * Amount - Number - total amount
//      * Currency - CatalogRef.Currencies - currency
//
Function NewDocumentData()
    Result = New Structure;
    Result.Insert("Date", CurrentDate());
    Result.Insert("Number", "");
    Result.Insert("Amount", 0);
    Result.Insert("Currency", Undefined);
    Return Result;
EndFunction
```

### Example 2 - Reference to type definition

```bsl
// Parameters:
//  Settings - See ConnectionSettings
//
Procedure Connect(Settings)

// Returns connection settings structure
//
// Returns:
//  Structure:
//      * User - String
//      * Password - String
//      * Server - String
//
Function ConnectionSettings()
```

## 🔧 Как исправить

1. Create a constructor function that returns the complex type
2. Document the structure in the constructor function
3. In other places, use `See FunctionName` reference
4. This avoids duplication and keeps structure definition in one place

### Типы, которые следует выносить в конструкторы:
- Structure / FixedStructure
- ValueTable / ValueTree
- Array with complex items
- Map with specific key-value types

## 🔍 Технические детали

- **Java class**: `FieldDefinitionTypeWithLinkRefCheck`
- **Location**: `com.e1c.v8codestyle.bsl.comment.check`
- **Configurable parameter**: `collectionTypes` - types to check

## 📚 Ссылки

- [Standard #453 - Commenting code](https://its.1c.ru/db/v8std#content:453:hdoc)
- [Constructor function pattern](https://its.1c.ru/db/v8std#content:640:hdoc)
