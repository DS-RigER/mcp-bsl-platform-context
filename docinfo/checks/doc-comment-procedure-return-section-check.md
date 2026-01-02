# Секция возвращаемого значения для процедуры

## 📋 Общая информация

| Параметр | Значение |
|----------|----------|
| **ID проверки** | `doc-comment-procedure-return-section` |
| **Severity** | Minor |
| **Тип** | Code style |

## 🎯 Что проверяет

Проверяет, что документирующий комментарий процедуры не содержит секцию "Возвращаемое значение". Процедуры не возвращают значения, поэтому секция Returns не должна присутствовать.

## ❌ Примеры ошибок

### Example 1 - Procedure with Returns section

```bsl
// Clears cache.
//
// Returns:
//  Boolean - success flag  ← ERROR: Procedure cannot have Returns section
//
Procedure ClearCache()
```

### Example 2 - Procedure with return type

```bsl
// Saves document.
//
// Parameters:
//  Document - DocumentObject - document to save
//
// Returns:  ← ERROR: Remove Returns section
//  Undefined
//
Procedure SaveDocument(Document)
```

## ✅ Правильные решения

### Example 1 - Procedure without Returns

```bsl
// Clears cache.
//
Procedure ClearCache()
```

### Example 2 - Use Function if returning value

```bsl
// Saves document and returns result.
//
// Parameters:
//  Document - DocumentObject - document to save
//
// Returns:
//  Boolean - True if saved successfully
//
Function SaveDocument(Document)
    // ...
    Return True;
EndFunction
```

### Example 3 - Proper procedure documentation

```bsl
// Saves document.
//
// Parameters:
//  Document - DocumentObject - document to save
//
Procedure SaveDocument(Document)
```

## 🔧 Как исправить

1. Remove `// Returns:` section from procedure documentation
2. If method needs to return value, change to Function
3. Keep only Description and Parameters sections for procedures

### Procedure documentation structure:
```
// Description
//
// Parameters:
//  ParamName - Type - description
//
```

## 🔍 Технические детали

- **Java class**: `ProcedureReturnSectionCheck`
- **Location**: `com.e1c.v8codestyle.bsl.comment.check`
- **Error message**: "Return section is not allowed for procedure"

## 📚 Ссылки

- [Standard #453 - Commenting code](https://its.1c.ru/db/v8std#content:453:hdoc)
