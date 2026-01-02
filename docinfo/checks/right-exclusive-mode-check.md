# Right set: Exclusive Mode

## 📋 Общая информация

| Свойство | Значение |
|----------|----------|
| **ID проверки** | `right-exclusive-mode` |
| **Категория** | Role Rights |
| **Серьёзность** | Critical |
| **Тип** | Security |
| **Стандарт** | 1C:Enterprise Development Standards |

## 🎯 Что проверяет

Проверяет назначение права `ExclusiveMode` (Монопольный режим) в ролях. Это право позволяет устанавливать монопольный режим, отключая всех других пользователей от информационной базы. Критически важная операция, должна быть доступна только администраторам.

## ❌ Примеры ошибок

```xml
<!-- Incorrect - ExclusiveMode for regular user -->
<Rights>
  <Right>
    <Name>ExclusiveMode</Name>
    <Value>true</Value>
  </Right>
</Rights>

<!-- Incorrect - Non-admin role with exclusive mode -->
<!-- Role: Accountant -->
<Rights>
  <Right>
    <Name>ExclusiveMode</Name>
    <Value>true</Value>
  </Right>
</Rights>
```

## ✅ Правильное решение

```xml
<!-- Correct - ExclusiveMode only for Administrator -->
<!-- Role: Administrator -->
<Rights>
  <Right>
    <Name>ExclusiveMode</Name>
    <Value>true</Value>
  </Right>
</Rights>

<!-- Correct - Users cannot set exclusive mode -->
<!-- Role: User -->
<Rights>
  <Right>
    <Name>ExclusiveMode</Name>
    <Value>false</Value>
  </Right>
</Rights>
```

## 🔧 Как исправить

1. Снимите право ExclusiveMode для всех ролей кроме Администратор
2. Монопольный режим блокирует работу всех пользователей
3. Используйте альтернативные механизмы блокировки данных
4. Документируйте случаи необходимости монопольного режима

## 🔍 Технические детали

- **Класс проверки**: `com.e1c.v8codestyle.right.check.RightExclusiveMode`
- **Плагин**: `com.e1c.v8codestyle.right`

## 📚 Ссылки

- [System Rights](https://1c-dn.com/library/system_rights/)
- [Exclusive Mode](https://1c-dn.com/library/exclusive_mode/)
