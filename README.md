# Luppolo

Questo repository contiene il **progetto d'esame** dell'insegnamento di
"Programmazione II" della sessione estiva dell'a.a 2023/2024.

Obiettivo del progetto è costruire un rudimentale [sistema computerizzato per
l'algebra](https://en.wikipedia.org/wiki/Computer_algebra) denominato
**Luppolo** in grado di effettuare semplici manipolazioni di [espressioni
algebriche](https://en.wikipedia.org/wiki/Algebraic_espressione).

> **Nota bene**: per informazioni tecniche su come svolgere il progetto,
> consulti le specifiche [istruzioni per
> l'uso](https://gitlab.di.unimi.it/prog2#istruzioni-duso) del materiale
> didattico presente su GitLab@DI.
> 
> Per **chiedere chiarimenti** sul testo, o **segnalare errori o imprecisioni**,
> usi *esclusivamente* la sezione
> [Issues](https://gitlab.di.unimi.it/prog2/progetti/240610/-/issues) del
> *repository pubblico* (**non di questa copia personale**); eventuali richieste
> pervenute per email saranno ignorate. Consulti le *issue* con frequenza, per
> **verificare che il docente non abbia segnalato problemi, o chiarito dubbi,
> rivolgendosi a tutti**.
>
> Presti anche attenzione alle **osservazioni a lei personalmente rivolte dal
> docente** che le verranno comunicate tramite la sezione [Issues](../../issues)
> di *questa suo repository privato*. Se al termine del suo lavoro ci saranno
> **issue aperte, il progetto non sarà valutato**.

## Descrizione

Per portare a termine il lavoro dovrà decidere se e quali classi (concrete o
astratte) e quali interfacce implementare. Per ciascuna di esse **dovrà
descrivere** (preferibilmente in formato Javadoc, ma comunque solo attraverso
commenti presenti nel codice) le scelte relative alla **rappresentazione** dello
stato (con particolare riferimento all'**invariante di rappresentazione** e alla
**funzione di astrazione** così come illustrati a lezione e definiti nel libro
di testo dell'insegnamento) e ai **metodi** (con particolare riferimento a
*pre/post-condizioni* ed *effetti collaterali*, soffermandosi ad illustrare le
ragioni della *correttezza* solo per le implementazioni che riterrà più
critiche). Osservi che l'esito di questa prova, che le consentirà di accedere o
meno all'orale, si baserà tanto su questa documentazione quanto sul codice
sorgente.

> Presti grande attenzione ai **test** (che può eseguire con il comando `gradle
> test`, come illustrato nelle istruzioni per [eseguire i test in locale e
> individuare i difetti](https://gitlab.di.unimi.it/prog2#eseguire-i-test-in-locale-e-individuare-i-difetti)):
> **in presenza anche di un solo errore, o "skip", nei test, il progetto non
> sarà valutato**. Osservi che condizione necessaria affinché i test possano
> essere eseguiti è che il progetto *compili correttamente senza errori o
> warning*; chi volesse compilare le classi senza l'uso di `gradle` deve
> aggiungere le opzioni `-Xlint:all -Werror` al comando `javac`. 
>
> Anche la **documentazione** deve essere completa e la sua compilazione, con il
> comando `gradle javadoc` deve terminare *senza errori o warning*, viceversa
> l'intero progetto **non sarà valutato**; chi volesse compilare la
> documentazione senza l'uso di `gradle` deve aggiungere le opzioni
> `-Xdoclint:all -Werror` al comando `javadoc`.


## Le espressioni algebriche

La directory `espressioni` di questo repository contiene una serie di documenti
che specificano cosa si intenda per **espressioni algebriche** e quali
funzionalità debba implementare il progetto. Può leggere i documenti nel giusto
ordine secondo il seguente elenco:

* la [definizione formale](./espressioni/README.md) di cosa qui si intenda per
  *espressione algebrica*,
* come esse possono essere [rappresentarte](./espressioni/1-Rappresentazione.md),
* in che modo sia possibile [costruirle](./espressioni/2-Costruzione.md) e
* a che tipo di [manipolazioni](./espressioni/3-Manipolazione.md) possono essere
  soggette.

> Legga con attenzione tutti i **quattro documenti citati nel precedente
> elenco** prima di procedere con la lettura del progetto!

## Cosa è necessario implementare

Dovrà implementare una gerarchia di oggetti utili a:

* realizzare le **espressioni algebriche** con i vari tipi di **nodo**
  (*interni* e *foglie*) che le compongono;
* emettere alcune **rappresentazioni** delle espressioni, almeno quella del
  disegno dell'[*albero
  testuale*](./espressioni/1-Rappresentazione.md#albero-testuale) e la [*forma
  linearizzata*](./espressioni/1-Rappresentazione.md#forma-linearizzata);
* **costruire** le espressioni, almeno a partire dalla [*notazione
  polacca*](./espressioni/2-Costruzione.md#notazione-polacca) e dai [*programmi
  lineari*](./espressioni/2-Costruzione.md#programmi-lineari);
* **manipolare** le espressioni, almeno per la
  [*semplificazione*](./espressioni/3-Manipolazione.md#semplificazione),
  [*espansione*](./espressioni/3-Manipolazione.md#espansione) e
  [*derivazione*](./espressioni/3-Manipolazione.md#derivazione).

Al fine di evitare confusione con il pacchetto di utilità e con i client
(descritti nella prossima sezione), è consigliabile che le classi della sua
soluzione siano contenute un un pacchetto (e in un numero ragionevole di
eventuali sotto-pacchetti) a parte, ad esempio nel pacchetto `luppolo` i cui
sorgenti dovranno essere nella directory `src/main/java/luppolo`.

Per evitare il rischio di perdere il lavoro svolto, **effettui consegne
periodiche** del suo materiale con il comando `gradle consegna`. Osservi che
**verrà valutata esclusivamente l'ultima versione consegnata entro il
mezzogiorno della data dell'appello**.

### I client di test

Secondo quanto illustrato nelle istruzioni per [eseguire i test in locale e
individuare i
difetti](https://gitlab.di.unimi.it/prog2#eseguire-i-test-in-locale-e-individuare-i-difetti),
per guidarla nel processo di sviluppo in `src/main/java/clients/` le sono
forniti alcuni scheletri di test sotto forma di "client" di cui **lei dovrà
produrre una implementazione** (secondo le specifiche che troverà nel Javadoc di
ciascuno di essi) *facendo uso delle classi della sua soluzione*; per ciascun
client, nella sottodirectory di `tests/clients/` avente nome uguale a quello
della classe client, si trovano un corredo di file di input e output che le
permetteranno di verificare il corretto funzionamento del suo codice.

Si ricorda che **il progetto non sarà valutato a meno che tutti i test** svolti
tramite il comando `gradle test` **diano esito positivo** .

### L'architettura della soluzione

Al fine di ottenere la miglior valutazione possibile il codice dovrà essere
progettato in modo da rendere *ragionevolmente semplice una eventuale futura
estensione* che preveda altre:

* modalità di *costruzione*,
* forme di *rappresentazione* e
* *manipolazioni*

e, in secondo luogo, altri *tipi* di nodo. Altrimenti detto, la scelta di
implementare interamente all'interno del codice dei nodi tutte le funzionalità
sopra citate potrebbe costituire un limite nel raggiungimento di una valutazione
elevata.

Per ottenere l'opportuno disaccoppiamento tra le *operazioni* sopra descritte e
i vari *tipi* di nodo, è *possibile* (ma **in nessun modo obbligatorio**) usare
il pattern *visitor* che potete trovare descritto su
[Wikipedia](https://en.wikipedia.org/wiki/Visitor_pattern)  in un articolo del
[Java
Magazine](https://blogs.oracle.com/javamagazine/post/the-visitor-design-pattern-in-depth)
di Oracle.


## Codice di condotta

Dovendo svolgere il progetto a casa non le vengono imposte particolari
restrizioni delle quali sarebbe peraltro difficile verificare il rispetto.

Le è pertanto **consentito** di avvalersi:

* di qualunque risorsa disponibile in rete,
* di strumenti di supporto basati sull'AI (come [GitHub
  Copilot](https://github.com/features/copilot), o
  [ChatGPT](https://chat.openai.com/)),
* del *confronto* con altri studenti, o professionisti,

sia per la *progettazione* che per l'*implementazione* e *documentazione* del
codice. Ogni supporto che la aiuti a apprendere e dominare gli obiettivi
culturali dell'insegnamento è benvenuto! 

D'altro canto le viene  **formalmente richiesto di elencare** (nella
documentazione del codice) in modo chiaro ed esaustivo **ogni risorsa di cui si
è avvalso al di fuori di quelle esplicitamente indicate come materiale
didattico** dell'insegnamento. L'omissione di tale elenco può costituire motivo
di **respingimento** del progetto e, in gravi casi di *plagio* alle **sanzioni
disciplinari** previste.

Si sottolinea che consegnando il progetto lei dichiara di fatto di esserne
l'**unico autore**, assumendosi la piena responsabilità dell'**originalità** del
codice e della documentazione che esso include, nonché della completezza e
veridicità del suddetto elenco. Per questa ragione **non le è consentito
condividere il suo codice con altri studenti**.

Durante la discussione orale, eventuali incertezze nell'*illustrare*,
*giustificare* o *modificare* il materiale consegnato non potranno che essere a
lei esclusivamente addebitate e, come tali, **valutate negativamente**.

## Note legali e copyright

Ai sensi della Legge n. 633/1941 e successive modificazioni, l'autore si
riserva, in ogni forma e modo nei limiti fissati dalla legge, il diritto
esclusivo di pubblicare e di utilizzare il materiale contenuto nel presente
repository.

Più specificatamente, è fatto **divieto di riprodurre, trascrivere, comunicare
al pubblico, distribuire, tradurre, elaborare e modificare il presente
materiale** (codice sorgente compreso), in tutto o in parte, senza specifica
autorizzazione scritta dell'autore.