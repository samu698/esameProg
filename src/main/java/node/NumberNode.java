package node;

import math.Rational;

import java.util.Objects;

/**
 * <p>The {@link Node} representing a {@link Rational} number.</p>
 * <p>AF: This represents a rational number in a mathematical expression tree.</p>
 * <p>REQUIREMENTS: value must be non-null.</p>
 * <p>MUTABILITY: This class is immutable, as per {@link Node} requirement.</p>
 * @param value The value of the number.
 */
public record NumberNode(Rational value) implements Node {
	/** A number with value zero */
	public final static NumberNode ZERO = new NumberNode(Rational.ZERO);
	/** A number with value one */
	public final static NumberNode ONE = new NumberNode(Rational.ONE);
	/** A number with value negative one */
	public final static NumberNode NEG_ONE = new NumberNode(Rational.NEG_ONE);

	/**
	 * Constructor for {@link NumberNode}.
	 * <p>REQUIREMENTS: value must be non-null</p>
	 *
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
	public boolean containsVariables() {
		return false;
	}

	@Override
	public int orderPosition() {
		return 0;
	}

	@Override
	public int compareTo(Node o) {
		int order = Integer.compare(this.orderPosition(), o.orderPosition());
		if (order != 0) return order;

		if (o instanceof NumberNode other) {
			return this.value.compareTo(other.value);
		} else {
			// This should never happen, as per orderPosition requirement.
			// If two Nodes have the same orderPosition they must be the same type.
			assert false;
			return 0;
		}
	}

	@Override
	public String toString() {
		return value.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof NumberNode other)) return false;
		return this.value.equals(other.value);
	}

}