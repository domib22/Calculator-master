package com.dool;

import com.dool.cal.Calculator;
import com.dool.met.Method;

import java.util.HashMap;
import java.util.Map;

public class BasicCalculator implements Calculator {
    private Map<String, Method> data2 = new HashMap<>();

    public BasicCalculator(){
        add(new AddMethod());
        add(new DivideMethod());
        add(new MultiplyMethod());
        add(new SubtractMethod());
    }

    public void add(Method m){
        data2.put(m.sign(), m);
    }

    public double result(String operator, double a, double b) {
        return data2.get(operator).calculate(a,b);
    }
}
