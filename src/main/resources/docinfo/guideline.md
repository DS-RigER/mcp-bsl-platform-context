# BSL Coding Guidelines for 1C:Enterprise Development

## 1. Query Functions - Always Separate and Typed

**Rule:** All database queries must be written in separate constructor functions that return a typed result (ValueTable or QueryResult). Every function must have full type documentation for all returned fields.

### ❌ WRONG - Query embedded in logic without typing
```bsl
Procedure ProcessOrders()
    Query = New Query;
    Query.Text = "SELECT
        |   Orders.Ref,
        |   Orders.Date,
        |   Orders.Customer
        |FROM
        |   Document.Order AS Orders";
    Result = Query.Execute().Unload();
    
    For Each Row In Result Do
        // Processing without knowing exact types
    EndDo;
EndProcedure
```

### ✅ CORRECT - Query in typed constructor function
```bsl
// Returns:
//  ValueTable:
//   * Ref - DocumentRef.Order
//   * Date - Date
//   * Customer - CatalogRef.Customers
//   * Amount - Number
Function GetOrdersData() Export
    Query = New Query;
    Query.Text = "SELECT
        |   Orders.Ref AS Ref,
        |   Orders.Date AS Date,
        |   Orders.Customer AS Customer,
        |   Orders.Amount AS Amount
        |FROM
        |   Document.Order AS Orders";
    Return Query.Execute().Unload();
EndFunction

Procedure ProcessOrders()
    OrdersData = GetOrdersData();
    For Each Row In OrdersData Do
        // Now IDE knows exact types of Row.Ref, Row.Customer, etc.
    EndDo;
EndProcedure
```

---

## 2. Structure Typing - Reference Instead of Duplicate

**Rule:** When using strict typing, don't expand structure definitions everywhere. If there's a constructor function with full type documentation, reference it using `See` directive.

### ❌ WRONG - Duplicating structure definition
```bsl
// Parameters:
//  Settings - Structure:
//   * ShowDeleted - Boolean
//   * DateFrom - Date
//   * DateTo - Date
//   * FilterByCustomer - Boolean
//   * Customer - CatalogRef.Customers
Procedure ApplySettings(Settings)
    ...
EndProcedure

// Parameters:
//  Settings - Structure:
//   * ShowDeleted - Boolean
//   * DateFrom - Date
//   * DateTo - Date
//   * FilterByCustomer - Boolean
//   * Customer - CatalogRef.Customers
Function GetDataWithSettings(Settings)
    ...
EndFunction
```

### ✅ CORRECT - Reference to constructor function
```bsl
// Returns:
//  Structure:
//   * ShowDeleted - Boolean
//   * DateFrom - Date
//   * DateTo - Date
//   * FilterByCustomer - Boolean
//   * Customer - CatalogRef.Customers
Function NewFilterSettings() Export
    Result = New Structure;
    Result.Insert("ShowDeleted", False);
    Result.Insert("DateFrom", BegOfYear(CurrentDate()));
    Result.Insert("DateTo", EndOfYear(CurrentDate()));
    Result.Insert("FilterByCustomer", False);
    Result.Insert("Customer", Undefined);
    Return Result;
EndFunction

// Parameters:
//  Settings - See NewFilterSettings
Procedure ApplySettings(Settings)
    ...
EndProcedure

// Parameters:
//  Settings - See NewFilterSettings
Function GetDataWithSettings(Settings)
    ...
EndFunction
```

---

## 3. Structure Constructor Functions

**Rule:** Avoid defining structures inline in code. Use constructor functions instead. Exception: simple filter structures for table methods like `FindRows`.

### ❌ WRONG - Inline structure definition
```bsl
Procedure ProcessDocument(DocumentRef)
    Data = New Structure;
    Data.Insert("Ref", DocumentRef);
    Data.Insert("Date", DocumentRef.Date);
    Data.Insert("Number", DocumentRef.Number);
    Data.Insert("Customer", DocumentRef.Customer);
    Data.Insert("Amount", 0);
    Data.Insert("Items", New Array);
    
    // Pass to another procedure...
    ProcessData(Data);
EndProcedure
```

### ✅ CORRECT - Using constructor function
```bsl
// Returns:
//  Structure:
//   * Ref - DocumentRef.Order
//   * Date - Date
//   * Number - String
//   * Customer - CatalogRef.Customers
//   * Amount - Number
//   * Items - Array of See NewOrderItem
Function NewOrderData() Export
    Result = New Structure;
    Result.Insert("Ref", Documents.Order.EmptyRef());
    Result.Insert("Date", Date(1, 1, 1));
    Result.Insert("Number", "");
    Result.Insert("Customer", Catalogs.Customers.EmptyRef());
    Result.Insert("Amount", 0);
    Result.Insert("Items", New Array);
    Return Result;
EndFunction

Procedure ProcessDocument(DocumentRef)
    Data = NewOrderData();
    FillPropertyValues(Data, DocumentRef);
    ProcessData(Data);
EndProcedure
```

### ✅ EXCEPTION - Simple filter for FindRows is acceptable inline
```bsl
Procedure FilterItems(ItemsTable, Customer)
    // Simple filter structure - acceptable inline
    FilterRows = ItemsTable.FindRows(New Structure("Customer", Customer));
EndProcedure
```

