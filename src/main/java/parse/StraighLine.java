package parse;

import math.Rational;
import node.*;
import node.NumberNode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ListIterator;

public class StraighLine {
	public final static String FORMAT_NAME = "Straight line";

	private final List<Node> previusExpressions;

	public StraighLine() {
		this.previusExpressions = new ArrayList<>();
	}

	public Node parse(String input)
		throws ParsingExcpetion
	{
		Node parseResult = parseImpl(input);
		// If an exception is thrown, the list of previous expressions is not updated
		this.previusExpressions.add(parseResult);
		return parseResult;
	}

	public Node getLast()
		throws IndexOutOfBoundsException
	{
		if (previusExpressions.isEmpty())
			throw new IndexOutOfBoundsException("Cannot get last expression, parser is empty");

		return previusExpressions.get(previusExpressions.size() - 1);
	}

	private Node parseImpl(String input)
		throws ParsingExcpetion
	{
		// Split line into parts, trim the input to remove possible trailing newlines
		String[] parts = input.trim().split("\\s+");

		if (parts.length == 0)
			throw new ParsingExcpetion("Invalid empty string", FORMAT_NAME, input);
		if (parts[0].length() != 1)
			throw new ParsingExcpetion("First part must be a dot or an operator", FORMAT_NAME, input);

		char operatorChar = parts[0].charAt(0);
		String[] arguments = Arrays.copyOfRange(parts, 1, parts.length);

		// Expression is a leaf
		if (operatorChar == '.') {
			if (arguments.length != 1)
				throw new ParsingExcpetion("Invalid number of arguments after a dot", FORMAT_NAME, input);

			// Argument is a variable
			if (arguments[0].matches("^[a-zA-Z]+$"))
				return new VariableNode(arguments[0]);

			// Argument must be a number
			try {
				long value = Long.parseLong(arguments[0]);
				return new NumberNode(Rational.fromInt(value));
			} catch (NumberFormatException e) {
				throw new ParsingExcpetion("Invalid syntax", FORMAT_NAME, input);
			}
		}

		// Expression is an operation
		// Read all the indices
		List<Node> operands = new ArrayList<>(arguments.length);
		for (String argument : arguments) {
			try {
				int index = Integer.parseInt(argument);
				if (index < 0)
					throw new ParsingExcpetion("Invalid negative index", FORMAT_NAME, input);
				if (index >= this.previusExpressions.size())
					throw new ParsingExcpetion("Index out of bounds", FORMAT_NAME, input);
				operands.add(this.previusExpressions.get(index));
			} catch (NumberFormatException e) {
				throw new ParsingExcpetion("Invalid syntax", FORMAT_NAME, input);
			}
		}

		return switch (operatorChar) {
			case '+' -> new SumNode(operands);
			case '-' -> SumNode.fromSub(operands);
			case '*' -> new MulNode(operands);
			case '/' -> MulNode.fromDiv(operands);
			case '^' -> makePowNode(operands);
			default -> throw new ParsingExcpetion("First part must be dot or an operator", FORMAT_NAME, input);
		};
	}

	// Create nested pow node using right associativity
	// TODO: should also simplify the exponent
	private Node makePowNode(List<Node> operands) {
		ListIterator<Node> iter = operands.listIterator();
		// Advance to the end of the iterator
		while (iter.hasNext()) iter.next();
		Node rhs = iter.previous();
		Node pow = new PowNode(iter.previous(), rhs);
		while (iter.hasPrevious()) pow = new PowNode(iter.previous(), pow);
		return pow;
	}
}