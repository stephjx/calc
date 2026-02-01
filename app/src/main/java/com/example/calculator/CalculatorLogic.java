package com.example.calculator;

import java.util.Stack;
import java.util.EmptyStackException;

/**
 * CalculatorLogic handles all mathematical operations and expression evaluation.
 * Implements PEMDAS (Parentheses, Exponents, Multiplication/Division, Addition/Subtraction)
 * order of operations with proper error handling.
 */
public class CalculatorLogic {
    
    /**
     * Evaluates a mathematical expression string and returns the result.
     * @param expression The mathematical expression to evaluate
     * @return The result as a double, or Double.NaN for errors
     */
    public static double evaluate(String expression) {
        if (expression == null || expression.trim().isEmpty()) {
            return 0.0;
        }
        
        try {
            // Preprocess the expression to handle special cases
            String processedExpression = preprocessExpression(expression);
            return evaluateExpression(processedExpression);
        } catch (Exception e) {
            return Double.NaN;
        }
    }
    
    /**
     * Preprocesses the expression to handle special functions and formatting.
     */
    private static String preprocessExpression(String expression) {
        // Replace display symbols with calculation symbols
        String processed = expression.replace("×", "*")
                                   .replace("÷", "/")
                                   .replace("−", "-")
                                   .replace("√", "sqrt")
                                   .replace("²", "^2");
        
        // Handle percentage operations
        processed = handlePercentages(processed);
        
        // Handle square operations (x²)
        processed = handleSquares(processed);
        
        // Handle square root operations
        processed = handleSquareRoots(processed);
        
        return processed;
    }
    
    /**
     * Handles percentage operations in the expression.
     */
    private static String handlePercentages(String expression) {
        // Handle cases like "50%" -> "50/100"
        // and "100+50%" -> "100+(100*50/100)"
        return expression.replaceAll("(\\d+(?:\\.\\d+)?)%", "$1/100");
    }
    
    /**
     * Handles square operations (x²).
     */
    private static String handleSquares(String expression) {
        StringBuilder result = new StringBuilder();
        int i = 0;
        
        while (i < expression.length()) {
            if (i < expression.length() - 1 && expression.charAt(i) == '^' && expression.charAt(i + 1) == '2') {
                result.append("^2");
                i += 2;
            } else {
                result.append(expression.charAt(i));
                i++;
            }
        }
        
        return result.toString();
    }
    
    /**
     * Handles square root operations.
     */
    private static String handleSquareRoots(String expression) {
        // Replace sqrt with Math.sqrt function call
        return expression.replace("sqrt", "sqrt");
    }
    
    /**
     * Main expression evaluation using Shunting Yard algorithm for PEMDAS.
     */
    private static double evaluateExpression(String expression) {
        // Tokenize the expression
        String[] tokens = tokenizeExpression(expression);
        
        // Convert to postfix notation using Shunting Yard algorithm
        String[] postfix = infixToPostfix(tokens);
        
        // Evaluate postfix expression
        return evaluatePostfix(postfix);
    }
    
    /**
     * Tokenizes the expression into numbers, operators, and functions.
     */
    private static String[] tokenizeExpression(String expression) {
        // Remove spaces and split into tokens
        expression = expression.replaceAll("\\s+", "");
        
        // Use regex to split while preserving multi-character operators and functions
        String[] tokens = expression.split("(?<=\\d|\\)|\\()(?=\\D)|(?<=\\D)(?=\\d|\\(|sqrt)|(?<=sqrt)(?=\\()");
        
        return tokens;
    }
    
    /**
     * Converts infix notation to postfix notation using Shunting Yard algorithm.
     */
    private static String[] infixToPostfix(String[] tokens) {
        Stack<String> operatorStack = new Stack<>();
        StringBuilder postfix = new StringBuilder();
        
        for (String token : tokens) {
            if (isNumber(token)) {
                postfix.append(token).append(" ");
            } else if (isFunction(token)) {
                operatorStack.push(token);
            } else if (token.equals("(")) {
                operatorStack.push(token);
            } else if (token.equals(")")) {
                while (!operatorStack.isEmpty() && !operatorStack.peek().equals("(")) {
                    postfix.append(operatorStack.pop()).append(" ");
                }
                if (!operatorStack.isEmpty()) {
                    operatorStack.pop(); // Remove the "("
                }
                // If there's a function before the parenthesis, pop it
                if (!operatorStack.isEmpty() && isFunction(operatorStack.peek())) {
                    postfix.append(operatorStack.pop()).append(" ");
                }
            } else if (isOperator(token)) {
                while (!operatorStack.isEmpty() && 
                       !operatorStack.peek().equals("(") &&
                       getPrecedence(operatorStack.peek()) >= getPrecedence(token)) {
                    postfix.append(operatorStack.pop()).append(" ");
                }
                operatorStack.push(token);
            }
        }
        
        // Pop remaining operators
        while (!operatorStack.isEmpty()) {
            postfix.append(operatorStack.pop()).append(" ");
        }
        
        return postfix.toString().trim().split("\\s+");
    }
    
