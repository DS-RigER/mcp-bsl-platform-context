# У каждого события должен быть свой обработчик

## 📋 Общая информация

| Параметр | Значение |
|----------|----------|
| **ID проверки** | `form-items-single-event-handler` |
| **Severity** | Minor |
| **Тип** | Code style |
| **Стандарт** | № 455 |

## 🎯 Что проверяет

Проверяет, что у каждого события формы назначена своя отдельная процедура-обработчик. Один обработчик не должен использоваться для нескольких событий или элементов.

## ❌ Примеры ошибок

### Пример 1 - Один обработчик для нескольких событий

```bsl
// Form designer: Both OnChange and OnActivate point to same handler

&AtClient
Procedure FieldHandler(Item)  // ← ERROR: Same handler for multiple events
    // Complex logic handling both events
    If SomeCondition Then
        // OnChange logic
    Else
        // OnActivate logic
    EndIf;
EndProcedure
```

### Пример 2 - Общий обработчик для разных элементов

```bsl
// Multiple form items use ProductOnChange handler

&AtClient
Procedure ProductOnChange(Item)  // ← ERROR: Shared between items
    // ...
EndProcedure
```

## ✅ Правильные решения

### Пример 1 - Отдельные обработчики

```bsl
&AtClient
Procedure FieldOnChange(Item)
    ProcessFieldChange();
EndProcedure

&AtClient
Procedure FieldOnActivate(Item)
    ProcessFieldActivation();
EndProcedure

&AtClient
Procedure ProcessFieldChange()
    // Shared logic extracted to separate procedure
EndProcedure

&AtClient
Procedure ProcessFieldActivation()
    // Activation logic
EndProcedure
```

### Пример 2 - Индивидуальные обработчики

```bsl
&AtClient
Procedure Product1OnChange(Item)
    ProcessProductChange(Item);
EndProcedure

&AtClient
Procedure Product2OnChange(Item)
    ProcessProductChange(Item);
EndProcedure

&AtClient
Procedure ProcessProductChange(Item)
    // Common logic in shared procedure
EndProcedure
```

## 🔧 Как исправить

1. Для каждого элемента формы создайте отдельный обработчик с именем по умолчанию
2. Выделите общую логику в отдельную процедуру/функцию
3. Из каждого обработчика вызывайте общую процедуру

### Паттерн:
```
ЭлементСобытие -> ОбработчикЭлементСобытие -> ОбщаяПроцедура
```

### Почему это важно:
- Смешение событий усложняет логику
- Снижает устойчивость кода
- Код должен рассчитывать только на один тип вызова

## 🔍 Технические детали

- **Категория**: Form checks
- **Применимость**: Обработчики событий элементов форм

## 📚 Ссылки

- [Standard #455 - Структура модуля](https://its.1c.ru/db/v8std/content/455/hdoc#2.4.3)
