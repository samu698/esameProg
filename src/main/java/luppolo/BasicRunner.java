package luppolo;

import luppolo.node.Node;
import luppolo.parse.*;
import luppolo.print.*;
import luppolo.transform.*;

import java.io.*;
import java.text.ParseException;

/**
 * Basic runner for luppolo.
 * This class must never be instantiated, it just holds static constants required by the static methods.
 */
public class BasicRunner {
	/** Constructor that mustn't be called */
	private BasicRunner() {
		assert false: "This class mustn't be instatiated";
	}

	/** The {@link Simplify} transformer instance. */
	private static final Simplify SIMPLIFY = new Simplify();
	/** The {@link Expand} transformer instance. */
	private static final Expand EXPAND = new Expand();
	/** The {@link BasicPrinter} printer instance. */
	private static final BasicPrinter BASIC_PRINT = new BasicPrinter();
	/** The {@link TreePrinter} printer instance. */
	private static final TreePrinter TREE_PRINT = new TreePrinter();

	/** {@link BufferedReader} for reading input from the user. */
	private static final BufferedReader reader;
	/** {@link PrintWriter} for writing the output to the user. */
	private static final PrintWriter writer;

	static {
		if (System.console() != null) {
			reader = new BufferedReader(System.console().reader());
			writer = System.console().writer();
		} else {
			reader = new BufferedReader(new InputStreamReader(System.in));
			writer = new PrintWriter(new OutputStreamWriter(System.out));
		}
	}

	/**
	 * Main method of the runner.
	 * <p>
	 *     EFFECTS: This method will run until the application is closed and it will:
	 *     <ol>
	 *         <li>Ask the type of expression to read</li>
	 *         <li>If the input is invalid, go to step 1</li>
	 *         <li>Read the expression from the user</li>
	 *         <li>Parse the expression</li>
	 *         <li>If a parsing error occurs, report it and go to step 1</li>
	 *         <li>Print the expression in all formats and after all transformations (except differentiation)</li>
	 *         <li>Repeat: go to step 1</li>
	 *     </ol>
	 * @param args Unused.
	 * @throws IOException If an error happens when reading or writing from/to the input/output.
	 */
	public static void main(String[] args) throws IOException {
		for (;;) {
			writer.print("Expression type [p]/s > ");
			writer.flush();

			String input = reader.readLine().toLowerCase();

			Node expression = switch (input) {
				case "p", "" -> readPolish();
				case "s" -> readProgram();
				default -> {
					writer.println("Invalid command type p or s");
					yield null;
				}
			};

			if (expression == null) continue;

			writer.println("Expression:");
			printExpr(expression);
			writer.println("\nSimplified:");
			printExpr(expression.transform(SIMPLIFY));
			writer.println("\nExpanded:");
			final Node expanded = expression.transform(EXPAND);
			printExpr(expanded);
			writer.println("\nExpanded and simplified:");
			printExpr(expanded.transform(SIMPLIFY));
		}
	}

	/**
	 * EFFECTS: reads a polish notation expression from the user.
	 * @return The parsed polish notation, or null if it couldn't be parsed.
	 * @throws IOException If an error occurred while reading the input.
	 */
	private static Node readPolish() throws IOException {
		writer.print("Polish > ");
		writer.flush();

		String input = reader.readLine();
		try {
			return Polish.parse(input);
		} catch (ParseException e) {
			writer.println(e.getMessage());
			return null;
		}
	}

	/**
	 * EFFECTS: reads a straight line program from the user.
	 * @return The parsed polish notation, or null if it couldn't be parsed.
	 * @throws IOException If an error occurred while reading the input.
	 */
	private static Node readProgram() throws IOException {
		StraightLine parser = new StraightLine();
		for (;;) {
			writer.print("Program > ");
			writer.flush();

			String line = reader.readLine();
			if (line.isBlank()) break;
			try {
				parser.parse(line);
			} catch (ParseException e) {
				writer.println(e.getMessage());
				return null;
			}
		}

		return parser.getLast();
	}

	/**
	 * EFFECTS: prints an expression in linearized format and in tree format.
	 * @param expr The expression to print.
	 */
	private static void printExpr(Node expr) {
		writer.println("Linearized format:");
		writer.println(expr.transform(BASIC_PRINT));
		writer.println("Tree format:");
		writer.println(expr.transform(TREE_PRINT));
	}
}