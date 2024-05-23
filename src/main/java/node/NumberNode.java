package node;

import math.Rational;

import java.util.Objects;

public record NumberNode(Rational value) implements Node {
	public NumberNode {
		Objects.requireNonNull(value);
	}

	@Override
	public <T> T accept(Visitor<T> visitor) {
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