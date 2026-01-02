# Right set: Start Thin Client

## 📋 Общая информация

| Свойство | Значение |
|----------|----------|
| **ID проверки** | `right-start-thin-client` |
| **Категория** | Role Rights |
| **Серьёзность** | Minor |
| **Тип** | Security |
| **Стандарт** | 1C:Enterprise Development Standards |

## 🎯 Что проверяет

Проверяет назначение права `StartThinClient` (Тонкий клиент) в ролях. Тонкий клиент — рекомендуемый режим работы для большинства пользователей. Проверяет корректность настройки права в соответствии с политикой использования клиентов.

## ❌ Примеры ошибок

```xml
<!-- Potentially incorrect - ThinClient disabled without reason -->
<!-- Role: User -->
<Rights>
  <Right>
    <Name>StartThinClient</Name>
    <Value>false</Value>
  </Right>
</Rights>

<!-- Incorrect - Inconsistent client settings -->
<!-- Role: Operator -->
<Rights>
  <Right>
    <Name>StartThinClient</Name>
    <Value>false</Value>
  </Right>
  <Right>
    <Name>StartThickClient</Name>
    <Value>false</Value>
  </Right>
  <Right>
    <Name>StartWebClient</Name>
    <Value>false</Value>
  </Right>
</Rights>
```

## ✅ Правильное решение

```xml
<!-- Correct - ThinClient enabled for regular users -->
<!-- Role: User -->
<Rights>
  <Right>
    <Name>StartThinClient</Name>
    <Value>true</Value>
  </Right>
</Rights>

<!-- Correct - Web-only users -->
<!-- Role: ExternalUser -->
<Rights>
  <Right>
    <Name>StartThinClient</Name>
    <Value>false</Value>
  </Right>
  <Right>
    <Name>StartWebClient</Name>
    <Value>true</Value>
  </Right>
</Rights>

<!-- Correct - Full access for administrator -->
<!-- Role: Administrator -->
<Rights>
  <Right>
    <Name>StartThinClient</Name>
    <Value>true</Value>
  </Right>
</Rights>
```

## 🔧 Как исправить

1. Разрешите StartThinClient для большинства ролей
2. Убедитесь, что у роли есть хотя бы один способ подключения
3. Используйте тонкий клиент как основной
4. Документируйте политику использования клиентов

## 🔍 Технические детали

- **Класс проверки**: `com.e1c.v8codestyle.right.check.RightStartThinClient`
- **Плагин**: `com.e1c.v8codestyle.right`

## 📚 Ссылки

- [Client Types](https://1c-dn.com/library/client_types/)
- [System Rights](https://1c-dn.com/library/system_rights/)
