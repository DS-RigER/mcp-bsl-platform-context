# Многострочное описание оканчивается на точку

## 📋 Общая информация

| Параметр | Значение |
|----------|----------|
| **ID проверки** | `doc-comment-description-ends-on-dot` |
| **Severity** | Trivial |
| **Тип** | Code style |
| **По умолчанию** | Выключена |

## 🎯 Что проверяет

Проверяет, что многострочное описание в документирующем комментарии оканчивается точкой. Это правило форматирования для единообразия документации.

## ❌ Примеры ошибок

### Example 1 - Missing dot at end

```bsl
// Processes document and creates movements.
// Returns processing result ← ERROR: No dot at end
//
Function ProcessDocument(Document)
```

### Example 2 - Multi-line without final dot

```bsl
// This function performs complex calculation
// of amounts for the reporting period
// and returns aggregated data ← ERROR: Missing final dot
//
Function CalculateReportData()
```

## ✅ Правильные решения

### Example 1 - Ends with dot

```bsl
// Processes document and creates movements.
// Returns processing result.
//
Function ProcessDocument(Document)
```

### Example 2 - Multi-line with final dot

```bsl
// This function performs complex calculation
// of amounts for the reporting period
// and returns aggregated data.
//
Function CalculateReportData()
```

### Example 3 - Single line is OK

```bsl
// Returns current user name
//
Function GetCurrentUserName()
```

## 🔧 Как исправить

1. Find multi-line descriptions in documentation comments
2. Check if the last line ends with a period
3. Add period at the end if missing

### Note
- This check is disabled by default
- Single-line descriptions are not checked
- Only multi-line (2+ lines) descriptions require final dot

## 🔍 Технические детали

- **Java class**: `MultilineDescriptionEndsOnDotCheck`
- **Location**: `com.e1c.v8codestyle.bsl.comment.check`
- **Default**: Disabled

## 📚 Ссылки

- [Standard #453 - Commenting code](https://its.1c.ru/db/v8std#content:453:hdoc)