    /**
     * Evaluates postfix expression.
     */
    private static double evaluatePostfix(String[] postfix) {
        Stack<Double> stack = new Stack<>();
        
        for (String token : postfix) {
            if (isNumber(token)) {
                stack.push(Double.parseDouble(token));
            } else if (isOperator(token)) {
                if (stack.size() < 2) {
                    throw new IllegalArgumentException("Invalid expression");
                }
                double b = stack.pop();
                double a = stack.pop();
                double result = performOperation(a, b, token);
                stack.push(result);
            } else if (isFunction(token)) {
                if (stack.isEmpty()) {
                    throw new IllegalArgumentException("Invalid expression");
                }
                double operand = stack.pop();
                double result = performFunction(operand, token);
                stack.push(result);
            }
        }
        
        if (stack.size() != 1) {
            throw new IllegalArgumentException("Invalid expression");
        }
        
        return stack.pop();
    }
    
    /**
     * Performs binary operations.
     */
    private static double performOperation(double a, double b, String operator) {
        switch (operator) {
            case "+":
                return a + b;
            case "-":
                return a - b;
            case "*":
                return a * b;
            case "/":
                if (b == 0) {
                    throw new ArithmeticException("Division by zero");
                }
                return a / b;
            case "^":
                return Math.pow(a, b);
            default:
                throw new IllegalArgumentException("Unknown operator: " + operator);
        }
    }
    
    /**
     * Performs unary functions.
     */
    private static double performFunction(double operand, String function) {
        switch (function) {
            case "sqrt":
                if (operand < 0) {
                    throw new ArithmeticException("Square root of negative number");
                }
                return Math.sqrt(operand);
            default:
                throw new IllegalArgumentException("Unknown function: " + function);
        }
    }
    
    // Helper methods
    private static boolean isNumber(String token) {
        try {
            Double.parseDouble(token);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    private static boolean isOperator(String token) {
        return token.equals("+") || token.equals("-") || 
               token.equals("*") || token.equals("/") || 
               token.equals("^");
    }
    
    private static boolean isFunction(String token) {
        return token.equals("sqrt");
    }
    
    private static int getPrecedence(String operator) {
        switch (operator) {
            case "+":
            case "-":
                return 1;
            case "*":
            case "/":
                return 2;
            case "^":
                return 3;
            default:
                return 0;
        }
    }
    
    /**
     * Formats a double result to a clean string representation.
     * @param result The result to format
     * @return Formatted string or "Error" for invalid results
     */
    public static String formatResult(double result) {
        if (Double.isNaN(result) || Double.isInfinite(result)) {
            return "Error";
        }
        
        // Remove trailing zeros and decimal point if whole number
        if (result == (long) result) {
            return String.valueOf((long) result);
        } else {
            // Limit decimal places to prevent overflow
            return String.format("%.10f", result).replaceAll("0*$", "").replaceAll("\\.$", "");
        }
    }
    
    /**
     * Checks if an expression is valid (properly formed).
     * @param expression The expression to validate
     * @return true if valid, false otherwise
     */
    public static boolean isValidExpression(String expression) {
        if (expression == null || expression.isEmpty()) {
            return false;
        }
        
        int parenthesesCount = 0;
        
        for (int i = 0; i < expression.length(); i++) {
            char c = expression.charAt(i);
            
            if (c == '(') {
                parenthesesCount++;
            } else if (c == ')') {
                parenthesesCount--;
                if (parenthesesCount < 0) {
                    return false; // More closing than opening
                }
            }
        }
        
        return parenthesesCount == 0;
    }
}