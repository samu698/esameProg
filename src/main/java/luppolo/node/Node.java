package luppolo.node;


/**
 * <p>Interface for a node in a expression tree structure.</p>
 * <p>
 *     AF: This represents a node of a mathematical expression.
 *     Each node forms a tree (that must be acyclic) representing a part of an expression.
 *     Each node can have a number n of children.
 * <p>
 *     REQUIREMENTS:
 *     <ul>
 *         <li>The implementers of this interface must be immutable</li>
 *     </ul>
 * <p>MUTABILITY: all implementers must be immutable.</p>
 * <p>
 *     NOTE: A tree structure must be acyclic, but because this class is immutable it's impossible to create one.
 *     To create a cycle you must create a node n that points at a node m that itself points at n, but because
 *     you must create n after m, it's impossible to create m because it requires n to be created before.<br>
 *     Here a discussion about this on <a href="https://stackoverflow.com/questions/3587995/why-no-cycles-in-eric-lipperts-immutable-binary-tree">Stackoverflow</a>.
 * <p>
 *     NOTE: Nodes have a total order relation and all implementors must respect it while implementing {@link Comparable#compareTo}
 *     The total order relation is described in the project description.
 */
public interface Node extends Comparable<Node> {
	/**
	 * <p>EFFECTS: Accepts a visitor that transforms the tree structure into a value of type Ret.</p>
	 * <p>REQUIREMENTS: The visitor must be non-null.</p>
	 * @param visitor The visitor to use.
	 * @return The transformed value.
	 * @param <Ret> The return value type.
	 * @throws NullPointerException If visitor is null.
	 * @throws RuntimeException if an error happened while applying the visitor.
	 */
	<Ret> Ret transform(Visitor<Ret> visitor) throws NullPointerException, RuntimeException;

	/**
	 * <p>EFFECTS: Checks if the node contains variables in its expression tree.</p>
	 * @return true if the node contains a variable.
	 */
	boolean containsVariables();

	/**
	 * <p>
	 *     EFFECTS: Returns the order precedence of a the node.
	 *     Given two nodes of different type a, b such that a.orderPosition() >= b.orderPosition() then a >= b.
	 * </p>
	 * <p>
	 *     REQUIREMENTS:
	 *     <ul>
	 *         <li>The return value must be constant.</li>
	 *         <li>Given two nodes of different type a, b such that a.orderPosition() >= b.orderPosition() the a >= b.</li>
	 *         <li>Different types must return different values.</li>
	 *     </ul>
	 * @return The order precedence.
	 */
	int orderPosition();

}