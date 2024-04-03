package br.com.victorkk.calculator;

public class Calculator {

    public Double sum(Double n1, Double n2) {
        return n1+n2;
    }

    public Double subtraction(Double n1, Double n2) {
        return n1-n2;
    }

    public Double division(Double n1, Double n2) {
        return n1/n2;
    }

    public Double multiplication(Double n1, Double n2) {
        return n1*n2;
    }

    public Double mean(Double n1, Double n2) {
        return (n1+n2)/2;
    }

    public Double sqrt(Double n) {
        return (Double) Math.sqrt(n);
    }
}
