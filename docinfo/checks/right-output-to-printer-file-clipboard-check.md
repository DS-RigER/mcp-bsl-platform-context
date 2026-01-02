# Right set: Output to Printer, File, Clipboard

## 📋 Общая информация

| Свойство | Значение |
|----------|----------|
| **ID проверки** | `right-output-to-printer-file-clipboard` |
| **Категория** | Role Rights |
| **Серьёзность** | Major |
| **Тип** | Security |
| **Стандарт** | 1C:Enterprise Development Standards |

## 🎯 Что проверяет

Проверяет назначение права `OutputToPrinterFileClipboard` (Вывод на принтер, файл, буфер обмена) в ролях. Это право контролирует возможность экспорта данных из системы. В некоторых случаях требуется ограничение для предотвращения утечки конфиденциальной информации.

## ❌ Примеры ошибок

```xml
<!-- Potentially incorrect - Output enabled for all users -->
<Rights>
  <Right>
    <Name>OutputToPrinterFileClipboard</Name>
    <Value>true</Value>
  </Right>
</Rights>

<!-- Incorrect - Temp worker can export data -->
<!-- Role: TemporaryWorker -->
<Rights>
  <Right>
    <Name>OutputToPrinterFileClipboard</Name>
    <Value>true</Value>
  </Right>
</Rights>
```

## ✅ Правильное решение

```xml
<!-- Correct - Output controlled based on role requirements -->
<!-- Role: Administrator -->
<Rights>
  <Right>
    <Name>OutputToPrinterFileClipboard</Name>
    <Value>true</Value>
  </Right>
</Rights>

<!-- Correct - Restricted roles cannot export -->
<!-- Role: RestrictedUser -->
<Rights>
  <Right>
    <Name>OutputToPrinterFileClipboard</Name>
    <Value>false</Value>
  </Right>
</Rights>

<!-- Correct - Standard users can export -->
<!-- Role: User -->
<Rights>
  <Right>
    <Name>OutputToPrinterFileClipboard</Name>
    <Value>true</Value>
  </Right>
</Rights>
```

## 🔧 Как исправить

1. Оцените требования безопасности к экспорту данных
2. Запретите право для ролей с ограниченным доступом
3. Разрешите для ролей, которым необходим экспорт
4. Документируйте политику экспорта данных

## 🔍 Технические детали

- **Класс проверки**: `com.e1c.v8codestyle.right.check.RightOutputToPrinterFileClipboard`
- **Плагин**: `com.e1c.v8codestyle.right`

## 📚 Ссылки

- [System Rights](https://1c-dn.com/library/system_rights/)
- [Data Protection](https://its.1c.ru/db/metod8dev/content/5785/hdoc)
