# Реквизит "Комментарий" имеет недопустимый тип

## 📋 Общая информация

| Параметр | Значение |
|----------|----------|
| **ID проверки** | `md-object-attribute-comment-incorrect-type` |
| **Severity** | Minor |
| **Тип** | Code style |

## 🎯 Что проверяет

Проверяет, что реквизит "Комментарий" у документов и справочников имеет корректный тип данных: строка неограниченной длины с включённым многострочным режимом.

## ❌ Примеры ошибок

### Пример 1 - Неправильный тип

```xml
<Attribute>
  <Name>Comment</Name>
  <Type>Number</Type>  <!-- ERROR: Type is not String -->
</Attribute>
```

### Пример 2 - Ограниченная строка

```xml
<Attribute>
  <Name>Comment</Name>
  <Type>
    <StringType>
      <Length>100</Length>  <!-- ERROR: Should be unlimited -->
    </StringType>
  </Type>
</Attribute>
```

### Пример 3 - Без многострочного режима

```xml
<Attribute>
  <Name>Comment</Name>
  <Type>
    <StringType>
      <Length>0</Length>  <!-- Unlimited -->
    </StringType>
  </Type>
  <MultiLine>false</MultiLine>  <!-- ERROR: MultiLine not enabled -->
</Attribute>
```

## ✅ Правильные решения

### Пример 1 - Корректный реквизит Комментарий

```xml
<Attribute>
  <Name>Comment</Name>
  <Type>
    <StringType>
      <Length>0</Length>  <!-- Unlimited string -->
    </StringType>
  </Type>
  <MultiLine>true</MultiLine>
</Attribute>
```

## 🔧 Как исправить

1. Откройте реквизит "Комментарий" в Конфигураторе
2. Установите тип: Строка неограниченной длины
3. Включите флаг "Многострочный режим"

### Требования к реквизиту "Комментарий":
- Тип: Строка
- Длина: Неограниченная (0)
- Многострочный режим: Да

### Проверяемые объекты:
- Документы
- Справочники (опционально)

## 🔍 Технические детали

- **Java class**: `MdObjectAttributeCommentCheck`
- **Location**: `com.e1c.v8codestyle.md.check`
- **Configurable parameters**:
  - `checkDocuments` - проверять документы
  - `checkCatalogs` - проверять справочники

## 📚 Ссылки

- [Стандарты разработки 1С](https://its.1c.ru/db/v8std)
