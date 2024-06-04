package node;

import math.Rational;

import java.util.*;

/**
 * <p>The {@link Node} representing a addition operation.
 * <p>
 *     AF: This represents the addition between all the operands.
 *     The order of the operands does not matter because addition is commutative.
 *     Given the list of operands op_n this represents op_1 + op_2 + op_3 ...
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
 * @param operands The operands of the sum.
 */
public record SumNode(List<Node> operands) implements Node {
	/**
	 * Constructor for {@link SumNode}.
	 * <p>
	 *     REQUIREMENTS:
	 *     <ul>
	 *         <li>operands must be non-null</li>
	 *         <li>operands must not contain a null element.</li>
	 *         <li>The size of operands must be greater than one.</li>
	 *     </ul>
	 * @param operands The operands of the addition.
	 */
	public SumNode(List<Node> operands) {
		Objects.requireNonNull(operands);
		assert operands.size() >= 2; // TODO: should this be an exception
		List<Node> sortedOperands = new ArrayList<>(operands);
		Collections.sort(sortedOperands);
		this.operands = Collections.unmodifiableList(sortedOperands);
	}

	/**
	 * Convenience constructor using varargs, allows to avoid calling {@link List#of}.
	 * <p>EFFECT: constructs a {@link SumNode} using the provided operands</p>
	 * <p>
	 *     REQUIREMENTS:
	 *     <ul>
	 *         <li>operands must not contain a null element.</li>
	 *         <li>The size of operands must be greater than one.</li>
	 *     </ul>
	 * @param operands The operands of the addition.
	 * @throws NullPointerException If any of the operands is null.
	 */
	public SumNode(Node... operands) {
		this(List.of(operands));
	}

	/**
	 * <p>
	 *     EFFECT: constructs a subtraction represented as an addition.
	 *     A subtraction A - B - C can be expressed as A + (B * -1) + (C * -1),
	 *     so this method will leave the first operand unchanged and modify the other operands to
	 *     the form (X * -1) and construct a {@link SumNode} of all the operands.
	 * <p>
	 *     REQUIREMENTS:
	 *     <ul>
	 *         <li>operands must be non-null</li>
	 *         <li>operands must not contain a null element.</li>
	 *         <li>The size of operands must be greater than one.</li>
	 *     </ul>
	 * @param operands The operands of the subtraction
	 * @return A subtraction represented using addition.
	 * @throws NullPointerException If any of the operands is null.
	 */
	public static SumNode fromSub(List<Node> operands) {
		assert operands.size() >= 2; // TODO: should this be an exception
		List<Node> sumOperands = new ArrayList<>(operands.size());

		Iterator<Node> iter = operands.iterator();
		// The first operand of a subtraction remains the same
		sumOperands.add(iter.next());

		final Node negOne = new NumberNode(Rational.fromInt(-1));
		while (iter.hasNext()) {
			// Multiply the subtraction terms by -1 to negate them
			sumOperands.add(new MulNode(negOne, iter.next()));
		}

		return new SumNode(sumOperands);
	}

	@Override
	public <T> T transform(Visitor<T> visitor) {
		return visitor.visit(this);
	}

	@Override
	public int compareTo(Node o) {
		if (o instanceof SumNode other) {
			return Arrays.compare(
				this.operands.toArray(Node[]::new),
				other.operands.toArray(Node[]::new)
			);
		} else {
			return 1;
		}
	}

	@Override
	public String toString() {
		return "+";
	}
}
