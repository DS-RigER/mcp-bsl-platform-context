# Определение типа документирующего комментария

## 📋 Общая информация

| Параметр | Значение |
|----------|----------|
| **ID проверки** | `doc-comment-type` |
| **Severity** | Minor |
| **Тип** | Code style |

## 🎯 Что проверяет

Проверяет, что определение типа в документирующем комментарии содержит правильное имя типа. Указанный тип должен быть известным типом данных 1C:Enterprise.

## ❌ Примеры ошибок

### Example 1 - Unknown type name

```bsl
// Parameters:
//  Value - Strng - some value  ← ERROR: Unknown type "Strng" (typo)
//
Procedure Process(Value)
```

### Example 2 - Invalid type

```bsl
// Returns:
//  MyType - result  ← ERROR: Unknown type "MyType"
//
Function GetResult()
```

### Example 3 - Non-existent reference type

```bsl
// Parameters:
//  Item - CatalogRef.NonExistent - item  ← ERROR: Catalog does not exist
//
Procedure ProcessItem(Item)
```

## ✅ Правильные решения

### Example 1 - Correct type name

```bsl
// Parameters:
//  Value - String - some value
//
Procedure Process(Value)
```

### Example 2 - Known type

```bsl
// Returns:
//  Structure - result
//
Function GetResult()
```

### Example 3 - Existing reference type

```bsl
// Parameters:
//  Item - CatalogRef.Products - item
//
Procedure ProcessItem(Item)
```

## 🔧 Как исправить

1. Check spelling of type names
2. Use valid 1C:Enterprise type names
3. For reference types, verify object exists
4. Use English or Russian type names consistently

### Valid type name examples:
- `String` / `Строка`
- `Number` / `Число`
- `Boolean` / `Булево`
- `Date` / `Дата`
- `Array` / `Массив`
- `Structure` / `Структура`
- `CatalogRef.CatalogName`
- `DocumentRef.DocumentName`

## 🔍 Технические детали

- **Java class**: `TypeDefinitionCheck`
- **Location**: `com.e1c.v8codestyle.bsl.comment.check`
- **Error message**: "Unknown type specified"

## 📚 Ссылки

- [Standard #453 - Commenting code](https://its.1c.ru/db/v8std#content:453:hdoc)
- [1C:Enterprise data types](https://its.1c.ru/db/v8321doc#bookmark:dev:TI000000037)