---

## 4. Function Parameters - Keep It Simple

**Rule:** Functions should have 3-5 parameters maximum. If more parameters are needed, group them into a typed structure.

### ❌ WRONG - Too many parameters
```bsl
Function CreateReport(DateFrom, DateTo, Customer, Warehouse, ShowDetails, 
    IncludeReturns, Currency, PriceType, GroupByDays, ShowZeroItems)
    ...
EndFunction
```

### ✅ CORRECT - Parameters grouped in structure
```bsl
// Returns:
//  Structure:
//   * DateFrom - Date
//   * DateTo - Date
//   * Customer - CatalogRef.Customers
//   * Warehouse - CatalogRef.Warehouses
//   * ShowDetails - Boolean
//   * IncludeReturns - Boolean
//   * Currency - CatalogRef.Currencies
//   * PriceType - CatalogRef.PriceTypes
//   * GroupByDays - Boolean
//   * ShowZeroItems - Boolean
Function NewReportSettings() Export
    Result = New Structure;
    Result.Insert("DateFrom", BegOfMonth(CurrentDate()));
    Result.Insert("DateTo", EndOfMonth(CurrentDate()));
    Result.Insert("Customer", Catalogs.Customers.EmptyRef());
    Result.Insert("Warehouse", Catalogs.Warehouses.EmptyRef());
    Result.Insert("ShowDetails", True);
    Result.Insert("IncludeReturns", False);
    Result.Insert("Currency", Catalogs.Currencies.EmptyRef());
    Result.Insert("PriceType", Catalogs.PriceTypes.EmptyRef());
    Result.Insert("GroupByDays", False);
    Result.Insert("ShowZeroItems", False);
    Return Result;
EndFunction

// Parameters:
//  Settings - See NewReportSettings
Function CreateReport(Settings) Export
    ...
EndFunction
```

---

## 5. Code Organization with Regions

**Rule:** Organize code into logical regions for better readability and navigation.

### ✅ CORRECT - Well-organized module structure
```bsl
#Region Public

// Public API functions available to other modules

#Region DataAccess

Function GetCustomerData(CustomerRef) Export
    ...
EndFunction

Function GetOrdersList(Filter) Export
    ...
EndFunction

#EndRegion

#Region Calculations

Function CalculateTotal(Items) Export
    ...
EndFunction

#EndRegion

#EndRegion

#Region Internal

// Internal functions for this subsystem only

#EndRegion

#Region Private

// Private helper functions

#Region Constructors

Function NewOrderData()
    ...
EndFunction

Function NewFilterSettings()
    ...
EndFunction

#EndRegion

#Region Queries

Function QueryOrdersData()
    ...
EndFunction

#EndRegion

#EndRegion
```

---

## 6. Function Size Limits

**Rule:** Maximum function size is 100-200 lines. Exceptions are possible but should be avoided. Large functions should be split into smaller, focused functions.

### ❌ WRONG - Monolithic function
```bsl
Procedure ProcessAllData()
    // 500+ lines of mixed logic
    // Validation...
    // Data loading...
    // Calculations...
    // Saving...
    // Notifications...
EndProcedure
```

### ✅ CORRECT - Split into focused functions
```bsl
Procedure ProcessAllData()
    
    ValidationResult = ValidateInputData();
    If Not ValidationResult.Success Then
        ReportValidationErrors(ValidationResult.Errors);
        Return;
    EndIf;
    
    Data = LoadSourceData();
    
    CalculatedData = PerformCalculations(Data);
    
    SaveResults(CalculatedData);
    
    SendNotifications(CalculatedData);
    
EndProcedure

// Each sub-function is 20-50 lines with single responsibility
```

---

## 7. Refactoring Guidelines

**Rule:** Don't be afraid to refactor code when needed. However, always consult with experts before major refactoring to ensure you're not breaking existing functionality or patterns.

### When to Refactor:
- Function exceeds 200 lines
- Same code pattern repeated 3+ times
- Function has more than 5 parameters
- Structure definitions are duplicated
- Queries are embedded in business logic
- Type information is missing or incomplete

### Before Refactoring:
1. Understand the current behavior completely
2. Write tests if they don't exist
3. Discuss approach with the team/expert
4. Make incremental changes
5. Test after each change

---

## 8. Additional Best Practices

### Naming Conventions
- Use descriptive names that indicate purpose
- Query functions: `QueryXxx()` or `GetXxxData()`
- Constructor functions: `NewXxx()`
- Boolean parameters/variables: start with `Is`, `Has`, `Show`, `Include`

### Comments
- Document all public functions with full typing
- Use `// BSLLS:` comments only when truly necessary
- Explain "why", not "what" in inline comments

### Error Handling
- Always validate input parameters
- Use meaningful error messages
- Don't swallow exceptions silently

### Performance
- Avoid queries in loops
- Use batch operations when possible
- Profile code before optimizing

---

## Summary

1. **Queries** → Separate typed functions
2. **Structures** → Constructor functions with `See` references
3. **Parameters** → Maximum 3-5, use structures for more
4. **Regions** → Organize by logical purpose
5. **Function size** → 100-200 lines max
6. **Refactoring** → Do it when needed, consult experts first
7. **Typing** → Always provide full type documentation
