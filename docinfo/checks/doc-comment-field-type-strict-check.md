# Поле документирующего комментария имеет описание типа (строгие типы)

## 📋 Общая информация

| Параметр | Значение |
|----------|----------|
| **ID проверки** | `doc-comment-field-type-strict` |
| **Severity** | Major |
| **Тип** | Error |
| **Категория** | Strict Types |

## 🎯 Что проверяет

Проверяет, что поле в документирующем комментарии имеет описание типа в контексте системы строгой типизации кода. Это более строгая версия проверки `doc-comment-field-type`.

## ❌ Примеры ошибок

### Example 1 - Field without type in strict mode

```bsl
// @strict-types

// Returns:
//  Structure:
//      * Name  ← ERROR: Field has no type definition
//
Function GetData()
```

### Example 2 - Incomplete type definition

```bsl
// @strict-types

// Parameters:
//  Settings - Structure:
//      * Timeout -  ← ERROR: Type not specified
//
Procedure Configure(Settings)
```

## ✅ Правильные решения

### Example 1 - Field with type

```bsl
// @strict-types

// Returns:
//  Structure:
//      * Name - String - user name
//
Function GetData()
```

### Example 2 - Complete type definition

```bsl
// @strict-types

// Parameters:
//  Settings - Structure:
//      * Timeout - Number - timeout in seconds
//
Procedure Configure(Settings)
```

## 🔧 Как исправить

1. Add type definition after field name
2. Use format: `* FieldName - Type - Description`
3. Ensure type is a valid 1C:Enterprise type
4. For strict typing, all fields must have explicit types

### Strict typing requirements:
- All fields must have types
- Use concrete types, avoid Arbitrary
- Required when module has `@strict-types` annotation

## 🔍 Технические детали

- **Java class**: `DocCommentFieldTypeCheck`
- **Location**: `com.e1c.v8codestyle.bsl.strict.check`
- **Applies when**: Module has `@strict-types` annotation

## 📚 Ссылки

- [Standard #453 - Commenting code](https://its.1c.ru/db/v8std#content:453:hdoc)
- [Code typification](https://its.1c.ru/db/metod8dev#content:5930:hdoc)
