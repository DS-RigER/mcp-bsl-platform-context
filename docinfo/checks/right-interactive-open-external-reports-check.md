# Right set: Interactive Open External Reports

## 📋 Общая информация

| Свойство | Значение |
|----------|----------|
| **ID проверки** | `right-interactive-open-external-reports` |
| **Категория** | Role Rights |
| **Серьёзность** | Critical |
| **Тип** | Security |
| **Стандарт** | 1C:Enterprise Development Standards |

## 🎯 Что проверяет

Проверяет назначение права `InteractiveOpenExtReports` (Интерактивное открытие внешних отчётов) в ролях. Аналогично внешним обработкам, внешние отчёты могут содержать произвольный код и представляют угрозу безопасности. Право должно быть строго ограничено.

## ❌ Примеры ошибок

```xml
<!-- Incorrect - External reports for regular user -->
<Rights>
  <Right>
    <Name>InteractiveOpenExtReports</Name>
    <Value>true</Value>
  </Right>
</Rights>

<!-- Incorrect - Analyst can open external reports -->
<!-- Role: Analyst -->
<Rights>
  <Right>
    <Name>InteractiveOpenExtReports</Name>
    <Value>true</Value>
  </Right>
</Rights>
```

## ✅ Правильное решение

```xml
<!-- Correct - Only Administrator can open external reports -->
<!-- Role: Administrator -->
<Rights>
  <Right>
    <Name>InteractiveOpenExtReports</Name>
    <Value>true</Value>
  </Right>
</Rights>

<!-- Correct - Users use built-in reports only -->
<!-- Role: User -->
<Rights>
  <Right>
    <Name>InteractiveOpenExtReports</Name>
    <Value>false</Value>
  </Right>
</Rights>
```

## 🔧 Как исправить

1. Снимите право для всех ролей кроме Администратор
2. Внешние отчёты могут содержать вредоносный код
3. Используйте каталог дополнительных отчётов из БСП
4. Проверяйте и утверждайте отчёты перед добавлением в каталог

## 🔍 Технические детали

- **Класс проверки**: `com.e1c.v8codestyle.right.check.RightInteractiveOpenExternalReports`
- **Плагин**: `com.e1c.v8codestyle.right`

## 📚 Ссылки

- [External Reports Security](https://its.1c.ru/db/metod8dev/content/5785/hdoc)
- [System Rights](https://1c-dn.com/library/system_rights/)
