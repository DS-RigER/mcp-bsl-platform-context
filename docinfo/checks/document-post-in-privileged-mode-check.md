# Привилегированный режим при проведении документа

## 📋 Общая информация

| Параметр | Значение |
|----------|----------|
| **ID проверки** | `document-post-in-privileged-mode` |
| **Severity** | Major |
| **Тип** | Warning |
| **Стандарт** | № 689 |

## 🎯 Что проверяет

Проверяет, что в документе, предполагающем проведение, установлены флаги "Привилегированный режим при проведении" и "Привилегированный режим при отмене проведения".

## ❌ Примеры ошибок

### Пример 1 - Флаг не установлен

**XML метаданных документа:**
```xml
<Document>
  <Name>SalesOrder</Name>
  <Posting>Allow</Posting>
  <PrivilegedModeOnPosting>false</PrivilegedModeOnPosting>  <!-- ERROR -->
  <PrivilegedModeOnCancelPosting>false</PrivilegedModeOnCancelPosting>  <!-- ERROR -->
</Document>
```

### Пример 2 - Частичная настройка

```xml
<Document>
  <Name>Receipt</Name>
  <Posting>Allow</Posting>
  <PrivilegedModeOnPosting>true</PrivilegedModeOnPosting>
  <PrivilegedModeOnCancelPosting>false</PrivilegedModeOnCancelPosting>  <!-- ERROR -->
</Document>
```

## ✅ Правильные решения

### Пример 1 - Оба флага установлены

```xml
<Document>
  <Name>SalesOrder</Name>
  <Posting>Allow</Posting>
  <PrivilegedModeOnPosting>true</PrivilegedModeOnPosting>
  <PrivilegedModeOnCancelPosting>true</PrivilegedModeOnCancelPosting>
</Document>
```

### Пример 2 - Документ без проведения

```xml
<Document>
  <Name>InternalDocument</Name>
  <Posting>Deny</Posting>
  <!-- Flags not required when posting is disabled -->
</Document>
```

## 🔧 Как исправить

1. Откройте свойства документа в Конфигураторе
2. Перейдите на вкладку "Движения"
3. Установите флаг "Привилегированный режим при проведении"
4. Установите флаг "Привилегированный режим при отмене проведения"

### Исключение
Документы, предназначенные для непосредственной корректировки записей регистров, могут проводиться с проверкой прав доступа. В этом случае необходимо предусмотреть роли, дающие права на изменение регистров.

## 🔍 Технические детали

- **Категория**: Metadata checks
- **Применимость**: Документы с разрешённым проведением

## 📚 Ссылки

- [Standard #689 - Настройка ролей и прав доступа](https://its.1c.ru/db/v8std#content:689:hdoc:1.7)
