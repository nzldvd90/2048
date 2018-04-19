package com.example.a2048.a2048;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class WelcomeActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_FOR_GAME_ACTIVITY = 1;

    Button btnPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        // Recuperare i componenti grafici per effettuare il binding
        btnPlay = findViewById(R.id.btnPlay);

        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), GameActivity.class);
                startActivityForResult(intent, REQUEST_CODE_FOR_GAME_ACTIVITY);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // La Game Activity ha terminato con uno stato
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_FOR_GAME_ACTIVITY) {
            String stato = data.getStringExtra("status");
            if (stato != null && stato.equals("game-over")) {
                Log.i("STATO", "GAME OVER");
            }
            if (stato != null && stato.equals("win")) {
                Log.i("STATO", "WIN");
            }
        }

    }
}
