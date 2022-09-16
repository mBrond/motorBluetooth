package com.example.appbluetooth;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;

public class MainActivity extends AppCompatActivity {
    private Button botao_cima, botao_baixo, botao_esquerda, botao_direita;
    private Switch switch1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        botao_cima = findViewById(R.id.cim);
        botao_baixo = findViewById(R.id.bai);
        botao_esquerda = findViewById(R.id.esq);
        botao_direita = findViewById(R.id.dir);
        switch1 = findViewById(R.id.switch1);

    }
}