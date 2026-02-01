# Modern Android Calculator

A clean, minimalist calculator app for Android with a beautiful lavender theme, built using Java and following Android Studio best practices.

## Features

### Design & UI
- **Lavender/Pastel Purple Theme**: Modern, clean aesthetic with carefully selected color palette
- **Responsive Design**: Works perfectly on all screen sizes and orientations
- **Material Design**: Follows Google's Material Design guidelines
- **Interactive Elements**: 
  - Ripple effects on all buttons
  - Press animations with elevation changes
  - Rounded corners (8dp radius)
  - Large, readable display area

### Functionality
- **Basic Operations**: Addition (+), Subtraction (−), Multiplication (×), Division (÷)
- **Advanced Functions**: 
  - Parentheses ( ) for complex expressions
  - Square (x²) operations
  - Square root (√) calculations
  - Percentage (%) calculations
- **Number Support**: Decimal numbers with proper formatting
- **Special Functions**: Clear (C) and Delete (⌫) operations
- **Smart Features**:
  - Proper operator precedence (PEMDAS)
  - Error handling for invalid inputs (e.g., division by zero)
  - Real-time expression display
  - Automatic result formatting

### Technical Implementation

#### Architecture
- **Language**: Pure Java (no Kotlin)
- **Pattern**: Separation of concerns with dedicated logic class
- **UI Framework**: ConstraintLayout for responsive design
- **Compatibility**: Supports API 21+ (Android 5.0 and above)

#### Key Components

**CalculatorLogic.java**
- Custom expression parser implementing PEMDAS order of operations
- Safe evaluation with comprehensive error handling
- Handles all mathematical operations and special functions
- Preprocessing for display symbols and special cases

**MainActivity.java**
- Manages all UI interactions and button handling
- Coordinates with CalculatorLogic for calculations
- Handles orientation changes seamlessly
- Manages display updates and user feedback

**Layout Files**
- `activity_main.xml`: Portrait orientation layout
- `activity_main_landscape.xml`: Landscape orientation with extended functions
- Responsive grid system using ConstraintLayout guidelines

## How It Works

### Expression Evaluation Logic
The calculator uses a custom implementation of the Shunting Yard algorithm:
1. **Tokenization**: Breaks input into numbers, operators, and functions
2. **Infix to Postfix**: Converts standard mathematical notation to postfix using operator precedence
3. **Evaluation**: Processes postfix expression using a stack-based approach
4. **Result Formatting**: Formats output appropriately (removes trailing zeros, handles whole numbers)

### Special Features Implementation
- **Percentage**: Converts "50%" to "50/100" automatically
- **Square Operations**: Handles "x²" notation properly
- **Square Root**: Implements √ function with error checking
- **Parentheses**: Full support for grouping operations
- **Decimal Handling**: Prevents multiple decimal points in single numbers

### Error Handling
- Division by zero protection
- Invalid expression detection
- Square root of negative numbers
- Malformed parentheses
- Overflow protection with proper formatting

## Usage Instructions

### Basic Operations
1. Tap number buttons to enter values
2. Use operator buttons (+, −, ×, ÷) for calculations
3. Press "=" to see the result
4. Use "C" to clear everything or "⌫" to delete last character

### Advanced Features (Landscape Mode)
- **Parentheses**: Use "( )" button to group operations
- **Square**: Tap "x²" after a number to square it
- **Square Root**: Use "√" button for square root calculations
- **Percentage**: Add "%" after numbers for percentage calculations

### Orientation Support
- Automatically switches between portrait and landscape layouts
- Maintains calculation state during orientation changes
- Landscape mode provides additional function buttons
- Responsive design works on all device sizes

## Android Studio Compatibility

This project is ready to run in Android Studio:
- Proper project structure following Android conventions
- Modern Gradle build configuration
- All necessary dependencies included
- Compatible with latest Android SDK versions
- No external dependencies beyond standard Android SDK

## File Structure
```
app/
├── src/main/
│   ├── java/com/example/calculator/
│   │   ├── MainActivity.java          # Main UI controller
│   │   └── CalculatorLogic.java       # Mathematical logic
│   ├── res/
│   │   ├── layout/
│   │   │   ├── activity_main.xml      # Portrait layout
│   │   │   └── activity_main_landscape.xml  # Landscape layout
│   │   ├── values/
│   │   │   ├── colors.xml             # Color definitions
│   │   │   ├── themes.xml             # Style definitions
│   │   │   ├── strings.xml            # App strings
│   │   │   └── bools.xml              # Orientation detection
│   │   └── values-land/
│   │       ├── bools.xml              # Landscape detection
│   └── AndroidManifest.xml
├── build.gradle                       # App build configuration
└── ...                                # Other project files
```

## Requirements
- Android Studio (latest version recommended)
- Android SDK API 21 or higher
- Java 8 or higher
- Minimum screen size: Any Android device

The calculator provides a smooth, intuitive user experience with robust mathematical capabilities while maintaining clean, modern design principles.