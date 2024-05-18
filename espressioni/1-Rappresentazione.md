# Rappresentazione

Una *espressione* può avere diverse **rappresentazioni**. 

## Albero testuale

La rappresentazione più "fedele" è quella data da un **disegno testuale
dell'albero** (basato su caratteri unicode, in particolare quelli per il
[box-drawing](https://en.wikipedia.org/wiki/Box-drawing_characters)):

```
+            ^             *
├── 1        ├── +         ├── 3/4
╰── *        │   ├── x     ├── x
    ├── x    │   ╰── -1    ╰── +
    ╰── y    ╰── 2             ├── k
                               ╰── y
```

Tale disegno può essere ottenuto abbastanza semplicemente in modo ricorsivo
(potete prendere spunto dal tema svolto
[filesystem](https://prog2unimi-temi-svolti.netlify.app/temi/filesystem), in
particolare dal codice della funzione
[`recursiveTree`](https://github.com/prog2-unimi/temi-svolti/blob/master/temi/filesystem/Shell.java#L71-L97)).

## Forma linearizzata

Dall'albero è anche semplice costruire una rappresentazione **linearizzata**
definita induttivamente come segue:

* razionali e simboli sono rappresentati dal proprio valore;
* ogni nodo interno è rappresentato nella forma `O(E1, E2, ..., En)` dove `O`
  $\in$ {`+`, `*`, `^`} è il tipo del nodo e `E1`, `E2`, $\ldots$, `En` 
  sono le rappresentazioni dei figli (con $n\geq 1$);

data la definizione induttiva, è immediato osservare che tale rappresentazione
si può ottenere semplicemente tramite una visita dell'albero dell'espressione
stessa.

Ad esempio, le espressioni precedenti possono essere rappresentate come

```
+(1, *(x, y))
^(+(x, -1), 2)
*(3/4, x, +(k, y))
```

## Notazione algebrica (infissa)

Una rappresentazione più suggestiva (ma che **non è necessario implementare**) è
quella usuale dell'algebra, che può essere prodotta facendo uso di
[LaTeX](https://www.latex-project.org/); ad esempio, le espressioni precedenti
possono essere rappresentate come

$$
1 + x \cdot y, \qquad (x - 1)^2, \qquad \frac 3 4 \cdot x \cdot (k + y)
$$

Noti che in questa rappresentazione compaiono sia gli operatori binari $-$, $/$
che le parentesi (solo laddove opportune) che non sono direttamente presenti
nell'albero. Ciò significa che questa rappresentazione è decisamente meno banale
da produrre di quella linearizzata!

Qualora la cosa non dia adito ad ambiguità o confusione, nel seguito useremo una
a scelta delle rappresentazioni qui presentate per specificare le espressioni
(intese come alberi). 

Compreso come si rappresentano le espressioni algebriche, è possibile passare
alle modalità di [costruzione](2-Costruzione.md) delle medesime.