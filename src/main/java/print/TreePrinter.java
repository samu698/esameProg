package print;

import node.*;
import node.NumberNode;

import java.util.List;

public class TreePrinter implements Visitor<String> {
	private static final String EXPR =      "├── ";
	private static final String CONT =      "│   ";
	private static final String LAST_EXPR = "╰── ";
	private static final String LAST_CONT = "    ";

	private String prefix;
	private String childPrefix;

	public TreePrinter() {
		this.prefix = "";
		this.childPrefix = "";
	}

	@Override
	public String visit(NumberNode node) {
		return prefix + node.toString() + "\n";
	}

	@Override
	public String visit(VariableNode node) {
		return prefix + node.toString() + "\n";
	}

	@Override
	public String visit(SumNode node) {
		StringBuilder builder = new StringBuilder();
		builder.append(prefix).append(node.toString()).append('\n');
		printOperands(node.operands(), builder);
		return builder.toString();
	}

	@Override
	public String visit(MulNode node) {
		StringBuilder builder = new StringBuilder();
		builder.append(prefix).append(node.toString()).append('\n');
		printOperands(node.operands(), builder);
		return builder.toString();
	}

	@Override
	public String visit(PowNode node) {
		StringBuilder builder = new StringBuilder();
		builder.append(prefix).append(node.toString()).append('\n');

		final String currentPrefix = this.prefix;
		final String currentChildPrefix = this.childPrefix;

		this.prefix = currentChildPrefix + EXPR;
		this.childPrefix = currentChildPrefix + CONT;
		builder.append(node.base().accept(this));

		this.prefix = currentPrefix;
		this.childPrefix = currentChildPrefix;

		builder.append(childPrefix).append(LAST_EXPR).append(node.exp().toString()).append('\n');

		return builder.toString();
	}

	private void printOperands(List<Node> operands, StringBuilder builder) {
		final String currentPrefix = this.prefix;
		final String currentChildPrefix = this.childPrefix;

		this.prefix = currentChildPrefix + EXPR;
		this.childPrefix = currentChildPrefix + CONT;
		for (int i = 0; i < operands.size() - 1; i++) {
			builder.append(operands.get(i).accept(this));
		}

		this.prefix = currentChildPrefix + LAST_EXPR;
		this.childPrefix = currentChildPrefix + LAST_CONT;
		final Node lastOperand = operands.get(operands.size() - 1);
		builder.append(lastOperand.accept(this));

		this.prefix = currentPrefix;
		this.childPrefix = currentChildPrefix;
	}
}