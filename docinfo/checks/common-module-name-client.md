# common-module-name-client

## 📋 General Information

| Parameter | Value |
|-----------|-------|
| **Check ID** | `common-module-name-client` |
| **Title** | Client common module should end with Client suffix |
| **Description** | Check client common module name has "Client" suffix |
| **Severity** | `CRITICAL` |
| **Type** | `WARNING` |
| **Complexity** | `NORMAL` |
| **Default State** | Enabled |
| **1C Standard** | [469](https://its.1c.ru/db/v8std/content/469/hdoc) |

---

## 🎯 What This Check Does

This check validates that common modules configured to run **only on client** have the appropriate naming suffix:
- Russian: `Клиент`
- English: `Client`

### Why This Is Important

- **Execution context clarity**: Module name indicates where code runs
- **Developer awareness**: Prevents calling server methods from client
- **Standards compliance**: Follows 1C naming conventions (Standard 469)
- **Code organization**: Clear separation of client/server responsibilities

---

## ❌ Error Example

### Error Message

```
Client common module name should end with "{suffix}" suffix
```

**Russian:**
```
Клиентский общий модуль должен оканчиваться на суффикс "{suffix}"
```

### Noncompliant XML Configuration

```xml
<!-- ❌ Noncompliant: Client-only module without Client suffix -->
<mdclass:CommonModule xmlns:mdclass="http://g5.1c.ru/v8/dt/metadata/mdclass">
  <name>UIHelpers</name>                            <!-- ❌ Missing "Client" suffix -->
  <server>false</server>
  <clientManagedApplication>true</clientManagedApplication>  <!-- Client-only module -->
</mdclass:CommonModule>

<mdclass:CommonModule xmlns:mdclass="http://g5.1c.ru/v8/dt/metadata/mdclass">
  <name>FormHandlers</name>                         <!-- ❌ Missing "Клиент" suffix -->
  <server>false</server>
  <clientManagedApplication>true</clientManagedApplication>
</mdclass:CommonModule>
```

### Noncompliant Code Example

```
Configuration/
└── CommonModules/
    └── UIHelpers/  ❌ Missing "Client" suffix
        └── Module.bsl
```

**Module Properties:**
- Client (managed application): ✓
- Server: ✗

---

## ✅ Compliant Solution

### Correct XML Configuration

```xml
<!-- ✅ Correct: Client-only module with Client suffix -->
<mdclass:CommonModule xmlns:mdclass="http://g5.1c.ru/v8/dt/metadata/mdclass">
  <name>UIHelpersClient</name>                      <!-- ✅ Has "Client" suffix -->
  <server>false</server>
  <clientManagedApplication>true</clientManagedApplication>
</mdclass:CommonModule>

<!-- ✅ Correct: Russian naming with Клиент suffix -->
<mdclass:CommonModule xmlns:mdclass="http://g5.1c.ru/v8/dt/metadata/mdclass">
  <name>РаботаСИнтерфейсомКлиент</name>            <!-- ✅ Has "Клиент" suffix -->
  <server>false</server>
  <clientManagedApplication>true</clientManagedApplication>
</mdclass:CommonModule>
```

### Correct Module Naming

```
Configuration/
└── CommonModules/
    └── UIHelpersClient/  ✅ Has "Client" suffix
        └── Module.bsl
```

Or in Russian:

```
Configuration/
└── CommonModules/
    └── РаботаСИнтерфейсомКлиент/  ✅ Has "Клиент" suffix
        └── Module.bsl
```

### Module Settings

| Property | Value |
|----------|-------|
| Name | `UIHelpersClient` |
| Client (managed application) | ✓ |
| Server | ✗ |
| External connection | ✗ |

---

## 📖 Client Module Characteristics

### What Makes a Client Module

A module is considered "Client" when:
- `Client (managed application)` = True
- `Server` = False
- `External connection` = False

### Suffix Options

| Language | Suffix | Example |
|----------|--------|---------|
| English | `Client` | `FormHelpersClient` |
| Russian | `Клиент` | `РаботаСФормамиКлиент` |

---

## 📋 Module Type Matrix

| Execution Context | Suffix | Example |
|-------------------|--------|---------|
| Server only | (none) | `DataProcessing` |
| Client only | `Client` | `UIHelpersClient` |
| Client + Server | `ClientServer` | `CommonUtilitiesClientServer` |
| Server call from client | `ServerCall` | `DataServiceServerCall` |
| Global | `Global` | `GlobalUtilitiesGlobal` |
| Privileged | `FullAccess` | `AdminFunctionsFullAccess` |

---

## 🔧 How to Fix

### Step 1: Identify the module type

Check module properties:
- Is it client-only? (Client=True, Server=False)

### Step 2: Rename the module

Add the `Client` suffix:

**Before:** `UIHelpers`  
**After:** `UIHelpersClient`

### Step 3: Update all references

```bsl
// Before
UIHelpers.ShowNotification("Done!");

// After  
UIHelpersClient.ShowNotification("Done!");
```

---

## 🔍 Technical Details

### Check Implementation Class

```
com.e1c.v8codestyle.md.commonmodule.check.CommonModuleNameClient
```

### Location in v8-code-style

```
bundles/com.e1c.v8codestyle.md/src/com/e1c/v8codestyle/md/commonmodule/check/
```

---

## 📚 References

- [1C:Enterprise Development Standards - Standard 469](https://its.1c.ru/db/v8std/content/469/hdoc)
- [Common Module Types](https://1c-dn.com/library/common_modules/)
