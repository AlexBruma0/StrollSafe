<!--
Good naming for class, method, and variable names.
• Good formatting counts.
• Comments on all classes.
-->

# Styling Guide

## Table of Contents

1. Introduction
2. File Names
3. File Organization
4. Declarations
5. Formatting
6. Naming Conventions
7. Programming Practices


<hr>

## 1. Introduction

The purpose of this document is to standardize the team's code so that it is consistent and maintainable. In this project, we will be using Android API 32 Platform (version 11.0.12).

### Acknowledgments 
This coding style guide is inspired by the [Google java Style Guide](https://google.github.io/styleguide/javaguide.html) and [Code Conventions for the Java Programming Language](https://www.oracle.com/java/technologies/javase/codeconventions-introduction.html) by Oracle. 

<hr>

## 2. File Names
### 2.1. Java files
The names of each Java file will describe the contents of the file and have `.java` extension. In addition, Each file can only contain a maximum of 1 class definition. 

<hr>

## 3. File Organization
A source file will be structured in the following manner:

1. Beginning Comments
2. Package Statement
3. Import statements
4. Exactly one top-level class or interface declaration

### 3.1. Beginning Comments
All source files should begin will a comment block with the class name, description, Creation date, creator's name, last modified date and last modifier's name. The

```
/*
 * Classname
 * 
 * Description: Short description about what the class does
 *
 * Creation Date:
 * Creator Name:
 * 
 * Last Modified: 
 * Last Modifier:
 */
```

### 3.2. Package & Import Statements
The first non-comment line of a Java source file must be a `package` statement.

Import statements will follow the package statement, with each statement taking up exactly one line. 

```
package java.mypackage;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

```

### 3.3. Class and Interface Declarations
Each source file will contain exactly one top-level class/interface declaration. Static variables and instance variables will be declared first, followed by constructors and finally methods with appropriate access modifiers. For each constructor and method, a short description must be included as well as any pre/post conditions.

```
public class int Position {
→   // static variables
→   public static final int TIME_OUT = 100;
    
→   // instance variables
→   public double latitude;
→   public double longitude;

→   // default constructor
→   public Position() {
→   →   this.latitude = 0.00;
→   →   this.longitude = 0.00;
→   }

→   // parameterized constructor
→   public Position (double lat, double long) {
→   →   this.latitude = lat;
→   →   this.longitude = long;
→   }

→   /* Description: Returns the latitude of the object
→    *
→    * @Return returns the latitude of the object
→    */
→   public double getLatitude() {
→   →   return this.latitude;
→   }
} // end of Position.java
```

<hr>

## 4. Declarations
One declaration/initialization per line. 
<hr>

## 5. Naming Conventions
### 5.1. packages
Packages are written in all lowercase ASCII letters.
```
com.sun.eng
```

### 5.1. Classes
Class names must use camel case and start with an upper case. Classes should be named in terms of a singular noun
```
class Student;
```

### 5.2. Constants
Constants must be all upper case, with multiple words separated by '_'.

```
final int DAYS_PER_WEEK = 7; 
```
Constants should be in the most restrictive scope possible. 

### 5.3. Methods
Methods must use camel case and start with a lower case. Methods should be named in terms of an action.
```
String getInput();

boolean verifyInput();
```
Each method is separated by one blank line.

### 5.4. Variables
Variables should use camel case and start with a lower case. Variable names should be descriptive of what the variable is holding. However, do not prefix the variable name with the its type.
```
class Car {
→   private String brand;
→   private String serialNumber;
}
```
Boolean variables should be named such that they make sense in branching statements. 
```
if (isOpen) {
→ ...
}

while (!endOfFile && hasMoreData) {
→ ... 
}
```
Do not use literal numbers (magic numbers) if possible. If needed, assign a named constant and make sure it is clear what it represents. 

<hr>

## 6. Formatting
### 6.1. Indentation
Tab size is 4; indentation is 4 (1 tab). Use tabs to indent code.

### 6.2. Line Length
Lines should be no longer than 100 characters long. For expressions that will not fit in a single line, break it up according to the general principles:

* Break after a comma
* Break before an operator
* Subsequent lines will be indented twice (8 spaces).

```
someMethod(longExpression1, longExpression2, longExpression3, 
→   →   longExpression4, longExpression5);
 
var = someMethod1(longExpression1,someMethod2(longExpression2,
      →   →   longExpression3));
```

### 6.2. Braces {...}
Opening braces are at the end of the enclosing statement. Closing braces will appear on their own line and be aligned with the start of the enclosing statement. Statements inside the block are indented with 1 tab.
```
if (j < 10) {
→ counter = getStudentCount(lowIndex,
→ → highIndex);

→ if (x == 0) {
→ → if (y != 0) {
→ → → x = y; → → // Insightful comment here
→ → }
→ }
}

for (int i = 0; i < 10; i++) {
→ ...
}

while (i > 0) {
→ ...
}

do {
→ ...
} while (x > 1);

if (y > 500) {
→ ...
} else if (y == 0) {
→ ...
} else {
→ ...
}

try {
→ ...
} catch (Exception e) {
→ ...
}
```

**EXCEPTION:** if statements with multi-line conditions have the starting brace aligned on the left to make it easier to spot the block in the if statement.
```
if (someBigBooleanExpression
→ && !someOtherExpression)
{
→ ...
}
```

All conditional statements and loops must include braces around their statements, even if there is only one statement in the body.

### 6.3. Statements
Declare each variable name in its own line rather than together
```
int x;
int y;
```
Each statement should be in its own line. 
```
i = j + k;

if (i == l) {
→ ...
} else {
→ ...
}
```

All binary operators (arithmetic, bitwise and assignment) and ternary conditionals must be surrounded by one space. 
```
int a = 9;
int b = 0;
b += a; 

a = b + a;
b |= 0x1b;

if (x && y) {
→ ...
}

while (x < y) {
→ ...
}

z = x < y ? 1 : 0; → → // if x < y, z = 1, otherwise, z = 0 
```
Commas must have one space after them and none before. 
```
myObject.someMethod(x, y, z);
```

Unary operators (!, *, &, -, +, ++, --) have no additional space on either side of the operator.
```
while (!x) {
→ x++;
}

x = -1 + (+2);
```

<hr>

## 7. Programming Practices

### 7.1. Caught Exceptions
All caught exceptions must be handled by the programmer. Typical responses are to log it or rethrow the exception.

When it truly is appropriate to take no action whatsoever in a catch block, the reason this is justified is explained in a comment.

**EXCEPTION:** In tests, a caught exception may be ignored without comment if its name is or begins with expected. 
