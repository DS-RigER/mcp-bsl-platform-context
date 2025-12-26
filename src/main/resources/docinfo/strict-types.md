# BSL Strict Typing Guide

> Methodological recommendation for code typing in 1C:Enterprise 8

## TOPIC: overview

### Purpose of Strict Typing

1. Reduce errors detected during development
2. Eliminate the practice of writing code in debug mode by maximizing code typing for static analyzer in 1C:EDT
3. Simpler code for human reading
4. Code typing enables more active use of various code analysis tools
5. Better refactoring when modifying code
6. Keeping documenting comments in actual (truthful) state
7. Application of automatic code processing tools (auto-formatting, auto-translation, etc.)
8. Simplifying code maintenance by new developers

### How It Works

When studying typing capabilities, consider that:

1. The 1C:Enterprise 8 execution language is/remains dynamically typed
2. All type calculation capabilities are basic 1C:EDT core features
3. The "1C:Development Standards V8" plugin only adds type control in 1C:EDT and problem situations based on 1C:EDT basic capabilities

### What is "Strict Typing"

Strict typing is the control (validation) of all types presence, both declarative and dynamically calculated, in 1C:Enterprise 8 code. This control is provided by the "1C:Development Standards V8" extension for 1C:EDT.

Key features:
- Control of types presence at object/variable creation point
- Control of types presence at usage point (accessing object properties or methods)
- Control of return value types for functions, object properties, variables
- Prohibition of type changes for variables and object properties
- Type intersection when passing objects to called method parameters
- Control of declared types and types from Data-flow analysis (DFA) system for "user" data objects

#### What Strict Typing Is NOT

- Not OOP
- Does not add 1C type hierarchy, and does not change existing hierarchy
- Does not add concepts of abstract objects or "Interfaces"
- Does not change 1C:Enterprise 8 type system - only controls existing types

## TOPIC: enabling

### Enabling Strict Typing

To enable strict typing for a module, add annotation before the first semantic object (region, procedure, variable):

```bsl
//@strict-types

#Region PublicInterface
...
```

After this, 1C:EDT will display errors if something is incorrect with objects and their types.

**Batch enabling** of strict typing can be done with the command `Enable strict typing (@strict-types) in modules` in context menu:
- In 1C Navigator: for project as whole, for selected list of metadata objects
- In Project Explorer: for selected list of directories or module files
- In module editor - Source submenu: for current open module

### When to Apply Strict Typing

Recommended cases:
- For all new configurations without legacy of untyped code - enable strict typing for all modules
- For existing modules - first adapt the code, then enable strict typing

## TOPIC: calculated-typing

### Calculated Typing

Dynamic type calculation performed in 1C:EDT based on 1C:Enterprise context and configuration metadata.

#### Variable Type State Tracking

1C:EDT typing system tracks variable type depending on its usage (location in code):

```bsl
MyVariable = 10;
...
If MyVariable > 0 Then // Here type is Number
EndIf;

...
MyVariable = True;
...
If MyVariable Then // Here type is Boolean
EndIf;
```

#### Property Type State Tracking

When analyzing data objects (Data-flow analysis) created programmatically, 1C:EDT typing system considers only types specified at property initialization moment:

```bsl
Parameters = New Structure("MyProperty"); // Initialization without specifying initial type
...
Parameters.MyProperty = 10; // Type change from Undefined to Number does NOT happen
...
If Parameters.MyProperty > 0 Then // Here MyProperty type is Undefined
EndIf;
...
Parameters.Insert("OtherProperty", False); // Property initialized with initial value

Parameters.OtherProperty = 1; // Type change does NOT happen
...
If Parameters.OtherProperty Then // Here property type is Boolean
EndIf;
```

When value type actually changes for object property that allows such behavior in 1C:Enterprise 8 runtime, 1C:EDT typing and data object analysis system does not account for this change.

Correct approach: specify initial typed value and do not allow type change further in code. Two options for variables:
1. Type is explicitly defined by user and will not change further; change attempts can be tracked
2. If type is not explicitly defined, it will ALWAYS be undefined further, and plugin will consider it undefined everywhere

