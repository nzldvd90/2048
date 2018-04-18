package com.example.a2048.a2048;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a2048.a2048.model.Direzione;
import com.example.a2048.a2048.model.GameOverException;
import com.example.a2048.a2048.model.Griglia;
import com.example.a2048.a2048.model.Numero;
import com.example.a2048.a2048.model.Posizione;

import java.util.HashMap;

public class GameActivity extends AppCompatActivity {
    RelativeLayout grigliaView;

    Griglia griglia;

    HashMap<Numero, TextView> viewNumeri = new HashMap<>();

    int viewSize, dimensioneNumero;

    View.OnClickListener listenerAzioni = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                switch (v.getId()) {
                    case R.id.btnSu:
                        griglia.muovi(Direzione.SU);
                        break;
                    case R.id.btnGiu:
                        griglia.muovi(Direzione.GIU);
                        break;
                    case R.id.btnSinistra:
                        griglia.muovi(Direzione.SINISTRA);
                        break;
                    case R.id.btnDestra:
                        griglia.muovi(Direzione.DESTRA);
                        break;
                }
                aggiornaLayoutGriglia();
            } catch (GameOverException e) {
                // Quando termina il gioco, chiudo la activity e mostro un messaggio
                finish();
                Toast.makeText(getBaseContext(), "Hai perso!", Toast.LENGTH_LONG).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        inizializzaGriglia();

        findViewById(R.id.btnSu).setOnClickListener(listenerAzioni);
        findViewById(R.id.btnGiu).setOnClickListener(listenerAzioni);
        findViewById(R.id.btnSinistra).setOnClickListener(listenerAzioni);
        findViewById(R.id.btnDestra).setOnClickListener(listenerAzioni);
    }

    private void inizializzaGriglia() {
        grigliaView = findViewById(R.id.griglia);

        // Attendo che il layout venga posizionato per ottenere la dimensione della width e
        // inizializzare la griglia di gioco
        final ViewTreeObserver vto = grigliaView.getViewTreeObserver();

        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (griglia == null) {
                    viewSize = grigliaView.getMeasuredWidth();
                    dimensioneNumero = viewSize / 4;

                    ViewGroup.LayoutParams params = grigliaView.getLayoutParams();
                    params.width = viewSize;
                    params.height = viewSize;

                    grigliaView.setLayoutParams(params);
                    grigliaView.requestLayout();

                    griglia = new Griglia(new Griglia.Eventi() {
                        @Override
                        public void numeroCreato(Numero numero, Posizione nuovaPosizione) {
                            TextView nuovoNumero = (TextView) getLayoutInflater().inflate(R.layout.numero, null);
                            nuovoNumero.setText(numero.numero + "");
                            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(dimensioneNumero, dimensioneNumero);

                            // Posiziono il numero
                            params.topMargin = dimensioneNumero * nuovaPosizione.riga;
                            params.leftMargin = dimensioneNumero * nuovaPosizione.colonna;
                            grigliaView.addView(nuovoNumero, params);
                            viewNumeri.put(numero, nuovoNumero);
                        }

                        @Override
                        public void numeroRaddoppiato(Numero numero) {
                            viewNumeri.get(numero).setText(numero.numero + "");
                        }

                        @Override
                        public void numeroEliminato(Numero numero) {
                            TextView viewNumeroEliminato = viewNumeri.remove(numero);
                            grigliaView.removeView(viewNumeroEliminato);
                        }
                    });

                    aggiornaLayoutGriglia();
                }
            }
        });
    }

    void aggiornaLayoutGriglia() {
        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < 4; c++) {
                if (griglia.griglia[r][c] != null) {
                    TextView viewNumero = viewNumeri.get(griglia.griglia[r][c]);
                    spostaNumero(viewNumero, r, c);
                    viewNumero.setTextSize(dimensioneNumero / 6);
                }
            }
        }
    }

    public void spostaNumero(final TextView viewNumero, final int r, final int c) {
        final RelativeLayout.LayoutParams lpNumero = (RelativeLayout.LayoutParams) viewNumero.getLayoutParams();
        final int xDelta = dimensioneNumero * c - lpNumero.leftMargin;
        final int yDelta = dimensioneNumero * r - lpNumero.topMargin;
                lpNumero.topMargin += yDelta;
                lpNumero.leftMargin += xDelta;
                viewNumero.setLayoutParams(lpNumero);
    }

    public void nascondiNumero(final TextView viewNumero) {
                grigliaView.removeView(viewNumero);
    }
}
