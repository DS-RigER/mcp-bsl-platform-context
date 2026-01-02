# Right set: Configuration Extensions Administration

## 📋 Общая информация

| Свойство | Значение |
|----------|----------|
| **ID проверки** | `right-configuration-extensions-administration` |
| **Категория** | Role Rights |
| **Серьёзность** | Critical |
| **Тип** | Security |
| **Стандарт** | 1C:Enterprise Development Standards |

## 🎯 Что проверяет

Проверяет назначение права `ConfigurationExtensionsAdministration` (Администрирование расширений конфигурации) в ролях. Это право позволяет устанавливать, удалять и управлять расширениями конфигурации. Расширения могут содержать произвольный код, поэтому право должно быть строго ограничено.

## ❌ Примеры ошибок

```xml
<!-- Incorrect - Extension administration for non-admin -->
<Rights>
  <Right>
    <Name>ConfigurationExtensionsAdministration</Name>
    <Value>true</Value>
  </Right>
</Rights>

<!-- Incorrect - Multiple roles can manage extensions -->
<!-- Role: Developer -->
<Rights>
  <Right>
    <Name>ConfigurationExtensionsAdministration</Name>
    <Value>true</Value>
  </Right>
</Rights>
```

## ✅ Правильное решение

```xml
<!-- Correct - Only Administrator manages extensions -->
<!-- Role: Administrator -->
<Rights>
  <Right>
    <Name>ConfigurationExtensionsAdministration</Name>
    <Value>true</Value>
  </Right>
</Rights>

<!-- Correct - Other roles cannot manage extensions -->
<!-- Role: User -->
<Rights>
  <Right>
    <Name>ConfigurationExtensionsAdministration</Name>
    <Value>false</Value>
  </Right>
</Rights>
```

## 🔧 Как исправить

1. Снимите право для всех ролей кроме Администратор
2. Расширения могут выполнять произвольный код
3. Установка расширений — критическая операция
4. Контролируйте расширения через систему безопасности

## 🔍 Технические детали

- **Класс проверки**: `com.e1c.v8codestyle.right.check.RightConfigurationExtensionsAdministration`
- **Плагин**: `com.e1c.v8codestyle.right`

## 📚 Ссылки

- [Configuration Extensions](https://1c-dn.com/library/configuration_extensions/)
- [System Rights](https://1c-dn.com/library/system_rights/)
