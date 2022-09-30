package com.example.appbluetooth;

import android.app.ListActivity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.view.TextureView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

public class listaDispositivos extends ListActivity {

    private BluetoothAdapter bluetoothAdaptador2 = null;

    static String ENDERECO_MAC = null;



    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        ArrayAdapter<String> ArrayBluetooth = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);

        bluetoothAdaptador2 = BluetoothAdapter.getDefaultAdapter();

        Set<BluetoothDevice> dispositivosPareados = bluetoothAdaptador2.getBondedDevices();

        if(dispositivosPareados.size() > 0 ){
            for(BluetoothDevice dispositivo : dispositivosPareados) {
                String nomeBlt =dispositivo.getName();
                String macBlt = dispositivo. getAddress();
                ArrayBluetooth.add(nomeBlt + '\n' + macBlt);
            }
        }
        setListAdapter(ArrayBluetooth);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        String informacaoGeral = ((TextView) v).getText().toString();
        //Toast.makeText(getApplicationContext(), "Info :"+ informacaoGeral, Toast.LENGTH_LONG).show(); // conferir se pegou o dispositivo certo
        String enderecoMac = informacaoGeral.substring(informacaoGeral.length() - 17);
        //Toast.makeText(getApplicationContext(), "mac :"+ enderecoMac, Toast.LENGTH_LONG).show();

        Intent retornaMac = new Intent();
        retornaMac.putExtra(ENDERECO_MAC, enderecoMac);
        setResult(RESULT_OK, retornaMac);
        finish();
    }
}
