package com.dool;

import com.dool.cal.Calculator;
import com.dool.met.Method;

import java.util.HashMap;
import java.util.Map;

public class BasicCalculator implements Calculator {
    private Map<String, Method> data2 = new HashMap<>();

    public BasicCalculator(){
        data2.put("+", new AddMethod());
        data2.put("-", new SubtractMethod());
        data2.put("*", new MultiplyMethod());
        data2.put("/", new DivideMethod());
        data2.put("^", new PowMethod());
    }

    public void add(Method m){
        data2.put(m.sign(), m);
    }

    public double result(String operator, double a, double b) {
        return data2.get(operator).calculate(a,b);
    }
}
