package node;

// NOTE: A tree structure must be acyclic, but because this class is immutable it's impossible to create one.
//       To create a cycle you must create a node n that points at a node m that itself points at n, but because
//       you must create n after m, it's impossible to create m because it requires n to be created before.
public interface Node extends Comparable<Node> {
	<Ret> Ret accept(Visitor<Ret> visitor);
}