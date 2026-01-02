# common-module-name-cached

## 📋 General Information

| Parameter | Value |
|-----------|-------|
| **Check ID** | `common-module-name-cached` |
| **Title** | Common module should end with correct postfix |
| **Description** | Check the postfix for a module with the cached attribute |
| **Severity** | `MINOR` |
| **Type** | `WARNING` |
| **Complexity** | `NORMAL` |
| **Default State** | Enabled |
| **1C Standard** | [469](https://its.1c.ru/db/v8std/content/469/hdoc) |

---

## 🎯 What This Check Does

This check validates that common modules with **Reuse Return Values** (caching) enabled have the appropriate naming suffix:
- Russian: `ПовтИсп` (Повторное Использование)
- English: `Cached`

### Why This Is Important

- **Code clarity**: Module name indicates caching behavior
- **Developer awareness**: Other developers understand return value caching
- **Standards compliance**: Follows 1C naming conventions (Standard 469)
- **Debugging**: Easier to identify cached modules in call stacks

---

## ❌ Error Example

### Error Message

```
Common module should end with {suffix}
```

**Russian:**
```
Общий модуль должен именоваться с постфиксом {suffix}
```

### Noncompliant XML Configuration

```xml
<!-- ❌ Noncompliant: Cached module without Cached suffix -->
<mdclass:CommonModule xmlns:mdclass="http://g5.1c.ru/v8/dt/metadata/mdclass">
  <name>DataProcessing</name>                       <!-- ❌ Missing "Cached" suffix -->
  <server>true</server>
  <returnValuesReuse>DuringSession</returnValuesReuse>  <!-- Has caching enabled -->
</mdclass:CommonModule>

<mdclass:CommonModule xmlns:mdclass="http://g5.1c.ru/v8/dt/metadata/mdclass">
  <name>Settings</name>                             <!-- ❌ Missing "ПовтИсп" suffix -->
  <server>true</server>
  <returnValuesReuse>DuringRequest</returnValuesReuse>  <!-- Has caching enabled -->
</mdclass:CommonModule>
```

### Noncompliant Code Example

```
Configuration/
└── CommonModules/
    └── DataProcessing/  ❌ Missing "Cached" suffix
        └── Module.bsl
```

**Module Properties:**
- Return value reuse: `During session` or `During call`

---

## ✅ Compliant Solution

### Correct XML Configuration

```xml
<!-- ✅ Correct: Cached module with Cached suffix -->
<mdclass:CommonModule xmlns:mdclass="http://g5.1c.ru/v8/dt/metadata/mdclass">
  <name>DataProcessingCached</name>                 <!-- ✅ Has "Cached" suffix -->
  <server>true</server>
  <returnValuesReuse>DuringSession</returnValuesReuse>
</mdclass:CommonModule>

<!-- ✅ Correct: Russian naming with ПовтИсп suffix -->
<mdclass:CommonModule xmlns:mdclass="http://g5.1c.ru/v8/dt/metadata/mdclass">
  <name>ОбработкаДанныхПовтИсп</name>               <!-- ✅ Has "ПовтИсп" suffix -->
  <server>true</server>
  <returnValuesReuse>DuringRequest</returnValuesReuse>
</mdclass:CommonModule>
```

### Correct Module Naming

```
Configuration/
└── CommonModules/
    └── DataProcessingCached/  ✅ Has "Cached" suffix
        └── Module.bsl
```

Or in Russian:

```
Configuration/
└── CommonModules/
    └── ОбработкаДанныхПовтИсп/  ✅ Has "ПовтИсп" suffix
        └── Module.bsl
```

### Module Settings

| Property | Value |
|----------|-------|
| Name | `DataProcessingCached` |
| Return value reuse | `During session` or `During call` |

---

## 📖 Cached Module Naming Patterns

### Suffix Options

| Language | Suffix | Example |
|----------|--------|---------|
| English | `Cached` | `CommonUtilitiesCached` |
| Russian | `ПовтИсп` | `ОбщиеФункцииПовтИсп` |

### Full Module Name Examples

| Purpose | Without Caching | With Caching |
|---------|----------------|--------------|
| Common utilities | `CommonUtilities` | `CommonUtilitiesCached` |
| Server call | `DataServiceServerCall` | `DataServiceServerCallCached` |
| Client module | `ClientUtilitiesClient` | `ClientUtilitiesClientCached` |

---

## 🔧 How to Fix

### Step 1: Identify the module

Find modules with:
- `ReturnValuesReuse` property set to `DuringSession` or `DuringCall`
- Name does NOT end with `Cached` or `ПовтИсп`

### Step 2: Rename the module

Add the appropriate suffix to the module name:

**Before:** `CommonUtilities`  
**After:** `CommonUtilitiesCached`

### Step 3: Update all references

After renaming, update all code that references the module:

```bsl
// Before
Result = CommonUtilities.GetValue(Key);

// After
Result = CommonUtilitiesCached.GetValue(Key);
```

---

## ⚙️ Module Properties Reference

### Return Value Reuse Options

| Option | Description | Use Case |
|--------|-------------|----------|
| Don't use | No caching | Regular modules |
| During call | Cache within single call | Repeated calls in one operation |
| During session | Cache for entire session | Configuration constants, reference data |

---

## 📁 File Structure

This check applies to:

| Object Type | Description |
|-------------|-------------|
| Common modules | Modules with caching enabled |

### Configuration Structure

```
Configuration/
├── CommonModules/
│   ├── RegularModule/           ← No caching
│   ├── CachedModuleCached/      ← ✅ Correct
│   └── OtherCachedModule/       ← ❌ Missing suffix
```

---

## 🔍 Technical Details

### What Is Checked

1. Finds common modules with `ReturnValuesReuse` != `DontUse`
2. Checks if module name ends with configured suffix
3. Default suffixes: `ПовтИсп,Cached`

### Check Implementation Class

```
com.e1c.v8codestyle.md.commonmodule.check.CommonModuleNameCached
```

### Location in v8-code-style

```
bundles/com.e1c.v8codestyle.md/src/com/e1c/v8codestyle/md/commonmodule/check/
```

---

## 📚 References

- [1C:Enterprise Development Standards - Standard 469](https://its.1c.ru/db/v8std/content/469/hdoc)
- [Common Module Naming Conventions](https://1c-dn.com/library/common_modules/)
