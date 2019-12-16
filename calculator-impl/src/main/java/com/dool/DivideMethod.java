package com.dool;

import com.dool.met.Method;

public class DivideMethod implements Method {

    @Override
    public double calculate(double a, double b) {
        return a / b;
    }

    @Override
    public String sign() {
        return "/";
    }

}