## TOPIC: declarative-typing

### Declarative Typing

Static type specification in documenting/typing comments, references to constructor functions and references to incoming parameters of other methods.

### Documentation Comment Structure

Documentation comment data structure in simplified hierarchical view:

```yaml
Description: # Description object consisting of lines and links collection
  - TextPart # Text description line
  - LinkPart # Link to website or configuration method

ParameterSection: # Method parameters section
  ParameterDefinitions: # Parameters list
    - FieldDefinition:
      Name: ParameterName # Parameter name
      TypeSections: # Type sections list
        - TypeSection:
          TypeDefinitions: # Type definitions list
            - TypeDefinition:
              TypeName: TypeName # 1C type name
              ContainTypes: # Collection element types descriptions
              FieldDefinitionExtension: # Ability to extend type with fields

ReturnSection: # Return value description section
  ReturnTypes: # Return value type sections list
    - TypeSection:
      TypeDefinitions:
        - TypeDefinition
```

## TOPIC: type-links

### Type Links

1C:EDT typing system supports type links instead of direct specification.
Use type links to reduce text volume in documenting comments and improve type description accuracy.

#### Link to Table Part Object

```bsl
// Parameters:
//  Object - See Catalog.Products.Units
Procedure ProcessObject(Object)
```

#### Link to Table Part Row

> Such link is not yet supported in 1C:EDT!

```bsl
// Parameters:
//  Object - TablePartRow: See Catalog.Products.Units
Procedure ProcessObject(Object)
```

#### Link to Object Attribute Type

```bsl
// Parameters:
//  Attribute1 - See Catalog.Products.Article
Procedure ProcessObject(Attribute1)
```

#### Link to Table Part Attribute Type

```bsl
// Parameters:
//  TPAttribute - See Catalog.Products.Units.Unit
Procedure ProcessObject(TPAttribute)
```

#### Link to Form

```bsl
// Parameters:
//  Form - See Catalog.Products.Form.ItemForm
Procedure ProcessObject(Form)
```

#### Link to Form Element or Attribute

```bsl
// Parameters:
//  Object - See Catalog.Products.Form.ItemForm.Object
Procedure ProcessObject(Object)
```

```bsl
// Parameters:
//  List - See Catalog.Products.Form.ListForm.Items.List
Procedure ProcessObject(List)
```

#### Link to Method Parameter Type

From manager module:
```bsl
// Parameters:
//  List - See Catalogs.Products.ManagerModuleMethod.Parameter1
Procedure ProcessObject(List)
```

From object module:
```bsl
// Parameters:
//  List - See CatalogObject.Products.ObjectModuleMethod.Parameter2
Procedure ProcessObject(List)
```

#### Link to Constructor Function

```bsl
// Parameters:
//  Object - See NewDataObject
Procedure ProcessObject(Object)
```

## TOPIC: local-variables

### Local Variable Initialization

- FORBIDDEN: Initialize variables via `Var` as such variable initializes with `Undefined` type and further type change may not be visible to static analyzer

WRONG:
```bsl
Procedure ProcessCurrentRow(TP)
    Var CurrentRef;

    For Each TPRow From TP Do
        CurrentRef = TPRow.Ref;
        ...
    EndDo;
    Object = CurrentRef.GetObject();
...
```

CORRECT:
```bsl
Procedure ProcessCurrentRow(TP)
    CurrentRef = Catalogs.Products.EmptyRef();

    For Each TPRow From TP Do
        CurrentRef = TPRow.Ref;
        ...
    EndDo;
    Object = CurrentRef.GetObject();
...
```

- FORBIDDEN: Initialize variables inside loops or conditions with subsequent use outside loops/conditions
- Type clarification for local variable initialized by function returning more general type is possible via inline type specification:

CORRECT:
```bsl
MyObject = CommonModule.CreateNewObjectByType(ObjectType); // CatalogObject.Products -
```

For complex data objects use constructor function references:
```bsl
MyObject = Parameters.MyObject; // See NewDataObject
```

