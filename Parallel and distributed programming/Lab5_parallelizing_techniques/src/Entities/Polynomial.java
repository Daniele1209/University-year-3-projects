package Entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class Polynomial {
    private final int degree;
    // List of Integers for coefficients
    private final List<Integer> coefficient_list;

    public Polynomial(List<Integer> coeff) {
        this.degree = coeff.size() - 1;
        this.coefficient_list = coeff;
    }

    public Polynomial(int degree) {
        this.degree = degree;
        coefficient_list = new ArrayList<>(degree + 1);
        coeffGeneration();
    }

    public int getLength() {
        return this.coefficient_list.size();
    }

    public List<Integer> getCoefficients() {
        return coefficient_list;
    }

    public int getDegree() {
        return degree;
    }

    private void coeffGeneration() {
        Random rand = new Random();
        for (int i = 0; i < degree; i++) {
            coefficient_list.add(rand.nextInt(10));
        }
        coefficient_list.add(rand.nextInt(10) + 1);
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        int pow = 0;
        for (int idx = 0; idx <= this.degree; idx++) {
            if (coefficient_list.get(idx) == 0) {
                pow++;
                continue;
            }
            // Building the polynomial as a string
            str.append(" ").append(coefficient_list.get(idx)).append("x^").append(pow).append(" +");
            pow++;
        }
        // We have a '+' that we need to delete at the end
        str.deleteCharAt(str.length() - 1);

        return str.toString();
    }

    public static Polynomial add(Polynomial p1, Polynomial p2) {
        // Used to add the two polynomials
        int minPolDegree = Math.min(p1.getDegree(), p2.getDegree());
        int maxPolDegree = Math.max(p1.getDegree(), p2.getDegree());
        List<Integer> coefficients_list = new ArrayList<>(maxPolDegree + 1);
        for (int idx = 0; idx <= minPolDegree; idx++) {
            coefficients_list.add(p1.getCoefficients().get(idx) + p2.getCoefficients().get(idx));
        }
        // Add the rest of the coefficients
        if (minPolDegree != maxPolDegree) {
            if (maxPolDegree == p1.getDegree()) {
                for (int idx = minPolDegree + 1; idx <= maxPolDegree; idx++) {
                    coefficients_list.add(p1.getCoefficients().get(idx));
                }
            } else {
                for (int idx = minPolDegree + 1; idx <= maxPolDegree; idx++) {
                    coefficients_list.add(p2.getCoefficients().get(idx));
                }
            }
        }
        return new Polynomial(coefficients_list);
    }


    public static Polynomial subtract(Polynomial p1, Polynomial p2) {
        // Used to subtract the two polynomials
        int minPolDegree = Math.min(p1.getDegree(), p2.getDegree());
        int maxPolDegree = Math.max(p1.getDegree(), p2.getDegree());
        List<Integer> coefficients_list = new ArrayList<>(maxPolDegree + 1);
        for (int idx = 0; idx <= minPolDegree; idx++) {
            coefficients_list.add(p1.getCoefficients().get(idx) - p2.getCoefficients().get(idx));
        }
        // Add the rest of the coefficients
        if (minPolDegree != maxPolDegree) {
            if (maxPolDegree == p1.getDegree()) {
                for (int idx = minPolDegree + 1; idx <= maxPolDegree; idx++) {
                    coefficients_list.add(p1.getCoefficients().get(idx));
                }
            } else {
                for (int idx = minPolDegree + 1; idx <= maxPolDegree; idx++) {
                    coefficients_list.add(p2.getCoefficients().get(idx));
                }
            }
        }
        // Check if coeff is 0 and remove it if needed
        int idx = coefficients_list.size() - 1;
        while (coefficients_list.get(idx) == 0 && idx > 0) {
            coefficients_list.remove(idx);
            idx--;
        }
        return new Polynomial(coefficients_list);
    }

    public static Polynomial addZeroValues(Polynomial p, int offset) {
        List<Integer> coefficients_list = IntStream.range(0, offset).mapToObj(i -> 0).collect(Collectors.toList());
        coefficients_list.addAll(p.getCoefficients());
        return new Polynomial(coefficients_list);
    }

}
