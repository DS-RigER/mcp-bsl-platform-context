# Right set: Start Thick Client

## 📋 Общая информация

| Свойство | Значение |
|----------|----------|
| **ID проверки** | `right-start-thick-client` |
| **Категория** | Role Rights |
| **Серьёзность** | Major |
| **Тип** | Security |
| **Стандарт** | 1C:Enterprise Development Standards |

## 🎯 Что проверяет

Проверяет назначение права `StartThickClient` (Толстый клиент) в ролях. Толстый клиент имеет больше возможностей, чем тонкий или веб-клиент, включая доступ к файловой системе. Должно быть ограничено для повышения безопасности.

## ❌ Примеры ошибок

```xml
<!-- Potentially incorrect - ThickClient for all users -->
<Rights>
  <Right>
    <Name>StartThickClient</Name>
    <Value>true</Value>
  </Right>
</Rights>

<!-- Incorrect - External users with thick client -->
<!-- Role: ExternalUser -->
<Rights>
  <Right>
    <Name>StartThickClient</Name>
    <Value>true</Value>
  </Right>
</Rights>
```

## ✅ Правильное решение

```xml
<!-- Correct - ThickClient for administrators -->
<!-- Role: Administrator -->
<Rights>
  <Right>
    <Name>StartThickClient</Name>
    <Value>true</Value>
  </Right>
</Rights>

<!-- Correct - Regular users use thin client -->
<!-- Role: User -->
<Rights>
  <Right>
    <Name>StartThickClient</Name>
    <Value>false</Value>
  </Right>
  <Right>
    <Name>StartThinClient</Name>
    <Value>true</Value>
  </Right>
</Rights>

<!-- Correct - Specific role for thick client users -->
<!-- Role: LocalUser -->
<Rights>
  <Right>
    <Name>StartThickClient</Name>
    <Value>true</Value>
  </Right>
</Rights>
```

## 🔧 Как исправить

1. Определите, каким ролям необходим толстый клиент
2. Запретите право для ролей, работающих через веб или тонкий клиент
3. Используйте тонкий клиент как основной режим работы
4. Документируйте причины использования толстого клиента

## 🔍 Технические детали

- **Класс проверки**: `com.e1c.v8codestyle.right.check.RightStartThickClient`
- **Плагин**: `com.e1c.v8codestyle.right`

## 📚 Ссылки

- [Client Types](https://1c-dn.com/library/client_types/)
- [System Rights](https://1c-dn.com/library/system_rights/)
