package clients.manipolazione;

import luppolo.node.Node;
import luppolo.parse.Polish;
import luppolo.print.BasicPrinter;
import luppolo.transform.Simplify;

import java.text.ParseException;
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
	public static void main(String[] args) throws ParseException {
		Scanner s = new Scanner(System.in);
		BasicPrinter printer = new BasicPrinter();
		while (s.hasNextLine()) {
			Node expr = Polish.parse(s.nextLine());
			Node simplified = expr.transform(new Simplify());
			System.out.println(simplified.transform(printer));
		}
	}
}