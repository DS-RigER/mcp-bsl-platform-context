# Имя подключаемого обработчика события

## 📋 Общая информация

| Параметр | Значение |
|----------|----------|
| **ID проверки** | `module-attachable-event-handler-name` |
| **Severity** | Minor |
| **Тип** | Code style |
| **Стандарт** | № 492 |

## 🎯 Что проверяет

Проверяет, что имя программно добавленного обработчика события соответствует шаблону: префикс **Attachable_** (или **Подключаемый_**).

## ❌ Примеры ошибок

### Example 1 - Wrong handler name

```bsl
// Parameters:
//  Item - FormField
//
Procedure SetupField(Item)
    
    Item.SetAction("OnChange", "ItemOnChange");  // ← ERROR: Missing prefix
    
EndProcedure
```

### Example 2 - No prefix

```bsl
Procedure ConfigureTable(Table)
    
    Table.SetAction("Selection", "ProcessSelection");  // ← ERROR: Should be Attachable_
    
EndProcedure
```

## ✅ Правильные решения

### Example 1 - Correct prefix

```bsl
// Parameters:
//  Item - FormField
//
Procedure SetupField(Item)
    
    Item.SetAction("OnChange", "Attachable_ItemOnChange");  // OK
    
EndProcedure
```

### Example 2 - Russian prefix

```bsl
Procedure НастроитьПоле(Элемент)
    
    Элемент.УстановитьДействие("ПриИзменении", "Подключаемый_ЭлементПриИзменении");  // OK
    
EndProcedure
```

## 🔧 Как исправить

1. Rename handler to include prefix:
   - English: `Attachable_HandlerName`
   - Russian: `Подключаемый_ИмяОбработчика`

2. Create handler procedure with matching name

### Naming pattern:
```
Attachable_<ElementName><EventName>
Подключаемый_<ИмяЭлемента><ИмяСобытия>
```

## 🔍 Технические детали

- **Java class**: `AttachableEventHandlerNameCheck`
- **Location**: `com.e1c.v8codestyle.bsl.check`

## 📚 Ссылки

- [Standard #492 - Обработчики событий модуля формы, подключаемые из кода](https://its.1c.ru/db/v8std#content:492:hdoc)
