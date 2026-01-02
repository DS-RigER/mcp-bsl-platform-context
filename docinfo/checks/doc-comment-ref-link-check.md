# Ссылка на существующий объект

## 📋 Общая информация

| Параметр | Значение |
|----------|----------|
| **ID проверки** | `doc-comment-ref-link` |
| **Severity** | Minor |
| **Тип** | Code style |

## 🎯 Что проверяет

Проверяет, что ссылка в документирующем комментарии (конструкция `См.` / `See`) указывает на существующий объект метаданных или метод.

## ❌ Примеры ошибок

### Example 1 - Reference to non-existent method

```bsl
// See Common.NonExistentMethod  ← ERROR: Method does not exist
//
Function GetData()
```

### Example 2 - Invalid object reference

```bsl
// See Catalogs.NonExistentCatalog  ← ERROR: Object does not exist
//
Procedure ProcessItem()
```

### Example 3 - Broken reference

```bsl
// Parameters:
//  Data - See DataProcessing.MyReport  ← ERROR: Reference not found
//
Procedure LoadData(Data)
```

## ✅ Правильные решения

### Example 1 - Valid method reference

```bsl
// See Common.GetCurrentUser
//
Function GetData()
```

### Example 2 - Reference to existing object

```bsl
// See Catalogs.Products
//
Procedure ProcessItem()
```

### Example 3 - Valid type reference

```bsl
// Parameters:
//  Data - See DataProcessor.ImportData.LoadedData
//
Procedure LoadData(Data)
```

## 🔧 Как исправить

1. Verify the referenced object exists
2. Check spelling of object and method names
3. Use correct path to the referenced object
4. Update references if objects were renamed

### Reference formats:
- `See ModuleName.MethodName`
- `See Catalogs.CatalogName`
- `See CommonModule.MethodName`
- `See DataProcessor.Name.MethodName`

### Configurable options:
- `allowSeeInDescription` - Allow "See" in description section

## 🔍 Технические детали

- **Java class**: `RefLinkPartCheck`
- **Location**: `com.e1c.v8codestyle.bsl.comment.check`
- **Error message**: "Link referenced to unexisting object"

## 📚 Ссылки

- [Standard #453 - Commenting code](https://its.1c.ru/db/v8std#content:453:hdoc)
