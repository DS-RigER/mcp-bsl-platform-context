# Определение поля в многострочном описании

## 📋 Общая информация

| Параметр | Значение |
|----------|----------|
| **ID проверки** | `doc-comment-field-in-description-suggestion` |
| **Severity** | Minor |
| **Тип** | Code style |

## 🎯 Что проверяет

Обнаруживает случаи, когда определение поля структуры случайно указано в секции описания, а не в секции типов. Это частая ошибка при написании документирующих комментариев.

## ❌ Примеры ошибок

### Example 1 - Field definition in description

```bsl
// Returns structure with user settings
// * Name - String - user name  ← ERROR: Field defined in description
// * Age - Number - user age
//
// Returns:
//  Structure
//
Function GetUserSettings()
```

### Example 2 - Mixed content

```bsl
// Gets order data.
// Contains fields:  ← ERROR: Fields should be in Returns section
// * OrderNumber - String
// * Date - Date
//
// Returns:
//  Structure
//
Function GetOrderData()
```

## ✅ Правильные решения

### Example 1 - Fields in type section

```bsl
// Returns structure with user settings.
//
// Returns:
//  Structure:
//      * Name - String - user name
//      * Age - Number - user age
//
Function GetUserSettings()
```

### Example 2 - Complete structure description

```bsl
// Gets order data.
//
// Returns:
//  Structure:
//      * OrderNumber - String - order number
//      * Date - Date - order date
//
Function GetOrderData()
```

## 🔧 Как исправить

1. Move field definitions from description to type section
2. Use proper indentation with `*` for fields
3. Place fields under the type definition in Returns section

### Correct structure:
```
// Description here (plain text)
//
// Returns:
//  Structure:
//      * FieldName - Type - description
```

## 🔍 Технические детали

- **Java class**: `MultilineDescriptionFieldSuggestionCheck`
- **Location**: `com.e1c.v8codestyle.bsl.comment.check`
- **Error message**: "Probably Field is defined in description"

## 📚 Ссылки

- [Standard #453 - Commenting code](https://its.1c.ru/db/v8std#content:453:hdoc)
