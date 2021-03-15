package com.dool.core;

import com.dool.BasicCalculator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.*;

public class Rpn {
    private BasicCalculator calculator;
    private final Logger LOG = LogManager.getLogger(getClass());

    private String equation;
    private String rpn;

    public Rpn(String equation, BasicCalculator cal) {
        this.calculator = cal;
        this.equation = equation;
        rpn = "";
        toRPN();
    }

    private void toRPN() {

        Stack<String> stackk = new Stack<>();
        StringTokenizer st = new StringTokenizer(equation, "+-*/^%()", true);
        String negative = "0";

        while(st.hasMoreTokens()) {

            String s = st.nextToken();

            if( s.equals("+") || s.equals("-") || s.equals("*") || s.equals("/") || s.equals("^") || s.equals("%"))
            {
                if(negative.equals("+") || negative.equals("-") || negative.equals("*") || negative.equals("/") || negative.equals("^") || negative.equals("%")){
                    throw new IllegalArgumentException("Błędny zapis! (*ujemne liczby zapisuj w nawiasach)");
                }

                if(s.equals("-") && negative.equals("(")){
                    rpn += "0" + " ";
                }

                while(!stackk.empty() && priority(stackk.peek()) >= priority(s)) {
                    rpn += stackk.pop() + " ";
                }
                stackk.push(s);
            }

            else if(s.equals("(")) {
                stackk.push(s);
            }

            else if(s.equals(")")) {

                while(!stackk.peek().equals("(")) {
                    rpn += stackk.pop() + " ";
                }
                stackk.pop();
            }
            else {
                rpn += s + " ";
            }
            negative = s;
        }

        while(!stackk.empty()) {
            rpn += stackk.pop() + " ";
        }
    }

    public static int priority(String operator) {

        if(operator.equals("+") || operator.equals("-")) {return 1;}
        else if(operator.equals("*") || operator.equals("/") || operator.equals("%")) {return 2;}
        else if(operator.equals("^")){return 3;}

        else {return 0;}
    }

    public @Override String toString(){
        return rpn;
    }

    public double calRPN() {

        String input = rpn +" =";

        Stack<Double> stackk = new Stack<>();

        double a;
        double b;
        double w = 0;

        String build = "";
        char sp = ' ';

        int counter = 0;

        do {
            char sign = input.charAt(counter);

            if(sign == '+' || sign == '-' || sign == '*' || sign == '/' || sign == '^' || sign == '%')
            {
                if(!stackk.empty()){
                    LOG.info("Stack: " + stackk);

                    b = stackk.pop();
                    LOG.info("b = {}", b);

                    if(!stackk.empty()) {
                        a = stackk.pop();
                        LOG.info("a = {}", a);

                        if (sign == '+') {
                            w = calculator.result("+", a, b);
                        } else if (sign == '-') {
                            w = calculator.result("-", a, b);
                        } else if (sign == '*') {
                            w = calculator.result("*", a, b);
                        } else if (sign == '/') {
                            w = calculator.result("/", a, b);
                        } else if (sign == '^') {
                            w = calculator.result("^", a, b);
                        } else {   // sign == '%'
                            w = calculator.result("%", a, b);
                        }
                    } else {
                        w = b;
                    }

                    stackk.push(w);
                }
            }

            else if(sign == sp) {

                if(build.compareTo("")!=0){
                    double tmp = Double.parseDouble(build);
                    stackk.push(tmp);
                    build = "";
                }
            }

            else if(sign=='=') {

                if(!stackk.empty()){
                    w = stackk.pop();
                    break;
                }
            }

            else if(sign == '.' || (sign >= '0' && sign <= '9')) {
                build += sign;
            }

            counter++;

        } while(counter < input.length());

        return w;
    }
}