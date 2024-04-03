package br.com.victorkk.calculator;

public class NumberProcessor {

    public static Double convertToDouble(String numberStr) {
        numberStr = numberStr.replaceAll(",", ".");
        return Double.parseDouble(numberStr);
    }

    public static boolean notNumeric(String numberStr) {
        numberStr = numberStr.replaceAll(",", ".");
        return !numberStr.matches("[-+]?[0-9]*\\.?[0-9]+");
    }
}