## TOPIC: module-variables

### Module Variable Initialization

Module object, form, configuration variables, including global (export) variables, should be declared with static type specification in comment. Also initialize module variable with initial/empty value in module code.

WRONG:
```bsl
#Region VariableDescriptions

&AtClient
Var CachedValues; //used by TP change processing mechanism

&AtClient
Var CurrentDataIdentifier; //used to pass current row to wait handler

&AtClient
Var ParametersForWrite Export;

#EndRegion
```

CORRECT:
```bsl
#Region VariableDescriptions

//used by TP change processing mechanism
&AtClient
Var CachedValues; // See TablePartClientServer.GetCachedValuesStructure

//used to pass current row to wait handler
&AtClient
Var CurrentDataIdentifier; // Number -

&AtClient
Var ParametersForWrite Export; // Structure - 

#EndRegion

....

CurrentDataIdentifier = -1;
ParametersForWrite = New Structure;
```

## TOPIC: structure-keys

### Structure Key Initialization

- Structure key values (both in constructor and when adding to structure) must be initialized immediately with empty value of the type that will be used later
- Changing structure key value type is NOT allowed

WRONG:
```bsl
Procedure Processing()
    CatalogObject = Catalogs.Products.CreateItem();
...
    Parameters = New Structure("Ref");
    Parameters.Ref = CatalogObject.Ref;
```

CORRECT:
```bsl
Procedure Processing()
    CatalogObject = Catalogs.Products.CreateItem();
...
    Parameters = New Structure("Ref", Catalogs.Products.EmptyRef());
    Parameters.Ref = CatalogObject.Ref;
```

CORRECT:
```bsl
Procedure Processing()
    CatalogObject = Catalogs.Products.CreateItem();
...
    Parameters = New Structure;
    Parameters.Insert("Ref", CatalogObject.Ref);
```

- When adding key to existing structure, write code "transparently" for static analyzer so that key insertion and its usage are in same visibility scope

## TOPIC: arrays

### Array Description

- Generally FORBIDDEN: Create array `New Array;` without specifying its value type if later in current module code there is access to array elements

WRONG:
```bsl
    RefList = New Array;
    RefList.Add(Ref);
...
    For Each Ref From RefList Do
        Object = Ref.GetObject();
...
```

CORRECT:
```bsl
    RefList = New Array; // Array of CatalogRef.Products -
    RefList.Add(Ref);
...
    For Each Ref From RefList Do
        Object = Ref.GetObject();
...
```

CORRECT (using constructor function):
```bsl
    RefList = NewProductsList();
    RefList.Add(Ref);
...
    For Each Ref From RefList Do
        Object = Ref.GetObject();
...

// Returns:
//  Array of CatalogRef.Products
Function NewProductsList()
    Return New Array;
EndFunction
```

- Not recommended: use objects of different types as values (strings with numbers, simple types with reference types, DB objects with structures, etc.)
- If array value is complex, use constructor function for empty array initialization or array with data
- In documenting comments, specify array element type for parameters and return values

## TOPIC: value-table

### Value Table and Value Tree Description

- When describing `ValueTable`, `ValueTree` objects, always describe all columns and their types

CORRECT:
```bsl
// Returns:
// ValueTable:
//  * Product - CatalogRef.Products
//  * Characteristic - CatalogRef.ProductCharacteristics
//  * Warehouse - CatalogRef.Warehouses
//  * Quantity - Number
// ...
Function ProductsForWriteOff()
....
    Return Table;
EndFunction
```

- Generally FORBIDDEN: use `ValueTable` type description as export method incoming parameter with column description. Use reference to constructor function that creates this table

WRONG:
```bsl
// Parameters:
//  Table - ValueTable
//   * Product - CatalogRef.Products
Procedure ProcessTable(Table) Export
    For Each TableRow From Table Do
        TableRow.Product = Catalogs.Products.EmptyRef();
....
```

