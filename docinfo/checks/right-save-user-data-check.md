# Right set: Save User Data

## 📋 Общая информация

| Свойство | Значение |
|----------|----------|
| **ID проверки** | `right-save-user-data` |
| **Категория** | Role Rights |
| **Серьёзность** | Minor |
| **Тип** | Security |
| **Стандарт** | 1C:Enterprise Development Standards |

## 🎯 Что проверяет

Проверяет назначение права `SaveUserData` (Сохранение пользовательских данных) в ролях. Это право позволяет сохранять персональные настройки пользователя: настройки форм, варианты отчётов, рабочий стол. Обычно должно быть разрешено для большинства ролей.

## ❌ Примеры ошибок

```xml
<!-- Potentially incorrect - SaveUserData disabled for regular user -->
<!-- Role: User -->
<Rights>
  <Right>
    <Name>SaveUserData</Name>
    <Value>false</Value>
  </Right>
</Rights>

<!-- Incorrect - Inconsistent settings across roles -->
<!-- Role: Operator (should have this right) -->
<Rights>
  <Right>
    <Name>SaveUserData</Name>
    <Value>false</Value>
  </Right>
</Rights>
```

## ✅ Правильное решение

```xml
<!-- Correct - SaveUserData enabled for regular users -->
<!-- Role: User -->
<Rights>
  <Right>
    <Name>SaveUserData</Name>
    <Value>true</Value>
  </Right>
</Rights>

<!-- Correct - Disabled only for special cases like kiosk mode -->
<!-- Role: KioskUser -->
<Rights>
  <Right>
    <Name>SaveUserData</Name>
    <Value>false</Value>
  </Right>
</Rights>

<!-- Correct - Enabled for all standard roles -->
<!-- Role: Manager -->
<Rights>
  <Right>
    <Name>SaveUserData</Name>
    <Value>true</Value>
  </Right>
</Rights>
```

## 🔧 Как исправить

1. Разрешите право SaveUserData для большинства ролей
2. Запрещайте только для специальных случаев (киоски, терминалы)
3. Сохранение настроек улучшает пользовательский опыт
4. Документируйте случаи отключения этого права

## 🔍 Технические детали

- **Класс проверки**: `com.e1c.v8codestyle.right.check.RightSaveUserData`
- **Плагин**: `com.e1c.v8codestyle.right`

## 📚 Ссылки

- [System Rights](https://1c-dn.com/library/system_rights/)
- [User Settings](https://1c-dn.com/library/user_settings/)
