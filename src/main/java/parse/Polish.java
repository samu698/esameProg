package parse;

import math.Rational;
import node.*;
import node.NumberNode;

import java.util.List;
import java.util.Stack;

// Parser for polish notation
public class Polish {
	private Polish() {
		assert false: "Utility class cannot be instantiated";
	}

	private static List<Node> getOperands(Stack<Node> operands)
		throws ParsingExcpetion
	{
		assert operands.size() >= 2: "Make this an exception";
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
				case "+" -> new SumNode(getOperands(operands));
				case "-" -> SumNode.fromSub(getOperands(operands));
				case "*" -> new MulNode(getOperands(operands));
				case "/" -> MulNode.fromDiv(getOperands(operands));
				case "^" -> {
					assert operands.size() >= 2: "Make this an exception";
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
				int value = Integer.parseInt(part);
				Rational rat = Rational.fromInt(value);
				operands.push(new NumberNode(rat));
			} catch (NumberFormatException e) {
				// TODO: throw parse exception
			}
		}

		assert operands.size() == 1; // FIXME: Throw parse exception
		return operands.pop();
	}
}