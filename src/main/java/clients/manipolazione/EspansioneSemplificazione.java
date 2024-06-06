package clients.manipolazione;

import node.Node;
import parse.ParsingException;
import parse.Polish;
import print.BasicPrinter;
import transform.Expand;
import transform.Simplify;

import java.util.Scanner;

/**
 * Classe contenete il client per verificare l'espansione (seguita dalla semplificazione) delle
 * espressioni.
 */
public class EspansioneSemplificazione {
	/**
	 * Client per verificare l'espansione, seguita da semplificazione, di un'espressione.
	 *
	 * <p>Legge una sequenza di linee dal flusso di ingresso standard in cui ciascuna linea
	 * rappresenta un'espressione in notazione polacca. Per ciascuna linea, costruisce l'espressione
	 * corrispondente, la <em>espande</em>, quindi <em>semplifica</em> l'espressione così ottenuta e
	 * emette la rappresentazione linearizzata dell'espressione semplificata nel flusso d'uscita
	 * standard.
	 *
	 * @param args non utilizzati.
	 */
	public static void main(String[] args) throws ParsingException {
		Scanner s = new Scanner(System.in);
		BasicPrinter printer = new BasicPrinter();
		while (s.hasNextLine()) {
			Node expr = Polish.parse(s.nextLine());
			Node expanded = expr.transform(new Expand());
			Node simplified = expanded.transform(new Simplify());
			System.out.println(simplified.transform(printer));
		}
	}
}
