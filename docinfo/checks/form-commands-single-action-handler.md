# form-commands-single-action-handler

## 📋 Общая информация

| Параметр | Значение |
|----------|----------|
| **Код проверки** | `form-commands-single-action-handler` |
| **Название** | Один обработчик назначен нескольким командам |
| **Описание** | У каждого события должна быть назначена своя процедура-обработчик |
| **Серьезность** | `MAJOR` |
| **Тип** | `WARNING` |
| **Сложность** | `NORMAL` |
| **Стандарт 1С** | [Структура модуля (п. 2.4.3)](https://its.1c.ru/db/v8std/content/455/hdoc#2.4.3) |

---

## 🎯 Суть проверки

Проверка контролирует, чтобы **каждая команда формы** имела **свой уникальный обработчик**. Один и тот же обработчик **не должен** быть назначен нескольким командам.

### Почему это важно

Смешение нескольких событий в одной процедуре:
- **Усложняет логику** обработчика
- **Снижает стабильность** кода (вместо одного предусмотренного вызова по событию из платформы, код процедуры должен рассчитывать и на другие вызовы)
- **Нарушает принцип единственной ответственности** (SRP)
- **Затрудняет отладку** и сопровождение кода

---

## ❌ Пример ошибки

### Сообщение об ошибке

```
Обработчик "{ИмяОбработчика}" команды "{ИмяКоманды1}" уже назначен для команды {ИмяКоманды2}
```

**Пример:**
```
Обработчик "Command1" команды "Command2" уже назначен для команды Command1
```

### Неправильный код (Form.form - XML)

```xml
<formCommands>
  <name>Command1</name>
  <id>1</id>
  <use>
    <common>true</common>
  </use>
  <action xsi:type="form:FormCommandHandlerContainer">
    <handler>
      <name>Command1</name>  <!-- ❌ Обработчик Command1 -->
    </handler>
  </action>
  <currentRowUse>Auto</currentRowUse>
</formCommands>
<formCommands>
  <name>Command2</name>
  <id>2</id>
  <use>
    <common>true</common>
  </use>
  <action xsi:type="form:FormCommandHandlerContainer">
    <handler>
      <name>Command1</name>  <!-- ❌ ОШИБКА: Тот же обработчик Command1 использован повторно! -->
    </handler>
  </action>
  <currentRowUse>Auto</currentRowUse>
</formCommands>
```

### Неправильный код (Module.bsl)

```bsl
&AtClient
Procedure Command1(Command)
    // Этот обработчик вызывается И для Command1, И для Command2
    // ❌ Плохо: логика смешивается
EndProcedure
```

---

## ✅ Правильное решение

### Правильный код (Form.form - XML)

```xml
<formCommands>
  <name>Command1</name>
  <id>1</id>
  <use>
    <common>true</common>
  </use>
  <action xsi:type="form:FormCommandHandlerContainer">
    <handler>
      <name>Command1</name>  <!-- ✅ Свой обработчик Command1 -->
    </handler>
  </action>
  <currentRowUse>Auto</currentRowUse>
</formCommands>
<formCommands>
  <name>Command2</name>
  <id>2</id>
  <use>
    <common>true</common>
  </use>
  <action xsi:type="form:FormCommandHandlerContainer">
    <handler>
      <name>Command2</name>  <!-- ✅ Свой обработчик Command2 -->
    </handler>
  </action>
  <currentRowUse>Auto</currentRowUse>
</formCommands>
```

### Правильный код (Module.bsl)

```bsl
&AtClient
Procedure Command1(Command)
    // ✅ Отдельный обработчик для Command1
    DoCommonAction();  // Вызов общей логики
EndProcedure

&AtClient
Procedure Command2(Command)
    // ✅ Отдельный обработчик для Command2
    DoCommonAction();  // Вызов общей логики
EndProcedure

&AtClient
Procedure DoCommonAction()
    // ✅ Общая логика вынесена в отдельную процедуру
    // Здесь выполняются действия, общие для обеих команд
EndProcedure
```

---

## 🔧 Как исправить

### Шаг 1: Определить конфликтующие команды

Из сообщения об ошибке определите:
- **Имя обработчика**, который используется повторно
- **Имена команд**, которые ссылаются на этот обработчик

### Шаг 2: Создать отдельный обработчик

В модуле формы (`Module.bsl`) создайте **новую процедуру-обработчик** для второй команды:

```bsl
&AtClient
Procedure НоваяКомандаОбработчик(Command)
    // Логика обработки
EndProcedure
```

### Шаг 3: Изменить привязку в файле формы

В файле `Form.form` (XML) найдите элемент `<formCommands>` для команды с дублирующимся обработчиком и измените имя обработчика:

**Было:**
```xml
<handler>
  <name>СтарыйОбработчик</name>
</handler>
```

**Стало:**
```xml
<handler>
  <name>НоваяКомандаОбработчик</name>
</handler>
```

### Шаг 4: Вынести общую логику (если нужно)

Если обе команды должны выполнять **одинаковые действия**, создайте отдельную процедуру для общей логики и вызывайте её из каждого обработчика:

```bsl
&AtClient
Procedure Команда1(Command)
    ВыполнитьОбщуюЛогику();
EndProcedure

&AtClient
Procedure Команда2(Command)
    ВыполнитьОбщуюЛогику();
EndProcedure

&AtClient
Procedure ВыполнитьОбщуюЛогику()
    // Общий код для обеих команд
EndProcedure
```

---

## 📁 Структура файлов

Проверка затрагивает следующие файлы:

| Файл | Описание | Что проверяется/изменяется |
|------|----------|----------------------------|
| `Form.form` | XML-описание формы | Элементы `<formCommands>` → `<action>` → `<handler>` → `<name>` |
| `Module.bsl` | Модуль формы | Процедуры-обработчики с директивой `&AtClient` или `&AtServer` |

### Путь к файлам в конфигурации

```
src/
└── Catalogs/           (или Documents/, DataProcessors/, и т.д.)
    └── ИмяСправочника/
        └── Forms/
            └── ИмяФормы/
                ├── Form.form      ← XML с описанием команд
                └── Module.bsl     ← Код обработчиков
```

---

## 🔍 Технические детали

### Что проверяет код

1. Получает форму (`Form`)
2. Итерирует по всем командам формы (`formCommands`)
3. Для каждой команды получает список обработчиков (`ModelUtils.getCommandHandlers`)
4. Ведет `Map<String, String>` где ключ — имя обработчика, значение — имя команды
5. Если обработчик уже есть в карте — генерирует ошибку

### Класс проверки

```
com.e1c.v8codestyle.form.check.FormCommandsSingleEventHandlerCheck
```

### Расположение в v8-code-style

```
bundles/com.e1c.v8codestyle.form/src/com/e1c/v8codestyle/form/check/FormCommandsSingleEventHandlerCheck.java
```

---

## 📚 Ссылки

- [Стандарт 1С: Структура модуля (п. 2.4.3)](https://its.1c.ru/db/v8std/content/455/hdoc#2.4.3)
- [1C:Enterprise Development Standards: Module structure](https://kb.1ci.com/1C_Enterprise_Platform/Guides/Developer_Guides/1C_Enterprise_Development_Standards/Code_conventions/Module_formatting/Module_structure)
