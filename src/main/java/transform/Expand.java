package transform;

import math.Rational;
import node.*;

import java.util.*;

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
			expanded.add(operand.accept(this));
		return new SumNode(expanded);
	}

	@Override
	public Node visit(MulNode node) {
		Iterator<Node> iter = node.operands().iterator();

		Node result = expandBinaryProduct(iter.next(), iter.next());
		while (iter.hasNext())
			result = expandBinaryProduct(result, iter.next());

		return result;
	}

	private Node expandBinaryProduct(Node lhs, Node rhs) {
		lhs = lhs.accept(this);
		List<Node> lhsTerms;
		if (lhs instanceof SumNode lhsSum) lhsTerms = lhsSum.operands();
		else lhsTerms = List.of(lhs);

		rhs = rhs.accept(this);
		List<Node> rhsTerms;
		if (rhs instanceof SumNode rhsSum) rhsTerms = rhsSum.operands();
		else rhsTerms = List.of(rhs);

		List<Node> terms = new ArrayList<>(lhsTerms.size() * rhsTerms.size());
		for (Node lterm : lhsTerms)
			for (Node rterm : rhsTerms)
				terms.add(new MulNode(List.of(lterm, rterm)));

		if (terms.size() == 1)
			return terms.get(0);

		return new SumNode(terms);
	}

	@Override
	public Node visit(PowNode node) {
		// b^0 = 1
		if (node.exp().equalInt(0))
			// TODO: check base != 0
			return new NumberNode(Rational.fromInt(1));

		Node base = node.base().accept(this);

		// b^1 = b
		if (node.exp().equalInt(1))
			return base;

		int repetitions = (int)node.exp().num;
		if (repetitions < 0) repetitions = -repetitions;

		final List<Node> expandedTerms = new ArrayList<>();
		if (base instanceof SumNode sumBase) {
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

		if (node.exp().isInteger() && node.exp().num >= 0) {
			// Remove the exponent because the base has been expanded
			return expandedBase;
		}

		// The new exponent is 1/d keeping the sign of the exponent
		Rational newExp = Rational.fromNumDen(Long.signum(node.exp().num), node.exp().den);
		return new PowNode(expandedBase, newExp);
	}

	private Node makeTerm(Node[] terms) {
		assert terms.length > 0;

		// If there is only one term return it as it is
		if (terms.length == 1) return terms[0];

		final int last = terms.length - 1;
		Node result = new MulNode(List.of(terms[last - 1], terms[last]));

		for (int i = last - 2; i >= 0; i--)
			result = new MulNode(List.of(terms[i], result));

		return result;
	}
}