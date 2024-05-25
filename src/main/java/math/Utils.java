package math;

import java.util.Optional;

public class Utils {
	private Utils() {
		assert false: "Utility class cannot be instantiated";
	}

	// Implementation of the binary gcd algorithm
	// Adapted from https://en.wikipedia.org/wiki/Binary_GCD_algorithm
	public static long gcd(long a, long b) {
		// Get the absolute value for both operands
		// gcd(a, b) = gcd(+-a, +-b)
		a = a >= 0 ? a : -a;
		b = b >= 0 ? b : -b;

		// gcd(0, n) = gcd(n, 0) = n
		if (a == 0) return b;
		if (b == 0) return a;

		// gcd(2^i * a, 2^j * b) = 2^k * gcd(a, b)
		// Remove factors of two in a and b
		int i = Long.numberOfTrailingZeros(a);
		int j = Long.numberOfTrailingZeros(b);
		a >>= i;
		b >>= j;
		// Calculate how many factor of two a and b have in common
		int k = Integer.min(i, j);

		for (;;) {
			assert a % 2 == 1: "a must be odd now";
			assert b % 2 == 1: "b must be odd now";

			if (a > b) {
				long temp = b;
				b = a;
				a = temp;
			}

			// gcd(a, b) = gcd(a, b - a)
			b -= a;
			// b is even now
			// odd - odd = even

			// gcd(n, 0) = n
			if (b == 0) {
				// Shift the number back to satisfy this equality
				// gcd(2^i * a, 2^j * b) = 2^k * gcd(a, b)
				return a << k;
			}

			// because b is even we can remove at least one factor of 2
			b >>= Long.numberOfTrailingZeros(b);
		}
	}

	// Calculate least common multiple
	public static long lcm(long a, long b) {
		// Remove common factors from the product of a and b
		long lcm = a * b / gcd(a, b);
		// Assure positive sign
		return lcm >= 0 ? lcm : -lcm;
	}

	// Evaluates base^exp where exp > 0
	// Implementation of Knuth's binary exponentiation algorithm (TAOCP Vol 2: 4.6.3)
	public static long pow(long base, long exp)
		throws IllegalArgumentException
	{
		if (exp < 0)
			throw new IllegalArgumentException("Exponent cannot be negative");

		long result = 1;
		while (exp != 0) {
			if (exp % 2 == 1) result *= base;
			exp /= 2;
			base *= base;
		}

		return result;
	}

	public static Optional<Long> perfectRoot(long radicand, long degree)
		throws IllegalArgumentException
	{
		boolean negative = radicand < 0;
		if (negative) radicand = -radicand;

		if (degree <= 0)
			throw new IllegalArgumentException("Invalid root degree");
		if (negative && degree % 2 == 0)
			throw new IllegalArgumentException("Even roots of negative numbers are not real");

		long root = Math.round(Math.pow(radicand, 1.0 / degree));
		if (pow(root, degree) == radicand) return Optional.of(negative ? -root : root);
		return Optional.empty();
	}
}