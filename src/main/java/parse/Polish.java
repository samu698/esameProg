package parse;

import math.Rational;
import node.*;
import node.NumberNode;

import java.util.List;
import java.util.Objects;
import java.util.Stack;

/** Utility class for polish notation parser functionality. */
public class Polish {
	/**
	 * The name of the format.
	 * This is used when constructing a {@link ParsingException}.
	 */
	public final static String FORMAT_NAME = "Polish notation";

	/** Constructor to the utility class that must never be called. */
	private Polish() {
		assert false: "Utility class cannot be instantiated";
	}

	/**
	 * <p>EFFECTS: Parses a polish notation expression to a {@link Node}.</p>
	 * <p>REQUIREMENTS: Input must be non-null.</p>
	 * @param input The input string to parse.
	 * @return A {@link Node} containing the parsed expression.
	 * @throws ParsingException If the passed expression is invalid.
	 * @throws NullPointerException If input is null.
	 * @see <a href="https://en.wikipedia.org/wiki/Polish_notation">Polish notation on Wikipedia</a>
	 */
	public static Node parse(String input)
		throws ParsingException, NullPointerException
	{
		Objects.requireNonNull(input);

		// Split into parts using whitespace as delimiter
		String[] parts = input.split("\\s+");

		Stack<Node> operands = new Stack<>();

		// Read the parts in reverse order, like in reverse polish notation
		for (int i = parts.length - 1; i >= 0; i--) {
			final String part = parts[i];

			// Try to parse an operator
			Node innerNode = switch (part) {
				case "+" -> new SumNode(getOperands(operands));
				case "-" -> SumNode.fromSub(getOperands(operands));
				case "*" -> new MulNode(getOperands(operands));
				case "/" -> MulNode.fromDiv(getOperands(operands));
				case "^" -> {
					List<Node> ops = getOperands(operands);
					yield new PowNode(ops.get(0), ops.get(1));
				}
				default -> null;
			};

			if (innerNode != null) {
				operands.push(innerNode);
				continue;
			}

			// Try to parse a variable
			if (part.matches("^[a-zA-Z]+$")) {
				operands.push(new VariableNode(part));
				continue;
			}

			// Try to parse a number
			try {
				long value = Long.parseLong(part);
				Rational rat = Rational.fromInt(value);
				operands.push(new NumberNode(rat));
			} catch (NumberFormatException e) {
				throw new ParsingException("Invalid number", FORMAT_NAME);
			}
		}

		if (operands.isEmpty())
			throw new ParsingException("Cannot parse empty string", FORMAT_NAME);
		if (operands.size() > 1)
			throw new ParsingException("Too many operands", FORMAT_NAME);
		return operands.pop();
	}

	/**
	 * <p>EFFECTS: Retrieves the last two operands.</p>
	 * <p>
	 *     REQUIREMENTS:
	 *     <ul>
	 *         <li>operands must be non-null.</li>
	 *     </ul>
	 * <p>MUTABILITY: This method mutates the operands parameter.</p>
	 * @param operands The operands to read.
	 * @return A list of the last two operands.
	 * @throws ParsingException If operands contains less than two operands.
	 */
	private static List<Node> getOperands(Stack<Node> operands)
		throws ParsingException
	{
		if (operands.size() < 2)
			throw new ParsingException("Not enough operands", FORMAT_NAME);
		return List.of(operands.pop(), operands.pop());
	}
}