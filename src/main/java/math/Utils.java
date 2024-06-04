package math;

import java.util.Optional;

/** Utility class for basic mathematical operations */
public class Utils {
	/** Constructor to the utility class that must never be called */
	private Utils() {
		assert false: "Utility class cannot be instantiated";
	}

	/**
	 * <p>EFFECTS: Computes the gcd between a and b.</p>
	 * <p>NOTES: This is an implementation of the <a href="https://en.wikipedia.org/wiki/Binary_GCD_algorithm">binary GCD algorithm</a></p>
	 * @param a The first operand of the gcd.
	 * @param b The second operand of the gcd.
	 * @return The gcd of a and b.
	 * @see <a href="https://en.wikipedia.org/wiki/Greatest_common_divisor">GCD</a>
	 * @see <a href="https://en.wikipedia.org/wiki/Binary_GCD_algorithm">Binary GCD algorithm</a>
	 */
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

	/**
	 * <p>EFFECTS: Computes the lcm between a and b.</p>
	 * <p>NOTES: This method uses the {@link Utils#gcd(long, long)} method.</p>
	 * @param a The first operand of the lcm.
	 * @param b The second operand of the lcm.
	 * @return The lcm of a and b.
	 * @see <a href="https://en.wikipedia.org/wiki/Least_common_multiple">LCM</a>
	 */
	public static long lcm(long a, long b) {
		// Remove common factors from the product of a and b
		long lcm = a * b / gcd(a, b);
		// Assure positive sign
		return lcm >= 0 ? lcm : -lcm;
	}

	/**
	 * <p>EFFECTS: Computes the result of base^exp, where exp &gt; 0.</p>
	 * <p>REQUIREMENTS: exp > 0.</p>
	 * <p>NOTES: This is an implementation of Knuth's binary exponentiation algorithm (TAOCP Vol 2: 4.6.3).</p>
	 * @param base The base of the power.
	 * @param exp The exponent.
	 * @return The result of base^exp
	 * @throws IllegalArgumentException If exp &lt; 0.
	 */
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

	/**
	 * <p>EFFECTS: Calculates if a the degree-th root of radicand is perfect and if it is returns the root.</p>
	 * <p>
	 *     REQUIREMENTS:
	 *     <ul>
	 *         <li>The degree must be positive and not zero.</li>
	 *         <li>The radicand must be non-negative if the degree is even.</li>
	 *     </ul>
	 * @param radicand The radicand of the root.
	 * @param degree The degree of the root.
	 * @return And {@link Optional} that contains the root if it is perfect.
	 * @throws IllegalArgumentException If any of the requirements is not satisfied.
	 */
	public static Optional<Long> perfectRoot(long radicand, long degree)
		throws IllegalArgumentException
	{
		boolean negative = radicand < 0;
		if (negative) radicand = -radicand;

		if (degree <= 0)
			throw new IllegalArgumentException("Invalid root degree");
		if (negative && degree % 2 == 0)
			throw new IllegalArgumentException("Even roots of negative numbers are not real");

		// Calculate the approximate root using floating point math.
		long root = Math.round(Math.pow(radicand, 1.0 / degree));

		// Check if approximate root is a perfect root.
		if (pow(root, degree) == radicand) return Optional.of(negative ? -root : root);

		return Optional.empty();
	}
}