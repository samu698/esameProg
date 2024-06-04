package node;

import java.util.Objects;

/**
 * <p>The {@link Node} representing a variable in a expression.</p>
 * <p>
 *     AF: This represents a variable in an algebraic expression tree.
 *     The variable is referred to by a name containing only upper or lowercase letters.
 * </p>
 * <p>
 *     REQUIREMENTS:
 *     <ul>
 *         <li>name must be non-null.</li>
 *         <li>name must be not empty.</li>
 *         <li>name must contain only lower or uppercase letters.</li>
 *     </ul>
 * <p>MUTABILITY: This class is immutable, as per {@link Node} requirement.</p>
 * @param name The name of the variable.
 */
public record VariableNode(String name) implements Node {
	/**
	 * Constructor for {@link VariableNode}.
	 * <p>
	 *     REQUIREMENTS:
	 *     <ul>
	 *         <li>name must be non-null.</li>
	 *         <li>name must be not empty.</li>
	 *         <li>name must contain only lower or uppercase letters.</li>
	 *     </ul>
	 * @param name The name of the variable.
	 */
	public VariableNode {
		assert name.matches("^[a-zA-Z]+$");
		Objects.requireNonNull(name);
	}

	@Override
	public <T> T transform(Visitor<T> visitor) {
		return visitor.visit(this);
	}

	@Override
	public int compareTo(Node o) {
		if (o instanceof NumberNode) return 1;

		if (o instanceof VariableNode other) {
			return this.name.compareTo(other.name);
		}

		return -1;
	}

	@Override
	public String toString() {
		return name;
	}
}
