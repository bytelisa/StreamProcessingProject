package it.uniroma2.sabd2.queries;

public class Query2Job {
    /***
     *     Class Responsibility: implement Query #1 using Flink.
     *
     *     Q2 La query Q2 si concentra sull’identificazione, in tempo reale, degli aeroporti di partenza maggiormente
     * interessati da ritardi significativi.
     * Considerare tutti i voli nello stream e aggregare gli eventi per aeroporto di partenza (ORIGIN AIRPORT ID)
     * usando finestre temporali basate sull’event time. Si consideri come ritardo significativo in partenza
     * un volo non cancellato e non deviato con DEP DELAY maggiore di 30 minuti.
     *
     * Calcolare la query sulle seguenti finestre temporali:
         * • 1 ora (event time);
         * • 6 ore (event time);
         * • dall’inizio del dataset.
     *
     * Per ciascuna finestra, calcolare la classifica aggiornata in tempo reale dei primi 10 aeroporti di partenza
     * ordinati per numero decrescente di voli con ritardo significativo in partenza.
     * Per ogni aeroporto in classifica, riportare:
         * • il numero totale di voli osservati nella finestra;
         * • il numero di voli con ritardo significativo in partenza;
         * • il valor medio di DEP DELAY, considerando solo i voli non cancellati e non deviati;
         * • il massimo valore di DEP DELAY osservato nella finestra;
         * • una lista dei voli con ritardo significativo in partenza, riportando almeno compagnia aerea, aeroporto
         * di destinazione e valore di DEP DELAY.
     *
     * Nota: Poich´e la lista potrebbe essere lunga,
     * si chiede di riportare al pi`u i primi 20 voli con ritardo significativo, ordinati per DEP DELAY
     * decrescente.
     * Si considerino, per ciascuna finestra, solo gli aeroporti con almeno 30 voli non cancellati e non deviati.
     */

}
