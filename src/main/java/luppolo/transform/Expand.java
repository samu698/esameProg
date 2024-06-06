package luppolo.transform;

import luppolo.math.Rational;
import luppolo.node.*;

import java.util.*;

/**
 * <p>
 *     A transformer that expands a {@link Node},
 *     by applying the distributive property of the product over the sum
 *     and evaluating powers as a product.
 * <p>REQUIREMENTS: see {@link Visitor} for the requirements.</p>
 * <p>MUTABILITY: This class has no state and it cannot mutate, respecting the mutability requirements of {@link Visitor}.</p>
 */
public class Expand implements Visitor<Node> {
	@Override
	public Node visit(NumberNode node) {
		return node;
	}

	@Override
	public Node visit(VariableNode node) {
		return node;
	}

	@Override
	public Node visit(SumNode node) {
		List<Node> expanded = new ArrayList<>(node.operands().size());
		// Expand all the sub nodes
		for (Node operand : node.operands())
			expanded.add(operand.transform(this));
		return new SumNode(expanded);
	}

	@Override
	public Node visit(MulNode node) {
		Iterator<Node> iter = node.operands().iterator();

		// Expand the multiplication terms in pairs
		Node result = expandBinaryProduct(iter.next(), iter.next());
		while (iter.hasNext())
			result = expandBinaryProduct(result, iter.next());

		return result;
	}

	/**
	 * <p>
	 *     EFFECTS: Expands a product between two nodes.
	 *     This method will first recursively expand the two nodes.
	 *     And will apply the distributive rule of the product over the sum, if possible.
	 * <p>
	 *     REQUIREMENTS:
	 *     <ul>
	 *         <li>lhs must be non-null</li>
	 *         <li>rhs must be non-null</li>
	 *     </ul>
	 * @param lhs The left hand side of the product.
	 * @param rhs The right hand side of the product.
	 * @return The expanded product.
	 */
	private Node expandBinaryProduct(Node lhs, Node rhs) {
		assert lhs != null;
		assert rhs != null;

		// Expand recursively the left hand side
		lhs = lhs.transform(this);
		List<Node> lhsTerms;
		// If the node is a sum distributivity can be applied
		if (lhs instanceof SumNode lhsSum) lhsTerms = lhsSum.operands();
		else lhsTerms = List.of(lhs);

		// Expand recursively the right hand side
		rhs = rhs.transform(this);
		List<Node> rhsTerms;
		// If the node is a sum distributivity can be applied
		if (rhs instanceof SumNode rhsSum) rhsTerms = rhsSum.operands();
		else rhsTerms = List.of(rhs);

		List<Node> terms = new ArrayList<>(lhsTerms.size() * rhsTerms.size());
		for (Node lterm : lhsTerms)
			for (Node rterm : rhsTerms)
				terms.add(new MulNode(lterm, rterm));

		if (terms.size() == 1)
			return terms.get(0);

		return new SumNode(terms);
	}

	@Override
	public Node visit(PowNode node)
		throws IllegalArgumentException
	{
		// b^0 = 1
		if (node.exp().equals(Rational.ZERO)) {
			if (node.base().equals(NumberNode.ZERO))
				throw new IllegalArgumentException("Cannot evaluate 0^0");
			return NumberNode.ONE;
		}

		// Expand the base recursively
		Node base = node.base().transform(this);

		// b^1 = b
		if (node.exp().equals(Rational.ONE))
			return base;

		int repetitions = (int)Math.abs(node.exp().num);
		final List<Node> expandedTerms = new ArrayList<>();
		if (base instanceof SumNode sumBase) {
			// Compute the all the permutations with repetitions
			List<Node> terms = sumBase.operands();

			int[] indices = new int[repetitions];
			int pos = repetitions - 1;
			Node[] result = new Node[repetitions];
			Arrays.fill(result, terms.get(0));

			outer: for (;;) {
				expandedTerms.add(makeTerm(result));
				indices[pos]++;
				while (indices[pos] >= terms.size()) {
					indices[pos] = 0;
					result[pos] = terms.get(0);
					if (pos-- == 0) break outer;
					indices[pos]++;
				}
				result[pos] = terms.get(indices[pos]);
				pos = repetitions - 1;
			}
		}
		else if (repetitions > 1) {
			expandedTerms.add(new MulNode(Collections.nCopies(repetitions, base)));
		} else {
			expandedTerms.add(base);
		}

		Node expandedBase;
		if (expandedTerms.size() > 1) expandedBase = new SumNode(expandedTerms);
		else expandedBase = expandedTerms.get(0);

		// Return the expanded base without if the exponent was a positive integer
		if (node.exp().isInteger() && node.exp().compareTo(Rational.ZERO) >= 0) {
			return expandedBase;
		}

		// Return the expanded base to the power of 1/d
		Rational newExp = Rational.fromNumDen(Long.signum(node.exp().num), node.exp().den);
		return new PowNode(expandedBase, newExp);
	}

	/**
	 * <p>
	 *     EFFECTS: Constructs a {@link MulNode} using the provided terms as input.
	 *     If there is only one term it will be returned as it is.
	 *     If there are multiple terms a expression of nested binary {@link MulNode}s will be built.
	 * <p>
	 *     REQUIREMENTS:
	 *     <ul>
	 *         <li>terms must be non-null.</li>
	 *         <li>terms must not be empty.</li>
	 *     </ul>
	 * @param terms The terms to build the {@link MulNode}
	 * @return An expression of binary {@link MulNode}s containing the provided terms.
	 */
	private Node makeTerm(Node[] terms) {
		assert terms != null;
		assert terms.length > 0;

		// If there is only one term return it as it is
		if (terms.length == 1) return terms[0];

		final int last = terms.length - 1;
		Node result = new MulNode(terms[last - 1], terms[last]);
		for (int i = last - 2; i >= 0; i--)
			result = new MulNode(terms[i], result);

		return result;
	}
}