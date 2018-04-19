package com.example.a2048.a2048;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a2048.a2048.model.Direzione;
import com.example.a2048.a2048.model.GameOverException;
import com.example.a2048.a2048.model.Griglia;
import com.example.a2048.a2048.model.Numero;
import com.example.a2048.a2048.model.Posizione;

import java.util.HashMap;

import static com.example.a2048.a2048.model.Direzione.*;

public class GameActivity extends AppCompatActivity {

    RelativeLayout grigliaView;

    int sizeGriglia, sogliaMinima;

    Griglia griglia;

    HashMap<Numero, TextView> listaNumeri = new HashMap<>();

    HashMap<Integer, Integer> coloreNumeri = new HashMap<>();

    public void mossa(Direzione direzione) {
        try {
            switch (direzione) {
                case SU:
                    griglia.muoviSu();
                    break;
                case GIU:
                    griglia.muoviGiu();
                    break;
                case SINISTRA:
                    griglia.muoviSinistra();
                    break;
                case DESTRA:
                    griglia.muoviDestra();
                    break;
            }
            aggiornaPosizioneNumeri();

        } catch (GameOverException e) {
            Intent intent = new Intent();
            intent.putExtra("status", "game-over");
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    private void aggiornaPosizioneNumeri() {
        for (int r = 0; r < 4; r++) {
            for (int c = 0; c < 4; c++) {
                if (griglia.griglia[r][c] != null) {
                    final TextView numeroView = listaNumeri.get(griglia.griglia[r][c]);

                    // Calcola la posizione e la dimensione del nuovo numero da inserire nella griglia
                    final RelativeLayout.LayoutParams paramsNumero = (RelativeLayout.LayoutParams) numeroView.getLayoutParams();

                    int toXDelta = c * paramsNumero.width - paramsNumero.leftMargin;
                    int toYDelta = r * paramsNumero.height - paramsNumero.topMargin;
                    Animation sposta = new TranslateAnimation(
                            0f, toXDelta,
                            0f, toYDelta);
                    sposta.setDuration(375);
                    sposta.setFillAfter(true);

                    final int finalC = c;
                    final int finalR = r;
                    sposta.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {
                            numeroView.clearAnimation();
                            paramsNumero.leftMargin = finalC * paramsNumero.width;
                            paramsNumero.topMargin = finalR * paramsNumero.width;
                            numeroView.setLayoutParams(paramsNumero);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
                    if (toXDelta != 0 || toYDelta != 0)
                        numeroView.startAnimation(sposta);
                }
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        grigliaView = findViewById(R.id.griglia);

        impostaColori();

        inizializzaGriglia();

        grigliaView.setOnTouchListener(new View.OnTouchListener() {
            float startX, startY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
                        startY = event.getY();
                        break;
                    case MotionEvent.ACTION_UP:
                        float deltaX = event.getX() - startX;
                        float deltaY = event.getY() - startY;
                        // Riconoscere un movimento orizzontale o verticale
                        if (Math.abs(deltaX) > Math.abs(deltaY)) {
                            if (Math.abs(deltaX) < sogliaMinima) {
                                return false; // Evento non gestito
                            }

                            // Movimento orizzontale (MuoviSinistra, MuoviDestra)
                            if (deltaX > 0) {
                                mossa(Direzione.DESTRA);
                            } else {
                                mossa(Direzione.SINISTRA);
                            }

                        } else {
                            if (Math.abs(deltaY) < sogliaMinima) {
                                return false; // Evento non gestito
                            }

                            // Movimento verticale (MuoviSu, MuoviGiu)
                            if (deltaY > 0) {
                                mossa(Direzione.GIU);
                            } else {
                                mossa(Direzione.SU);
                            }
                        }
                        break;
                }
                return true; // Evento gestito
            }
        });
    }

    private void inizializzaGriglia() {
        ViewTreeObserver vto = grigliaView.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                sizeGriglia = grigliaView.getMeasuredWidth();
                sogliaMinima = sizeGriglia / 8; // Spostamento minimo = 1/2 dimensione del numero


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

                            Animation compari = new ScaleAnimation(
                                    0f, 1f, // X: 0% --> 100%
                                    0f, 1f, // Y: 0% --> 100%
                                    Animation.RELATIVE_TO_SELF, 0.5f, // centro rispetto a X
                                    Animation.RELATIVE_TO_SELF, 0.5f // centro rispetto a Y
                            );
                            compari.setDuration(375);
                            compari.setStartOffset(375);
                            viewNumero.startAnimation(compari);

                            // Inserisce la nuova vista con le dimensioni calcolate sopra
                            grigliaView.addView(viewNumero, paramsNumero);

                            listaNumeri.put(numero, viewNumero);
                        }

                        @Override
                        public void numeroRaddoppiato(Numero numero) {
                            TextView numeroRaddoppiatoView = listaNumeri.get(numero);
                            numeroRaddoppiatoView.setText(numero.numero + "");
                            numeroRaddoppiatoView.setBackgroundColor(coloreNumeri.get(numero.numero));
                            if (numero.numero > 99) {
                                numeroRaddoppiatoView.setTextSize(sizeGriglia / 25);
                            }
                            if (numero.numero > 999) {
                                numeroRaddoppiatoView.setTextSize(sizeGriglia / 30);
                            }

                            if (numero.numero == 2048) {
                                Intent intent = new Intent();
                                intent.putExtra("status", "win");
                                setResult(RESULT_OK, intent);
                                finish();
                            }
                        }

                        @Override
                        public void numeroEliminato(Numero numero) {
                            final TextView vistaNumeroEliminato = listaNumeri.remove(numero);

                            Animation scompari = new ScaleAnimation(
                                    1f, 0f, // X: 100% --> 0%
                                    1f, 0f, // Y: 100% --> 0%
                                    Animation.RELATIVE_TO_SELF, 0.5f, // centro rispetto a X
                                    Animation.RELATIVE_TO_SELF, 0.5f // centro rispetto a Y
                            );
                            scompari.setDuration(375);
                            scompari.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {
                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    grigliaView.removeView(vistaNumeroEliminato);
                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {
                                }
                            });

                            vistaNumeroEliminato.startAnimation(scompari);
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
