package parse;

import java.io.Serial;

public class ParsingExcpetion extends Exception {
	@Serial
	private static final long serialVersionUID = 6537571119915166583L;

	public final String parseInput;
	public final String format;

	public ParsingExcpetion(String description, String format, String parseInput) {
		super(generateMessage(description, format, parseInput));
		this.parseInput = parseInput;
		this.format = format;
	}

	private static String generateMessage(String description, String format, String parseInput) {
		return String.format(
			"Parse exception (format: %s): %s\nWhile parsing: %s",
			format,
			description,
			parseInput
		);
	}
}