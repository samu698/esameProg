package node;

public interface Visitor<Ret> {
	Ret visit(NumberNode node);
	Ret visit(VariableNode node);
	Ret visit(SumNode node);
	Ret visit(MulNode node);
	Ret visit(PowNode node);
}