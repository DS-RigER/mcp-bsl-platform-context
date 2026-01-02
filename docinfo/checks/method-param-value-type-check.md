# Параметр метода имеет тип

## 📋 Общая информация

| Параметр | Значение |
|----------|----------|
| **ID проверки** | `method-param-value-type` |
| **Severity** | Major |
| **Тип** | Code style |
| **Категория** | Strict Types |

## 🎯 Что проверяет

Система строгой типизации проверяет, что каждый параметр метода имеет тип значения. Тип должен быть указан в документирующем комментарии.

## ❌ Примеры ошибок

### Example 1 - Parameter without type

```bsl
// @strict-types

// Processes data.
//
// Parameters:
//  Data  ← ERROR: Parameter has no value type
//
Procedure ProcessData(Data)
```

### Example 2 - Missing type in parameters

```bsl
// @strict-types

// Saves user settings.
//
// Parameters:
//  User - String - user name
//  Settings  ← ERROR: Missing type for "Settings" parameter
//
Procedure SaveSettings(User, Settings)
```

## ✅ Правильные решения

### Example 1 - Parameter with type

```bsl
// @strict-types

// Processes data.
//
// Parameters:
//  Data - Structure - data to process
//
Procedure ProcessData(Data)
```

### Example 2 - All parameters typed

```bsl
// @strict-types

// Saves user settings.
//
// Parameters:
//  User - String - user name
//  Settings - Structure - user settings
//
Procedure SaveSettings(User, Settings)
```

## 🔧 Как исправить

1. Add type for each parameter in documentation comment
2. Format: `//  ParamName - Type - description`
3. Use concrete types, avoid Arbitrary when possible
4. Required when using `@strict-types` annotation

### Parameter format:
```
// Parameters:
//  ParamName - Type - description
```

## 🔍 Технические детали

- **Java class**: `MethodParamTypeCheck`
- **Location**: `com.e1c.v8codestyle.bsl.strict.check`
- **Error message**: "Method param has no value type"
- **Applies when**: Module has `@strict-types` annotation

## 📚 Ссылки

- [Code typification](https://its.1c.ru/db/metod8dev#content:5930:hdoc)
- [Standard #453 - Commenting code](https://its.1c.ru/db/v8std#content:453:hdoc)
