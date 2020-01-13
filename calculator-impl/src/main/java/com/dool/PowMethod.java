package com.dool;

import com.dool.met.Method;

public class PowMethod implements Method {

    @Override
    public double calculate(double a, double b) {
        return Math.pow(a, b);
    }

    @Override
    public String sign() {
        return "^";
    }
}
