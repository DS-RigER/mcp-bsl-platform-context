# Right set: Update Database Configuration

## 📋 Общая информация

| Свойство | Значение |
|----------|----------|
| **ID проверки** | `right-update-database-configuration` |
| **Категория** | Role Rights |
| **Серьёзность** | Critical |
| **Тип** | Security |
| **Стандарт** | 1C:Enterprise Development Standards |

## 🎯 Что проверяет

Проверяет назначение права `UpdateDatabaseConfiguration` (Обновление конфигурации базы данных) в ролях. Это критически важное право, позволяющее изменять структуру базы данных. Должно быть доступно только для роли с полными правами.

## ❌ Примеры ошибок

```xml
<!-- Incorrect - UpdateDatabaseConfiguration for non-admin -->
<Rights>
  <Right>
    <Name>UpdateDatabaseConfiguration</Name>
    <Value>true</Value>
  </Right>
</Rights>

<!-- Incorrect - Developer role in production -->
<!-- Role: Developer -->
<Rights>
  <Right>
    <Name>UpdateDatabaseConfiguration</Name>
    <Value>true</Value>
  </Right>
</Rights>
```

## ✅ Правильное решение

```xml
<!-- Correct - Only FullRights role -->
<!-- Role: FullRights -->
<Rights>
  <Right>
    <Name>UpdateDatabaseConfiguration</Name>
    <Value>true</Value>
  </Right>
</Rights>

<!-- Correct - Administrator without database update -->
<!-- Role: Administrator -->
<Rights>
  <Right>
    <Name>UpdateDatabaseConfiguration</Name>
    <Value>false</Value>
  </Right>
</Rights>

<!-- Correct - Regular roles never have this right -->
<!-- Role: User -->
<Rights>
  <Right>
    <Name>UpdateDatabaseConfiguration</Name>
    <Value>false</Value>
  </Right>
</Rights>
```

## 🔧 Как исправить

1. Снимите право для всех ролей кроме ПолныеПрава
2. Обновление конфигурации — техническая операция
3. Выполняйте обновления только через регламент
4. Ведите журнал изменений конфигурации

## 🔍 Технические детали

- **Класс проверки**: `com.e1c.v8codestyle.right.check.RightUpdateDatabaseConfiguration`
- **Плагин**: `com.e1c.v8codestyle.right`

## 📚 Ссылки

- [Configuration Update](https://1c-dn.com/library/configuration_update/)
- [System Rights](https://1c-dn.com/library/system_rights/)
