package clients.manipolazione;

import node.Node;
import parse.ParsingExcpetion;
import parse.Polish;
import print.BasicPrinter;
import transform.Differentiate;
import transform.Simplify;

import java.util.Scanner;

/** Classe contenete il client per verificare la derivazione delle espressioni. */
public class DerivazioneSemplificazione {

  /**
   * Client per verificare la derivazione, seguita da semplificazione, di un'espressione.
   *
   * <p>Legge una sequenza di linee dal flusso di ingresso standard in cui ciascuna linea
   * rappresenta un'espressione in notazione polacca. Per ciascuna linea, costruisce l'espressione
   * corrispondente, la <em>deriva</em> rispetto alla variabile specificata come argomento sulla
   * linea di comando, quindi <em>semplifica</em> l'espressione così ottenuta e emette la
   * rappresentazione linearizzata dell'espressione semplificata nel flusso d'uscita standard.
   *
   * @param args non utilizzati.
   */
  public static void main(String[] args) throws ParsingExcpetion {
	  String variable = args[0];

	  BasicPrinter printer = new BasicPrinter();
	  Differentiate differentiate = new Differentiate(variable);
	  Simplify simplify = new Simplify();

	  Scanner s = new Scanner(System.in);
	  while (s.hasNextLine()) {
		  Node expr = Polish.parse(s.nextLine());
		  Node derivative = expr.transform(differentiate);
		  Node simplified = derivative.transform(simplify);
		  System.out.println(simplified.transform(printer));
	  }
  }

}
