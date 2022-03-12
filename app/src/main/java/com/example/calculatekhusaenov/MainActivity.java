package com.example.calculatekhusaenov;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;


public class MainActivity extends AppCompatActivity implements calculateOperation {
    public  static Map map;
    TextView resultField; // текстовое поле для вывода результата
    EditText numberField;   // поле для ввода числа
    TextView operationField;    // текстовое поле для вывода знака операции
    Double operand = null;  // операнд операции
    String lastOperation = "="; // последняя операция
    static boolean testNumberResult;
    boolean chetnostNumber;
    static {
        map = new HashMap<>();
        map.put("+","add");
        map.put("=","equal");
        map.put("/","division");
        map.put("*","multiplication");
        map.put("^","degree");
        map.put("sin","sin");
        map.put("четность","parity");
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_fragment);
        // получаем все поля по id из activity_main.xml
        resultField = findViewById(R.id.resultField);
        numberField = findViewById(R.id.numberField);
        operationField = findViewById(R.id.operationField);
    }
    // сохранение состояния
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("OPERATION", lastOperation);
        if(operand!=null)
            outState.putDouble("OPERAND", operand);
        super.onSaveInstanceState(outState);
    }
    // получение ранее сохраненного состояния
    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        lastOperation = savedInstanceState.getString("OPERATION");
        operand= savedInstanceState.getDouble("OPERAND");
        resultField.setText(operand.toString());
        operationField.setText(lastOperation);
    }
    // обработка нажатия на числовую кнопку
    public void onNumberClick(View view){

        Button button = (Button)view;
        numberField.append(button.getText());

        if(lastOperation.equals("=") && operand!=null){
            operand = null;
        }
    }
    // обработка нажатия на кнопку операции
    public void onOperationClick(View view){

        Button button = (Button)view;
        String op = button.getText().toString();
        String number = numberField.getText().toString();
        // если введенно что-нибудь
        if(number.length()>0){
            number = number.replace(',', '.');
            try{
                performOperation(Double.valueOf(number), op);
            }catch (NumberFormatException | ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException ex){
                numberField.setText("");
            }
        }
        lastOperation = op;
        operationField.setText(lastOperation);
    }
    private void performOperation(Double number, String operation) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        // если операнд ранее не был установлен (при вводе самой первой операции)
        map = new HashMap<>();
        map.put("+","add");
        map.put("=","equal");
        map.put("/","division");
        map.put("*","multiplication");
        map.put("^","degree");
        map.put("sin","sin");
        map.put("четность","parity");

        if(operand ==null){
            operand = number;
        }
        else{
            if(lastOperation.equals("=")){
                testNumberResult = true;
                lastOperation = operation;
            }
            Class<?> cls = Class.forName("MainActivity");
            Object obj = cls.newInstance();
            Method option = MainActivity.class.getMethod((String) (Objects.requireNonNull(map.get(lastOperation))),Double.class);
            operand = (Double) option.invoke(obj, number);
            System.out.println("String");
            System.out.println(option.invoke(obj, number)instanceof String);
            System.out.println("Double");
            System.out.println(option.invoke(obj, number)instanceof Double);

            System.out.println(operand);
//            switch(lastOperation){
//                case "=":
//                    operand =number;
//                    testNumberResult = true;
//                    break;
//                case "/":
//                    testNumberResult = true;
//                    if(number==0){
//                        operand =0.0;
//                    }
//                    else{
//                        operand /=number;
//                    }
//                    break;
//                case "*":
//                    testNumberResult = true;
//                    operand *=number;
//                    break;
//                case "+":
//                    testNumberResult = true;
//                    operand +=number;
//                    break;
//                case "-":
//                    testNumberResult = true;
//                    operand -=number;
//                    break;
//                case "^":
//                    testNumberResult = true;
//                    for (int i = 1; i < number; i++) {
//                        operand *= number;
//                    }
//                    break;
//                case "sin":
//                    testNumberResult = true;
//                    operand =Math.sin(number);
//                    break;
//                case "четность":
//                    testNumberResult = false;
//                    chetnostNumber = number % 2 == 0;
//                    break;
//            }
        }
        if (testNumberResult){
            System.out.println(operand+"-----------------------------------------------------------------------------------");
            resultField.setText(operand.toString().replace('.', ','));
            numberField.setText("");
        }else{
            System.out.println(operand+"-----------------------------------------------------------------------------------");
            resultField.setText(String.valueOf(chetnostNumber));
            numberField.setText("");
        }
    }

    @Override
    public Double add(Double number) {
        testNumberResult = true;
        operand +=number;
        System.out.println("+");
        return operand;
    }

    @Override
    public Double subtraction(Double number) {
        testNumberResult = true;
        operand -=number;
        return operand;
    }

    @Override
    public Double equal(Double number) {
        operand =number;
        testNumberResult = true;
        return operand;
    }

    @Override
    public Double division(Double number) {
        testNumberResult = true;
        if(number==0){
            operand =0.0;
        }
        else{
            operand /=number;
        }
        return operand;
    }

    @Override
    public Double multiplication(Double number) {
        testNumberResult = true;
        operand *=number;
        return operand;
    }

    @Override
    public Double degree(Double number) {
        testNumberResult = true;
        for (int i = 1; i < number; i++) {
            operand *= number;
        }
        return operand;
    }

    @Override
    public Double sin(Double number) {
        testNumberResult = true;
        operand =Math.sin(number);
        return operand;
    }

    @Override
    public Double parity(Double number) {
        testNumberResult = false;
        chetnostNumber = number % 2 == 0;
        return operand;
    }
}