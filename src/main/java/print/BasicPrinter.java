package print;

import node.*;
import node.NumberNode;

import java.util.List;

/**
 * <p>A printer that prints a {@link Node} in linearized format.</p>
 * <p>REQUIREMENTS: see {@link Visitor} for the requirements.</p>
 * <p>MUTABILITY: This class has no state and it cannot mutate, respecting the mutability requirements of {@link Visitor}.</p>
 */
public class BasicPrinter implements Visitor<String> {
	/** Constructor for {@link BasicPrinter} */
	public BasicPrinter() {}

	@Override
	public String visit(NumberNode node) {
		return node.toString();
	}

	@Override
	public String visit(VariableNode node) {
		return node.toString();
	}

	@Override
	public String visit(SumNode node) {
		StringBuilder builder = new StringBuilder(node.toString());
		printOperands(node.operands(), builder);
		return builder.toString();
	}

	@Override
	public String visit(MulNode node) {
		StringBuilder builder = new StringBuilder(node.toString());
		printOperands(node.operands(), builder);
		return builder.toString();
	}

	@Override
	public String visit(PowNode node) {
		return String.format(
			"%s(%s, %s)",
			node.toString(),
			node.base().transform(this),
			node.exp().toString()
		);
	}

	/**
	 * <p>
	 *     EFFECTS: appends the printed operands to the builder.
	 *     The operands are printed in a comma delimited list enclosed by parenthesis.
	 * <p>REQUIREMENTS: The parameters must be non-null</p>
	 * <p>NOTES: This method modifies the provided builder</p>
	 * @param operands The operands to print.
	 * @param builder The build to append the result to.
	 */
	private void printOperands(List<Node> operands, StringBuilder builder) {
		assert operands != null;
		assert builder != null;

		builder.append("(").append(operands.get(0).transform(this));
		for (int i = 1; i < operands.size(); i++) {
			Node operand = operands.get(i);
			builder.append(", ").append(operand.transform(this));
		}
		builder.append(")");
	}
}