CORRECT:
```bsl
// Parameters:
// Table - See ProductsForWriteOff
Procedure ProcessTable(Table)
    For Each TableRow From Table Do
        TableRow.Product = Catalogs.Products.EmptyRef();
....
```

## TOPIC: table-row

### Value Table or Tree Row Description

Use one of two options:

**Option 1: Direct column specification:**

WRONG:
```bsl
// Parameters:
// TableRow - ValueTableRow
Procedure ProcessTable(TableRow)
    TableRow.Product = Catalogs.Products.EmptyRef();
....
```

CORRECT:
```bsl
// Parameters:
// TableRow - ValueTableRow:
//  * Product - CatalogRef.Products
Procedure ProcessTable(TableRow)
    TableRow.Product = Catalogs.Products.EmptyRef();
....
```

**Option 2: Reference to VT row from constructor function:**

CORRECT:
```bsl
// Parameters:
// TableRow - ValueTableRow: See NewTableWithProducts
Procedure ProcessTable(TableRow)
    TableRow.Product = Catalogs.Products.EmptyRef();
....

// Returns:
// ValueTable - with columns:
//  * Product - CatalogRef.Products - 
Function NewTableWithProducts()
....
```

## TOPIC: map

### Map Description

- Map is complex type with fixed set of columns (key and value). Default types for key and value are `Arbitrary`; create constructor function that initializes map and describes key and value types
- Avoid combining different types in key and value
- Cannot specify key and value types for initialized variable in inline comment
- When getting data object from container, use constructor function reference

WRONG:
```bsl
   MyVariable = New Map; // Map of String - this won't work
```

CORRECT:
```bsl
// Returns:
// Map of KeyAndValue:
// * Key - String
// * Value - Array of DocumentObject
Function NewMapping()
   ...
```

CORRECT:
```bsl
// Create data with constructor function and fill
Data = NewMapping();
...
PutToTempStorage(Address, Data);
...

// When initializing variable, specify reference to original function that created typed object
MyVariable = GetFromStorageValue(Address); // See NewMapping
   ...
```

## TOPIC: value-list

### Value List Description

- ValueList is complex type with defined collection element where only "Value" property is typed. Default type for value is `Arbitrary`; create constructor function that initializes list and describes value types
- If value list is used to pass data outside current function - use return value of constructor function for new empty list or getter function with filled data

WRONG:
```bsl
// Returns:
// ValueList of ValueListItem:
// * Value - DocumentObject
Function NewDocumentList()
   Return New ValueList;
EndFunction
```

CORRECT:
```bsl
// Constructor function for new empty data object
//
// Returns:
// ValueList of DocumentObject:
Function NewDocumentList()
   Return New ValueList;
EndFunction
```

## TOPIC: constructor-functions

### Constructor Functions for Complex Data Objects

For complex types created based on abstract platform types (`Structure`, `Map`, `ValueTable`, `ValueTree`, etc.), use data constructor function.

Function name should be `New` + data object name.

CORRECT:
```bsl
// Returns:
// ValueTable:
// * Product - CatalogRef.Products
Function NewSelectedProductsTable()
    ....
EndFunction
```

- Data constructor can also fill data, i.e., provide new object with filled data. For example, function executes query to DB and returns value table with columns and data.

### Specifying Reference to Data Constructor Function

When describing method parameter, specify reference to data constructor function without specifying base data type.

WRONG:
```bsl
// Parameters:
//  PrintCommands - ValueTable - field composition see in PrintManagement.CreatePrintCommandsCollection.
//
Procedure AddPrintCommands(PrintCommands) Export
```

CORRECT:
```bsl
// Parameters:
//  PrintCommands - See PrintManagement.CreatePrintCommandsCollection
//
Procedure AddPrintCommands(PrintCommands) Export
```

## TOPIC: type-narrowing

### Narrowing Local Variable or Parameter Type

You can safely narrow (or actually set for static analyzer) local method variable type, incoming parameter or module variable through type check:

```bsl
If TypeOf(MyVariable) = Type("CatalogObject.Products") Then
    MyVariable.Article = "";
    MyVariable.Write();
```

