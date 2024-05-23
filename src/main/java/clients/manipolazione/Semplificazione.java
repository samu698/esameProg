package clients.manipolazione;

import node.Node;
import parse.ParsingExcpetion;
import parse.Polish;
import print.BasicPrinter;
import transform.Simplify;

import java.util.Scanner;

/** Classe contenete il client per verificare la semplificazione delle espressioni. */
public class Semplificazione {

  /**
   * Client per verificare la semplificazione delle espressioni.
   *
   * <p>Legge una sequenza di linee dal flusso di ingresso standard in cui ciascuna linea
   * rappresenta un'espressione in notazione polacca. Per ciascuna linea, costruisce l'espressione
   * corrispondente, la <em>semplifica</em> e emette la rappresentazione linearizzata
   * dell'espressione così ottenuta nel flusso d'uscita standard.
   *
   * @param args non utilizzati.
   */
  public static void main(String[] args) throws ParsingExcpetion {
	  Scanner s = new Scanner(System.in);
	  BasicPrinter printer = new BasicPrinter();
	  while (s.hasNextLine()) {
		  Node expr = Polish.parse(s.nextLine());
		  Node simplified = expr.accept(new Simplify());
		  System.out.println(simplified.accept(printer));
	  }
  }

}
