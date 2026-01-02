# Right set: All Functions Mode

## 📋 Общая информация

| Свойство | Значение |
|----------|----------|
| **ID проверки** | `right-all-functions-mode` |
| **Категория** | Role Rights |
| **Серьёзность** | Critical |
| **Тип** | Security |
| **Стандарт** | 1C:Enterprise Development Standards |

## 🎯 Что проверяет

Проверяет назначение права `AllFunctionsMode` (Режим "Все функции") в ролях. Это право даёт доступ к специальному режиму, позволяющему открывать все объекты конфигурации напрямую. Должно быть доступно только для административных ролей, так как может обходить ограничения интерфейса.

## ❌ Примеры ошибок

```xml
<!-- Incorrect - AllFunctionsMode for regular user -->
<Rights>
  <Right>
    <Name>AllFunctionsMode</Name>
    <Value>true</Value>
  </Right>
</Rights>

<!-- Incorrect - Multiple roles with AllFunctionsMode -->
<!-- Role: PowerUser -->
<Rights>
  <Right>
    <Name>AllFunctionsMode</Name>
    <Value>true</Value>
  </Right>
</Rights>
```

## ✅ Правильное решение

```xml
<!-- Correct - AllFunctionsMode only for Administrator -->
<!-- Role: Administrator -->
<Rights>
  <Right>
    <Name>AllFunctionsMode</Name>
    <Value>true</Value>
  </Right>
</Rights>

<!-- Correct - Regular roles without AllFunctionsMode -->
<!-- Role: User -->
<Rights>
  <Right>
    <Name>AllFunctionsMode</Name>
    <Value>false</Value>
  </Right>
</Rights>
```

## 🔧 Как исправить

1. Снимите право AllFunctionsMode для всех ролей кроме Администратор
2. Обеспечьте доступ к необходимым функциям через интерфейс
3. Создайте подсистемы и команды для нужных действий
4. Не используйте AllFunctionsMode как замену правильной настройки интерфейса

## 🔍 Технические детали

- **Класс проверки**: `com.e1c.v8codestyle.right.check.RightAllFunctionsMode`
- **Плагин**: `com.e1c.v8codestyle.right`

## 📚 Ссылки

- [System Rights](https://1c-dn.com/library/system_rights/)
- [All Functions Mode](https://1c-dn.com/library/all_functions_mode/)
