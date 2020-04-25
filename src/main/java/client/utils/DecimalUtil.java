package client.utils;

import javax.xml.bind.annotation.XmlType;
import java.math.BigDecimal;

public class DecimalUtil {

    public enum Calculate {
        ADD, SUB, MULTI, DIV
    }

    public static float Calc(float num1, float num2, Calculate calc) {
        BigDecimal value1 = new BigDecimal(Float.toString(num1));
        BigDecimal value2 = new BigDecimal(Float.toString(num2));

        switch (calc) {
            case ADD:
                return value1.add(value2).floatValue();
            case SUB:
                return value1.subtract(value2).floatValue();
            case MULTI:
                return value1.multiply(value2).floatValue();
            case DIV:
                return value1.divide(value2).floatValue();
        }

        return value1.add(value2).floatValue();
    }

    public static float Calc(float num1, double num2, Calculate calc) {
        BigDecimal value1 = new BigDecimal(Float.toString(num1));
        BigDecimal value2 = new BigDecimal(Double.toString(num2));

        switch (calc) {
            case ADD:
                return value1.add(value2).floatValue();
            case SUB:
                return value1.subtract(value2).floatValue();
            case MULTI:
                return value1.multiply(value2).floatValue();
            case DIV:
                return value1.divide(value2).floatValue();
        }

        return value1.add(value2).floatValue();
    }

    public static double Calc(double num1, double num2, Calculate calc) {
        BigDecimal value1 = new BigDecimal(Double.toString(num1));
        BigDecimal value2 = new BigDecimal(Double.toString(num2));

        switch (calc) {
            case ADD:
                return value1.add(value2).doubleValue();
            case SUB:
                return value1.subtract(value2).doubleValue();
            case MULTI:
                return value1.multiply(value2).doubleValue();
            case DIV:
                return value1.divide(value2).doubleValue();
        }

        return value1.add(value2).doubleValue();
    }

    public static double Calc(double num1, float num2, Calculate calc) {
        BigDecimal value1 = new BigDecimal(Double.toString(num1));
        BigDecimal value2 = new BigDecimal(Float.toString(num2));

        switch (calc) {
            case ADD:
                return value1.add(value2).doubleValue();
            case SUB:
                return value1.subtract(value2).doubleValue();
            case MULTI:
                return value1.multiply(value2).doubleValue();
            case DIV:
                return value1.divide(value2).doubleValue();
        }

        return value1.add(value2).doubleValue();
    }
}
