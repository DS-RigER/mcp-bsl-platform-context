# Функция возвращает типизированное значение

## 📋 Общая информация

| Параметр | Значение |
|----------|----------|
| **ID проверки** | `function-return-value-type` |
| **Severity** | Major |
| **Тип** | Code style |
| **Категория** | Strict Types |

## 🎯 Что проверяет

Система строгой типизации проверяет, что каждая функция возвращает типизированное значение. Тип должен быть вычислен из кода или объявлен в документирующем комментарии.

## ❌ Примеры ошибок

### Example 1 - Function without return type

```bsl
// @strict-types

Function GetValue()  // ← ERROR: Function has no return value type
    If Condition Then
        Return Value1;
    Else
        Return Value2;
    EndIf;
EndFunction
```

### Example 2 - Untyped return

```bsl
// @strict-types

Function ProcessData(Data)
    Result = SomeOperation();  // ← ERROR: Return type unknown
    Return Result;
EndFunction
```

## ✅ Правильные решения

### Example 1 - With documentation comment type

```bsl
// @strict-types

// Gets configuration value.
//
// Returns:
//  String - configuration value
//
Function GetValue()
    If Condition Then
        Return Value1;
    Else
        Return Value2;
    EndIf;
EndFunction
```

### Example 2 - Explicit typed return

```bsl
// @strict-types

// Processes data.
//
// Parameters:
//  Data - Structure - input data
//
// Returns:
//  Boolean - processing result
//
Function ProcessData(Data)
    // ...processing...
    Return True;
EndFunction
```

### Example 3 - Type from context

```bsl
// @strict-types

Function GetUserName()
    // Type inferred from return statement
    Return CurrentUser().Description;  // Returns String
EndFunction
```

## 🔧 Как исправить

1. Add `// Returns:` section with type in documentation comment
2. Ensure all return paths return same type
3. Use explicit type declarations
4. Avoid returning Undefined without declaring it

### Required format:
```
// Returns:
//  Type - description
```

## 🔍 Технические детали

- **Java class**: `FunctionReturnTypeCheck`
- **Location**: `com.e1c.v8codestyle.bsl.strict.check`
- **Applies when**: Module has `@strict-types` annotation

## 📚 Ссылки

- [Code typification](https://its.1c.ru/db/metod8dev#content:5930:hdoc)
- [Standard #453 - Commenting code](https://its.1c.ru/db/v8std#content:453:hdoc)
