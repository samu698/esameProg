package node;

import math.Rational;
import transform.Simplify;

import java.util.Objects;

public record PowNode(Node base, Rational exp) implements Node {
	public PowNode(Node base, Rational exp) {
		this.base = Objects.requireNonNull(base);
		this.exp = Objects.requireNonNull(exp);
	}

	public PowNode(Node base, Node exp)
		throws IllegalArgumentException
	{
		this(base, simplifyToRational(exp));
	}

	private static Rational simplifyToRational(Node exp)
		throws IllegalArgumentException
	{
		if (exp.transform(new Simplify()) instanceof NumberNode numExp) {
			return numExp.value();
		} else {
			throw new IllegalArgumentException("Exponent cannot be converted to a rational number");
		}
	}


	@Override
	public <T> T transform(Visitor<T> visitor) {
		return visitor.visit(this);
	}

	@Override
	public int compareTo(Node o) {
		if (o instanceof NumberNode) return 1;
		if (o instanceof VariableNode) return 1;

		if (o instanceof PowNode other) {
			int cmp = this.base.compareTo(other.base);
			if (cmp != 0) return cmp;
			return this.exp.compareTo(other.exp);
		}

		return -1;
	}

	@Override
	public String toString() {
		return "^";
	}
}
