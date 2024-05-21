package Etude12;

/**
 * The HarmoniousNumbers class is responsible for finding and printing pairs of harmonious numbers.
 * A pair of numbers (a, b) is considered harmonious if the sum of the proper divisors of a is equal to b,
 * and the sum of the proper divisors of b is equal to a, where a and b are positive integers.
 */
public class HarmoniousNumbers {

    /**
    * Calculates the sum of the proper divisors of a given number.
    * 
    * @param n the number for which to calculate the sum of proper divisors
    * @return the sum of the proper divisors of the given number
    */
    public static int getDivisorSum(int n) {
        int sum = 0; // for amicable numbers, change this to 1 (adding 1 to the sum)
        double param = Math.sqrt(n);
        for (int i = 2; i <= param; i++) {
            if (n % i == 0)
                if (n / i == i) {
                    sum += i;
                } else {
                    sum += (i + n / i);
                }
        }
        return sum;
    }

    public static void main(String[] args) {
        for (int i = 1; i <= 2000000; i++) { // only does up to 2000000 for now
            int a = getDivisorSum(i);
            if (a > i) {
                int b = getDivisorSum(a);
                if (b == i && b != a) {
                    System.out.println(b + " " + a);
                }
            }
        }
    } 
}
