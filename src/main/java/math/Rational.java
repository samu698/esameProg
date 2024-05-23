package math;

import java.util.Objects;
import java.util.Optional;
import java.util.Random;

public class Rational implements Comparable<Rational> {
	public final int num;
	public final int den;

	// Partial constructor used internally
	// Invariants:
	// den > 0
	// gcd(num, den) = 1: fraction must be simplified
	private Rational(int num, int den) {
		assert den > 0: "Denominator must be positive";
		assert Utils.gcd(num, den) == 1: "Fraction must be simplified";

		this.num = num;
		this.den = den;
	}

	public static Rational fromNumDen(int num, int den)
		throws IllegalArgumentException
	{
		if (den == 0)
			throw new IllegalArgumentException("Math.Rational number denominator cannot be zero");

		// Assure that denominator sign is positive
		if (den < 0) {
			num = -num;
			den = -den;
		}

		// Simplify
		int gcd = Utils.gcd(num, den);
		num /= gcd;
		den /= gcd;

		return new Rational(num, den);
	}

	public static Rational fromInt(int num) {
		return new Rational(num, 1);
	}

	public boolean isInteger() {
		return this.den == 1;
	}

	public boolean equalInt(int value) {
		return this.den == 1 && this.num == value;
	}

	public Rational reciprocal() {
		// FIXME: handle reciprocal of zero
		// Assure that denominator sign is positive
		if (num < 0) return new Rational(-den, -num);
		return new Rational(den, num);
	}

	public Rational opposite() {
		return new Rational(-num, den);
	}

	public Rational add(Rational other) {
		int den = Utils.lcm(this.den, other.den);
		int num = this.num * (den / this.den);
		num += other.num * (den / other.den);

		// Simplify
		int gcd = Utils.gcd(num, den);
		num /= gcd;
		den /= gcd;

		return new Rational(num, den);
	}

	public Rational mul(Rational other) {
		// Cross simplify
		int gcd1 = Utils.gcd(this.num, other.den);
		int num1 = this.num / gcd1;
		int den2 = other.den / gcd1;

		int gcd2 = Utils.gcd(other.num, this.den);
		int num2 = other.num / gcd2;
		int den1 = this.den / gcd2;

		return new Rational(num1 * num2, den1 * den2);
	}

	public static class PowResult {
		public final Rational rationalPart;
		public final Rational irrationalPart;

		private PowResult(Rational rationalPart, Rational irrationalPart) {
			this.rationalPart = Objects.requireNonNull(rationalPart);
			this.irrationalPart = Objects.requireNonNull(irrationalPart);
		}
	}

	// The expression (n/d)^(e/r)
	// Can be represented as the product of a rational number a and a rational number b to the exponent (e/r)
	// such that (n/d)^(e/r) = a * (b ^ e) ^ (1/r) and a is maximized
	public PowResult pow(Rational exp) {
		Rational base = exp.num >= 0 ? this : this.reciprocal();

		// b^0 = 1 TODO: check b != 0
		if (exp.equalInt(0))
			return new PowResult(fromInt(1), fromInt(1));

		// b^1 = b
		if (exp.equalInt(1))
			return new PowResult(base, fromInt(1));

		// 0^e = 0 TODO check e != 0
		if (base.equalInt(0))
			return new PowResult(fromInt(0), fromInt(1));

		Rational rationalPart;
		Rational irrationalPart;
		if (exp.den != 1) {
			Optional<Integer> numRoot, denRoot;
			if (base.num < 0 && exp.den % 2 == 0) {
				// even root of negative number
				numRoot = Optional.empty();
				denRoot = Optional.empty();
			} else {
				numRoot = Utils.perfectRoot(base.num, exp.den);
				denRoot = Utils.perfectRoot(base.den, exp.den);
			}


			int rationalNum = numRoot.orElse(1);
			int irrationalNum = numRoot.isPresent() ? 1 : base.num;
			int rationalDen = denRoot.orElse(1);
			int irrationalDen = denRoot.isPresent() ? 1 : base.den;

			rationalPart = new Rational(rationalNum, rationalDen);
			irrationalPart = new Rational(irrationalNum, irrationalDen);
		} else {
			rationalPart = base;
			irrationalPart = fromInt(1);
		}

		rationalPart = new Rational(
			Utils.pow(rationalPart.num, Math.abs(exp.num)),
			Utils.pow(rationalPart.den, Math.abs(exp.num))
		);

		return new PowResult(rationalPart, irrationalPart);
	}

	@Override
	public int compareTo(Rational other) {
		Rational diff = this.add(other.opposite());
		return Integer.signum(diff.num);
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