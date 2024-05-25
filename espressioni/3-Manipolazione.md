# Manipolazione

Una volta costruita una *espressione* è possibile *manipolarla* o *trasformarla*
in vari modi. Il semplice sistema da realizzare per questo progetto prevede tre
tipi di manipolazioni: 

* semplificazione, 
* espansione e 
* derivazione.

## Semplificazione

Sebbene in questo progetto non si tratterà mai esplicitamente del *valore* di
una espressione, è necessario definire formalmente cosa si intenda con tale
termine per poter introdurre la definizione di espressioni *equivalenti*. 

Il **valore** di una espressione per un certo *assegnamento* di valori razionali
ai suoi simboli è (in modo molto naturale) definito induttivamente come segue

* i simboli hanno il valore assegnato,
* il valore dei nodi interni si ottiene interpretando le operazioni come
  usualmente si fa con i numeri razionali,
* il *valore* dell'espressione è il valore della radice.

Due espressioni sono **equivalenti** quando il loro valore è identico qualunque
sia l'assegnamento scelto. Ovviamente due espressioni uguali sono anche
equivalenti, ma non necessariamente vero il contrario.

A titolo di esempio, le due espressioni 

```
+        *
├── x    ├── 2
╰── x    ╰── x
```

sono evidentemente diverse (sono alberi differenti), ma altrettanto
evidentemente equivalenti (non c'è assegnamento che possa distinguerne il
valore).

