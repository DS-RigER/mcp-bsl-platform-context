# Использование дефис-минуса в документирующем комментарии

## 📋 Общая информация

| Параметр | Значение |
|----------|----------|
| **ID проверки** | `doc-comment-use-minus` |
| **Severity** | Minor |
| **Тип** | Code style |

## 🎯 Что проверяет

Проверяет, что в документирующем комментарии используется только символ дефис-минус (hyphen-minus, U+002D), а не другие похожие символы (тире, длинное тире и т.д.).

## ❌ Примеры ошибок

### Example 1 - Wrong dash character

```bsl
// Parameters:
//  Value — String — value  ← ERROR: Em dash used instead of hyphen-minus
//
Procedure Process(Value)
```

### Example 2 - En dash

```bsl
// Returns:
//  Number – count  ← ERROR: En dash (U+2013) instead of hyphen-minus
//
Function GetCount()
```

### Example 3 - Various wrong characters

```bsl
// Parameters:
//  Data ‒ Structure ‒ data  ← ERROR: Figure dash used
//
Procedure LoadData(Data)
```

## ✅ Правильные решения

### Example 1 - Correct hyphen-minus

```bsl
// Parameters:
//  Value - String - value
//
Procedure Process(Value)
```

### Example 2 - Proper format

```bsl
// Returns:
//  Number - count
//
Function GetCount()
```

### Example 3 - Standard hyphen

```bsl
// Parameters:
//  Data - Structure - data
//
Procedure LoadData(Data)
```

## 🔧 Как исправить

1. Replace all dash-like characters with hyphen-minus (-)
2. Hyphen-minus is the standard keyboard hyphen
3. ASCII code 45 (U+002D)
4. Available on standard keyboard next to "0" key

### Characters to avoid:
- Em dash (—) U+2014
- En dash (–) U+2013
- Figure dash (‒) U+2012
- Horizontal bar (―) U+2015
- Minus sign (−) U+2212

### Correct character:
- Hyphen-minus (-) U+002D

## 🔍 Технические детали

- **Java class**: `DocCommentUseMinusCheck`
- **Location**: `com.e1c.v8codestyle.bsl.comment.check`
- **Error message**: "Only hyphen-minus symbol is allowed in documentation comment"

## 📚 Ссылки

- [Standard #453 - Commenting code](https://its.1c.ru/db/v8std#content:453:hdoc)
