import java.io.*;
import java.io.File;
import java.util.Arrays;
import java.util.Scanner;
import java.lang.Math;


public class Polynomial {
    public double[] coefficients;
    public int[] exponents;

    public Polynomial() {;
	    this.coefficients = new double[0];
        this.exponents = new int[0]; 
    }

    public Polynomial(double[] coefficients, int[] exponents) {;
	    if (coefficients.length != exponents.length) {
            System.out.println("Coefficients and exponents arrays must have the same length");
        }
        this.coefficients = new double[coefficients.length];
        this.exponents = new int[exponents.length];

        int index = 0;
        for (int i = 0; i < coefficients.length; i++) {
            if (coefficients[i] != 0.0) {
                this.coefficients[index] = coefficients[i];
                this.exponents[index] = exponents[i];
                index++;
            }
        }

        this.coefficients = Arrays.copyOf(this.coefficients, index);
        this.exponents = Arrays.copyOf(this.exponents, index);
    }

    public Polynomial(File file) throws FileNotFoundException, IOException {
        this();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();
            parsePoly(line);
        }
    }

    private void parsePoly(String polyString) {
        polyString = polyString.replaceAll("-", "+-");
        String[] terms = polyString.split("\\+");

        for (String term : terms) {
            String[] parts = term.split("x");

            if (parts.length == 2) {
                double coefficient = Double.parseDouble(parts[0]);
                int exponent = Integer.parseInt(parts[1]);
                if (coefficient != 0.0) {
                    addTerm(coefficient, exponent);
                }
            } else if (parts.length == 1) {
                double coefficient = Double.parseDouble(parts[0]);
                if (coefficient != 0.0) {
                    addTerm(coefficient, 0);
                }
            }
        }

    }

    public void addTerm(double coefficient, int exponent) {
        int newSize = coefficients.length + 1;
        coefficients = Arrays.copyOf(coefficients, newSize);
        exponents = Arrays.copyOf(exponents, newSize);
        coefficients[newSize - 1] = coefficient;
        exponents[newSize - 1] = exponent;
    }

    //helper function to find exponent index. indexOf just doesn't work.
    private int findIndex(int[] exponents, int targetExponent) {
        for (int i = 0; i < exponents.length; i++) {
            if (exponents[i] == targetExponent) {
                return i;
            }
        }
        return -1;
    }


    public Polynomial add(Polynomial added) {
        int maxLength = this.coefficients.length + added.coefficients.length;
        double[] newCoefficients = new double[maxLength];
        int[] newExponents = new int[maxLength];
        int index = 0;

        for (int i = 0; i < this.coefficients.length; i++) {
        	newCoefficients[i] += this.coefficients[i];
            newExponents[i] += this.exponents[i];
            index ++;
        }

        for (int i = 0; i < added.coefficients.length; i++) {
            int target = findIndex(newExponents,added.exponents[i]);
        	if (target != -1){
                newCoefficients[target] += added.coefficients[i];
            }else{
                newCoefficients[index] += added.coefficients[i];
                newExponents[index] += added.exponents[i];
                index++;
            } 
        }

        return new Polynomial(newCoefficients, newExponents);
    }
    
    public double evaluate(double x) {
        double result = 0.0;

        for (int i = 0; i < this.coefficients.length; i++) {
            result += coefficients[i] * Math.pow(x, exponents[i]);
        }

        return result;
    }

    public boolean hasRoot(double x) {
        return evaluate(x) == 0.0;
    }



    public Polynomial multiply(Polynomial other) {
        int maxPower = Math.max(Arrays.stream(this.exponents).max().orElse(0), Arrays.stream(other.exponents).max().orElse(0));
        int resultSize = maxPower + 1;
        double[] complexCoefficients = new double[resultSize];
        int[] complexExponents = new int[resultSize];
        double[] resultCoefficients = new double[resultSize];
        int[] resultExponents = new int[resultSize];

        int index = 0;
        for (int i = 0; i < this.coefficients.length; i++) {
            for (int j = 0; j < other.coefficients.length; j++) {
                complexCoefficients[index] = this.coefficients[i] * other.coefficients[j];
                complexExponents[index] = this.exponents[i] + other.exponents[j];
                index++;
            }
        }
        //cut redundance before returning
        
        int kIndex = 0;
        int resultIndex = 0; 
        for (int k : complexExponents){
            int exist = findIndex(resultExponents, k);
            if (exist == -1){
                resultExponents[resultIndex] = k;
                resultCoefficients[resultIndex] += complexCoefficients[kIndex];
                resultIndex++;
            }else{
                resultCoefficients[exist] += complexCoefficients[kIndex];
            }
            kIndex++;
        }

        return new Polynomial(resultCoefficients, resultExponents);
    }

    public String toString() {
        StringBuilder polynomialString = new StringBuilder();
        
        for (int i = 0; i < coefficients.length; i++) {
            double coefficient = coefficients[i];
            int exponent = exponents[i];
            
            if (i != 0 && coefficient >= 0) {
                polynomialString.append("+");
            }
            
            if (exponent == 0) {
                polynomialString.append(coefficient);
            } else {
                polynomialString.append(coefficient).append("x").append(exponent);
            }
        }
        
        return polynomialString.toString();
    }
    
    public void saveToFile(String fileName) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            String polynomialString = this.toString(); // Get the string representation
            writer.write(polynomialString);
        }
    }

}
