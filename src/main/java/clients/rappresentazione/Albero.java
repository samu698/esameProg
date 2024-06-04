package clients.rappresentazione;

import node.Node;
import parse.ParsingExcpetion;
import parse.Polish;
import print.TreePrinter;

import java.util.Scanner;

/** Classe contenete il client per verificare la rappresentazione ad albero delle espressioni. */
public class Albero {

  /**
   * Client per verificare la rappresentazione di un'espressione come disegno testuale dell'albero.
   *
   * <p>Legge una sequenza di linee dal flusso di ingresso standard corrispondenti ad un programma
   * lineare, costruisce l'espressione corrispondente e ne emette la rappresentazione come disegno
   * testuale dell'albero nel flusso d'uscita standard.
   *
   * @param args non utilizzati.
   */
  public static void main(String[] args) throws ParsingExcpetion {
	  Scanner s = new Scanner(System.in);
	  TreePrinter treePrinter = new TreePrinter();
	  while (s.hasNextLine()) {
		  Node expr = Polish.parse(s.nextLine());
		  System.out.println(expr.transform(treePrinter));
	  }
  }

}
