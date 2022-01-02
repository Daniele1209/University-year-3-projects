package Entities;

import java.util.List;
import java.util.ArrayList;

public class Operation {

    public static Polynomial multiplicationOperation(Object obj1, Object obj2, int begin, int end) {
        Polynomial pol1 = (Polynomial) obj1;
        Polynomial pol2 = (Polynomial) obj2;
        Polynomial result = Polynomial.buildEmptyPolynomial(pol1.getDegree()*2 + 1);

        for (int idx = begin; idx < end; idx++) {
            for (int idx2 = 0; idx2 < pol2.getCoefficients().size(); idx2++) {
                result.getCoefficients().set(idx + idx2, result.getCoefficients().get(idx + idx2) + pol1.getCoefficients().get(idx) * pol2.getCoefficients().get(idx2));
            }
        }
        return result;
    }

    public static Polynomial buildResult(Object[] results) {
        int degree = ((Polynomial) results[0]).getDegree();
        Polynomial result = Polynomial.buildEmptyPolynomial(degree+1);
        for (int i = 0; i < result.getCoefficients().size(); i++) {
            for (Object o : results) {
                result.getCoefficients().set(i, result.getCoefficients().get(i) + ((Polynomial) o).getCoefficients().get(i));
            }
        }
        return result;
    }

    // Simple Threaded case
    public static Polynomial simpleThreaded(Polynomial pol1, Polynomial pol2) {
        int coeffListLength  = pol1.getDegree() + pol2.getDegree() + 1;
        List<Integer> coefficient_array = new ArrayList<>();

        for (int idx = 0; idx < coeffListLength; idx++) {
            coefficient_array.add(0);
        }

        for (int idx = 0; idx < pol1.getCoefficients().size(); idx++) {
            for (int idx2 = 0; idx2 < pol2.getCoefficients().size(); idx2++) {
                int index = idx + idx2;
                int value = pol1.getCoefficients().get(idx) * pol2.getCoefficients().get(idx2);
                coefficient_array.set(index, coefficient_array.get(index) + value);
            }
        }

        return new Polynomial(coefficient_array);
    }

    // Karatsuba Threaded case
    public static Polynomial karatsubaThreaded(Polynomial pol1, Polynomial pol2) {
        // We do a simpleSequential if the degree is smaller than 2
        if (pol1.getDegree() < 2 || pol2.getDegree() < 2) {
            return simpleThreaded(pol1, pol2);
        }

        int length = Math.max(pol1.getDegree(), pol2.getDegree()) / 2;

        Polynomial firstpol1 = new Polynomial(pol1.getCoefficients().subList(0, length));
        Polynomial firstpol2 = new Polynomial(pol2.getCoefficients().subList(0, length));
        Polynomial secondpol1 = new Polynomial(pol1.getCoefficients().subList(length, pol1.getLength()));
        Polynomial secondpol2 = new Polynomial(pol2.getCoefficients().subList(length, pol2.getLength()));

        // Recursively calculate the three products
        Polynomial prod1 = simpleThreaded(firstpol1, firstpol2);
        Polynomial prod2 = simpleThreaded(Polynomial.add(firstpol1, secondpol1), Polynomial.add(firstpol2, secondpol2));
        Polynomial prod3 = simpleThreaded(secondpol1, secondpol2);

        return getFinalResult(prod1, prod2, prod3, length);
    }

    public static Polynomial getFinalResult(Polynomial prod1, Polynomial prod2, Polynomial prod3, int length) {
        // Combine results using formula P1*(1<<(2*sh)) + (P3 - P1 - P2)*(1<<sh) + P2;
        Polynomial first_result = Polynomial.addZeroValues(prod3, 2 * length);
        Polynomial second_result = Polynomial.addZeroValues(Polynomial.subtract(Polynomial.subtract(prod2, prod3), prod1), length);
        return Polynomial.add(Polynomial.add(first_result, second_result), prod1);
    }
}
