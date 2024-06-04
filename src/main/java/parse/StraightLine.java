package parse;

import math.Rational;
import node.*;
import node.NumberNode;

import java.util.*;

/** TODO */
public class StraightLine {
	/**
	 * The name of the format.
	 * This is used when constructing a {@link ParsingExcpetion}.
	 */
	public final static String FORMAT_NAME = "Straight line";

	/** List of all previously parsed instructions */
	private final List<Node> previousExpressions;

	/**
	 * Constructor for a new {@link StraightLine} parser.
	 */
	public StraightLine() {
		this.previousExpressions = new ArrayList<>();
	}

	/**
	 * TODO: add links
	 * <p>
	 *     EFFECTS: Parsers a straight line program notation expression.
	 *     It will use the previous parsed expressions to parse the passed expression.
	 *     It will store the parsed expression in the previous expressions (if no error occurs).
	 * <p>
	 *     REQUIREMENTS:
	 *     <ul>
	 *         <li>input must be non-null.</li>
	 *         <li>previousExpression must remain unchanged if an error occurs.</li>
	 *     </ul>
	 * <p>MUTABILITY: This method will modify the previous expressions list.</p>
	 * @param input The input string to parse.
	 * @return A {@link Node} containing the parsed expression.
	 * @throws ParsingExcpetion If the passed expression is invalid.
	 * @throws NullPointerException If input is null.
	 */
	public Node parse(String input)
		throws ParsingExcpetion
	{
		Objects.requireNonNull(input);
		Node parseResult = parseImpl(input);
		// If an exception is thrown, the list of previous expressions is not updated
		this.previousExpressions.add(parseResult);
		return parseResult;
	}

	/**
	 * <p>EFFECTS: Returns the last parsed expression.</p>
	 * @return The last parsed expression.
	 * @throws IndexOutOfBoundsException If no expressions where parsed by this parser.
	 */
	public Node getLast()
		throws IndexOutOfBoundsException
	{
		if (previousExpressions.isEmpty())
			throw new IndexOutOfBoundsException("Cannot get last expression, parser is empty");

		return previousExpressions.get(previousExpressions.size() - 1);
	}

	/**
	 * TODO: links
	 * <p>
	 *     EFFECTS: Parses a line of straight line program.
	 *     Using the previous expression as input.
	 * </p>
	 * @param input The input string to parse.
	 * @return A {@link Node} containing the parsed expression.
	 * @throws ParsingExcpetion If the passed expression is invalid.
	 * @throws NullPointerException If input is null.
	 */
	private Node parseImpl(String input)
		throws ParsingExcpetion, NullPointerException
	{
		Objects.requireNonNull(input);
		// Split line into parts, trim the input to remove possible trailing newlines
		String[] parts = input.trim().split("\\s+");

		if (parts.length == 0)
			throw new ParsingExcpetion("Invalid empty string", FORMAT_NAME);
		if (parts[0].length() != 1)
			throw new ParsingExcpetion("First part must be a dot or an operator", FORMAT_NAME);

		char operatorChar = parts[0].charAt(0);
		String[] arguments = Arrays.copyOfRange(parts, 1, parts.length);

		// Expression is a leaf
		if (operatorChar == '.') {
			if (arguments.length != 1)
				throw new ParsingExcpetion("Invalid number of arguments after a dot", FORMAT_NAME);

			// Argument is a variable
			if (arguments[0].matches("^[a-zA-Z]+$"))
				return new VariableNode(arguments[0]);

			// Argument must be a number
			try {
				long value = Long.parseLong(arguments[0]);
				return new NumberNode(Rational.fromInt(value));
			} catch (NumberFormatException e) {
				throw new ParsingExcpetion("Invalid syntax", FORMAT_NAME);
			}
		}

		// Expression is an operation
		// Read all the indices
		List<Node> operands = new ArrayList<>(arguments.length);
		for (String argument : arguments) {
			try {
				int index = Integer.parseInt(argument);
				if (index < 0)
					throw new ParsingExcpetion("Invalid negative index", FORMAT_NAME);
				if (index >= this.previousExpressions.size())
					throw new ParsingExcpetion("Index out of bounds", FORMAT_NAME);
				operands.add(this.previousExpressions.get(index));
			} catch (NumberFormatException e) {
				throw new ParsingExcpetion("Invalid syntax", FORMAT_NAME);
			}
		}

		return switch (operatorChar) {
			case '+' -> new SumNode(operands);
			case '-' -> SumNode.fromSub(operands);
			case '*' -> new MulNode(operands);
			case '/' -> MulNode.fromDiv(operands);
			case '^' -> makePowNode(operands);
			default -> throw new ParsingExcpetion("First part must be dot or an operator", FORMAT_NAME);
		};
	}

	/**
	 * <p>
	 *     EFFECTS: Constructs a {@link PowNode} applying the operation on the operands.
	 *     The operations must be applied form right to left, because exponentiation is right associative.
	 * <p>REQUIREMENTS: the size of the operands must be greater than one</p>
	 * @param operands The operands from which construct the {@link PowNode}.
	 * @return The constructed {@link PowNode}.
	 */
	private Node makePowNode(List<Node> operands) {
		Objects.requireNonNull(operands);
		assert operands.size() >= 2;

		ListIterator<Node> iter = operands.listIterator();
		// Advance to the end of the iterator
		while (iter.hasNext()) iter.next();
		Node rhs = iter.previous();
		Node pow = new PowNode(iter.previous(), rhs);
		while (iter.hasPrevious()) pow = new PowNode(iter.previous(), pow);
		return pow;
	}
}