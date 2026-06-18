public class Query1 {
    /***
    Class Responsibility: implement Query #1 using Flink.

     Query 1: Q1 La query Q1 si concentra sul monitoraggio in tempo reale dello stato operativo delle principali compagnie
     aeree. Facendo riferimento ai voli relativi alle compagnie AA (American Airlines), DL (Delta),
     UA (United) e WN (Southwest), aggregare gli eventi usando finestre tumbling di durata pari a 1 ora,
     basate sull’event time.

     Per ciascuna finestra e per ciascuna compagnia, calcolare:
         • il numero totale di voli osservati;
         • il numero di voli completati (cio`e non cancellati e non deviati), cancellati e deviati;
         • il valor medio di DEP DELAY, considerando solo i voli non cancellati;
         • il tasso di cancellazione, definito come percentuale di voli cancellati sul totale dei voli osservati
         nella finestra;
         • il tasso di partenze in ritardo, definito come percentuale di voli non cancellati con DEP DELAY
         maggiore di 15 minuti.

     L’output della query deve avere il seguente schema:
     window start, window end, airline, num flights, completed, cancelled,
     diverted, dep delay mean, cancellation rate, late departure rate

     */
}
