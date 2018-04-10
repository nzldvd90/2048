package com.example.a2048.a2048.model;

import android.util.Pair;

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

    private Pair<Integer, Integer>[] ottieniCelleVuote() {
        Vector<Pair<Integer, Integer>> celleVuote = new Vector<>();
        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < 4; c++) {
                if(griglia[r][c] == null) {
                    celleVuote.add(new Pair<>(r, c));
                }
            }
        }

        return (Pair<Integer, Integer>[]) celleVuote.toArray();
    }

    /**
     * Inserisce un 2 o 4 in una cella libera.
     * Se la griglia è piena viene sollevata una Exception per segnalare il problema.
     */
    public void aggiungiNumeroCasuale() throws Exception {
        int riga = (int) Math.floor(Math.random() * 4);
        int colonna = (int) Math.floor(Math.random() * 4);
        if (griglia[riga][colonna] == null) {
            int valoreCasuale = (int) ((Math.floor(Math.random() * 2) + 1) * 2);
            Numero nuovoNumero = new Numero(valoreCasuale);
            griglia[riga][colonna] = nuovoNumero;
        }

    }

    /**
     * Sposta tutti i Numeri della griglia verso l'alto.
     * Se ci sono due numeri adiacenti uguali sulla stessa direzione il numero più in alto viene
     * cancellato, il numero più in basso viene raddoppiato e riposizionato.
     */
    public void muoviSu() {
    }

    public void muoviGiu() {
    }

    public void muoviDestra() {
    }

    public void muoviSinistra() {
    }

    // Metodo temporaneo, per testare la componente
    public void stampaGriglia() {
    }
}
