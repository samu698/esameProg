package clients.costruzione;

import node.Node;
import parse.ParsingExcpetion;
import parse.Polish;
import print.BasicPrinter;

import java.util.Scanner;

/**
 * Classe contenete il client per verificare la costruzione di un'espressione a partire dalla
 * notazione polacca.
 */
public class NotazionePolacca {

	/**
	* Client per verificare la costruzione di un'espressione a partire dalla notazione polacca.
	*
	* <p>Legge una sequenza di linee dal flusso di ingresso standard in cui ciascuna linea
	* rappresenta un'espressione in notazione polacca. Per ciascuna linea, costruisce l'espressione
	* corrispondente e ne emette la rappresentazione linearizzata nel flusso d'uscita standard.
	*
	* @param args non utilizzati.
	*/

	public static void main(String[] args) throws ParsingExcpetion {
		Scanner s = new Scanner(System.in);
		BasicPrinter basicPrinter = new BasicPrinter();
		while (s.hasNextLine()) {
			Node expr = Polish.parse(s.nextLine());
			System.out.println(expr.transform(basicPrinter));
		}
	}

}
