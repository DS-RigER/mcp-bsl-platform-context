# Длина ресурса регистра накопления или бухгалтерии

## 📋 Общая информация

| Параметр | Значение |
|----------|----------|
| **ID проверки** | `register-resource-precision` |
| **Severity** | Minor |
| **Тип** | Warning |

## 🎯 Что проверяет

Проверяет, что длина ресурса регистра накопления или бухгалтерии не превышает 25 знаков. Это ограничение связано с точностью вычислений в платформе.

## ❌ Примеры ошибок

### Пример 1 - Слишком длинный ресурс

```xml
<AccumulationRegister>
  <Name>BalanceOfGoods</Name>
  <Resource>
    <Name>Quantity</Name>
    <Type>
      <NumberType>
        <Precision>30</Precision>  <!-- ERROR: More than 25 -->
        <Scale>5</Scale>
      </NumberType>
    </Type>
  </Resource>
</AccumulationRegister>
```

### Пример 2 - Максимальная длина превышена

```xml
<AccountingRegister>
  <Name>Accounting</Name>
  <Resource>
    <Name>Amount</Name>
    <Type>
      <NumberType>
        <Precision>31</Precision>  <!-- ERROR: Maximum exceeded -->
        <Scale>2</Scale>
      </NumberType>
    </Type>
  </Resource>
</AccountingRegister>
```

## ✅ Правильные решения

### Пример 1 - Корректная длина ресурса

```xml
<AccumulationRegister>
  <Name>BalanceOfGoods</Name>
  <Resource>
    <Name>Quantity</Name>
    <Type>
      <NumberType>
        <Precision>15</Precision>  <!-- OK: Less than 25 -->
        <Scale>3</Scale>
      </NumberType>
    </Type>
  </Resource>
</AccumulationRegister>
```

### Пример 2 - Максимально допустимая длина

```xml
<AccountingRegister>
  <Name>Accounting</Name>
  <Resource>
    <Name>Amount</Name>
    <Type>
      <NumberType>
        <Precision>25</Precision>  <!-- OK: Exactly 25 -->
        <Scale>2</Scale>
      </NumberType>
    </Type>
  </Resource>
</AccountingRegister>
```

## 🔧 Как исправить

1. Откройте ресурс регистра в Конфигураторе
2. Уменьшите длину числового поля до 25 или менее

### Рекомендации:
- Для количественных ресурсов: 15 знаков
- Для суммовых ресурсов: 17 знаков
- Максимум для ресурса регистра: 25 знаков

## 🔍 Технические детали

- **Категория**: Metadata checks
- **Применимость**: 
  - Регистры накопления
  - Регистры бухгалтерии

## 📚 Ссылки

- [Числовые данные в 1С](https://its.1c.ru/db/v8std)
