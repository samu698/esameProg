package math;

import java.util.Objects;
import java.util.Optional;
import java.util.Random;

/**
 * <p>A rational number.</p>
 * <p>
 *     AF: This class represents a simplified rational number,
 *     composed of a numerator and a denominator,
 *     where the sign of the number is stored in the numerator.
 * </p>
 * <p>
 *     REQUIREMENTS:
 *     <ul>
 *         <li>The denominator must be positive.</li>
 *         <li>The denominator must not be zero.</li>
 *         <li>gcd(numerator, denominator) == 1 (must be simplified).</li>
 *         <li>Unchecked: the numerator nor the denominator must not overflow.</li>
 *     </ul>
 * <p>MUTABILITY: This class is immutable.</p>
 * <p>NOTES: Handling overflows would add to much complexity to this class, so the caller must verify that it doesn't happen.</p>
 */
public class Rational implements Comparable<Rational> {
	/** Numerator of the rational */
	public final long num;
	/** Denominator of the rational */
	public final long den;

	/**
	 * <p>Partial private constructor of rational.</p>
	 * <p>EFFECTS: Constructs a new {@link Rational}</p>
	 * <p>REQUIREMENTS: The arguments must respect the invariants of the class.</p>
	 * @param num The numerator of the rational.
	 * @param den The denominator of the rational.
	 */
	private Rational(long num, long den) {
		assert den != 0: "Denominator cannot be zero";
		assert den > 0: "Denominator must be positive";
		assert Utils.gcd(num, den) == 1: "Fraction must be simplified";

		this.num = num;
		this.den = den;
	}

	/**
	 * <p>Factory method for a {@link Rational}</p>
	 * <p>
	 *     EFFECTS: Constructs a new {@link Rational} after validating and normalizing the numerator and denominator.
	 *     A rational number is considered to be normalized if it respects the invariants of {@link Rational}.
	 * </p>
	 * <p>
	 *     REQUIREMENTS:
	 *     <ul>
	 *         <li>den must not be zero.</li>
	 *         <li>Unchecked: den must not be {@link Long#MIN_VALUE}. (will cause overflow when negated)</li>
	 *         <li>Unchecked: num must not be {@link Long#MIN_VALUE} if den &lt; 0. (will cause overflow when negated)</li>
	 *     </ul>
	 * @param num The numerator of the rational
	 * @param den The denominator of the rational
	 * @return A new instance of {@link Rational}
	 * @throws IllegalArgumentException If den == 0
	 */
	public static Rational fromNumDen(long num, long den)
		throws IllegalArgumentException
	{
		assert den != Long.MIN_VALUE: "This value will overflow when negated";
		assert num != Long.MIN_VALUE || den > 0: "This value will overflow when negated";

		if (den == 0)
			throw new IllegalArgumentException("rational number denominator cannot be zero");

		// Assure that denominator sign is positive
		if (den < 0) {
			num = -num;
			den = -den;
		}

		// Simplify
		long gcd = Utils.gcd(num, den);
		num /= gcd;
		den /= gcd;

		return new Rational(num, den);
	}

	/**
	 * <p>Factory method for an integer {@link Rational}</p>
	 * <p>EFFECTS: Constructs a new integer {@link Rational}</p>
	 * <p>REQUIREMENTS: None (all values are valid)</p>
	 * @param value The value of the integer
	 * @return The new integer {@link Rational}
	 */
	public static Rational fromInt(long value) {
		return new Rational(value, 1);
	}

	/**
	 * <p>EFFECTS: Check if the rational number is an integer.</p>
	 * <p>REQUIREMENTS: None.</p>
	 * @return true if the rational is an integer.
	 */
	public boolean isInteger() {
		return this.den == 1;
	}

	/**
	 * <p>EFFECTS: Check if the rational is equal to an integer.</p>
	 * <p>REQUIREMENTS: None.</p>
	 * @param value The integer to compare against.
	 * @return true if the rational number is equal to the provided integer.
	 */
	public boolean equalInt(long value) {
		return this.den == 1 && this.num == value;
	}

