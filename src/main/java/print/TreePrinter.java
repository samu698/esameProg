package print;

import node.*;
import node.NumberNode;

import java.util.List;

/**
 * <p>A printer that prints a {@link Node} in a visual tree format.</p>
 * <p>
 *     REQUIREMENTS:
 *     <ul>
 *         <li>prefix must be non-null</li>
 *         <li>childPrefix must be non-null</li>
 *         <li>see {@link Visitor} for the additional requirements.</li>
 *     </ul>
 * <p>MUTABILITY: This class is mutable, but the state must reset after calling apply on a node as per {@link Visitor} requirements</p>
 * <p>
 *     NOTES:
 *     <ul>
 *         <li>The tree is printed using the <a href="https://en.wikipedia.org/wiki/Box-drawing_characters">Box drawing characters</a>.</li>
 *         <li>This is adapted from <a href="https://stackoverflow.com/questions/4965335/how-to-print-binary-tree-diagram-in-java">Stackoverflow</a>.</li>
 *     </ul>
 */
public class TreePrinter implements Visitor<String> {
	/** String to put in front of an expression. */
	private static final String EXPR =      "├── ";
	/** String to continue the line of the expression. */
	private static final String CONT =      "│   ";
	/** String to put in front of the last expression. */
	private static final String LAST_EXPR = "╰── ";
	/** String to continue after the last expression. */
	private static final String LAST_CONT = "    ";

	/** The prefix to use to print the current node */
	private String prefix;
	/** The prefix to use to print the child node */
	private String childPrefix;

	/**
	 * <p>EFFECTS: Constructs a new {@link TreePrinter}</p>
	 */
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
		builder.append(node.base().transform(this));

		// Reset the state, as it was before calling this method.
		// Necessary to uphold the reset requirement.
		this.prefix = currentPrefix;
		this.childPrefix = currentChildPrefix;

		builder
			.append(this.childPrefix)
			.append(LAST_EXPR)
			.append(node.exp().toString())
			.append('\n');


		return builder.toString();
	}

	/**
	 * <p>EFFECTS: print the operands of node in tree fashion and appends the result to the builder.</p>
	 * <p>REQUIREMENTS: The parameters must be non-null</p>
	 * @param operands The operands to print.
	 * @param builder The build to append the result to.
	 */
	private void printOperands(List<Node> operands, StringBuilder builder) {
		assert operands != null;
		assert builder != null;

		final String currentPrefix = this.prefix;
		final String currentChildPrefix = this.childPrefix;

		this.prefix = currentChildPrefix + EXPR;
		this.childPrefix = currentChildPrefix + CONT;
		for (int i = 0; i < operands.size() - 1; i++) {
			builder.append(operands.get(i).transform(this));
		}

		this.prefix = currentChildPrefix + LAST_EXPR;
		this.childPrefix = currentChildPrefix + LAST_CONT;
		final Node lastOperand = operands.get(operands.size() - 1);
		builder.append(lastOperand.transform(this));

		// Reset the state, as it was before calling this method.
		// Necessary to uphold the reset requirement.
		this.prefix = currentPrefix;
		this.childPrefix = currentChildPrefix;
	}
}