Per via del [teorema di
Richardson](https://en.wikipedia.org/wiki/Richardson's_theorem) in generale *non
è decidibile determinare se due espressioni siano equivalenti*, purtuttavia,
data una espressione non semplificata, è sempre possibile ottenere una
**espressione algebrica** ad essa equivalente, attraverso diverse manipolazioni
dell'albero, dette **semplificazioni**, che hanno in genere per obiettivo
ridurre il numero di nodi, definite induttivamente come segue.

* Ogni sotto-espressione le cui foglie sono tutte **razionali** può essere
  rimpiazzata con il razionale dato dal suo valore (nel senso ovvio del termine)
  ridotto ai minimi termini: ad esempio $x \cdot (1 + 4^{1/2})$ può essere
  semplificata in $3\cdot x$.

 Per tutte gli altri nodi, si procede dapprima semplificando tutti i
 sotto-alberi, quindi si procede a seconda del tipo del nodo come segue.

* Per i nodi **potenza**, se l'esponente è 0 il nodo può essere rimpiazzato con
  1, se viceversa è 1 con la base; similmente se la base è 0 può essere
  sostituito da 0 (qualunque sia l'esponente, se diverso dal razionale 0). Se la
  base è *razionale*, si tenta di ottenere una frazione: ad esempio $(3/4)^{-1}$
  si semplifica in $4/3$, così come $(4/9)^{3/2}$ si semplifica in $8/27$, ma se
  il numeratore o il numeratore elevati all'esponente non fossero razionali,
  restano espressi come potenza, ad esempio $(5/9)^{1/2} = 1/3\cdot 5^{1/2}$.

* I nodi **moltiplicazione** vengono semplificati attraverso varie
  trasformazioni: 

  * tutti i fattori che sono a loro volta moltiplicazioni sono sostituiti dai
    propri fattori: ad esempio $x \cdot (y^2 \cdot z)$ viene semplificato in 
    $x \cdot z \cdot y^2$;
    
  * si raccolgono i fattori razionali in un unico fattore razionale (che può
    essere omesso se pari a 1, o che può ridurre a 0 tutta la moltiplicazione se
    vale 0): ad esempio $3 \cdot x^2 \cdot 2 \cdot y$ viene semplificato in 
    $6 \cdot y\cdot x^2$;

  * si raccolgono i fattori non razionali identici sostituendoli con la potenza
    data dalla somma dei loro esponenti: ad esempio 
    $x^{-1} \cdot (1 + y^2)^2 \cdot x^4 \cdot (1 +y^2)$ viene semplificato in 
    $\left(1+ y^2\right)^3\cdot x^3$. 
  
* I nodi **addizione** vengono analogamente semplificati attraverso altre
  trasformazioni: 

  * tutti i termini che sono a loro volta addizioni vengono sostituiti dai
    propri addendi: ad esempio $x + (y^2 + z)$ viene semplificato in $x + z + y^2$;

  * si raccolgono i termini razionali in un unico termine razionale (che può
    essere omesso se pari a 0): ad esempio $3 + x^2 + 2 + y$ viene semplificato
    in $5 + y + x^2$;
  
  * si raccolgono i termini non razionali identici (a meno di un fattore
    moltiplicativo razionale) sostituendoli con il prodotto dato dalla somma dei
    fattori moltiplicativi: ad esempio 
    $3 \cdot x + (1 + y^2)^{-1} + 2 \cdot x + 3 \cdot (1 +y^2)^{-1}$ viene 
    semplificato in $4\cdot \left(1+ y^2\right)^{-1}+ 5\cdot x$.

## Espansione

Esiste un'altra importante manipolazione dell'albero di una espressione, che
prende il nome di **espansione**, e si basa in buona sostanza sulla *proprietà
distributiva* del prodotto rispetto alla somma.

Per tutti i nodi interni, si procede dapprima espandendo i sotto-alberi, quindi
si applicano le due manipolazioni seguenti.

* Nel caso della **moltiplicazione** (con almeno due fattori), si dividono i
  fattori in due espressioni: la prima corrispondente all'*espansione* del
  prodotto di tutti i fattori tranne l'ultimo e la seconda dall'ultimo fattore;
  a questo punto si procede moltiplicando tra di loro le due espressioni
  applicando, se almeno una delle due è una addizione, la *proprietà
  distributiva* (del prodotto rispetto alla somma). Ad esempio nel caso in cui
  solo una delle due espressioni sia una somma, come in $3 \cdot (x + y)$,
  l'espansione è data da $3\cdot x + 3\cdot y$, se viceversa sono due somme
  $(a + b)\cdot (c + d)$ l'espansione è $a\cdot c + a\cdot d + b\cdot c +
  b\cdot d$; infine, se la moltiplicazione ha più di due fattori, come in 
  $(a + b)\cdot (c + d)\cdot (e + f)$, la definizione induttiva porta
  all'espansione 
  $e\cdot (a\cdot c) + e\cdot (a\cdot d) + e\cdot (b\cdot c) + e\cdot (b\cdot d) + f\cdot (a\cdot c) + f\cdot (a\cdot d) + f\cdot (b\cdot c) + f\cdot (b\cdot d)$.  

* I nodi **potenza** con *esponente* $p / q$ vengono espansi come l'espansione
  del prodotto della *base* per se stessa $|p|$ volte, elevata a $\frac {p/|p|} q$: 
  ad esempio $(x+y)^{-2/3} = (x\cdot x + x\cdot y + x\cdot y + y\cdot y)^{-3}$. 
  Se l'esponente è $0$ la potenza viene espansa come $1$, se è $1$ come l'espansione
  della base.
  
* I nodi **addizione** non vengono ulteriormente modificati (ossia sono espansi
  come somma dell'espansione dei propri addendi).

Si noti come l'espansione di un'espressione possa portare ad un aumento del
numero di nodi, per questa ragione talvolta è utile, dopo l'espansione,
procedere alla semplificazione.

L'espansione è una operazione molto importante perché, in assenza di potenze con
esponente non naturale, consente di trasformare una qualunque espressione in un
[polinomio](https://en.wikipedia.org/wiki/Polynomial) (eventualmente
multivariato, se sono presenti più simboli diversi tra loro).

## Derivazione

Una ulteriore manipolazione dell'albero di una espressione è la **derivazione**
(rispetto ad una sola *variabile*); sebbene essa sia generalmente definita
attraverso il concetto di *limite*, ai fini del progetto verrà intesa come un
semplice trasformazione dell'albero (prescindendo dal sul significato analitico)
secondo le usuali [regole di
derivazione](https://en.wikipedia.org/wiki/Differentiation_rules) che sono di
seguito richiamate per i casi inerenti al progetto.

Fissata la **variabile**, ossia il *simbolo* rispetto a cui derivare, la
derivata dell'espressione $e$ si indica con 

$$
\frac d{dx} e
$$

dove $x$ è la variabile rispetto a cui si deriva. La derivata è definita
induttivamente come segue:

* per i **razionali** è pari a $0$;

* per i **simboli** essa è pari a $1$ solo nel caso del simbolo scelto, mentre è
  $0$;

* per le **potenze** si ha

$$
\frac{d}{dx} e^q = q\cdot e^{q-1} \cdot \frac{d}{dx} e
$$

*  mentre per le **addizioni** si ha

$$
\frac{d}{dx} \left[\sum_{i=1}^n e_i\right] = 
\sum_{i=1}^n \frac{d}{dx} e_i
$$

* infine, per le **moltiplicazioni** si ha

$$
\frac{d}{dx} \left[\prod_{i=1}^n e_i\right] = 
\sum_{i=1}^n \left(\left(\frac{d}{dx} e_i\right) \prod_{j=1, j\neq i}^n e_j\right)
$$

dove $q\in \mathbf{Q}$, $n > 0$ e $e, e_1, e_2, \ldots e_n$ sono espressioni.

Ad esempio, data l'espressione $(3\cdot x + a)^4$ la sua derivata rispetto alla
variabile $x$ è $12\cdot (3\cdot x + a)^3$; data l'espressione $(2\cdot x + a)^3$ 
la sua derivata rispetto alla variabile $a$ è $6\cdot (2\cdot x + a)^2$.

Ora che ha compreso come [rappresentare](./1-Rappresentazione.md), [costruire](./2-Costruzione.md) e [manipolare](./3-Manipolazione.md) le espressioni algebriche, può tornare al [testo principale](../README.md).
