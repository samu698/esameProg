package node;

import math.Rational;

import java.util.*;

/**
 * <p>The {@link Node} representing a multiplication operation.
 * <p>
 *     AF: This represents the multiplication operation between all the operands.
 *     The order of the operands does not matter because multiplication is commutative.
 *     Given the list of operands op_n this represents op_1 * op_2 * op_3 ...
 * <p>
 *     REQUIREMENTS:
 *     <ul>
 *         <li>operands must be non-null.</li>
 *         <li>operands must not contain a null element.</li>
 *         <li>The size of operands must be greater than one.</li>
 *         <li>operands must be sorted using the total order relation defined in {@link Node}.</li>
 *         <li>The operands list must be immutable.</li>
 *     </ul>
 * <p>MUTABILITY: This class is immutable, as per {@link Node} requirement.</p>
 * @param operands The operands of the multiplication.
 */
public record MulNode(List<Node> operands) implements Node {
	/**
	 * Constructor for {@link MulNode}.
	 * <p>
	 *     REQUIREMENTS:
	 *     <ul>
	 *         <li>operands must be non-null</li>
	 *         <li>operands must not contain a null element.</li>
	 *         <li>The size of operands must be greater than one.</li>
	 *     </ul>
	 * @param operands The operands of the multiplication
	 */
	public MulNode(List<Node> operands) {
		Objects.requireNonNull(operands);
		for (Node operand : operands) Objects.requireNonNull(operand);

		assert operands.size() >= 2;

		List<Node> sortedOperands = new ArrayList<>(operands);
		Collections.sort(sortedOperands);
		this.operands = Collections.unmodifiableList(sortedOperands);
	}

	/**
	 * Convenience constructor using varargs, allows to avoid calling {@link List#of}.
	 * <p>EFFECT: constructs a {@link MulNode} using the provided operands</p>
	 * <p>
	 *     REQUIREMENTS:
	 *     <ul>
	 *         <li>operands must not contain a null element.</li>
	 *         <li>The size of operands must be greater than one.</li>
	 *     </ul>
	 * @param operands The operands of the multiplication.
	 * @throws NullPointerException If any of the operands is null.
	 */
	public MulNode(Node... operands)
		throws NullPointerException
	{
		this(List.of(operands));
	}

	/**
	 * <p>
	 *     EFFECT: constructs a division represented as a multiplication.
	 *     A division A / B / C can be expressed as A * (B^-1) * (C^-1),
	 *     so this method will leave the first operand unchanged and modify the other operands to
	 *     the form (X^-1) and construct a {@link MulNode} of all the operands.
	 * <p>
	 *     REQUIREMENTS:
	 *     <ul>
	 *         <li>operands must be non-null</li>
	 *         <li>operands must not contain a null element.</li>
	 *         <li>The size of operands must be greater than one.</li>
	 *     </ul>
	 * @param operands The operands of the division
	 * @return A division represented using multiplication.
	 * @throws NullPointerException If any of the operands is null.
	 */
	public static MulNode fromDiv(List<Node> operands)
		throws NullPointerException
	{
		Objects.requireNonNull(operands);
		for (Node operand : operands) Objects.requireNonNull(operand);

		assert operands.size() >= 2;

		List<Node> mulOperands = new ArrayList<>(operands.size());

		Iterator<Node> iter = operands.iterator();
		// The first operand of a division remains the same
		mulOperands.add(iter.next());

		while (iter.hasNext()) {
			// Multiply the subtraction terms by -1 to negate them
			mulOperands.add(new PowNode(iter.next(), Rational.NEG_ONE));
		}

		return new MulNode(mulOperands);
	}

	@Override
	public <T> T transform(Visitor<T> visitor) {
		return visitor.visit(this);
	}

	@Override
	public boolean containsVariables() {
		for (Node op : this.operands) {
			if (op.containsVariables())
				return true;
		}
		return false;
	}

	@Override
	public int orderPosition() {
		return 3;
	}

	@Override
	public int compareTo(Node o) {
		int order = Integer.compare(this.orderPosition(), o.orderPosition());
		if (order != 0) return order;

		if (o instanceof MulNode other) {
			return Arrays.compare(
				this.operands.toArray(Node[]::new),
				other.operands.toArray(Node[]::new)
			);
		} else {
			// This should never happen, as per orderPosition requirement.
			// If two Nodes have the same orderPosition they must be the same type.
			assert false;
			return 0;
		}
	}

	@Override
	public String toString() {
		return "*";
	}
}
