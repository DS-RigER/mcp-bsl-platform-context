# Избыточная секция параметров

## 📋 Общая информация

| Параметр | Значение |
|----------|----------|
| **ID проверки** | `doc-comment-redundant-parameter-section` |
| **Severity** | Minor |
| **Тип** | Code style |

## 🎯 Что проверяет

Проверяет, что документирующий комментарий для метода без параметров не имеет секции параметров. Если метод не принимает параметров, секция `// Parameters:` избыточна и должна быть удалена.

## ❌ Примеры ошибок

### Example 1 - Parameters section for parameterless method

```bsl
// Returns current date.
//
// Parameters:  ← ERROR: Redundant Parameters section
//
// Returns:
//  Date - current date
//
Function GetCurrentDate()
```

### Example 2 - Empty parameters section

```bsl
// Clears cache.
//
// Parameters:  ← ERROR: Remove empty Parameters section
//
Procedure ClearCache()
```

## ✅ Правильные решения

### Example 1 - No Parameters section

```bsl
// Returns current date.
//
// Returns:
//  Date - current date
//
Function GetCurrentDate()
```

### Example 2 - Simple procedure

```bsl
// Clears cache.
//
Procedure ClearCache()
```

### Example 3 - Function with return only

```bsl
// Gets default timeout value.
//
// Returns:
//  Number - timeout in seconds
//
Function GetDefaultTimeout()
```

## 🔧 Как исправить

1. Remove `// Parameters:` section if method has no parameters
2. Keep only Description and Returns sections (for functions)
3. For procedures without parameters, only Description is needed

### Correct structure for parameterless methods:
```
// Description
//
// Returns:  (for functions only)
//  Type - description
//
```

## 🔍 Технические детали

- **Java class**: `RedundantParametersSectionCheck`
- **Location**: `com.e1c.v8codestyle.bsl.comment.check`
- **Error message**: "Remove useless parameter section"

## 📚 Ссылки

- [Standard #453 - Commenting code](https://its.1c.ru/db/v8std#content:453:hdoc)
