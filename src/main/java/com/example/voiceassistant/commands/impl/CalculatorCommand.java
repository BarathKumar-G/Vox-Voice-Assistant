package com.example.voiceassistant.commands.impl;

import com.example.voiceassistant.commands.Command;
import com.example.voiceassistant.utils.NumberParser;

/**
 * Performs basic mathematical calculations.
 * Usage: "calculate 5 plus 3" or "calculate one plus two" or "calculate 10 times 2"
 */
public class CalculatorCommand implements Command {
    @Override
    public String execute(String arg) throws Exception {
        if (arg == null || arg.trim().isEmpty()) {
            return "Please specify a calculation. Example: calculate 5 plus 3";
        }

        try {
            String expression = arg.trim().toLowerCase();

            expression = convertSpokenNumbersToDigits(expression);

            // Replace words with operators
            expression = expression.replace(" plus ", " + ");
            expression = expression.replace(" minus ", " - ");
            expression = expression.replace(" times ", " * ");
            expression = expression.replace(" divided by ", " / ");
            expression = expression.replace(" mod ", " % ");

            double result = evaluateExpression(expression);

            return "Result: " + (result == (long) result ? String.valueOf((long) result) : String.format("%.2f", result));
        } catch (Exception e) {
            return "Invalid calculation: " + e.getMessage();
        }
    }

    private String convertSpokenNumbersToDigits(String expression) {
        String[] tokens = expression.split("\\s+");
        StringBuilder result = new StringBuilder();

        for (String token : tokens) {
            int num = NumberParser.parseNumber(token);
            if (num >= 0) {
                result.append(num).append(" ");
            } else {
                result.append(token).append(" ");
            }
        }

        return result.toString().trim();
    }

    private double evaluateExpression(String expression) throws Exception {
        // Remove spaces
        expression = expression.replaceAll("\\s+", "");

        // Use a simple safe evaluator for basic math
        return new ExpressionEvaluator().evaluate(expression);
    }

    private static class ExpressionEvaluator {
        public double evaluate(String expression) throws Exception {
            // Handle order of operations: *, /, % first, then +, -
            expression = handleMultiplyDivideModulo(expression);
            expression = handleAddSubtract(expression);

            return Double.parseDouble(expression);
        }

        private String handleMultiplyDivideModulo(String expr) throws Exception {
            while (expr.contains("*") || expr.contains("/") || expr.contains("%")) {
                int mulIdx = expr.indexOf("*");
                int divIdx = expr.indexOf("/");
                int modIdx = expr.indexOf("%");

                int idx = findSmallestPositive(mulIdx, divIdx, modIdx);
                if (idx == -1) break;

                String[] parts = splitAtOperator(expr, idx);
                long left = Math.round(Double.parseDouble(parts[0]));
                long right = Math.round(Double.parseDouble(parts[2]));
                long result;

                if (expr.charAt(idx) == '*') {
                    result = left * right;
                } else if (expr.charAt(idx) == '/') {
                    result = left / right;
                } else {
                    result = left % right;
                }

                expr = String.valueOf(result) + expr.substring(idx + 1);
            }
            return expr;
        }

        private String handleAddSubtract(String expr) throws Exception {
            for (int i = 1; i < expr.length(); i++) {
                if (expr.charAt(i) == '+' || expr.charAt(i) == '-') {
                    String[] parts = splitAtOperator(expr, i);
                    long left = Math.round(Double.parseDouble(parts[0]));
                    long right = Math.round(Double.parseDouble(parts[2]));
                    long result = expr.charAt(i) == '+' ? left + right : left - right;
                    expr = String.valueOf(result) + expr.substring(i + 1);
                    i = 0;
                }
            }
            return expr;
        }

        private int findSmallestPositive(int... indices) {
            int smallest = -1;
            for (int i : indices) {
                if (i != -1 && (smallest == -1 || i < smallest)) {
                    smallest = i;
                }
            }
            return smallest;
        }

        private String[] splitAtOperator(String expr, int idx) {
            String left = expr.substring(0, idx);
            String op = String.valueOf(expr.charAt(idx));
            String right = expr.substring(idx + 1);
            return new String[]{left, op, right};
        }
    }
}






















