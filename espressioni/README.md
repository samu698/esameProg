# Le espressioni algebriche

Nel contesto del progetto, per **espressione algebrica** si intende un [*albero*
$n$-ario ordinato](https://en.wikipedia.org/wiki/Ordered_tree) i cui nodi sono
definiti induttivamente come segue, le **foglie** sono di due *tipi*

  * numeri **razionali** $q$ con $q\in \mathbf{Q}$, oppure 
  * **simboli** $x$ con $x\in\{\text{a}, \text{b}, \ldots, \text{z}\}$;

mentre i **nodi interni** sono di tre *tipi*

  * **addizioni** con figli $e_1, e_2, \ldots e_n$ denominati *addendi*,
  * **moltiplicazioni** con figli $e_1, e_2, \ldots e_n$ denominati *fattori*,
  * **potenze** con figli $e, q$ corrispondenti rispettivamente alla *base* ed
    *esponente* della potenza.

dove $n > 0$, $e, e_1, e_2, \ldots e_n$ sono espressioni e $q\in \mathbf{Q}$.

Per comodità assumeremo che i *razionali* siano sempre in **forma normale** che
corrisponde a dire che:

* il *denominatore* sia *positivo* e
* il *numeratore* e il *denominatore* siano *coprimi tra loro*;

questo significa che $\frac 3 {-6}$ (qualora dovesse emergere dalla
*costruzione* o *manipolazione* descritte in seguito) sarà sempre riscritto come
$\frac {-1} 2$.

Tra i nodi vige un **ordine totale** $\preceq$ definito a partire dall'ordine
tra i tipi di nodo (foglia, o interno) per cui 

$$
\text{razionali} 
\preceq \text{simboli}
\preceq \text{potenze}
\preceq \text{moltiplicazioni}  
\preceq \text{addizioni}    
$$

e tale che, tra due nodi dello stesso tipo, viga l'ordine derivato da quello del
loro valore (per le foglie), o lessicografico rispetto ai due elenchi di
sotto-alberi (per i nodi interni). Questo ordine determina, tra l'altro,
l'ordine stesso dei sotto-alberi dei nodi $n$-ari.

Le espressione algebriche hanno diversi modi per essere:

* [rappresentate](1-Rappresentazione.md),
* [costruite](2-Costruzione.md) e 
* [manipolate](3-Manipolazione.md);

legga attentamente i tre documenti elencati nel precedente elenco prima di
tornare al [testo principale](../README.md).