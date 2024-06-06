package transform;

import math.Rational;
import node.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * <p>A Transformer for {@link Node} that computes the derivative with respect to the selected variable.</p>
 * <p>REQUIREMENTS:
 * <ul>
 *     <li>variable must be non-null not empty and contain only upper or lowercase letters.</li>
 *     <li>see {@link Visitor} for the additional requirements.</li>
 * </ul>
 *
 * <p>MUTABILITY: This class is immutable. respecting the mutability requirement of {@link Visitor}.</p>
 */
public class Differentiate implements Visitor<Node> {
	/** The variable with respect to compute the derivative. */
	public final String variable;

	/**
	 * <p>EFFECTS: Constructs a new instance of {@link Differentiate}</p>
	 * <p>
	 *     REQUIREMENTS:
	 *     <ul>
	 *         <li>variable must be non-null.</li>
	 *         <li>variable must not be empty.</li>
	 *         <li>variable must contain only upper or lowercase letters.</li>
	 *     </ul>
	 * @param variable The variable with respect to differentiate.
	 * @throws IllegalArgumentException If variable is empty or contains illegal chars.
	 * @throws NullPointerException If variable is null.
	 */
	public Differentiate(String variable)
		throws IllegalArgumentException, NullPointerException
	{
		Objects.requireNonNull(variable);
		if (!variable.matches("^[a-zA-Z]+$"))
			throw new IllegalArgumentException("Invalid variable name");
		this.variable = Objects.requireNonNull(variable);
	}

	@Override
	public Node visit(NumberNode node) {
		return NumberNode.ZERO;
	}

	@Override
	public Node visit(VariableNode node) {
		if (node.name().equals(this.variable))
			return NumberNode.ONE;
		return NumberNode.ZERO;
	}

	@Override
	public Node visit(SumNode node) {
		List<Node> derivatives = new ArrayList<>(node.operands().size());
		for (Node expr : node.operands()) {
			Node derivative = expr.transform(this);
			// Don't add zero terms
			if (!(derivative instanceof NumberNode num) || !num.value().equals(Rational.ZERO))
				derivatives.add(derivative);
		}

		if (derivatives.isEmpty())
			return NumberNode.ZERO;

		if (derivatives.size() == 1)
			return derivatives.get(0);

		return new SumNode(derivatives);
	}

	@Override
	public Node visit(MulNode node) {
		List<Node> constantTerms = new ArrayList<>();
		List<Node> variableTerms = new ArrayList<>();
		List<Node> derivatives = new ArrayList<>();

		// Split constant factors, from variable factors
		for (Node expr : node.operands()) {
			Node derivative = expr.transform(this);

			if (derivative instanceof NumberNode numDerivative && numDerivative.value().equals(Rational.ZERO)) {
				// If the derivative is zero the factor is constant.
				constantTerms.add(expr);
			} else {
				variableTerms.add(expr);
				derivatives.add(derivative);
			}
		}

		// If all the factors are constant, the product is constant
		if (variableTerms.isEmpty())
			return NumberNode.ZERO;

		// If only one factor is variable linearity can be applied
		if (variableTerms.size() == 1) {
			List<Node> mulOperands = new ArrayList<>(constantTerms);
			mulOperands.add(derivatives.get(0));
			return new MulNode(mulOperands);
		}

		// Apply product rule
		List<Node> sumOperands = new ArrayList<>();
		for (int i = 0; i < derivatives.size(); i++) {
			// Multiply all the variable term.
			List<Node> mulOperands = new ArrayList<>(variableTerms);
			// Except the i-th that will be substituted with its derivative.
			mulOperands.remove(i);
			mulOperands.add(derivatives.get(i));
			// Add all the constant terms as factors.
			mulOperands.addAll(constantTerms);

			sumOperands.add(new MulNode(mulOperands));
		}

		return new SumNode(sumOperands);
	}

	@Override
	public Node visit(PowNode node) {
		// [f(x)^n]' = nf(x)^(n - 1) + f'(x)

		// b^0 is a constant
		if (node.exp().equals(Rational.ZERO))
			return NumberNode.ZERO;

		//  f(x)^1 = f(x), so (f(x)^1)' = f'(x)
		if (node.exp().equals(Rational.ONE))
			return node.base().transform(this);

		Node chain = node.base().transform(this);

		if (chain instanceof NumberNode numChain && numChain.value().equals(Rational.ZERO)) {
			// The derivative of the base is constant, so the entire exponentiation is constant
			return NumberNode.ZERO;
		}

		Rational derivativeExp = node.exp().add(Rational.NEG_ONE);
		Node coefficient = new NumberNode(node.exp());
		Node pow = new PowNode(node.base(), derivativeExp);

		if (chain instanceof NumberNode numChain && numChain.value().equals(Rational.ONE))
			// If the chain is one it can be elided
			return new MulNode(coefficient, pow);
		else
			return new MulNode(coefficient, pow, chain);
	}
}
