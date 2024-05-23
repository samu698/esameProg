package node;

import java.util.Objects;

public record VariableNode(String value) implements Node {
	public VariableNode {
		assert value.matches("^[a-zA-Z]+$");
		Objects.requireNonNull(value);
	}

	@Override
	public <T> T accept(Visitor<T> visitor) {
		return visitor.visit(this);
	}

	@Override
	public int compareTo(Node o) {
		if (o instanceof NumberNode) return 1;

		if (o instanceof VariableNode other) {
			return this.value.compareTo(other.value);
		}

		return -1;
	}

	@Override
	public String toString() {
		return value;
	}
}
