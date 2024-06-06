package luppolo.parse;

import java.io.Serial;

/**
 * Checked exception caused by parsing.
 * <p>MUTABILITY: This class is immutable.</p>
 */
public class ParsingException extends Exception {
	/** Required field of a serializable class */
	@Serial
	private static final long serialVersionUID = 6537571119915166583L;

	/**
	 * Constructs a {@link ParsingException}.
	 * <p>REQUIREMENTS: All of the parameters must be non-null.</p>
	 * @param description Description on what caused the exception.
	 * @param format The name of the format that was being parsed.
	 */
	public ParsingException(String description, String format) {
		super("Parse exception (format: %s): %s".formatted(format, description));
	}
}