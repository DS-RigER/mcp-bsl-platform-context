# Пропущено определение параметра

## 📋 Общая информация

| Параметр | Значение |
|----------|----------|
| **ID проверки** | `doc-comment-parameter-section` |
| **Severity** | Minor |
| **Тип** | Code style |

## 🎯 Что проверяет

Проверяет, что в секции параметров документирующего комментария описаны все параметры метода. Каждый параметр должен иметь описание типа.

## ❌ Примеры ошибок

### Example 1 - Missing parameter

```bsl
// Processes data.
//
// Parameters:
//  Data - Structure - data to process
//  ← ERROR: Missing definition for "Options" parameter
//
Procedure ProcessData(Data, Options) Export
```

### Example 2 - No parameters section

```bsl
// Calculates total.
// ← ERROR: Missing Parameters section for "Amount", "Rate"
//
Function CalculateTotal(Amount, Rate) Export
```

## ✅ Правильные решения

### Example 1 - All parameters documented

```bsl
// Processes data.
//
// Parameters:
//  Data - Structure - data to process
//  Options - Structure - processing options
//
Procedure ProcessData(Data, Options) Export
```

### Example 2 - Complete parameters section

```bsl
// Calculates total.
//
// Parameters:
//  Amount - Number - base amount
//  Rate - Number - tax rate percentage
//
// Returns:
//  Number - calculated total with tax
//
Function CalculateTotal(Amount, Rate) Export
```

## 🔧 Как исправить

1. Add `// Parameters:` section if missing
2. List all method parameters
3. Each parameter: `//  ParamName - Type - Description`
4. Match parameter names exactly

### Configurable options:
- `checkOnlyExportMethods` - check only export methods (default: true)

## 🔍 Технические детали

- **Java class**: `ParametersSectionCheck`
- **Location**: `com.e1c.v8codestyle.bsl.comment.check`
- **Error message**: "Parameter definition missed for: {0}"

## 📚 Ссылки

- [Standard #453 - Commenting code](https://its.1c.ru/db/v8std#content:453:hdoc)
