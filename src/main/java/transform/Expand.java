package transform;

import math.Rational;
import node.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

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

		// b^1 = b
		if (node.exp().equalInt(1))
			return node.base();

		// If the numerator is one the power cannot be expanded
		if (node.exp().num == 1)
			return node;

		Node base = node.base().accept(this);
		Node multipliedBase = base;
		int copies = (int)(node.exp().num >= 0 ? node.exp().num : -node.exp().num);
		if (copies >= 2) {
			List<Node> baseCopies = Collections.nCopies(copies, base);
			multipliedBase = (new MulNode(baseCopies)).accept(this);
		}

		if (node.exp().isInteger() && node.exp().num >= 0) {
			// Remove the exponent because the base has been expanded
			return multipliedBase;
		}

		// The new exponent is 1/d keeping the sign of the exponent
		Rational newExp = Rational.fromNumDen(Long.signum(node.exp().num), node.exp().den);
		return new PowNode(multipliedBase, newExp);
	}
}