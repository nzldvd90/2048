package com.example.a2048.a2048;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a2048.a2048.model.GameOverException;
import com.example.a2048.a2048.model.Griglia;
import com.example.a2048.a2048.model.Numero;
import com.example.a2048.a2048.model.Posizione;

import java.util.HashMap;

public class GameActivity extends AppCompatActivity {

    RelativeLayout grigliaView;

    int sizeGriglia;

    Griglia griglia;

    HashMap<Numero, TextView> listaNumeri = new HashMap<>();

    HashMap<Integer, Integer> coloreNumeri = new HashMap<>();

    View.OnClickListener buttonListeners = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            try {
                switch (v.getId()) {
                    case R.id.btnSu:
                        griglia.muoviSu();
                        break;
                    case R.id.btnGiu:
                        griglia.muoviGiu();
                        break;
                    case R.id.btnSinistra:
                        griglia.muoviSinistra();
                        break;
                    case R.id.btnDestra:
                        griglia.muoviDestra();
                        break;
                }
                aggiornaPosizioneNumeri();

            } catch (GameOverException e) {
                finish();
                Toast.makeText(getBaseContext(), "Game over!", Toast.LENGTH_LONG).show();
            }
        }
    };

    private void aggiornaPosizioneNumeri() {
        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < 4; c++) {
                if (griglia.griglia[r][c] != null) {
                    TextView numeroView = listaNumeri.get(griglia.griglia[r][c]);

                    // Calcola la posizione e la dimensione del nuovo numero da inserire nella griglia
                    RelativeLayout.LayoutParams paramsNumero = (RelativeLayout.LayoutParams) numeroView.getLayoutParams();
                    paramsNumero.leftMargin = c * paramsNumero.width;
                    paramsNumero.topMargin = r * paramsNumero.width;
                    numeroView.setLayoutParams(paramsNumero);
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        grigliaView = findViewById(R.id.griglia);

        impostaColori();

        findViewById(R.id.btnSu).setOnClickListener(buttonListeners);
        findViewById(R.id.btnGiu).setOnClickListener(buttonListeners);
        findViewById(R.id.btnSinistra).setOnClickListener(buttonListeners);
        findViewById(R.id.btnDestra).setOnClickListener(buttonListeners);

        ViewTreeObserver vto = grigliaView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                sizeGriglia = grigliaView.getMeasuredWidth();

                // Rendo la Height == alla width
                ViewGroup.LayoutParams params = grigliaView.getLayoutParams();
                params.height = sizeGriglia;
                grigliaView.setLayoutParams(params);

                if (griglia == null) {

                    griglia = new Griglia(new Griglia.Eventi() {
                        @Override
                        public void numeroCreato(Numero numero, Posizione nuovaPosizione) {
                            // Costruisce un nuovo TextView per il nuovo numero
                            TextView viewNumero = (TextView) getLayoutInflater().inflate(R.layout.numero, null);

                            // Imposta come testo della TextView il valore del numero
                            viewNumero.setText(numero.numero + "");

                            viewNumero.setBackgroundColor(coloreNumeri.get(numero.numero));

                            // Calcola la posizione e la dimensione del nuovo numero da inserire nella griglia
                            RelativeLayout.LayoutParams paramsNumero = new RelativeLayout.LayoutParams(sizeGriglia / 4, sizeGriglia / 4);
                            paramsNumero.leftMargin = nuovaPosizione.colonna * paramsNumero.width;
                            paramsNumero.topMargin = nuovaPosizione.riga * paramsNumero.width;

                            viewNumero.setTextSize(sizeGriglia / 20);
                            if(numero.numero > 99) {
                                viewNumero.setTextSize(sizeGriglia / 25);
                            }
                            if(numero.numero > 999) {
                                viewNumero.setTextSize(sizeGriglia / 30);
                            }

                            // Inserisce la nuova vista con le dimensioni calcolate sopra
                            grigliaView.addView(viewNumero, paramsNumero);

                            listaNumeri.put(numero, viewNumero);
                        }

                        @Override
                        public void numeroRaddoppiato(Numero numero) {
                            TextView numeroRaddoppiatoView = listaNumeri.get(numero);
                            numeroRaddoppiatoView.setText(numero.numero + "");
                            numeroRaddoppiatoView.setBackgroundColor(coloreNumeri.get(numero.numero));
                        }

                        @Override
                        public void numeroEliminato(Numero numero) {
                            TextView vistaNumeroEliminato = listaNumeri.remove(numero);
                            grigliaView.removeView(vistaNumeroEliminato);
                        }
                    });
                }
            }
        });

    }

    private void impostaColori() {
        coloreNumeri.put(2, Color.parseColor("#ffd289"));
        coloreNumeri.put(4, Color.parseColor("#facc6b"));
        coloreNumeri.put(8, Color.parseColor("#ffd131"));
        coloreNumeri.put(16, Color.parseColor("#f5b82e"));
        coloreNumeri.put(32, Color.parseColor("#f4ac32"));
        coloreNumeri.put(64, Color.parseColor("#db8c04"));
        coloreNumeri.put(128, Color.parseColor("#d97b29"));
        coloreNumeri.put(256, Color.parseColor("#d9631e"));
        coloreNumeri.put(512, Color.parseColor("#bf3415"));
        coloreNumeri.put(1024, Color.parseColor("#a60d0d"));
        coloreNumeri.put(2048, Color.parseColor("#a60d0d"));
    }
}
