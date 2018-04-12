package com.example.a2048.a2048.model;

import java.util.Vector;

public class Griglia {
    // Alloco una matrice di gioco 4x4
    public Numero[][] griglia = new Numero[4][4];

    /**
     * Crea una nuova griglia di gioco.
     * Inserisce 3 numeri casuali ('2' o '4') sparsi nella griglia.
     */
    public Griglia() {
        for (int i = 0; i < 3; i++) {
            try {
                aggiungiNumeroCasuale();
            } catch (GameOverException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Inserisce un 2 o 4 in una cella libera.
     * Se la griglia è piena viene sollevata una GameOverException.
     */
    public void aggiungiNumeroCasuale() throws GameOverException {
        // Ottengo le celle vuote nella griglia
        Vector<Posizione> celleVuote = ottieniCelleVuote();

        // Se ce n'è almeno una vuota
        if (celleVuote.size() != 0) {
            // Ne prendo una a caso
            int indiceCellaVuota = (int) Math.floor(Math.random() * celleVuote.size());
            int riga = celleVuote.get(indiceCellaVuota).riga;
            int colonna = celleVuote.get(indiceCellaVuota).colonna;

            // Scelgo a caso tra 2 e 4 e inizializzo il numero
            int valoreCasuale = (int) ((Math.floor(Math.random() * 2) + 1) * 2);
            Numero nuovoNumero = new Numero(valoreCasuale);
            griglia[riga][colonna] = nuovoNumero;
        } else {
            throw new GameOverException();
        }
    }

    /**
     * Sposta e somma i numeri verso una direzione. Poi aggiunge un numero in una cella disponibile.
     *
     * @param direzione direzione lungo la quale spostare e sommare i numeri
     * @throws GameOverException se non ci sono più celle vuote dopo aver spostato e
     *                           sommato le celle della griglia
     */
    public void muovi(Direzione direzione) throws GameOverException {
        // Inizialmente sposto i numeri in una direzione
        spostaVerso(direzione);

        // Se ci sono due numeri (n1, n2) uguali e adiacenti lungo la stessa direzione
        // raddoppia n1 e rimuove n2
        sommaAdiacentiUguali(direzione);

        // Poiché sono stati rimossi dei numeri, ri-sposta tutti i numeri nella stessa direzione
        spostaVerso(direzione);

        // Inserisce un numero casuale in una cella vuota (se esiste)
        aggiungiNumeroCasuale();
    }

    /**
     * @throws GameOverException se non ci sono più celle vuote dopo la mossa
     */
    public void muoviSu() throws GameOverException {
        muovi(Direzione.SU);
    }

    /**
     * @throws GameOverException se non ci sono più celle vuote dopo la mossa
     */
    public void muoviGiu() throws GameOverException {
        muovi(Direzione.GIU);
    }

    /**
     * @throws GameOverException se non ci sono più celle vuote dopo la mossa
     */
    public void muoviDestra() throws GameOverException {
        muovi(Direzione.DESTRA);
    }

    /**
     * @throws GameOverException se non ci sono più celle vuote dopo la mossa
     */
    public void muoviSinistra() throws GameOverException {
        muovi(Direzione.SINISTRA);
    }

    private Vector<Posizione> ottieniCelleVuote() {
        Vector<Posizione> celleVuote = new Vector<>();
        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < 4; c++) {
                if (griglia[r][c] == null) {
                    celleVuote.add(new Posizione(r, c));
                }
            }
        }
        return celleVuote;
    }

    private void sommaAdiacentiUguali(Direzione direzione) {
        for (int x = 0; x < 4; x++) {
            for (int y = 1; y < 4; y++) {
                Posizione posCorr = ottieniPosizione(direzione, x, y);
                Posizione posPrec = ottieniPosizione(direzione, x, y - 1);
                Numero numCorr = griglia[posCorr.riga][posCorr.colonna];
                Numero numPrec = griglia[posPrec.riga][posPrec.colonna];
                // Il numero corrente è uguale al precedente? Se si, sommo i due numeri
                if (numCorr != null && numPrec != null && numCorr.numero == numPrec.numero) {
                    numCorr.numero *= 2;
                    griglia[posPrec.riga][posPrec.colonna] = numCorr;
                    griglia[posCorr.riga][posCorr.colonna] = null;
                }
            }
        }
    }

    private void spostaVerso(Direzione direzione) {
        Numero[][] nuovaGriglia = new Numero[4][4];
        for (int x = 0; x < 4; x++) {
            int nuovaPosizioneY = 0;
            for (int y = 0; y < 4; y++) {
                Posizione posizione = ottieniPosizione(direzione, x, y);
                Numero cella = griglia[posizione.riga][posizione.colonna];
                if (cella != null) {
                    Posizione nuovaPosizione = ottieniPosizione(direzione, x, nuovaPosizioneY);
                    nuovaGriglia[nuovaPosizione.riga][nuovaPosizione.colonna] = cella;
                    nuovaPosizioneY++;
                }
            }
        }
        griglia = nuovaGriglia;
    }

    private Posizione ottieniPosizione(Direzione direzione, int xOffset, int yOffset) {
        int riga = yOffset, colonna = xOffset;
        switch (direzione) {
            case SU:
                riga = yOffset;
                colonna = xOffset;
                break;
            case GIU:
                riga = 3 - yOffset;
                colonna = xOffset;
                break;
            case SINISTRA:
                riga = xOffset;
                colonna = yOffset;
                break;
            case DESTRA:
                riga = 3 - xOffset;
                colonna = 3 - yOffset;
                break;
        }
        return new Posizione(riga, colonna);
    }
}