	/**
	 * <p>EFFECTS: Computes the reciprocal of the number.</p>
	 * <p>REQUIREMENTS: The value of the rational must not be zero.</p>
	 * @return The reciprocal of the number.
	 * @throws IllegalArgumentException If this rational is equal to zero.
	 */
	public Rational reciprocal()
		throws IllegalArgumentException
	{
		if (num == 0)
			throw new IllegalArgumentException("Reciprocal of zero is undefined");

		if (num < 0) return new Rational(-den, -num);
		return new Rational(den, num);
	}

	/**
	 * <p>EFFECTS: Computes the opposite of the number.</p>
	 * @return The opposite of the number.
	 */
	public Rational opposite() {
		return new Rational(-num, den);
	}

	/**
	 * <p>EFFECTS: Computes the sum of the two rationals.</p>
	 * @param other The rhs operand to sum.
	 * @return The sum of this and other.
	 * @throws NullPointerException If other is null.
	 */
	public Rational add(Rational other)
		throws NullPointerException
	{
		Objects.requireNonNull(other);
		long den = Utils.lcm(this.den, other.den);
		long num = this.num * (den / this.den);
		num += other.num * (den / other.den);

		// Simplify
		long gcd = Utils.gcd(num, den);
		num /= gcd;
		den /= gcd;

		// Here the den is guaranteed to be positive because the lcm function always returns a positive number
		// And the gcd(num, den) must be equal to one, after the simplify step
		return new Rational(num, den);
	}

	/**
	 * <p>EFFECTS: Computes the product of the two rationals.</p>
	 * @param other The rhs operand of the multiplication.
	 * @return The product of this and other.
	 * @throws NullPointerException If other is null.
	 */
	public Rational mul(Rational other)
		throws NullPointerException
	{
		Objects.requireNonNull(other);
		// Simplify in a cross pattern
		long gcd1 = Utils.gcd(this.num, other.den);
		long num1 = this.num / gcd1;
		long den2 = other.den / gcd1;

		long gcd2 = Utils.gcd(other.num, this.den);
		long num2 = other.num / gcd2;
		long den1 = this.den / gcd2;

		// den is guaranteed to be positive because both den1 and den2 are positive.
		// And gcd(num1 * num2, den1 * den2) must be equal to one, because in the previous steps all the common factor have been removed.
		return new Rational(num1 * num2, den1 * den2);
	}

	/**
	 * <p>A result of pow between two rationals.</p>
	 * <p>
	 *     AF: The result of the operation b^e where both b and e are rational.<br>
	 *     The result of b^e is expressed in the form b^e = R * I^E, where:
	 *     <ul>
	 *         <li>R is the rationalPart, the maximum rational coefficient of the value b^e.</li>
	 *         <li>I is the irrationalPart, the part of the base that cannot be expressed as a rational.</li>
	 *         <li>E is the irrationalExp, the exponent of I in the form of [+-]1/x.</li>
	 *     </ul>
	 * <p>
	 *     REQUIREMENTS:
	 *     <ul>
	 *         <li>All the rational components of the result must be in the rationalPart.</li>
	 *         <li>The irrationalExp must be in the form of [+-]1/x.</li>
	 *     </ul>
	 * <p>MUTABILITY: This class is immutable.</p>
	 * <p>NOTES: This class cannot be a record because the constructor needs to be private.</p>
	 * @see Rational#pow(Rational)
	 */
	public static class PowResult {
		/** The rational part R */
		public final Rational rationalPart;
		/** The irrational part I */
		public final Rational irrationalPart;
		/** The irrational exponent E */
		public final Rational irrationalExp;

