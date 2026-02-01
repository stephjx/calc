package com.example.calculator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

/**
 * MainActivity handles the calculator UI and user interactions.
 * Manages button clicks, display updates, and coordinates with CalculatorLogic.
 */
public class MainActivity extends AppCompatActivity {
    
    private TextView textDisplay;
    private StringBuilder currentExpression;
    private boolean isNewExpression;
    private boolean isError;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Set the appropriate layout based on orientation
        if (getResources().getBoolean(R.bool.is_landscape)) {
            setContentView(R.layout.activity_main_landscape);
        } else {
            setContentView(R.layout.activity_main);
        }
        
        initializeViews();
        setupButtonClickListeners();
        resetCalculator();
    }
    
    /**
     * Initialize UI components.
     */
    private void initializeViews() {
        textDisplay = findViewById(R.id.textDisplay);
        currentExpression = new StringBuilder();
        isNewExpression = true;
        isError = false;
    }
    
    /**
     * Set up click listeners for all calculator buttons.
     */
    private void setupButtonClickListeners() {
        // Number buttons (0-9)
        setupNumberButton(R.id.button0, "0");
        setupNumberButton(R.id.button1, "1");
        setupNumberButton(R.id.button2, "2");
        setupNumberButton(R.id.button3, "3");
        setupNumberButton(R.id.button4, "4");
        setupNumberButton(R.id.button5, "5");
        setupNumberButton(R.id.button6, "6");
        setupNumberButton(R.id.button7, "7");
        setupNumberButton(R.id.button8, "8");
        setupNumberButton(R.id.button9, "9");
        
        // Operator buttons
        setupOperatorButton(R.id.buttonAdd, "+");
        setupOperatorButton(R.id.buttonSubtract, "−");
        setupOperatorButton(R.id.buttonMultiply, "×");
        setupOperatorButton(R.id.buttonDivide, "÷");
        
        // Function buttons
        setupFunctionButton(R.id.buttonPercent, "%");
        setupFunctionButton(R.id.buttonDecimal, ".");
        setupFunctionButton(R.id.buttonClear, "C");
        setupFunctionButton(R.id.buttonDelete, "⌫");
        setupFunctionButton(R.id.buttonEquals, "=");
        
        // Landscape-specific buttons
        View parenthesesButton = findViewById(R.id.buttonParentheses);
        if (parenthesesButton != null) {
            parenthesesButton.setOnClickListener(v -> handleParentheses());
        }
        
        View squareButton = findViewById(R.id.buttonSquare);
        if (squareButton != null) {
            squareButton.setOnClickListener(v -> handleSquare());
        }
        
        View squareRootButton = findViewById(R.id.buttonSquareRoot);
        if (squareRootButton != null) {
            squareRootButton.setOnClickListener(v -> handleSquareRoot());
        }
    }
    
    /**
     * Set up number button click listener.
     */
    private void setupNumberButton(int buttonId, String value) {
        Button button = findViewById(buttonId);
        if (button != null) {
            button.setOnClickListener(v -> handleNumberInput(value));
        }
    }
    
    /**
     * Set up operator button click listener.
     */
    private void setupOperatorButton(int buttonId, String operator) {
        Button button = findViewById(buttonId);
        if (button != null) {
            button.setOnClickListener(v -> handleOperatorInput(operator));
        }
    }
    
    /**
     * Set up function button click listener.
     */
    private void setupFunctionButton(int buttonId, String function) {
        Button button = findViewById(buttonId);
        if (button != null) {
            button.setOnClickListener(v -> handleFunctionInput(function));
        }
    }
    
    /**
     * Handle number input (0-9).
     */
    private void handleNumberInput(String number) {
        if (isError) {
            resetCalculator();
        }
        
        if (isNewExpression) {
            currentExpression.setLength(0);
            isNewExpression = false;
        }
        
        // Prevent multiple leading zeros
        if (currentExpression.length() == 0 && number.equals("0")) {
            currentExpression.append("0");
        } else if (currentExpression.length() == 1 && currentExpression.charAt(0) == '0') {
            // Replace leading zero with the new number
            currentExpression.setCharAt(0, number.charAt(0));
        } else {
            currentExpression.append(number);
        }
        
        updateDisplay();
    }
    
    /**
     * Handle operator input (+, −, ×, ÷).
     */
    private void handleOperatorInput(String operator) {
        if (isError) return;
        
        if (isNewExpression) {
            // If starting new expression, use previous result
            if (currentExpression.length() == 0) {
                currentExpression.append("0");
            }
            isNewExpression = false;
        }
        
        // Prevent consecutive operators
        if (currentExpression.length() > 0) {
            char lastChar = currentExpression.charAt(currentExpression.length() - 1);
            if (isOperator(lastChar)) {
                // Replace last operator
                currentExpression.setCharAt(currentExpression.length() - 1, operator.charAt(0));
            } else {
                currentExpression.append(operator);
            }
        } else {
            // Start with zero if expression is empty
            currentExpression.append("0").append(operator);
        }
        
        updateDisplay();
    }
    
    /**
     * Handle function input (C, ⌫, %, ., =).
     */
    private void handleFunctionInput(String function) {
        switch (function) {
            case "C":
                resetCalculator();
                break;
            case "⌫":
                handleDelete();
                break;
            case "%":
                handlePercentage();
                break;
            case ".":
                handleDecimal();
                break;
            case "=":
                calculateResult();
                break;
        }
    }
    
    /**
     * Handle parentheses input (landscape only).
     */
    private void handleParentheses() {
        if (isError) return;
        
        if (isNewExpression) {
            currentExpression.setLength(0);
            isNewExpression = false;
        }
        
        int openCount = 0;
        int closeCount = 0;
        
        for (int i = 0; i < currentExpression.length(); i++) {
            if (currentExpression.charAt(i) == '(') openCount++;
            if (currentExpression.charAt(i) == ')') closeCount++;
        }
        
        // Add opening parenthesis if we can
        if (openCount <= closeCount) {
            if (currentExpression.length() == 0 || 
                isOperator(currentExpression.charAt(currentExpression.length() - 1)) ||
                currentExpression.charAt(currentExpression.length() - 1) == '(') {
                currentExpression.append("(");
            } else {
                // Add multiplication before parenthesis if needed
                currentExpression.append("×(");
            }
        } else {
            // Add closing parenthesis
            currentExpression.append(")");
        }
        
        updateDisplay();
    }
    
    /**
     * Handle square function (x²).
     */
    private void handleSquare() {
        if (isError) return;
        
        if (isNewExpression) {
            currentExpression.setLength(0);
            isNewExpression = false;
        }
        
        if (currentExpression.length() > 0) {
            char lastChar = currentExpression.charAt(currentExpression.length() - 1);
            if (Character.isDigit(lastChar) || lastChar == ')') {
                currentExpression.append("²");
            }
        }
        
        updateDisplay();
    }
    
    /**
     * Handle square root function (√).
     */
    private void handleSquareRoot() {
        if (isError) return;
        
        if (isNewExpression) {
            currentExpression.setLength(0);
            isNewExpression = false;
        }
        
        if (currentExpression.length() == 0 || 
            isOperator(currentExpression.charAt(currentExpression.length() - 1))) {
            currentExpression.append("√");
        } else {
            // Add multiplication before square root
            currentExpression.append("×√");
        }
        
        updateDisplay();
    }
    
    /**
     * Handle delete operation.
     */
    private void handleDelete() {
        if (isError) {
            resetCalculator();
            return;
        }
        
        if (currentExpression.length() > 0) {
            currentExpression.deleteCharAt(currentExpression.length() - 1);
            if (currentExpression.length() == 0) {
                currentExpression.append("0");
                isNewExpression = true;
            }
        }
        
        updateDisplay();
    }
    
    /**
     * Handle percentage operation.
     */
    private void handlePercentage() {
        if (isError) return;
        
        if (isNewExpression) {
            currentExpression.setLength(0);
            isNewExpression = false;
        }
        
        if (currentExpression.length() > 0) {
            char lastChar = currentExpression.charAt(currentExpression.length() - 1);
            if (Character.isDigit(lastChar) || lastChar == ')') {
                currentExpression.append("%");
            }
        }
        
        updateDisplay();
    }
    
    /**
     * Handle decimal point input.
     */
    private void handleDecimal() {
        if (isError) return;
        
        if (isNewExpression) {
            currentExpression.setLength(0);
            currentExpression.append("0");
            isNewExpression = false;
        }
        
        // Check if we're already in a number with a decimal point
        int lastOperatorIndex = Math.max(
            Math.max(currentExpression.lastIndexOf("+"), currentExpression.lastIndexOf("−")),
            Math.max(currentExpression.lastIndexOf("×"), currentExpression.lastIndexOf("÷"))
        );
        
        String lastNumber = currentExpression.substring(lastOperatorIndex + 1);
        if (!lastNumber.contains(".")) {
            currentExpression.append(".");
        }
        
        updateDisplay();
    }
    
    /**
     * Calculate and display the result.
     */
    private void calculateResult() {
        if (isError || currentExpression.length() == 0) return;
        
        // Validate expression before evaluation
        if (!CalculatorLogic.isValidExpression(currentExpression.toString())) {
            showError("Invalid expression");
            return;
        }
        
        double result = CalculatorLogic.evaluate(currentExpression.toString());
        
        if (Double.isNaN(result)) {
            showError("Error");
        } else {
            String formattedResult = CalculatorLogic.formatResult(result);
            textDisplay.setText(formattedResult);
            currentExpression.setLength(0);
            currentExpression.append(formattedResult);
            isNewExpression = true;
        }
    }
    
    /**
     * Reset calculator to initial state.
     */
    private void resetCalculator() {
        currentExpression.setLength(0);
        currentExpression.append("0");
        isNewExpression = true;
        isError = false;
        updateDisplay();
    }
    
    /**
     * Show error message and set error state.
     */
    private void showError(String message) {
        textDisplay.setText(message);
        isError = true;
        currentExpression.setLength(0);
    }
    
    /**
     * Update the display with current expression.
     */
    private void updateDisplay() {
        if (currentExpression.length() == 0) {
            textDisplay.setText("0");
        } else {
            textDisplay.setText(currentExpression.toString());
        }
    }
    
    /**
     * Check if character is an operator.
     */
    private boolean isOperator(char c) {
        return c == '+' || c == '−' || c == '×' || c == '÷';
    }
    
    /**
     * Handle configuration changes (orientation changes).
     */
    @Override
    public void onConfigurationChanged(android.content.res.Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        
        // Recreate the activity to load the appropriate layout
        recreate();
    }
}