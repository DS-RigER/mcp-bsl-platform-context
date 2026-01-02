# Определение параметра в многострочном описании

## 📋 Общая информация

| Параметр | Значение |
|----------|----------|
| **ID проверки** | `doc-comment-parameter-in-description-suggestion` |
| **Severity** | Minor |
| **Тип** | Code style |

## 🎯 Что проверяет

Обнаруживает случаи, когда определение параметра метода случайно указано в секции описания, а не в секции "Параметры". Это частая ошибка при написании документирующих комментариев.

## ❌ Примеры ошибок

### Example 1 - Parameter in description

```bsl
// Saves user data.
// User - String - user name  ← ERROR: Looks like parameter definition
// Settings - Structure - user settings
//
Procedure SaveUserData(User, Settings)
```

### Example 2 - Mixed format

```bsl
// Processes order.
// Accepts:
// Order - DocumentRef - order reference  ← ERROR: Should be in Parameters section
//
Procedure ProcessOrder(Order)
```

## ✅ Правильные решения

### Example 1 - Parameters in correct section

```bsl
// Saves user data.
//
// Parameters:
//  User - String - user name
//  Settings - Structure - user settings
//
Procedure SaveUserData(User, Settings)
```

### Example 2 - Properly structured

```bsl
// Processes order.
//
// Parameters:
//  Order - DocumentRef.Order - order reference
//
Procedure ProcessOrder(Order)
```

## 🔧 Как исправить

1. Move parameter definitions to `// Parameters:` section
2. Keep description section for general explanation
3. Use proper format for parameter definitions

### Correct structure:
```
// Description here (plain text about what method does)
//
// Parameters:
//  ParamName - Type - description
```

## 🔍 Технические детали

- **Java class**: `MultilineDescriptionParameterSuggestionCheck`
- **Location**: `com.e1c.v8codestyle.bsl.comment.check`
- **Error message**: "Probably method parameter is defined in description"

## 📚 Ссылки

- [Standard #453 - Commenting code](https://its.1c.ru/db/v8std#content:453:hdoc)
