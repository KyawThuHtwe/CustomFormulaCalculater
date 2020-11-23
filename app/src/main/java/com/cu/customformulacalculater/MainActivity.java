package com.cu.customformulacalculater;

import androidx.appcompat.app.AppCompatActivity;
import de.congrace.exp4j.Calculable;
import de.congrace.exp4j.ExpressionBuilder;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    LinearLayout container;
    TextView tv;
    EditText etFormula;
    List<EditText> etInputs;
    String formula;
    Map<String,Double> values;
    ArrayList<String> variables;
    private final String[] functions = new String[]{"abs", "acos", "asin", "atan", "cbrt", "ceil", "cos", "cosh", "exp", "floor", "log", "sin", "sinh", "sqrt", "tan", "tanh"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv=findViewById(R.id.tv);
        etFormula=findViewById(R.id.etFormula);
        container=findViewById(R.id.container);
        values=new HashMap<>();
        etInputs=new ArrayList<>();
    }
    public void startClick(View v){
        formula=etFormula.getText().toString();
        variables=getVariableArray(formula);
        etInputs.clear();
        container.removeAllViews();
        for(int i=0;i<variables.size();i++){
            addEditText(container,variables.get(i));
        }
    }
    public void calculateClick(View v){
        values=new HashMap<>();
        for(int i=0;i<etInputs.size();i++){
            values.put(variables.get(i),Double.parseDouble(etInputs.get(i).getText().toString()));
        }
        try{
            Calculable calc=new ExpressionBuilder(formula)
                    .withVariables(values)
                    .build();
            double result=calc.calculate();
            tv.setText("Result = "+result);
        }catch(Exception e){
            tv.setText(e.toString());
        }
    }
    public void addEditText(LinearLayout container, String hint){
        EditText et=new EditText(container.getContext());
        //et.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
        et.setHint(hint);
        container.addView(et,new ViewGroup.LayoutParams(-1,-2));
        etInputs.add(et);
    }
    public ArrayList<String> getVariableArray(String formula) {
        ArrayList<String> variableNames = new ArrayList<>();
        String tmpWord = "";
        for (int i = 0; i < formula.length(); i++) {
            char currentChar = formula.charAt(i);
            if (Character.isLetter(currentChar)) {
                tmpWord = new StringBuilder(String.valueOf(tmpWord)).append(currentChar).toString();
            } else {
                if (tmpWord != "") {
                    addVariableToList(variableNames, tmpWord);
                }
                tmpWord = "";
            }
            if (i == formula.length() - 1 && tmpWord != "") {
                addVariableToList(variableNames, tmpWord);
            }
        }
        return variableNames;
    }
    private void addVariableToList(ArrayList<String> variableNames, String variable) {
        if (!isKeyword(variable) && !variableAlreadyAdded(variableNames, variable)) {
            variableNames.add(variable);
        }
    }
    private boolean isKeyword(String word) {
        for (String item : functions) {
            if (item.equals(word)) {
                return true;
            }
        }
        return false;
    }
    private static boolean variableAlreadyAdded(ArrayList<String> variableNames, String variable) {
        if (variableNames.contains(variable)) {
            return true;
        }
        return false;
    }
}