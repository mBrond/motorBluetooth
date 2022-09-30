package com.example.appbluetooth;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Button botao_cima, botao_baixo, botao_esquerda, botao_direita, botao_conexao;
    public BluetoothAdapter bluetoothAdaptador = null; //objeto adaptador bluetooth

    private static final int SOLICITADOR_BLUETOOTH = 1;
    private static final int SOLICITA_CONEXAO = 2;

    boolean conexao = false;

    private static String MAC = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        botao_cima = findViewById(R.id.cim);
        botao_baixo = findViewById(R.id.bai);
        botao_esquerda = findViewById(R.id.esq);
        botao_direita = findViewById(R.id.dir);
        botao_conexao = findViewById(R.id.conexao);

        bluetoothAdaptador = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdaptador == null){ //confere se o dispositivo tem bluetooth
            Toast.makeText(getApplicationContext(), "Sem módulo bluetooth disponível", Toast.LENGTH_LONG).show();
            finish();
        }
        else if (!bluetoothAdaptador.isEnabled()){ //se tem bluetooth, ativa se não está ligado
                Intent ativarBlue = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(ativarBlue, SOLICITADOR_BLUETOOTH);
        }

        botao_conexao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(conexao ){
                    //desconectar
                }else{
                    //conectar
                    Intent abreLista = new Intent(MainActivity.this, listaDispositivos.class);
                    startActivityForResult(abreLista, SOLICITA_CONEXAO);
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SOLICITADOR_BLUETOOTH:
                if (resultCode == Activity.RESULT_OK){
                    Toast.makeText(getApplicationContext(), "Bluetooth ativado", Toast.LENGTH_LONG).show();
                } else{
                    Toast.makeText(getApplicationContext(), "Bluetooth NÃO ativado, encerrando aplicativo", Toast.LENGTH_LONG).show();
                    finish();
                }
                break;
            case SOLICITA_CONEXAO:
                if(resultCode == Activity.RESULT_OK){
                    MAC = data.getExtras().getString(listaDispositivos.ENDERECO_MAC);
                    //Toast.makeText(getApplicationContext(), "MAC FINAL: "+MAC, Toast.LENGTH_LONG).show();
                } else {
                    //Toast.makeText(getApplicationContext(), "Falha ao obter endereço MAC", Toast.LENGTH_LONG).show();
                }

        }
    }
}