Inside condition, variable will be of type specified in check, both in runtime and for static analyzer. Check condition must be simple.

For task of overriding calculated types based on code that 1C:EDT system calculates, specify incoming parameter type in documenting comments:

```bsl
// @strict-types

...
// Somewhere in code - call to function below
Response = PrintTemplate(Documents.Order.CreateDocument());
...

// Parameters:
//  Doc - DocumentObject.Order - This is a string
// 
// Returns:
//  SpreadsheetDocument - Test
&AtServerNoContext
Function PrintTemplate(Doc)
    Doc.Author = Catalogs.Users.EmptyRef();
    Template = Doc.ReturnTemplate();
    Return Template;
EndFunction
```

## TOPIC: forms

### Form-Related Typing

#### Describing Used Form Attributes and Elements for Common Form Code

- Allowed: describe part of form common for several forms or implementation of some interface for general configuration mechanism

CORRECT:
```bsl
// Parameters:
// Form - ClientApplicationForm:
// * Object - FormDataStructure, CatalogObject, DocumentObject - form main attribute
// * Items - AllFormItems:
//  ** Products - FormTable - products table element
Procedure OnCreateAtServer(Form)
   
    Ref = Form.Object.Ref;
    CurrentData = Form.Items.Products.CurrentData;
....
```

- If specific form is used, specify full form reference in documenting comments:

```bsl
// Parameters:
// Form - See Catalog.Products.Form.ItemForm
Procedure OnCreateAtServer(Form)
   
    Ref = Form.Object.Ref;
    Form.Items.Article.Visibility = True;
....
```

#### Getting and Opening Form

- When using `GetForm()` and `OpenForm()` methods with assigning form return value after opening - specify string literal with full form name as first parameter
- Do NOT move string literal to separate variable as in this case form variable will contain general type `ClientApplicationForm` and not specific product catalog form type

WRONG:
```bsl
FormName = "Catalog.Products.Form.ItemForm";
Form = GetForm(FormName);
```

CORRECT:
```bsl
Form = GetForm("Catalog.Products.Form.ItemForm");
// or
Form = Catalogs.Products.GetForm("ItemForm");
```

## TOPIC: temp-storage

### Using Temporary Storage and Other Containers

- You can put anything into temporary storage and get it back, so create function returning type placed into temporary storage and use reference when getting:

```bsl
Data = GetFromTempStorage(Address); // See NewDataObject
```

- Use same approach when placing user object inside another container object (e.g., `AdditionalParameters` of objects, or form `Parameters`, user parameters of DCS elements, `AdditionalParameters` of notification handlers, etc.)

## TOPIC: string-literals

### Using String Literals as Names

Do NOT access named collection elements (e.g., `AllFormItems`) through string index with element name. Instead, access element directly as static analyzer can control element presence and type.

WRONG:
```bsl
Items["Name"].Visibility = False;
```

CORRECT:
```bsl
Items.Name.Visibility = False;
```

Exceptions:
* Accessing programmatically created elements that don't exist at static analysis moment. Specify local variable type and then access its properties:

```bsl
Element = Items["Name"]; // FormField -
Element.Visibility = False;
Element.Accessibility = True;
```

* Element presence in collection may be optional, with element presence check:

```bsl
    Element = Items.Find("Name");
    If Element <> Undefined Then
        Element.Visibility = False;
...
```

## TOPIC: export-methods

### Export Procedures and Functions

- All method parameters and function return values must contain type descriptions
- Local procedures and functions are not required to contain type descriptions in documenting comments and can be fully calculated if code is written transparently for static analyzer

## TOPIC: query-results

### Query Result Selection and Unloading

When typing selection/unloading from query result, use capabilities:

1. Static description of all selection fields in return value of data getter function. Applicable for functions dynamically forming query text and selection, as well as all export functions returning selection.
2. Dynamic field typing based on query text. Future 1C:EDT versions may implement ability to access query fields when query text and selection are in same procedure, query text has no errors, selection is not passed to another module.

