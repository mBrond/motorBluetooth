package com.example.appbluetooth;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;

@RequiresApi(api = Build.VERSION_CODES.M)
public class MainActivity extends AppCompatActivity {
    private Button botao_cima, botao_baixo, botao_esquerda, botao_direita, botao_conexao, botao_buzina;
    BluetoothAdapter bluetoothAdaptador = null; //objeto adaptador bluetooth
    BluetoothDevice meuDevice = null;
    BluetoothSocket meuSocket = null;

    BluetoothManager bluetoothManager = getSystemService(BluetoothManager.class);
    BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();

    OutputStream mmOutStream;

    private static final int SOLICITADOR_BLUETOOTH = 0;
    private static final int SOLICITA_CONEXAO = 2;

    boolean conexao = false;

    private static String MAC = null;

    ConnectedThread connectedThread;

    UUID MEU_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        botao_cima = findViewById(R.id.cim);
        botao_baixo = findViewById(R.id.bai);
        botao_esquerda = findViewById(R.id.buz);
        botao_direita = findViewById(R.id.dir);
        botao_conexao = findViewById(R.id.conexao);
        botao_buzina = findViewById(R.id.buz);

        if (bluetoothAdapter == null) {
            Toast.makeText(getApplicationContext(), "Sem módulo bluetooth disponível", Toast.LENGTH_LONG).show();

        } else if (!bluetoothAdapter.isEnabled()) {
            Intent ativarBlue = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
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
        botao_cima.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (conexao){
                    connectedThread.enviar("F");

                } else{
                    Toast.makeText(getApplicationContext(), "Bluetooth não está conectado", Toast.LENGTH_LONG).show();
                }
            }
        });
        botao_baixo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (conexao){
                    connectedThread.enviar("B");

                } else{
                    Toast.makeText(getApplicationContext(), "Bluetooth não está conectado", Toast.LENGTH_LONG).show();
                }
            }
        });
        botao_esquerda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (conexao){
                    connectedThread.enviar("L");

                } else{
                    Toast.makeText(getApplicationContext(), "Bluetooth não está conectado", Toast.LENGTH_LONG).show();
                }
            }
        });
        botao_direita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (conexao){
                    connectedThread.enviar("R");

                } else{
                    Toast.makeText(getApplicationContext(), "Bluetooth não está conectado", Toast.LENGTH_LONG).show();
                }
            }
        });
        botao_buzina.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (conexao){
                    connectedThread.enviar("V");

                } else{
                    Toast.makeText(getApplicationContext(), "Bluetooth não está conectado", Toast.LENGTH_LONG).show();
                }
            }
        });
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
                    meuDevice = bluetoothAdapter.getRemoteDevice(MAC);

                    try {
                        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    ActivityCompat#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for ActivityCompat#requestPermissions for more details.
                            return;
                        }
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
    }

        class ConnectedThread extends Thread {
            OutputStream mmOutStream;
            //BluetoothSocket meuSocket;
            private byte[] mmBuffer; // mmBuffer store for the stream

            public ConnectedThread(BluetoothSocket socket) {
                //meuSocket = socket;
                OutputStream tmpOut = null;

                // Get the input and output streams; using temp objects because
                // member streams are final.
                try {
                    tmpOut = socket.getOutputStream();
                } catch (IOException i) {

                }

                mmOutStream = tmpOut;
            }

            // Call this from the main activity to send data to the remote device.
            public void enviar(String dadosEnviar) {
                byte[] msgBuffer = dadosEnviar.getBytes();
                try {
                    mmOutStream.write(msgBuffer);


                } catch (IOException e) {

                }
            }
            }

        //}


