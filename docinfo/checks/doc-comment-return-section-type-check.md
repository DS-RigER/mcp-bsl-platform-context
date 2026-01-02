# Секция возвращаемого значения содержит корректные типы

## 📋 Общая информация

| Параметр | Значение |
|----------|----------|
| **ID проверки** | `doc-comment-return-section-type` |
| **Severity** | Minor |
| **Тип** | Code style |

## 🎯 Что проверяет

Проверяет, что секция возвращаемого значения документирующего комментария содержит корректные типы. Тип должен быть указан и должен быть известным типом данных.

## ❌ Примеры ошибок

### Example 1 - Missing return type

```bsl
// Returns current user.
//
// Returns:
//  ← ERROR: Return type is mandatory
//
Function GetCurrentUser()
```

### Example 2 - Unknown type

```bsl
// Gets settings.
//
// Returns:
//  MyCustomType - settings object  ← ERROR: Unknown type
//
Function GetSettings()
```

## ✅ Правильные решения

### Example 1 - With return type

```bsl
// Returns current user.
//
// Returns:
//  CatalogRef.Users - current user reference
//
Function GetCurrentUser()
```

### Example 2 - Known types

```bsl
// Gets settings.
//
// Returns:
//  Structure - settings object
//
Function GetSettings()
```

### Example 3 - Multiple return types

```bsl
// Gets value from cache.
//
// Returns:
//  Arbitrary, Undefined - cached value or Undefined if not found
//
Function GetCachedValue(Key)
```

## 🔧 Как исправить

1. Add type after `// Returns:` section
2. Use known 1C:Enterprise types
3. For multiple types, separate with comma
4. Add description after type

### Return section format:
```
// Returns:
//  Type - description
//  Type, Type2 - description for multiple types
```

### Common valid types:
- Primitive: String, Number, Boolean, Date, Undefined
- References: CatalogRef.*, DocumentRef.*, etc.
- Collections: Array, Structure, Map, ValueTable
- Special: Arbitrary, Type, TypeDescription

## 🔍 Технические детали

- **Java class**: `FunctionReturnSectionCheck`
- **Location**: `com.e1c.v8codestyle.bsl.comment.check`
- **Error messages**: 
  - "Return type is mandatory"
  - "Return type unknown"

## 📚 Ссылки

- [Standard #453 - Commenting code](https://its.1c.ru/db/v8std#content:453:hdoc)
