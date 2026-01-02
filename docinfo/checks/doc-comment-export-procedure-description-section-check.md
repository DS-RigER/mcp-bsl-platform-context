# Отсутствует секция описания в комментарии экспортной процедуры

## 📋 Общая информация

| Параметр | Значение |
|----------|----------|
| **ID проверки** | `doc-comment-export-procedure-description-section` |
| **Severity** | Minor |
| **Тип** | Code style |
| **Стандарт** | № 453 |

## 🎯 Что проверяет

Проверяет, что документирующий комментарий к экспортной процедуре (функции) содержит секцию "Описание" - текстовое описание назначения метода.

## ❌ Примеры ошибок

### Example 1 - No description section

```bsl
// Parameters:
//  Data - Structure - data to save  ← ERROR: No description before parameters
//
Procedure SaveData(Data) Export
```

### Example 2 - Empty description

```bsl
//
// Parameters:  ← ERROR: Empty description section
//  User - String - user name
//
Procedure SetCurrentUser(User) Export
```

## ✅ Правильные решения

### Example 1 - With description

```bsl
// Saves data to the database.
// Validates data before saving and raises exception if validation fails.
//
// Parameters:
//  Data - Structure - data to save
//
Procedure SaveData(Data) Export
```

### Example 2 - Complete documentation comment

```bsl
// Sets current user for the session.
// This procedure should be called at the beginning of each session
// to initialize user context.
//
// Parameters:
//  User - String - user name
//
Procedure SetCurrentUser(User) Export
```

### Example 3 - Simple one-line description

```bsl
// Clears temporary files from cache.
//
Procedure ClearCache() Export
```

## 🔧 Как исправить

1. Add text description before `// Parameters:` section
2. Description should explain what the method does
3. Use complete sentences ending with periods
4. For complex logic, use multi-line descriptions

### Documentation comment structure:
```
// Description text (required)
// Can be multi-line for complex methods.
//
// Parameters:
//  ...
//
// Returns:
//  ...
```

## 🔍 Технические детали

- **Java class**: `ExportProcedureCommentDescriptionCheck`
- **Location**: `com.e1c.v8codestyle.bsl.comment.check`
- **Applies to**: Export procedures and functions

## 📚 Ссылки

- [Standard #453 - Commenting code](https://its.1c.ru/db/v8std#content:453:hdoc)
