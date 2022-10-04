package com.example.appbluetooth;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private Button botao_cima, botao_baixo, botao_esquerda, botao_direita, botao_conexao;
    BluetoothAdapter bluetoothAdaptador = null; //objeto adaptador bluetooth
    BluetoothDevice meuDevice = null;
    BluetoothSocket meuSocket = null;


    private static final int SOLICITADOR_BLUETOOTH = 1;
    private static final int SOLICITA_CONEXAO = 2;

    ConnectedThread connectedThread;

    boolean conexao = false;

    private static String MAC = null;

    UUID MEU_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

    Intent data;
    int requestCode;
    int resultCode;

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
        if (bluetoothAdaptador == null) { //confere se o dispositivo tem bluetooth
            Toast.makeText(getApplicationContext(), "Sem módulo bluetooth disponível", Toast.LENGTH_LONG).show();
            finish();
        } else if (!bluetoothAdaptador.isEnabled()) { //se tem bluetooth, ativa se não está ligado
            Intent ativarBlue = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(ativarBlue, SOLICITADOR_BLUETOOTH);
        }

        botao_conexao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (conexao) {
                    //desconectar
                    try {
                        meuSocket.close();
                        conexao = false;
                        botao_conexao.setText("Conectar");
                        Toast.makeText(getApplicationContext(), "Bluetooth Desconectado", Toast.LENGTH_LONG).show();
                    } catch (IOException erro) {
                        Toast.makeText(getApplicationContext(), "Ocorreu um erro:" + erro, Toast.LENGTH_LONG).show();
                    }
                } else {
                    //conectar
                    Intent abreLista = new Intent(MainActivity.this, listaDispositivos.class);
                    startActivityForResult(abreLista, SOLICITA_CONEXAO);
                }
            }
        });

        botao_baixo.setOnClickListener(View.OnClickListener() {

            public void onClick(View view) {

                if (conexao) {
                    connectedThread.enviar("B");
                } else {
                    Toast.makeText(getApplicationContext(), "Bluetooth não está conectado", Toast.LENGTH_LONG).show();
                }

            }
        });

        protected void onActivityResult(requestCode,resultCode,data){
            //super.onActivityResult(requestCode, resultCode, data);
            switch (requestCode) {
                case SOLICITADOR_BLUETOOTH:
                    if (resultCode == Activity.RESULT_OK) {
                        Toast.makeText(getApplicationContext(), "Bluetooth ativado", Toast.LENGTH_LONG).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "Bluetooth NÃO ativado, encerrando aplicativo", Toast.LENGTH_LONG).show();
                        finish();
                    }
                    break;
                case SOLICITA_CONEXAO:
                    if (resultCode == Activity.RESULT_OK) {
                        MAC = data.getExtras().getString(listaDispositivos.ENDERECO_MAC);
                        //Toast.makeText(getApplicationContext(), "MAC FINAL: "+MAC, Toast.LENGTH_LONG).show();
                        meuDevice = bluetoothAdaptador.getRemoteDevice(MAC);

                        try {
                            meuSocket = meuDevice.createRfcommSocketToServiceRecord(MEU_UUID);
                            meuSocket.connect();

                            conexao = true;

                            connectedThread = new ConnectedThread(meuSocket);
                            connectedThread.start();

                            botao_conexao.setText("Desconectar");

                            Toast.makeText(getApplicationContext(), "Você foi conectado com: " + MAC, Toast.LENGTH_LONG).show();

                        } catch (IOException erro) {
                            conexao = false;
                            Toast.makeText(getApplicationContext(), "Ocorreu um erro:" + erro, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Falha ao obter endereço MAC", Toast.LENGTH_LONG).show();
                    }

            }
        }
        private class ConnectedThread extends Thread {
            private InputStream mnInStream;
            private OutputStream mnOutStream;

            public ConnectedThread(BluetoothSocket socket) {
                meuSocket = socket;
                InputStream tmpIn = null;
                OutputStream tmpOut = null;

                try {
                    tmpIn = socket.getInputStream();
                    tmpOut = socket.getOutputStream();
                } catch (IOException e) {
                }
                mnInStream = tmpIn;
                mnOutStream = tmpOut;

            }

            public void enviar(String dadosEnviar) {
                byte[] msgBuffer = dadosEnviar.getBytes();

                try {
                    mnOutStream.write(msgBuffer);
                } catch (IOException e) {
                }
            }
        }

    }
}