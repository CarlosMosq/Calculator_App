package com.company.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    TextView operations;
    TextView result;
    Button clear;
    Button delete;
    Button division;
    Button multiplication;
    Button seven;
    Button eight;
    Button nine;
    Button minus;
    Button four;
    Button five;
    Button six;
    Button plus;
    Button one;
    Button two;
    Button three;
    Button equals;
    Button dot;
    Button zero;
    SharedPreferences sharing;
    String saveResult;
    String saveOps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        operations = findViewById(R.id.operations);
        result = findViewById(R.id.result);
        clear = findViewById(R.id.btnAC);
        delete = findViewById(R.id.btnDel);
        division = findViewById(R.id.btnDiv);
        multiplication = findViewById(R.id.btnMult);
        seven = findViewById(R.id.btn7);
        eight = findViewById(R.id.btn8);
        nine = findViewById(R.id.btn9);
        minus = findViewById(R.id.btnMinus);
        four = findViewById(R.id.btn4);
        five = findViewById(R.id.btn5);
        six = findViewById(R.id.btn6);
        plus = findViewById(R.id.btnPlus);
        one = findViewById(R.id.btn1);
        two = findViewById(R.id.btn2);
        three = findViewById(R.id.btn3);
        equals = findViewById(R.id.btnEquals);
        dot = findViewById(R.id.btnDot);
        zero = findViewById(R.id.btn0);

        dot.setOnClickListener(v -> {
            if (!result.getText().toString().contains(".")) {
                result.setText(result.getText().toString().concat("."));
                operations.setText(operations.getText().toString().concat("."));
            }
            else if (result.getText().toString().equals("Division by Zero")) {
                result.setText("0");
                operations.setText("0");
            }
            else {
                result.setText(result.getText().toString());
            }
        });
        clear.setOnClickListener(v -> {
            operations.setText("0");
            result.setText("0");
        });
        delete.setOnClickListener(v -> {
            if (result.getText().toString().length() == 1 ) {
                result.setText("0");
            }
            else if (operations.getText().toString().endsWith("= ")) {
                operations.setText("");
            }
            else if (result.getText().toString().equals("Division by Zero")) {
                result.setText("0");
                operations.setText("0");
            }
            else if (operations.getText().toString().equals("")) {
                result.setText("");
            }
            else {
                result.setText(result
                        .getText()
                        .toString()
                        .substring(0, result.getText().toString().length() - 1));
            }
        });
        equals.setOnClickListener(v -> {
            double finalValue;
            if (testEndingWithOperator()) {
               String copy = operations
                       .getText()
                       .toString()
                       .substring(0, operations.getText().toString().length() - 1);
               finalValue = evaluate(operations
                       .getText()
                       .toString()
                       .concat(copy));
               operations.setText(operations.getText().toString().concat(" = "));
               result.setText(formatNumber(String.format("%s", finalValue)));
            }
            else if (operations.getText().toString().endsWith("/0")) {
                operations.setText("");
                result.setText(String.format("%s", "Division by Zero"));
            }
            else if (result.getText().toString().equals("Division by Zero")) {
                result.setText("0");
                operations.setText("0");
            }
            else if (operations.getText().toString().endsWith("= ")) {
                result.setText(result.getText().toString());
            }
            else {
               finalValue = evaluate(operations.getText().toString());
               operations.setText(operations.getText().toString().concat(" = "));
               result.setText(formatNumber(String.format("%s", finalValue)));
            }

        });

        plus.setOnClickListener(v -> operatorLogic("+"));
        minus.setOnClickListener(v -> operatorLogic("-"));
        multiplication.setOnClickListener(v -> operatorLogic("*"));
        division.setOnClickListener(v -> operatorLogic("/"));

        seven.setOnClickListener(v -> numberLogic("7"));
        eight.setOnClickListener(v -> numberLogic("8"));
        nine.setOnClickListener(v -> numberLogic("9"));
        four.setOnClickListener(v -> numberLogic("4"));
        five.setOnClickListener(v -> numberLogic("5"));
        six.setOnClickListener(v -> numberLogic("6"));
        one.setOnClickListener(v -> numberLogic("1"));
        two.setOnClickListener(v -> numberLogic("2"));
        three.setOnClickListener(v -> numberLogic("3"));
        zero.setOnClickListener(v -> numberLogic("0"));
    }

    public void numberLogic(String number) {
        if (result.getText().toString().equals("0") && operations.getText().toString().equals("0")) {
            operations.setText(number);
            result.setText(number);
        }
        else if (testEndingWithOperator()) {
            result.setText(number);
            operations.setText(operations.getText().toString().concat(number));
        }
        else if (result.getText().toString().equals("Division by Zero")) {
            operations.setText(String.format("%s", number));
            result.setText(String.format("%s", number));
        }
        else if (operations.getText().toString().endsWith("= ")) {
            result.setText(number);
            operations.setText(number);
        }
        else {
            operations.setText(operations.getText().toString().concat(number));
            result.setText(result.getText().toString().concat(number));
        }
    }

    public void operatorLogic(String operator) {
        if (testEndingWithOperator()) {
            operations.setText(operations
                    .getText()
                    .toString()
                    .substring(0, operations.getText().toString().length() - 1)
                    .concat(operator));
        }
        else if (operations.getText().toString().endsWith("/0")) {
            operations.setText("");
            result.setText(String.format("%s", "Division by Zero"));
        }
        else if (result.getText().toString().equals("Division by Zero")) {
            operations.setText(String.format("%s", 0).concat(operator));
            result.setText(String.format("%s", 0).concat(operator));
        }
        else if (operations.getText().toString().endsWith("= ")) {
            operations.setText(result.getText().toString().replace(",", "").concat(operator));
            result.setText("");
        }
        else if (containsOperator()) {
            double finalValue = evaluate(operations.getText().toString());
            result.setText(String.format("%s", finalValue));
            operations.setText(String.format("%s", finalValue).concat(operator));
        }
        else {
            operations.setText(operations.getText().toString().concat(operator));
        }
    }

    public double evaluate(String sequence) {
        Expression expression = new ExpressionBuilder(sequence).build();
        return expression.evaluate();
    }

    public boolean testEndingWithOperator() {
        return operations.getText().toString().endsWith("/") ||
                operations.getText().toString().endsWith("*") ||
                operations.getText().toString().endsWith("-") ||
                operations.getText().toString().endsWith("+");
    }

    public boolean containsOperator() {
        return operations.getText().toString().substring(0, operations.getText().toString().length() - 1).contains("/") ||
                operations.getText().toString().substring(0, operations.getText().toString().length() - 1).contains("*") ||
                operations.getText().toString().substring(0, operations.getText().toString().length() - 1).contains("-") ||
                operations.getText().toString().substring(0, operations.getText().toString().length() - 1).contains("+");
    }

    public String formatNumber(String number) {
        double amount = Double.parseDouble(number);
        DecimalFormat formatter = new DecimalFormat("#,###.00");
        return formatter.format(amount);
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveData();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        retrieveData();
    }

    public void saveData() {
        sharing = getSharedPreferences("saveData", MODE_PRIVATE);
        saveOps = operations.getText().toString();
        saveResult = result.getText().toString();

        SharedPreferences.Editor editor = sharing.edit();
        editor.putString("saveOps", saveOps);
        editor.putString("saveResult", saveResult);
        editor.apply();
    }

    public void retrieveData() {
        sharing = getSharedPreferences("saveData", MODE_PRIVATE);
        saveOps = sharing.getString("saveOps", null);
        saveResult = sharing.getString("saveResult", null);
        operations.setText(saveOps);
        result.setText(saveResult);
    }

}