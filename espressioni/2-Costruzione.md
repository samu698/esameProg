# Costruzione

Ci sono diversi modi per **costruire** una *espressione*.

Il modo più diretto è quello di costruire direttamente l'albero a partire dai
nodi foglia, componendone man mano i nodi interni.

Diverso è il caso in cui si voglia costruire una espressione a partire da una
qualche "rappresentazione" dell'espressione stessa. Sfortunatamente è molto
complesso partire dalla rappresentazione più naturale (che è quella mostrata per
ultima nella precedente sezione), così come non è del tutto banale farlo a
partire dalla forma linearizzata. 

Ci sono però due rappresentazioni a partire dalle quali è più semplice costruire
una espressione:

* la *notazione polacca* e
* i *programmi lineari*,

che saranno mostrate nelle prossime sezioni.

## Notazione polacca

Uno dei metodi più semplici per costruire delle espressione è legato ad una loro
rappresentazione priva di parentesi inventata dal logico polacco [Jan
Łukasiewicz](https://en.wikipedia.org/wiki/Jan_Łukasiewicz) nel 1924, detta
[notazione polacca](https://en.wikipedia.org/wiki/Polish_notation); la
semplicità di tale approccio l'ha reso il modo di elezione per la costruzione di
molti modelli di calcolatrice tascabile a partire dagli anni '60, comprese le
famose calcolatrici scientifiche prodotte da Hewlett-Packard.

L'idea è considerare solo operazioni di arietà fissata (nel nostro caso, 2) e di
anteporre l'operatore agli operandi che lo riguardano. Ad esempio, l'espressione
$2 \cdot (3 + x)$ può essere scritta in notazione polacca come `* 2 + 3 x`.

Per costruire una espressione a partire da tale rappresentazione si procede
leggendola da destra verso sinistra e facendo uso di una *pila* di espressioni;
se la parte letta è:

* un *intero* o un *simbolo* si mette in cima alla pila la foglia
  corrispondente,
* un *operatore* si estraggono dalla pila tanti elementi quanti sono i suoi
  operandi (nel nostro caso sempre 2), si costruisce il nodo corrispondente e lo
  si rimette in cima alla pila.

Nel caso della **potenza** è opportuno *semplificare* (come illustrato in
seguito) l'esponente per poter determinare efficacemente se sia *razionale* o
meno.

Secondo l'esempio precedente, la pila inizialmente vuota viene popolata come
segue:

* si incontra e mette in pila `x`,
* si incontra e mette in pila `3`,
* si incontra `+`, quindi si estraggono `3` e `x` dalla pila, si costruisce il
  nodo `+(3, x)` e si mette in pila `+(3, x)`,
* si incontra e mette in pila `2`,
* si incontra `*`, quindi si estraggono `2` e `+(3, x)` dalla pila, si
  costruisce il nodo `*(2, +(3, x))` e si mette in pila `*(2, +(3, x))`;

alla fine del processo la pila contiene un solo elemento, corrispondente
all'espressione desiderata.

Per garantire maggior brevità, la notazione polacca contiene anche gli operatori
`-` e `/` che, non essendo presenti come nodi delle espressioni, vanno resi in
modo opportuno tramite le operazioni di moltiplicazione e potenza. Ad esempio, a
`- 3 x` corrisponde l'espressione `+(3, *(-1, x))` mentre a `/ 3 x` corrisponde
`*(3, ^(x, -1))`. 

Presti però attenzione all'associatività degli operatori non commutativi: ad
esempio `- - x y z` corrisponde a $(x-y)-z=x-y-z$ mentre `- x - y z` corrisponde
a $x-(y-z)=x-y+z$; similmente `/ / x y z` corrisponde a $(x/y)/z=\frac x {y\cdot z}$
mentre `/ x / y z` corrisponde a $x/(y/z)=\frac {x\cdot z} y$; infine `^ ^
x p q` (dove `p` e `q` sono razionali) corrisponde a $(x^p)^q=x^{p\cdot q}$
mentre `^ x ^ p q` corrisponde a $x^{(p^q)} = x^{p^q}$.

## Programmi lineari

Un modo alternativo per costruire una espressione è utilizzare un [programma
lineare](https://en.wikipedia.org/wiki/Straight-line_program), ossia una
sequenza di *istruzioni* $I_1, I_2, \ldots I_n$ (con $n\geq 1$) che non
comprendono iterazione, o selezione. Nel nostro caso ciascuna istruzione
definisce una espressione $e_i$ nel modo seguente:

* se $I_i$ è data da un punto `.` seguito da un *simbolo* o *razionale* allora
  $e_i$ è l'espressione corrispondente,
* se $I_i$ è data da un operando in $\in$ {`+`, `-`, `*`, `/`, `^`}
  seguita da una lista di *indici*, ossia interi i_1, i_2, \dots i_k$ allora
  $e_i$ è l'espressione corrispondente all'operando applicato alle espressioni
  $e_{i_1}, e_{i_2}, \ldots e_{i_k}$ (dove $1 \leq i_j < i$ per ogni $1 \leq j\leq k$ e $k \geq 1$).

Anche secondo questo modo di costruzione, nel caso della **potenza** è opportuno
*semplificare* (come illustrato in seguito) l'esponente per poter determinare
efficacemente se sia *razionale* o meno.

Ad esempio, al programma lineare

```
. x
. 12
+ 0 1
. 7
. 6
. 5
- 3 4 5
^ 2 3 6
/ 3 4 5 0
```

corrisponde la sequenza di espressioni

$$
\begin{align*}
e_0 &= x \\
e_1 &= 12 \\
e_2 &= x + 12 \\
e_3 &= 7 \\
e_4 &= 6 \\
e_5 &= 5 \\
e_6 &= 7 - 6 - 5 \\
e_7 &= (x + 12)^{1/2401} \\
e_8 &= \left((7/6)/5\right)/x
\end{align*}
$$

da cui si evince, tra l'altro, sono presenti tutti gil operatori della notazione
polacca, ma non hanno arietà fissata (compreso `^`); si nota inoltre che, come
comunemente noto, l'elevamento a potenza è *associativo a destra* mentre tutti
gli altri operatori sono *associativi a sinistra*.

Una volta costruita l'espressione, è talvolta utile procede ad alcune
[manipolazioni](3-Manipolazione.md) della medesima.