package com.example.a2048.a2048.model;

import java.util.Vector;

public class Griglia {
    Numero[][] griglia = new Numero[4][4];

    public Griglia() {
        inizializza();
    }

    /**
     * Crea una nuova griglia di gioco.
     * Inserisce 3 numeri casuali ('2' o '4') sparsi nella griglia.
     */
    public void inizializza() {
        for (int i = 0; i < 3; i++) {
            try {
                aggiungiNumeroCasuale();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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

    /**
     * Inserisce un 2 o 4 in una cella libera.
     * Se la griglia è piena viene sollevata una Exception per segnalare il problema.
     */
    public void aggiungiNumeroCasuale() throws Exception {
        Vector<Posizione> celleVuote = ottieniCelleVuote();
        if (celleVuote.size() != 0) {
            int indiceCellaVuota = (int) Math.floor(Math.random() * celleVuote.size());
            int riga = celleVuote.get(indiceCellaVuota).riga;
            int colonna = celleVuote.get(indiceCellaVuota).colonna;
            int valoreCasuale = (int) ((Math.floor(Math.random() * 2) + 1) * 2);
            Numero nuovoNumero = new Numero(valoreCasuale);
            griglia[riga][colonna] = nuovoNumero;
        } else {
            throw new Exception("Non ci sono celle vuote");
        }
    }

    /**
     * Sposta tutti i Numeri della griglia verso l'alto.
     * Se ci sono due numeri adiacenti uguali sulla stessa direzione il numero più in alto viene
     * cancellato, il numero più in basso viene raddoppiato e riposizionato.
     */
    public void muoviSu() {
        for (int c = 0; c < 4; c++) {
            int posizioneSposta = 0;
            for (int r = 0; r < 4; r++) {
                if (griglia[r][c] != null) {
                    Numero cella = griglia[r][c];
                    if (posizioneSposta > 0) {
                        if (cella.numero == griglia[posizioneSposta - 1][c].numero) {
                            Numero cellaDaCancellare = griglia[posizioneSposta - 1][c];
                            cella.raddoppia();
                            cellaDaCancellare.elimina();
                            posizioneSposta--;
                        }
                    }

                    if (r != posizioneSposta) {
                        griglia[r][c] = null;
                        griglia[posizioneSposta][c] = cella;
                        cella.spostaIn(posizioneSposta, c);
                    }
                    posizioneSposta++;
                }
            }
        }
    }

    public void muoviGiu() {
    }

    public void muoviDestra() {
    }

    public void muoviSinistra() {
    }
}
