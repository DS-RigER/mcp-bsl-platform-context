# Метод или переменная доступны НаКлиенте

## 📋 Общая информация

| Параметр | Значение |
|----------|----------|
| **ID проверки** | `module-accessibility-at-client` |
| **Severity** | Major |
| **Тип** | Warning |
| **Стандарт** | № 680 |

## 🎯 Что проверяет

Проверяет, что метод или переменная модуля менеджера или объекта не доступны на клиенте. Такие модули должны работать только на сервере.

## ❌ Примеры ошибок

```bsl
// Object module without preprocessor protection

Var ModuleVariable;  // ERROR: Accessible at client

Procedure BeforeDelete(Cancel)
    // ERROR: Method accessible at client
EndProcedure

Procedure DoSomething() Export
    // ERROR: Export method accessible at client
EndProcedure

ModuleVariable = Undefined;
```

## ✅ Правильные решения

```bsl
#If Server Or ThickClientOrdinaryApplication Or ExternalConnection Then

Var ModuleVariable;

Procedure BeforeDelete(Cancel)
    // OK: Protected by preprocessor
EndProcedure

Procedure DoSomething() Export
    // OK: Only available on server
EndProcedure

ModuleVariable = Undefined;

#Else
    Raise NStr("en = 'Invalid client call of object.'");
#EndIf
```

## 🔧 Как исправить

1. Wrap entire module code in preprocessor directive:
   ```bsl
   #If Server Or ThickClientOrdinaryApplication Or ExternalConnection Then
   // ... module code ...
   #Else
       Raise NStr("en = 'Invalid client call.'");
   #EndIf
   ```

2. Or use `&AtServer` compiler directive for specific methods

### Применимость:
- Модули объектов
- Модули менеджеров
- Модули наборов записей

## 🔍 Технические детали

- **Java class**: `AccessibilityAtClientInObjectModuleCheck`
- **Location**: `com.e1c.v8codestyle.bsl.check`

## 📚 Ссылки

- [Standard #680 - Поддержка толстого клиента](https://its.1c.ru/db/v8std#content:680:hdoc:2)
- [Standard #746 - Обработчики представления](https://its.1c.ru/db/v8std#content:746:hdoc)
