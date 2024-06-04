package node;

import math.Rational;

import java.util.Objects;

/**
 * <p>The {@link Node} representing a {@link Rational} number.</p>
 * <p>AF: This represents a rational number in a mathematical expression tree.</p>
 * <p>REQUIREMENTS: value must be non-null.</p>
 * <p>MUTABILITY: This class is immutable, as per {@link Node} requirement.</p>
 * @param value The rational value of the number.
 */
public record NumberNode(Rational value) implements Node {
	/**
	 * Constructor for {@link NumberNode}.
	 * <p>REQUIREMENTS: value must be non-null</p>
	 * @param value The rational value of the number.
	 */
	public NumberNode {
		Objects.requireNonNull(value);
	}

	@Override
	public <T> T transform(Visitor<T> visitor) {
		return visitor.visit(this);
	}

	@Override
	public int compareTo(Node o) {
		if (o instanceof NumberNode other) {
			return this.value.compareTo(other.value);
		}
		// A number is lower than any other type
		return -1;
	}

	@Override
	public String toString() {
		return value.toString();
	}
}