package parse;

import math.Rational;
import node.*;
import node.NumberNode;

import java.util.List;
import java.util.Stack;

// Parser for polish notation
public class Polish {
	public final static String FORMAT_NAME = "Polish notation";

	private Polish() {
		assert false: "Utility class cannot be instantiated";
	}

	private static List<Node> getOperands(Stack<Node> operands, String input)
		throws ParsingExcpetion
	{
		if (operands.size() < 2)
			throw new ParsingExcpetion("Not enough operands", FORMAT_NAME, input);
		return List.of(operands.pop(), operands.pop());
	}

	public static Node parse(String input)
		throws ParsingExcpetion
	{
		// Split into parts using whitespace as delimiter
		String[] parts = input.split("\\s+");

		Stack<Node> operands = new Stack<>();

		// Read the parts in reverse order, like in reverse polish notation
		for (int i = parts.length - 1; i >= 0; i--) {
			final String part = parts[i];

			// Try to parse an operator
			Node innerNode = switch (part) {
				case "+" -> new SumNode(getOperands(operands, input));
				case "-" -> SumNode.fromSub(getOperands(operands, input));
				case "*" -> new MulNode(getOperands(operands, input));
				case "/" -> MulNode.fromDiv(getOperands(operands, input));
				case "^" -> {
					if (operands.size() < 2)
						throw new ParsingExcpetion("Not enough operands", FORMAT_NAME, input);
					yield new PowNode(operands.pop(), operands.pop());
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
				throw new ParsingExcpetion("Invalid number", FORMAT_NAME, input);
			}
		}

		if (operands.isEmpty())
			throw new ParsingExcpetion("Cannot parse empty string", FORMAT_NAME, input);
		if (operands.size() > 1)
			throw new ParsingExcpetion("Too many operands", FORMAT_NAME, input);
		return operands.pop();
	}
}