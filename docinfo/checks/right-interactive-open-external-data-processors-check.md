# Right set: Interactive Open External Data Processors

## 📋 Общая информация

| Свойство | Значение |
|----------|----------|
| **ID проверки** | `right-interactive-open-external-data-processors` |
| **Категория** | Role Rights |
| **Серьёзность** | Critical |
| **Тип** | Security |
| **Стандарт** | 1C:Enterprise Development Standards |

## 🎯 Что проверяет

Проверяет назначение права `InteractiveOpenExtDataProcessors` (Интерактивное открытие внешних обработок) в ролях. Это право является критическим с точки зрения безопасности, так как внешние обработки могут содержать произвольный код. Должно быть строго ограничено.

## ❌ Примеры ошибок

```xml
<!-- Incorrect - External data processors for regular user -->
<Rights>
  <Right>
    <Name>InteractiveOpenExtDataProcessors</Name>
    <Value>true</Value>
  </Right>
</Rights>

<!-- Incorrect - Multiple roles can open external processors -->
<!-- Role: Operator -->
<Rights>
  <Right>
    <Name>InteractiveOpenExtDataProcessors</Name>
    <Value>true</Value>
  </Right>
</Rights>
```

## ✅ Правильное решение

```xml
<!-- Correct - Only Administrator can open external processors -->
<!-- Role: Administrator -->
<Rights>
  <Right>
    <Name>InteractiveOpenExtDataProcessors</Name>
    <Value>true</Value>
  </Right>
</Rights>

<!-- Correct - Users cannot open external processors -->
<!-- Role: User -->
<Rights>
  <Right>
    <Name>InteractiveOpenExtDataProcessors</Name>
    <Value>false</Value>
  </Right>
</Rights>
```

## 🔧 Как исправить

1. Снимите право для всех ролей кроме Администратор
2. Внешние обработки могут выполнять любой код
3. Используйте каталог дополнительных обработок из БСП
4. Реализуйте механизм проверки и утверждения обработок

## 🔍 Технические детали

- **Класс проверки**: `com.e1c.v8codestyle.right.check.RightInteractiveOpenExternalDataProcessors`
- **Плагин**: `com.e1c.v8codestyle.right`

## 📚 Ссылки

- [External Data Processors Security](https://its.1c.ru/db/metod8dev/content/5785/hdoc)
- [System Rights](https://1c-dn.com/library/system_rights/)
