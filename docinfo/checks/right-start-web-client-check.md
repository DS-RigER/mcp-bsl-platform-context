# Right set: Start Web Client

## 📋 Общая информация

| Свойство | Значение |
|----------|----------|
| **ID проверки** | `right-start-web-client` |
| **Категория** | Role Rights |
| **Серьёзность** | Minor |
| **Тип** | Security |
| **Стандарт** | 1C:Enterprise Development Standards |

## 🎯 Что проверяет

Проверяет назначение права `StartWebClient` (Веб-клиент) в ролях. Веб-клиент обеспечивает доступ через браузер без установки приложения. Проверяет корректность настройки в соответствии с политикой доступа к системе.

## ❌ Примеры ошибок

```xml
<!-- Potentially incorrect - WebClient disabled for mobile users -->
<!-- Role: MobileUser -->
<Rights>
  <Right>
    <Name>StartWebClient</Name>
    <Value>false</Value>
  </Right>
</Rights>

<!-- Incorrect - No client access at all -->
<!-- Role: User -->
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
<!-- Correct - WebClient for remote users -->
<!-- Role: RemoteUser -->
<Rights>
  <Right>
    <Name>StartWebClient</Name>
    <Value>true</Value>
  </Right>
</Rights>

<!-- Correct - WebClient only for external users -->
<!-- Role: ExternalUser -->
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
    <Value>true</Value>
  </Right>
</Rights>

<!-- Correct - All clients for administrator -->
<!-- Role: Administrator -->
<Rights>
  <Right>
    <Name>StartWebClient</Name>
    <Value>true</Value>
  </Right>
</Rights>
```

## 🔧 Как исправить

1. Разрешите StartWebClient для ролей с удалённым доступом
2. Используйте веб-клиент для внешних пользователей
3. Убедитесь, что у роли есть способ подключения к системе
4. Документируйте политику использования веб-клиента

## 🔍 Технические детали

- **Класс проверки**: `com.e1c.v8codestyle.right.check.RightStartWebClient`
- **Плагин**: `com.e1c.v8codestyle.right`

## 📚 Ссылки

- [Web Client](https://1c-dn.com/library/web_client/)
- [System Rights](https://1c-dn.com/library/system_rights/)
