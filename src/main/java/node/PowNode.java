package node;

import math.Rational;
import transform.Simplify;

import java.util.Objects;

/**
 * <p>The {@link Node} representing a exponentiation.</p>
 * <p>
 *     AF: This represents a exponentiation operation between an expression and a rational exponent.
 *     Given a expression B (of type {@link Node}) and a rational exponent e this represents B^e.
 * <p>
 *     REQUIREMENTS:
 *     <ul>
 *         <li>base must be non-null.</li>
 *         <li>exp must be non-null.</li>
 *     </ul>
 * <p>MUTABILITY: This class is immutable, as per {@link Node} requirement.</p>
 * @param base The base of the exponentiation.
 * @param exp The exponent of the exponentiation.
 */
public record PowNode(Node base, Rational exp) implements Node {
	/**
	 * Constructor for {@link PowNode}.
	 * <p>REQUIREMENTS: All the parameters must be non-null.</p>
	 * @param base The base of the exponentiation.
	 * @param exp The exponent of the exponentiation.
	 */
	public PowNode(Node base, Rational exp) {
		this.base = Objects.requireNonNull(base);
		this.exp = Objects.requireNonNull(exp);
	}

	/**
	 * Constructor for {@link PowNode} from two expressions.
	 * <p>EFFECTS: construct a {@link PowNode} after simplifying the exponent to a rational (if possible)</p>
	 * <p>
	 *     REQUIREMENTS:
	 *     <ul>
	 *         <li>All the parameters must be non-null.</li>
	 *         <li>The exponent expression has to be simplifiable to a rational number.</li>
	 *     </ul>
	 * @param base The base of the exponentiation.
	 * @param exp The exponent of the exponentiation.
	 * @throws IllegalArgumentException If the exponent cannot be simplified to a rational.
	 * @throws NullPointerException If any of the parameters is null.
	 */
	public PowNode(Node base, Node exp)
		throws IllegalArgumentException, NullPointerException
	{
		// NOTE: simplifyToRational cannot be inlined.
		//       Because a record constructor must start with the this(...) line.
		this(base, simplifyToRational(exp));
	}

	/**
	 * <p>EFFECTS: Simplifies an expression to a rational (if possible)</p>
	 * <p>
	 *     REQUIREMENTS:
	 *     <ul>
	 *         <li>All the parameters must be non-null.</li>
	 *         <li>The expression has to be simplifiable to a rational number.</li>
	 *     </ul>
	 * @param exp The expression to simplify.
	 * @return The simplified rational.
	 * @throws IllegalArgumentException If the expression cannot be simplified to a rational.
	 * @throws NullPointerException If exp is null.
	 */
	private static Rational simplifyToRational(Node exp)
		throws IllegalArgumentException, NullPointerException
	{
		Objects.requireNonNull(exp);
		if (exp.transform(new Simplify()) instanceof NumberNode numExp) {
			return numExp.value();
		} else {
			throw new IllegalArgumentException("Exponent cannot be converted to a rational number");
		}
	}

	@Override
	public <T> T transform(Visitor<T> visitor) {
		return visitor.visit(this);
	}

	@Override
	public boolean containsVariables() {
		return this.base.containsVariables();
	}

	@Override
	public int orderPosition() {
		return 2;
	}

	@Override
	public int compareTo(Node o) {
		int order = Integer.compare(this.orderPosition(), o.orderPosition());
		if (order != 0) return order;

		if (o instanceof PowNode other) {
			order = this.base.compareTo(other.base);
			if (order != 0) return order;
			return this.exp.compareTo(other.exp);
		} else {
			// This should never happen, as per orderPosition requirement.
			// If two Nodes have the same orderPosition they must be the same type.
			assert false;
			return 0;
		}
	}

	@Override
	public String toString() {
		return "^";
	}
}
