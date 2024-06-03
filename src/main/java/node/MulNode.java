package node;

import math.Rational;

import java.util.*;

public record MulNode(List<Node> operands) implements Node {
	public MulNode(List<Node> operands) {
		Objects.requireNonNull(operands);
		assert operands.size() >= 2;
		List<Node> sortedOperands = new ArrayList<>(operands);
		Collections.sort(sortedOperands);
		this.operands = Collections.unmodifiableList(sortedOperands);
	}

	public MulNode(Node... operands) {
		this(List.of(operands));
	}

	@Override
	public <T> T accept(Visitor<T> visitor) {
		return visitor.visit(this);
	}

	public static MulNode fromDiv(List<Node> operands) {
		assert operands.size() >= 2;
		List<Node> mulOperands = new ArrayList<>(operands.size());

		Iterator<Node> iter = operands.iterator();
		// The first operand of a division remains the same
		mulOperands.add(iter.next());

		final Node negOne = new NumberNode(Rational.fromInt(-1));
		while (iter.hasNext()) {
			// Multiply the subtraction terms by -1 to negate them
			mulOperands.add(new PowNode(iter.next(), negOne));
		}

		return new MulNode(mulOperands);
	}

	public static MulNode fromDiv(Node... operands) {
		return fromDiv(List.of(operands));
	}

	@Override
	public int compareTo(Node o) {
		if (o instanceof NumberNode) return 1;
		if (o instanceof VariableNode) return 1;
		if (o instanceof PowNode) return 1;

		if (o instanceof MulNode other) {
			return Arrays.compare(
				this.operands.toArray(Node[]::new),
				other.operands.toArray(Node[]::new)
			);
		}

		return -1;
	}

	@Override
	public String toString() {
		return "*";
	}
}
