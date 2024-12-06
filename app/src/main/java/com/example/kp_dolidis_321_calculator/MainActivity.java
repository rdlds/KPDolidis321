package com.example.kp_dolidis_321_calculator;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Stack;

public class MainActivity extends AppCompatActivity {

    private TextView tvDisplay;
    private boolean isNewOperation = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvDisplay = findViewById(R.id.tvDisplay);

        setNumericButtonListeners();
        setOperationButtonListeners();
    }

    private void setNumericButtonListeners() {
        View.OnClickListener listener = v -> {
            Button button = (Button) v;
            String value = button.getText().toString();

            if (isNewOperation) {
                tvDisplay.setText("");
                isNewOperation = false;
            }

            tvDisplay.append(value);
        };

        findViewById(R.id.btn0).setOnClickListener(listener);
        findViewById(R.id.btn1).setOnClickListener(listener);
        findViewById(R.id.btn2).setOnClickListener(listener);
        findViewById(R.id.btn3).setOnClickListener(listener);
        findViewById(R.id.btn4).setOnClickListener(listener);
        findViewById(R.id.btn5).setOnClickListener(listener);
        findViewById(R.id.btn6).setOnClickListener(listener);
        findViewById(R.id.btn7).setOnClickListener(listener);
        findViewById(R.id.btn8).setOnClickListener(listener);
        findViewById(R.id.btn9).setOnClickListener(listener);
        findViewById(R.id.btnDot).setOnClickListener(listener);
    }

    private void setOperationButtonListeners() {
        // Операции: +, -, *, /
        View.OnClickListener simpleOperationListener = v -> {
            Button button = (Button) v;
            tvDisplay.append(" " + button.getText().toString() + " ");
        };

        findViewById(R.id.btnPlus).setOnClickListener(simpleOperationListener);
        findViewById(R.id.btnMinus).setOnClickListener(simpleOperationListener);
        findViewById(R.id.btnMultiply).setOnClickListener(simpleOperationListener);
        findViewById(R.id.btnDivide).setOnClickListener(simpleOperationListener);

        // Кнопка "="
        findViewById(R.id.btnEquals).setOnClickListener(v -> calculateExpression());

        // Сложные операции (вычисляются мгновенно)
        findViewById(R.id.btnSqrt).setOnClickListener(v -> calculateInstant("sqrt"));
        findViewById(R.id.btnPower).setOnClickListener(v -> calculateInstant("pow"));
        findViewById(R.id.btnClean).setOnClickListener(v -> calculateInstant("clean"));
        findViewById(R.id.btnLog).setOnClickListener(v -> calculateInstant("log"));
    }

    private void calculateExpression() {
        String expression = tvDisplay.getText().toString();

        try {
            double result = evaluate(expression);
            tvDisplay.setText(String.valueOf(result));
            isNewOperation = true;
        } catch (Exception e) {
            tvDisplay.setText("Error");
        }
    }

    private void calculateInstant(String operation) {
        try {
            double value = Double.parseDouble(tvDisplay.getText().toString());
            double result = 0;

            switch (operation) {
                case "sqrt":
                    result = Math.sqrt(value);
                    break;
                case "pow":
                    result = Math.pow(value, 2); // Возведение в квадрат
                    break;
                case "clean":
                    result = 0;
                    break;
                case "log":
                    result = Math.log10(value);
                    break;
            }

            tvDisplay.setText(String.valueOf(result));
            isNewOperation = true;
        } catch (NumberFormatException e) {
            tvDisplay.setText("Error");
        }
    }

    // Метод для вычисления строкового выражения
    private double evaluate(String expression) {
        Stack<Double> numbers = new Stack<>();
        Stack<Character> operations = new Stack<>();

        String[] tokens = expression.split(" ");
        for (String token : tokens) {
            if (isNumeric(token)) {
                numbers.push(Double.parseDouble(token));
            } else if (token.length() == 1 && isOperator(token.charAt(0))) {
                while (!operations.isEmpty() && precedence(operations.peek()) >= precedence(token.charAt(0))) {
                    double b = numbers.pop();
                    double a = numbers.pop();
                    char op = operations.pop();
                    numbers.push(applyOperation(a, b, op));
                }
                operations.push(token.charAt(0));
            }
        }

        while (!operations.isEmpty()) {
            double b = numbers.pop();
            double a = numbers.pop();
            char op = operations.pop();
            numbers.push(applyOperation(a, b, op));
        }

        return numbers.pop();
    }

    private boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean isOperator(char c) {
        return c == '+' || c == '-' || c == '*' || c == '/';
    }

    private int precedence(char op) {
        if (op == '+' || op == '-') return 1;
        if (op == '*' || op == '/') return 2;
        return 0;
    }

    private double applyOperation(double a, double b, char op) {
        switch (op) {
            case '+': return a + b;
            case '-': return a - b;
            case '*': return a * b;
            case '/': return a / b;
        }
        return 0;
    }
}
