package print;

import node.*;
import node.NumberNode;

import java.util.List;

public class BasicPrinter implements Visitor<String> {
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
			node.base().accept(this),
			node.exp().toString()
		);
	}

	private void printOperands(List<Node> operands, StringBuilder builder) {
		builder.append("(").append(operands.get(0).accept(this));
		for (int i = 1; i < operands.size(); i++) {
			Node operand = operands.get(i);
			builder.append(", ").append(operand.accept(this));
		}
		builder.append(")");
	}
}