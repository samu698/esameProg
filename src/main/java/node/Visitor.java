package node;

/**
 * <p>A visitor interface, for the {@link Node} class</p>
 * <p>REQUIREMENTS: a visitor must reset after being applied, so that applying it twice one the same node gives the same output.</p>
 * <p>
 *     NOTES:
 *     <ul>
 *         <li>This interface implements the visitor pattern</li>
 *         <li>Because each method has a return value, this interface use useful for transforming a {@link Node} to the Ret type</li>
 *     </ul>
 * @param <Ret> The return type of the visitor.
 */
public interface Visitor<Ret> {
	/**
	 * <p>EFFECTS: Visits a {@link NumberNode}.</p>
	 * <p>REQUIREMENTS: node must be non-null.</p>
	 * @param node The node to visit.
	 * @return The result.
	 */
	Ret visit(NumberNode node);
	/**
	 * <p>EFFECTS: Visits a {@link VariableNode}.</p>
	 * <p>REQUIREMENTS: node must be non-null.</p>
	 * @param node The node to visit.
	 * @return The result.
	 */
	Ret visit(VariableNode node);
	/**
	 * <p>EFFECTS: Visits a {@link SumNode}.</p>
	 * <p>REQUIREMENTS: node must be non-null.</p>
	 * @param node The node to visit.
	 * @return The result.
	 */
	Ret visit(SumNode node);
	/**
	 * <p>EFFECTS: Visits a {@link MulNode}.</p>
	 * <p>REQUIREMENTS: node must be non-null.</p>
	 * @param node The node to visit.
	 * @return The result.
	 */
	Ret visit(MulNode node);
	/**
	 * <p>EFFECTS: Visits a {@link PowNode}.</p>
	 * <p>REQUIREMENTS: node must be non-null.</p>
	 * @param node The node to visit.
	 * @return The result.
	 */
	Ret visit(PowNode node);
}