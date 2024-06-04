package clients.costruzione;

import parse.ParsingExcpetion;
import parse.StraightLine;
import print.BasicPrinter;

import java.util.Scanner;

/**
 * Classe contenete il client per verificare la costruzione di un'espressione a partire da un
 * programma lineare.
 */
public class ProgrammaLineare {

  /**
   * Client per verificare la costruzione di un'espressione a partire da un programma lineare.
   *
   * <p>Legge una sequenza di linee dal flusso di ingresso standard corrispondenti ad un programma
   * lineare, costruisce l'espressione corrispondente e ne emette la rappresentazione linearizzata
   * nel flusso d'uscita standard.
   *
   * @param args non utilizzati.
   */
  public static void main(String[] args) throws ParsingExcpetion {
	  Scanner s = new Scanner(System.in);
	  StraightLine parser = new StraightLine();
	  while (s.hasNextLine()) {
		  String line = s.nextLine();
		  if (line.isBlank()) break;
		  parser.parse(line);
	  }
	  System.out.println(parser.getLast().transform(new BasicPrinter()));
  }
}
