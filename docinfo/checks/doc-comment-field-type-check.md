# Поле не имеет определения типа

## 📋 Общая информация

| Параметр | Значение |
|----------|----------|
| **ID проверки** | `doc-comment-field-type` |
| **Severity** | Minor |
| **Тип** | Code style |

## 🎯 Что проверяет

Проверяет, что поле в документирующем комментарии имеет определение типа. Каждое поле структуры должно иметь указание типа данных.

## ❌ Примеры ошибок

### Example 1 - Field without type

```bsl
// Returns:
//  Structure:
//      * Name  ← ERROR: Field has no type definition
//      * Age   ← ERROR: Field has no type definition
//
Function GetUserData()
```

### Example 2 - Missing type after hyphen

```bsl
// Returns:
//  Structure:
//      * Name -  ← ERROR: Type definition missing after hyphen
//      * Age - Number - user age
//
Function GetUserData()
```

## ✅ Правильные решения

### Example 1 - Fields with types

```bsl
// Returns:
//  Structure:
//      * Name - String - user name
//      * Age - Number - user age
//
Function GetUserData()
```

### Example 2 - Complete field definitions

```bsl
// Returns:
//  Structure:
//      * Name - String
//      * Age - Number
//      * Active - Boolean
//
Function GetUserData()
```

## 🔧 Как исправить

1. Add type after field name using hyphen separator
2. Format: `* FieldName - Type - Description`
3. Type can be primitive or reference type
4. Description is optional but recommended

### Field format:
```
* FieldName - Type - Description
* FieldName - Type
```

## 🔍 Технические детали

- **Java class**: `FieldDefinitionTypeCheck`
- **Location**: `com.e1c.v8codestyle.bsl.comment.check`
- **Error message**: "Field has no type definition"

## 📚 Ссылки

- [Standard #453 - Commenting code](https://its.1c.ru/db/v8std#content:453:hdoc)
