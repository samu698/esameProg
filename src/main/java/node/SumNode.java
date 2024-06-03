package node;

import math.Rational;

import java.util.*;

// The operands must be sorted
public record SumNode(List<Node> operands) implements Node {
	public SumNode(List<Node> operands) {
		Objects.requireNonNull(operands);
		assert operands.size() >= 2; // TODO: should this be an exception
		List<Node> sortedOperands = new ArrayList<>(operands);
		Collections.sort(sortedOperands);
		this.operands = Collections.unmodifiableList(sortedOperands);
	}

	public SumNode(Node... operands) {
		this(List.of(operands));
	}

	@Override
	public <T> T accept(Visitor<T> visitor) {
		return visitor.visit(this);
	}

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

	public static SumNode fromSub(Node... operands) {
		return fromSub(List.of(operands));
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