		/**
		 * <p>Partial constructor for a PowResult.</p>
		 * <p>
		 *     REQUIREMENTS:
		 *     <ul>
		 *         <li>All of the class invariants must be respected.</li>
		 *         <li>This constructor should only be called by {@link Rational#pow(Rational)}.</li>
		 *         <li>All the parameters must be not null.</li>
		 *     </ul>
		 * <p>NOTES: The invariants {@link Rational#pow(Rational)} must uphold the invariants.</p>
		 * @param rationalPart The rational part R.
		 * @param irrationalPart The irrational part I.
		 * @param irrationalExp The irrational exponent E.
		 * @throws NullPointerException If any of the parameters is null.
		 */
		private PowResult(Rational rationalPart, Rational irrationalPart, Rational irrationalExp)
			throws NullPointerException
		{
			this.rationalPart = Objects.requireNonNull(rationalPart);
			this.irrationalPart = Objects.requireNonNull(irrationalPart);
			this.irrationalExp = Objects.requireNonNull(irrationalExp);
		}
	}

	/**
	 * <p>EFFECTS: Compute the result of the pow of two rationals in the form of a {@link PowResult}</p>
	 * <p>
	 *     REQUIREMENTS:
	 *     <ul>
	 *         <li>exp must be non-null</li>
	 *         <li>this and exp cannot both be equal to 0</li>
	 *     </ul>
	 * @param exp The exponent of the power.
	 * @return The result in thje form of a {@link PowResult}
	 * @throws NullPointerException If exp is null.
	 */
	public PowResult pow(Rational exp)
		throws NullPointerException
	{
		Objects.requireNonNull(exp);

		// b^0 = 1 TODO: check 0^0
		if (exp.equalInt(0))
			return new PowResult(fromInt(1), fromInt(1), fromInt(1));

		// b^1 = b
		if (exp.equalInt(1))
			return new PowResult(this, fromInt(1), fromInt(1));

		// Ignore complex results
		if (exp.den % 2 == 0 && this.num < 0)
			return new PowResult(fromInt(1), this, exp);

		// Remove the negative sign from the exponent if the exponent is negative
		// by computing the reciprocal of the base
		final Rational base;
		if (exp.num < 0) {
			exp = exp.opposite();
			base = this.reciprocal();
		} else {
			base = this;
		}

		// If the numerator or the denominator are perfect roots they can be extracted to the rational part
		Optional<Long> numRoot = Utils.perfectRoot(base.num, exp.den);
		Optional<Long> denRoot = Utils.perfectRoot(base.den, exp.den);

		long rationalNum = 1, rationalDen = 1;
		long irrationalNum = 1, irrationalDen = 1;

		// Extract the numerator to the rational part, if possible
		if (numRoot.isPresent()) rationalNum = numRoot.get();
		else irrationalNum = base.num;

		// Extract the denominator to the rational part, if possible
		if (denRoot.isPresent()) rationalDen = denRoot.get();
		else irrationalDen = base.den;

		long exponent = exp.num >= 0 ? exp.num : -exp.num;
		Rational rationalPart = fromNumDen(
			Utils.pow(rationalNum, exponent),
			Utils.pow(rationalDen, exponent)
		);

		Rational irrationalPart = fromNumDen(
			Utils.pow(irrationalNum, exponent),
			Utils.pow(irrationalDen, exponent)
		);

		// Remove the numerator for the irrationalExp.
		Rational irrationalExp = new Rational(Long.signum(exp.num), exp.den);

		return new PowResult(rationalPart, irrationalPart, irrationalExp);
	}

	@Override
	public int compareTo(Rational other) {
		Rational diff = this.add(other.opposite());
		return Long.signum(diff.num);
	}

	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		result.append(this.num);
		if (!this.isInteger())
			result.append('/').append(this.den);
		return result.toString();
	}

	@Override
	public boolean equals(Object other) {
		if (this == other) return true;
		if (other instanceof Rational o) {
			return this.num == o.num && this.den == o.den;
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return Objects.hash(num, den);
	}
}