WRONG:
```bsl
Query = New Query;
Query.Text = 
    BalancesQueryText()
    + ReservesQueryText();
QueryResult = Query.Execute();
Selection = QueryResult.Select();
While Selection.Next() Do
    Ref = Selection.Ref.GetObject();
...
```

CORRECT:
```bsl
// Returns:
// QueryResultSelection:
//  * Product - CatalogRef.Products
Function BalancesForProcessing()
    Query = New Query;
    Query.Text = 
        BalancesQueryText()
        + ReservesQueryText();
    QueryResult = Query.Execute();
    Return QueryResult.Select();
EndFunction;

...

Selection = BalancesForProcessing();
While Selection.Next() Do
    Ref = Selection.Product.GetObject();
...
```

## TOPIC: composite-types

### Composite Type Limitations

- Generally WRONG: create composite types that are not similar to each other. Examples:
    - String and CatalogObject
    - Number and Array
    - Collections (array, VT, Map, TP, Structure) and simple types (string, number, reference)
    - ValueTree and ValueTable
- Such mixed types presence is definite reason for code refactoring

## TOPIC: documentation-syntax

### Documentation Comment Syntax

Description - multiline, with links:
```bsl
// In description it's recommended to write all links on separate line.
// links to web pages:
// See https://1c.ru
// Link to function in description text
// See CommonModule.ModuleFunction
//
Procedure ObjectProcessing(Object)
```

In parameters section, after keyword `Parameters` - colon is REQUIRED:
```bsl
// Method description
// 
// Parameters:
//  Object - CatalogObject.Products
Procedure ObjectProcessing(Object)
```

Multiple types on separate lines with dash:
```bsl
// Parameters:
//  Object - CatalogObject.Products - Description for type
//         - DocumentObject.Sales - Description for another type
Procedure ObjectProcessing(Object)
```

Multiple types on one line - comma separated:
```bsl
// Parameters:
//  Object - CatalogObject.Products, DocumentObject.Sales - Description for type list
Procedure ObjectProcessing(Object)
```

Field extension for type, **strictly after colon** at end of single-line description:
```bsl
// Parameters:
//  Object - Structure - Cannot be multiline description here:
//  * StructureField - ValueTable - Structure field description, extension for table:
//    ** ColumnName - Number - table column description
Procedure ObjectProcessing(Object)
```

Can omit description, keep only types and required colon:
```bsl
// Parameters:
//  Object - Structure:
//  * StructureField - ValueTable:
//    ** ColumnName - Number
Procedure ObjectProcessing(Object)
```

Collection element type in parameters:
```bsl
// Parameters:
//  Object - Array of CatalogObject.Products - Here single element type
Procedure ObjectProcessing(Object)
```

Composite element type - comma separated:
```bsl
// Parameters:
//  Object - Array of CatalogObject.Products, DocumentObject.Sales -
Procedure ObjectProcessing(Object)
```

Return value type list - each on new line:
```bsl
// Returns:
//  - Structure
//  - ValueTable
Function ObjectProcessing(Object)
```

Reference to constructor function in return value:
```bsl
// Returns:
//  See NewDataObject
Function ObjectProcessing(Object)
```

## TOPIC: diagnostics

### How to Diagnose Untyped Code

Diagnosing the problem:
- When hovering mouse over object in code - popup tooltip panel doesn't show type, meaning 1C:EDT typing system couldn't calculate it
- In code after dot accessing property/method call content-assist (press `Ctrl+Space`) - input hint doesn't show properties in format `Object.Property <PropertyType> ~ ObjectType`

To find causes of untyped code:
1. Find the very first object in call chain in this line
2. Determine if this object has type - hover mouse or press `F2` for tooltip
3. Go to object definition - press `F3` and at definition location determine type
4. Type defined too generally (abstract), e.g., `ManagedForm`, `ValueTable`, `Array`, `Structure`, `DocumentObject`, `CatalogRef`, etc. without specifying specific properties to access
5. Is typing documenting comment written correctly
6. If types are correct - from object/variable definition location to type loss location - check all usage places: re-assignment, passing to method call, etc.
