package luppolo.node;

/**
 * <p>A visitor interface, for the {@link Node} class</p>
 * <p>
 *     REQUIREMENTS: 
 *     <ul>
 *         <li>A visitor must reset after being applied, so that applying it twice one the same node gives the same output.</li>
 *         <li>A the method inside this interface should only be called by {@link Node#transform(Visitor)}</li>
 *     </ul>
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
	 * @throws RuntimeException If an error happens while applying the visitor.
	 */
	Ret visit(NumberNode node) throws RuntimeException;
	/**
	 * <p>EFFECTS: Visits a {@link VariableNode}.</p>
	 * <p>REQUIREMENTS: node must be non-null.</p>
	 * @param node The node to visit.
	 * @return The result.
	 * @throws RuntimeException If an error happens while applying the visitor.
	 */
	Ret visit(VariableNode node) throws RuntimeException;
	/**
	 * <p>EFFECTS: Visits a {@link SumNode}.</p>
	 * <p>REQUIREMENTS: node must be non-null.</p>
	 * @param node The node to visit.
	 * @return The result.
	 * @throws RuntimeException If an error happens while applying the visitor.
	 */
	Ret visit(SumNode node) throws RuntimeException;
	/**
	 * <p>EFFECTS: Visits a {@link MulNode}.</p>
	 * <p>REQUIREMENTS: node must be non-null.</p>
	 * @param node The node to visit.
	 * @return The result.
	 * @throws RuntimeException If an error happens while applying the visitor.
	 */
	Ret visit(MulNode node) throws RuntimeException;
	/**
	 * <p>EFFECTS: Visits a {@link PowNode}.</p>
	 * <p>REQUIREMENTS: node must be non-null.</p>
	 * @param node The node to visit.
	 * @return The result.
	 * @throws RuntimeException If an error happens while applying the visitor.
	 */
	Ret visit(PowNode node) throws RuntimeException;
}