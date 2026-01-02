# Right set: Start Automation

## 📋 Общая информация

| Свойство | Значение |
|----------|----------|
| **ID проверки** | `right-start-automation` |
| **Категория** | Role Rights |
| **Серьёзность** | Critical |
| **Тип** | Security |
| **Стандарт** | 1C:Enterprise Development Standards |

## 🎯 Что проверяет

Проверяет назначение права `StartAutomation` (Запуск автоматизации / OLE Automation) в ролях. Это право позволяет подключаться к информационной базе через COM-соединение (OLE Automation). Представляет серьёзную угрозу безопасности и должно быть строго ограничено.

## ❌ Примеры ошибок

```xml
<!-- Incorrect - StartAutomation for regular user -->
<Rights>
  <Right>
    <Name>StartAutomation</Name>
    <Value>true</Value>
  </Right>
</Rights>

<!-- Incorrect - Multiple roles with automation -->
<!-- Role: Developer -->
<Rights>
  <Right>
    <Name>StartAutomation</Name>
    <Value>true</Value>
  </Right>
</Rights>
```

## ✅ Правильное решение

```xml
<!-- Correct - StartAutomation for service account only -->
<!-- Role: IntegrationService -->
<Rights>
  <Right>
    <Name>StartAutomation</Name>
    <Value>true</Value>
  </Right>
</Rights>

<!-- Correct - Users cannot use automation -->
<!-- Role: User -->
<Rights>
  <Right>
    <Name>StartAutomation</Name>
    <Value>false</Value>
  </Right>
</Rights>
```

## 🔧 Как исправить

1. Снимите право StartAutomation для всех пользовательских ролей
2. Разрешите только для служебных учётных записей интеграции
3. Используйте веб-сервисы или HTTP-сервисы для интеграции
4. Документируйте все случаи использования автоматизации

## 🔍 Технические детали

- **Класс проверки**: `com.e1c.v8codestyle.right.check.RightStartAutomation`
- **Плагин**: `com.e1c.v8codestyle.right`

## 📚 Ссылки

- [OLE Automation](https://1c-dn.com/library/ole_automation/)
- [System Rights](https://1c-dn.com/library/system_rights/)
