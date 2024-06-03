import math.Rational;
import math.Utils;
import node.Node;
import node.Visitor;
import parse.ParsingExcpetion;
import parse.Polish;
import print.BasicPrinter;
import print.TreePrinter;
import transform.Differentiate;
import transform.Expand;
import transform.Simplify;

import java.util.Scanner;

public class Main {
	public static void main(String[] args) throws ParsingExcpetion {
		TreePrinter treePrinter = new TreePrinter();
		//Differentiate transform = new Differentiate("x");
		Visitor<Node> transform = new Simplify();

		Scanner s = new Scanner(System.in);
		for (;;) {
			Node expr = Polish.parse(s.nextLine());
			System.out.print(expr.accept(treePrinter));
			//Node derivative = expr.accept(transform);
			//System.out.print(derivative.accept(treePrinter));
			Node simplified = expr.accept(transform);
			System.out.println(simplified.accept(new BasicPrinter()));
		}
	}
}