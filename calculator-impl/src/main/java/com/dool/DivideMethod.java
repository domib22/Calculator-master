package com.dool;

import com.dool.met.Method;

public class DivideMethod implements Method {

    @Override
    public double calculate(double a, double b) {
        if(b==0){
            throw new IllegalArgumentException("ERROR: Dzielenie przez zero jest niemożliwe!");
        }else {
            return a / b; }
    }

    @Override
    public String sign() {
        return "/";
    }

}
