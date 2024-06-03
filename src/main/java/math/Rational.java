package math;

import java.util.Objects;
import java.util.Optional;
import java.util.Random;

public class Rational implements Comparable<Rational> {
	public final long num;
	public final long den;

	// Partial constructor used internally
	// Invariants:
	// den > 0
	// gcd(num, den) = 1: fraction must be simplified
	private Rational(long num, long den) {
		assert den > 0: "Denominator must be positive";
		assert Utils.gcd(num, den) == 1: "Fraction must be simplified";

		this.num = num;
		this.den = den;
	}

	public static Rational fromNumDen(long num, long den)
		throws IllegalArgumentException
	{
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

	public static Rational fromInt(long num) {
		return new Rational(num, 1);
	}

	public boolean isInteger() {
		return this.den == 1;
	}

	public boolean equalInt(long value) {
		return this.den == 1 && this.num == value;
	}

	public Rational reciprocal()
		throws IllegalArgumentException
	{
		if (num == 0)
			throw new IllegalArgumentException("Reciprocal of zero is undefined");

		if (num < 0) return new Rational(-den, -num);
		return new Rational(den, num);
	}

	public Rational opposite() {
		return new Rational(-num, den);
	}

	public Rational add(Rational other) {
		long den = Utils.lcm(this.den, other.den);
		long num = this.num * (den / this.den);
		num += other.num * (den / other.den);

		// Simplify
		long gcd = Utils.gcd(num, den);
		num /= gcd;
		den /= gcd;

		return new Rational(num, den);
	}

	public Rational mul(Rational other) {
		// Cross simplify
		long gcd1 = Utils.gcd(this.num, other.den);
		long num1 = this.num / gcd1;
		long den2 = other.den / gcd1;

		long gcd2 = Utils.gcd(other.num, this.den);
		long num2 = other.num / gcd2;
		long den1 = this.den / gcd2;

		return new Rational(num1 * num2, den1 * den2);
	}

	public static class PowResult {
		public final Rational rationalPart;
		public final Rational irrationalPart;
		public final Rational irrationalExp;

		private PowResult(Rational rationalPart, Rational irrationalPart, Rational irrationalExp) {
			this.rationalPart = Objects.requireNonNull(rationalPart);
			this.irrationalPart = Objects.requireNonNull(irrationalPart);
			this.irrationalExp = Objects.requireNonNull(irrationalExp);
		}
	}

	// The expression (n/d)^(e/r)
	// Can be represented as the product of a rational number a and a rational number b to the exponent (e/r)
	// such that (n/d)^(e/r) = a * b ^ (e/r) and a is maximized
	public PowResult pow(Rational exp) {
		// b^0 = 1
		if (exp.equalInt(0))
			return new PowResult(fromInt(1), fromInt(1), fromInt(1));

		// b^1 = b
		if (exp.equalInt(1))
			return new PowResult(this, fromInt(1), fromInt(1));

		// Ignore complex results
		if (exp.den % 2 == 0 && this.num < 0)
			return new PowResult(fromInt(1), this, exp);

		// Remove the negative sign from the exponent if the exponent is negative
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

		if (numRoot.isPresent()) rationalNum = numRoot.get();
		else irrationalNum = base.num;

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