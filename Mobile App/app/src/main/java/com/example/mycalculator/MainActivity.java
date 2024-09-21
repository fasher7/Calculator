package com.example.mycalculator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Stack;

public class MainActivity extends AppCompatActivity {

    private TextView display;
    private StringBuilder input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        display = findViewById(R.id.display);
        input = new StringBuilder();

        Button btn0 = findViewById(R.id.btn0);
        Button btn1 = findViewById(R.id.btn1);
        Button btn2 = findViewById(R.id.btn2);
        Button btn3 = findViewById(R.id.btn3);
        Button btn4 = findViewById(R.id.btn4);
        Button btn5 = findViewById(R.id.btn5);
        Button btn6 = findViewById(R.id.btn6);
        Button btn7 = findViewById(R.id.btn7);
        Button btn8 = findViewById(R.id.btn8);
        Button btn9 = findViewById(R.id.btn9);
        Button btnDot = findViewById(R.id.btnDot);
        Button btnPlus = findViewById(R.id.btnPlus);
        Button btnMinus = findViewById(R.id.btnMinus);
        Button btnMultiply = findViewById(R.id.btnMultiply);
        Button btnDivide = findViewById(R.id.btnDivide);
        Button btnPower = findViewById(R.id.btnPower);
        Button btnOpenParen = findViewById(R.id.btnOpenParen);
        Button btnCloseParen = findViewById(R.id.btnCloseParen);
        Button btnEqual = findViewById(R.id.btnEqual);
        Button btnClear = findViewById(R.id.btnClear);
        Button btnBackspace = findViewById(R.id.btnBackspace);

        View.OnClickListener listener = v -> {
            Button b = (Button) v;
            input.append(b.getText().toString());
            display.setText(input.toString());
        };

        btn0.setOnClickListener(listener);
        btn1.setOnClickListener(listener);
        btn2.setOnClickListener(listener);
        btn3.setOnClickListener(listener);
        btn4.setOnClickListener(listener);
        btn5.setOnClickListener(listener);
        btn6.setOnClickListener(listener);
        btn7.setOnClickListener(listener);
        btn8.setOnClickListener(listener);
        btn9.setOnClickListener(listener);
        btnDot.setOnClickListener(listener);
        btnPlus.setOnClickListener(listener);
        btnMinus.setOnClickListener(listener);
        btnMultiply.setOnClickListener(listener);
        btnDivide.setOnClickListener(listener);
        btnPower.setOnClickListener(listener);
        btnOpenParen.setOnClickListener(listener);
        btnCloseParen.setOnClickListener(listener);

        btnEqual.setOnClickListener(v -> {
            String result = evaluateExpression(input.toString());
            display.setText(result);
            input.setLength(0);
        });

        btnClear.setOnClickListener(v -> {
            input.setLength(0);
            display.setText("0");
        });

        btnBackspace.setOnClickListener(v -> {
            if (input.length() > 1) {
                input.deleteCharAt(input.length() - 1);
                display.setText(input.toString());
            } else {
                input.setLength(0);
                display.setText("0");
            }
        });
    }

    private String evaluateExpression(String expression) {
        try {
            expression = expression.replaceAll("\\s", "");
            String postfix = infixToPostfix(expression);
            double result = evaluatePostfix(postfix);
            return Double.toString(result);
        } catch (Exception e) {
            return "Error";
        }
    }

    private String infixToPostfix(String infix) {
        StringBuilder postfix = new StringBuilder();
        Stack<Character> stack = new Stack<>();

        for (int i = 0; i < infix.length(); i++) {
            char c = infix.charAt(i);

            if (Character.isDigit(c) || c == '.') {
                postfix.append(c);
                while (i + 1 < infix.length() && (Character.isDigit(infix.charAt(i + 1)) || infix.charAt(i + 1) == '.')) {
                    postfix.append(infix.charAt(++i));
                }
                postfix.append(' ');
            } else if (c == '(') {
                stack.push(c);
            } else if (c == ')') {
                while (!stack.isEmpty() && stack.peek() != '(') {
                    postfix.append(stack.pop()).append(' ');
                }
                stack.pop();
            } else if (c == '+' || c == '-' || c == '*' || c == '/' || c == '^') {
                while (!stack.isEmpty() && precedence(c) <= precedence(stack.peek())) {
                    postfix.append(stack.pop()).append(' ');
                }
                stack.push(c);
            }
        }

        while (!stack.isEmpty()) {
            postfix.append(stack.pop()).append(' ');
        }

        return postfix.toString();
    }

    private double evaluatePostfix(String postfix) {
        Stack<Double> stack = new Stack<>();
        String[] tokens = postfix.split("\\s");

        for (String token : tokens) {
            if (token.isEmpty()) continue;

            if (isOperator(token.charAt(0))) {
                double b = stack.pop();
                double a = stack.pop();
                stack.push(applyOperator(token.charAt(0), a, b));
            } else {
                stack.push(Double.parseDouble(token));
            }
        }

        return stack.pop();
    }

    private double applyOperator(char operator, double a, double b) {
        switch (operator) {
            case '+': return a + b;
            case '-': return a - b;
            case '*': return a * b;
            case '/': return a / b;
            case '^': return Math.pow(a, b);
            default: throw new IllegalArgumentException("Invalid operator");
        }
    }

    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/' || c == '^';
    }

    private int precedence(char op) {
        switch (op) {
            case '+':
            case '-': return 1;
            case '*':
            case '/': return 2;
            case '^': return 3;
            default: return -1;
        }
    }
}
