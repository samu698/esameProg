package transform;

// Possible simplifications
// 1. Evaluate terms equal to a rational
// 6. Multiply rationals in product
// 7. Group same terms in product and sum exponents
// 8. Sum rational in sum
// 9. Group same terms in sums and sum factors

import math.Rational;
import node.*;

import java.util.*;

public class Simplify implements Visitor<Node> {
	public Simplify() {}

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
		List<Node> simplified = new ArrayList<>(node.operands().size());
		for (Node operand : node.operands()) {
			// Simplify each child node
			Node s = operand.accept(this);
			// Flatten nested sums
			if (s instanceof SumNode sum) {
				simplified.addAll(sum.operands());
			} else {
				simplified.add(s);
			}
		}

		HashMap<Node, Rational> terms = new HashMap<>();
		Rational rationalSum = Rational.fromInt(0);
		for (Node s : simplified) {
			// Sum rational terms
			if (s instanceof NumberNode num) {
				rationalSum = rationalSum.add(num.value());
				continue;
			}

			Node term = s;
			Rational termFactor = Rational.fromInt(1);

			// If the node is a term multiplied by a factor we can consider the term and factor separately
			if (s instanceof MulNode mul && mul.operands().get(0) instanceof NumberNode factor) {
				List<Node> mulOperands = mul.operands();
				termFactor = factor.value();
				// Construct the multiplication node without the rational factor
				if (mulOperands.size() > 2) {
					term = new MulNode(mulOperands.subList(1, mulOperands.size()));
				} else {
					term = mulOperands.get(1);
				}
			}

			// Add the factor to the map of terms
			Rational oldFactor = terms.getOrDefault(term, Rational.fromInt(0));
			terms.put(term, oldFactor.add(termFactor));
		}

		// Using the grouped terms generate the new sum tree
		simplified.clear();
		// If the rational constant is zero it can be elided
		if (!rationalSum.equalInt(0)) simplified.add(new NumberNode(rationalSum));
		for (Map.Entry<Node, Rational> term : terms.entrySet()) {
			// Ignore terms with factor of zero
			if (term.getValue().equalInt(0)) continue;
			// Copy terms with factor of one
			if (term.getValue().equalInt(1)) {
				simplified.add(term.getKey());
				continue;
			}
			// Multiply remaining terms by their respective factor
			Node factor = new NumberNode(term.getValue());
			// Add the factor to the multiplication if the term is already a multiplication
			if (term.getKey() instanceof MulNode mul) {
				List<Node> operands = new ArrayList<>(mul.operands());
				operands.add(factor);
				simplified.add(new MulNode(operands));
			} else {
				simplified.add(new MulNode(List.of(factor, term.getKey())));
			}
		}

		if (simplified.isEmpty()) return new NumberNode(Rational.fromInt(0));
		if (simplified.size() == 1) return simplified.get(0);
		return new SumNode(simplified);
	}

	@Override
	public Node visit(MulNode node) {
		List<Node> simplified = new ArrayList<>(node.operands().size());
		for (Node operand : node.operands()) {
			// Simplify each child node
			Node s = operand.accept(this);
			// Flatten nested products
			if (s instanceof MulNode mul) {
				simplified.addAll(mul.operands());
			} else {
				simplified.add(s);
			}
		}

		HashMap<Node, Rational> terms = new HashMap<>();
		Rational rationalProd = Rational.fromInt(1);
		for (Node s : simplified) {
			// Multiply rational terms
			if (s instanceof NumberNode num) {
				rationalProd = rationalProd.mul(num.value());
				continue;
			}

			Node term = s;
			Rational termExp = Rational.fromInt(1);

			if (s instanceof PowNode pow) {
				termExp = pow.exp();
				term = pow.base();
			}

			// Add the factor to the map of terms
			Rational oldPow = terms.getOrDefault(term, Rational.fromInt(0));
			terms.put(term, oldPow.add(termExp));
		}

		simplified.clear();

		// If the rational constant is zero the multiplication is equal to zero
		if (rationalProd.equalInt(0)) return new NumberNode(Rational.fromInt(0));
		// If the rational constant is one it can be elided
		if (!rationalProd.equalInt(1)) simplified.add(new NumberNode(rationalProd));
		// Using the grouped terms generate the new mul tree
		for (Map.Entry<Node, Rational> term : terms.entrySet()) {
			// Ignore terms with exponent of zero TODO: check if base is zero
			if (term.getValue().equalInt(0)) continue;
			// Copy terms with factor of one
			if (term.getValue().equalInt(1)) {
				simplified.add(term.getKey());
				continue;
			}
			// Multiply remaining terms to their respective power
			Node exp = new NumberNode(term.getValue());
			simplified.add(new PowNode(term.getKey(), exp));
		}

		if (simplified.isEmpty()) return new NumberNode(Rational.fromInt(0));
		if (simplified.size() == 1) return simplified.get(0);
		return new MulNode(simplified);
	}

	// 2. b^0 = 1 (if b != 0)
	// 3. b^1 = b
	// 4. 0^e = 0 (if e != 0)
	// 5. Power of fraction: remove irrational (5/9)^(1/2) = 3^-1 * 5^(1/2)
	@Override
	public Node visit(PowNode node) {
		Node base = node.base().accept(this);
		Rational exp = node.exp();

		// b^0 = 1 TODO: check b != 0
		if (exp.equalInt(0))
			return new NumberNode(Rational.fromInt(1));

		// b^1 = b
		if (exp.equalInt(1))
			return base;

		if (base instanceof NumberNode numBase) {
			// 0^e = 0 TODO: check e != 0
			if (numBase.value().equalInt(0))
				return new NumberNode(Rational.fromInt(0));

			// 1^e = 1
			if (numBase.value().equalInt(1))
				return new NumberNode(Rational.fromInt(1));
		}

		if (base instanceof NumberNode numBase) {
			Rational.PowResult result = numBase.value().pow(exp);
			if (result.irrationalPart.equalInt(1)) return new NumberNode(result.rationalPart);

			Node rationalNode;
			if (result.rationalPart.num == 1 && result.rationalPart.den != 1) {
				Rational reciprocal = result.rationalPart.reciprocal();
				rationalNode = new PowNode(new NumberNode(reciprocal), new NumberNode(Rational.fromInt(-1)));
			} else if (!result.rationalPart.equalInt(1)) {
				rationalNode = new NumberNode(result.rationalPart);
			} else {
				// If the rational coefficient is one it can be elided
				rationalNode = null;
			}

			Node irrationalNode;
			// XXX: Begin testfix
			int numAbs = result.irrationalPart.num;
			numAbs = numAbs >= 0 ? numAbs : -numAbs;
			boolean testFix = !result.irrationalPart.isInteger()&& numAbs > result.irrationalPart.den;
			// XXX: End testfix
			if (result.irrationalPart.num == 1 || testFix) {
				Rational reciprocal = result.irrationalPart.reciprocal();
				Rational expOpposite = node.exp().opposite();
				irrationalNode = new PowNode(new NumberNode(reciprocal), expOpposite);
			} else {
				irrationalNode = new PowNode(new NumberNode(result.irrationalPart), node.exp());
			}

			if (rationalNode != null) {
				return new MulNode(List.of(rationalNode, irrationalNode));
			} else {
				return irrationalNode;
			}
		} else if (base instanceof PowNode powBase) {
			// Flatten nested powers
			return new PowNode(powBase.base(), exp.mul(powBase.exp()));
		}

		return new PowNode(base, exp);
	}
}