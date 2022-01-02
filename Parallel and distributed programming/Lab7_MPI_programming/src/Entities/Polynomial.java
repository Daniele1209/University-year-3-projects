package Entities;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public final class Polynomial implements Serializable {
    private final int degree;
    // List of Integers for coefficients
    private final List<Integer> coefficient_list;

    public Polynomial(List<Integer> coefficient_list) {
        this.coefficient_list = coefficient_list;
        this.degree = coefficient_list.size() - 1;
    }

    public List<Integer> getCoefficients() {
        return coefficient_list;
    }

    public int getLength() {
        return this.coefficient_list.size();
    }

    public int getDegree() {
        return degree;
    }

    public Polynomial(int degree) {
        this.degree = degree;
        coefficient_list = new ArrayList<>(degree + 1);

        coeffGeneration();
    }

    private void coeffGeneration() {
        Random r = new Random();
        for (int i = 0; i < degree; i++) {
            coefficient_list.add(r.nextInt(10));
        }
        coefficient_list.add(r.nextInt(10) + 1);
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

    public static Polynomial add(Polynomial pol1, Polynomial pol2) {
        // Used to add the two polynomials
        
        int minPolDegree = Math.min(pol1.getDegree(), pol2.getDegree());
        int maxPolDegree = Math.max(pol1.getDegree(), pol2.getDegree());
        List<Integer> coefficient_array = new ArrayList<>(maxPolDegree + 1);
        for (int idx = 0; idx <= minPolDegree; idx++) {
            coefficient_array.add(pol1.getCoefficients().get(idx) + pol2.getCoefficients().get(idx));
        }

        // Add the rest of the coefficients
        if (minPolDegree != maxPolDegree) {
            if (maxPolDegree == pol1.getDegree()) {
                for (int idx = minPolDegree + 1; idx <= maxPolDegree; idx++) {
                    coefficient_array.add(pol1.getCoefficients().get(idx));
                }
            } else {
                for (int idx = minPolDegree + 1; idx <= maxPolDegree; idx++) {
                    coefficient_array.add(pol2.getCoefficients().get(idx));
                }
            }
        }
        return new Polynomial(coefficient_array);
    }


    public static Polynomial subtract(Polynomial pol1, Polynomial pol2) {
        // Used to subtract the two polynomial
        int minPolDegree = Math.min(pol1.getDegree(), pol2.getDegree());
        int maxPolDegree = Math.max(pol1.getDegree(), pol2.getDegree());
        List<Integer> coefficient_array = new ArrayList<>(maxPolDegree + 1);
        for (int idx = 0; idx <= minPolDegree; idx++) {
            coefficient_array.add(pol1.getCoefficients().get(idx) - pol2.getCoefficients().get(idx));
        }

        // Add the rest of the coefficients
        if (minPolDegree != maxPolDegree) {
            if (maxPolDegree == pol1.getDegree()) {
                for (int idx = minPolDegree + 1; idx <= maxPolDegree; idx++) {
                    coefficient_array.add(pol1.getCoefficients().get(idx));
                }
            } else {
                for (int idx = minPolDegree + 1; idx <= maxPolDegree; idx++) {
                    coefficient_array.add(pol2.getCoefficients().get(idx));
                }
            }
        }

        // Check if coeff is 0 and remove it if needed
        int idx = coefficient_array.size() - 1;
        while (coefficient_array.get(idx) == 0 && idx > 0) {
            coefficient_array.remove(idx);
            idx--;
        }

        return new Polynomial(coefficient_array);
    }

    public static Polynomial buildEmptyPolynomial(int degree){
        List<Integer> zeros = IntStream.range(0, degree).mapToObj(i -> 0).collect(Collectors.toList());
        return new Polynomial(zeros);
    }

    public static Polynomial addZeroValues(Polynomial p, int offset) {
        List<Integer> coefficients = IntStream.range(0, offset).mapToObj(i -> 0).collect(Collectors.toList());
        coefficients.addAll(p.getCoefficients());
        return new Polynomial(coefficients);
    }
}
