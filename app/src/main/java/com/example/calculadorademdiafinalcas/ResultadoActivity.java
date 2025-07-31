package com.example.calculadorademdiafinalcas;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ResultadoActivity extends AppCompatActivity {

    private TextView resultadoText;
    private Button btnVoltar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_resultado);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setContentView(R.layout.activity_resultado);

        resultadoText = findViewById(R.id.textResultado);
        btnVoltar = findViewById(R.id.btnVoltar);

        // Recebe a média
        double media = getIntent().getDoubleExtra("media", 0);
        resultadoText.setText(String.valueOf(media));

        // Voltar à tela principal
        btnVoltar.setOnClickListener(v -> finish());
    }
}