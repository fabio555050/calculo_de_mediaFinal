package com.example.calculadorademdiafinalcas;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

public class MainActivity extends AppCompatActivity {

    private TextInputEditText mediaAtividades;
    private TextInputEditText notaProva;
    private TextView mediaFinal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Button btnCalcular = findViewById(R.id.btnCalcular);
        btnCalcular.setOnClickListener(v -> calcularMedia());

        // 1️⃣ Referencie os campos
        mediaAtividades = findViewById(R.id.mediaAtividades);
        notaProva = findViewById(R.id.notaProva);

        // 2️⃣ Aplique o validador nos campos
        aplicarValidador(mediaAtividades);
        aplicarValidador(notaProva);
    }
    
    

    // 3️⃣ Função que cria e aplica o TextWatcher
    private void aplicarValidador(TextInputEditText campo) {
        campo.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                try {
                    String texto = s.toString().replace(',', '.');
                    if (!texto.isEmpty()) {
                        double valor = Double.parseDouble(texto);
                        campo.setError((valor < 0 || valor > 10) ? "Valor deve estar entre 0 e 10" : null);
                    } else {
                        campo.setError(null);
                    }
                } catch (NumberFormatException e) {
                    campo.setError("Número inválido");
                }
            }

            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        mediaAtividades.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                mediaFinal.setText("");
            }
        });

        notaProva.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                mediaFinal.setText("");
            }
        });

        mediaAtividades = findViewById(R.id.mediaAtividades);
        notaProva = findViewById(R.id.notaProva);
        mediaFinal =findViewById(R.id.mediaFinal);

        notaProva.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                calcularMedia(); // ou apenas calcularMedia(null) se preferir
                return true;
            }
            return false;
        });

    }

    public void calcularMedia() {
        String nAtividades = mediaAtividades.getText().toString().replace(',', '.');
        String nProva = notaProva.getText().toString().replace(',', '.');

        if (validarCampos(nAtividades, nProva)) {
            try {
                double notaAtv = Double.parseDouble(nAtividades);
                double notaPr = Double.parseDouble(nProva);

                if ((notaAtv < 0 || notaAtv > 10)) {
                    mediaAtividades.setError("Digite um valor entre 0 e 10");
                    return;
                }

                if ((notaPr < 0 || notaPr > 10)) {
                    notaProva.setError("Digite um valor entre 0 e 10");
                    return;
                }

                double resultado = (((notaPr * 4) + (notaAtv * 1)) / 5);
                DecimalFormat formato = new DecimalFormat("0.0000");
                DecimalFormatSymbols symbols = new DecimalFormatSymbols();
                symbols.setDecimalSeparator(',');
                formato.setDecimalFormatSymbols(symbols);
                //mediaFinal.setText("Média final: " + formato.format(resultado));

                esconderTeclado(getCurrentFocus()); // adapta sem view explícito

                // Criar um Intent para iniciar a próxima Activity
                Intent intent = new Intent(this, ResultadoActivity.class);
                intent.putExtra("media", resultado); // envia o valor calculado
                startActivity(intent);

                // Aplicar a animação
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                // Limpar campos
                mediaAtividades.setText("");
                notaProva.setText("");
                notaProva.clearFocus();
                mediaAtividades.clearFocus();

            } catch (NumberFormatException e) {
                Toast.makeText(this, "Digite valores válidos!", Toast.LENGTH_LONG).show();
            }
        } else {
            esconderTeclado(getCurrentFocus());
            Toast.makeText(this, "Preencha os dois campos antes de calcular", Toast.LENGTH_LONG).show();
        }
    }


    public boolean validarCampos(String nAtividades, String nProva) {
        boolean camposValidados = true;

        if (nAtividades.isEmpty()) {
            mediaAtividades.setError("Campo obrigatório");
            camposValidados = false;
        }

        if (nProva.isEmpty()) {
            notaProva.setError("Campo obrigatório");
            camposValidados = false;
        }

        return camposValidados;
    }

    private void esconderTeclado(View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}