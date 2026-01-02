# Свойство объекта имеет тип возвращаемого значения

## 📋 Общая информация

| Параметр | Значение |
|----------|----------|
| **ID проверки** | `property-return-type` |
| **Severity** | Major |
| **Тип** | Code style |
| **Категория** | Strict Types |

## 🎯 Что проверяет

Система строгой типизации проверяет, что динамическое свойство объекта имеет тип возвращаемого значения. Это обеспечивает типизацию при доступе к свойствам объектов.

## ❌ Примеры ошибок

### Example 1 - Property without return type

```bsl
// @strict-types

Data = GetData();
Value = Data.SomeProperty;  // ← ERROR: Property has no return type
```

### Example 2 - Dynamic property access

```bsl
// @strict-types

// Returns:
//  Structure  ← ERROR: Structure fields not typed
//
Function GetSettings()
    Return Settings;
EndFunction

Result = GetSettings();
Timeout = Result.Timeout;  // ← Property type unknown
```

## ✅ Правильные решения

### Example 1 - Typed structure property

```bsl
// @strict-types

// Returns:
//  Structure:
//      * SomeProperty - String - property value
//
Function GetData()
    Return Data;
EndFunction

Data = GetData();
Value = Data.SomeProperty;  // Type known: String
```

### Example 2 - Complete property typing

```bsl
// @strict-types

// Returns:
//  Structure:
//      * Timeout - Number - timeout in seconds
//      * Server - String - server address
//
Function GetSettings()
    Return Settings;
EndFunction

Result = GetSettings();
Timeout = Result.Timeout;  // Type known: Number
```

## 🔧 Как исправить

1. Add complete type definition in documentation comments
2. For structures, define all field types
3. Use `See FunctionName` for shared type definitions
4. Avoid untyped property access

### Structure typing format:
```
// Returns:
//  Structure:
//      * PropertyName - Type - description
```

## 🔍 Технические детали

- **Java class**: `DynamicFeatureAccessTypeCheck`
- **Location**: `com.e1c.v8codestyle.bsl.strict.check`
- **Error message**: "Feature access has no return type"
- **Applies when**: Module has `@strict-types` annotation

## 📚 Ссылки

- [Code typification](https://its.1c.ru/db/metod8dev#content:5930:hdoc)
