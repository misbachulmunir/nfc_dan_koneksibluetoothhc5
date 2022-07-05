package id.go.tangerangkota.nsc.bluetoth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.ParcelUuid;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.chip.Chip;

import java.io.IOException;
import java.util.UUID;

import id.go.tangerangkota.nsc.R;

public class Dashboard extends AppCompatActivity {
    //ActivityDashboardBinding binding;
    BluetoothSocket bluetoothSocket=null;
    private ProgressDialog progress;
    String address="null";
    private boolean isBtConnected = false;
    BluetoothAdapter myBluetooth = null;
    static final UUID myUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case  android.R.id.home:
                try {
                    bluetoothSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                finish();
            default:
                return super.onOptionsItemSelected(item);
        }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        try {
            bluetoothSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finish();
    }

    private class ConnectBT extends AsyncTask<Void, Void, Void>  // UI thread
    {
        private boolean ConnectSuccess = true; //if it's here, it's almost connected

        @Override
        protected void onPreExecute()
        {
            progress = ProgressDialog.show(Dashboard.this, "Menghubungi", "Tunggu sebentar...");  //show a progress dialog
        }

        @Override
        protected Void doInBackground(Void... devices) //while the progress dialog is shown, the connection is done in background
        {
            try
            {
                if (bluetoothSocket == null || !isBtConnected)
                {

                    myBluetooth = BluetoothAdapter.getDefaultAdapter();//get the mobile bluetooth device
                    BluetoothDevice dispositivo = myBluetooth.getRemoteDevice(address);//connects to the device's address and checks if it's available
                    Log.d(TAG, "cek adress: "+address);
                  //  bluetoothSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(myUUID);//create a RFCOMM (SPP) connection
                    Log.d(TAG, "cek soket: "+bluetoothSocket);
                    UUID uuid = dispositivo.getUuids()[0].getUuid();
                    bluetoothSocket = dispositivo.createInsecureRfcommSocketToServiceRecord(uuid);//create a RFCOMM (SPP) connection
                    BluetoothAdapter.getDefaultAdapter().cancelDiscovery();
                    bluetoothSocket.connect();//start connection
                }
            }
            catch (IOException e)
            {
                Log.d(TAG, "doInBackground: cek gagal"+e.getMessage());
                ConnectSuccess = false;//if the try failed, you can check the exception here
            }
            return null;
        }
        @Override
        protected void onPostExecute(Void result) //after the doInBackground, it checks if everything went fine
        {
            super.onPostExecute(result);

            if (!ConnectSuccess)
            {
               // msg("Connection Failed. Is it a SPP Bluetooth? Try again.");
                Log.d(TAG, "onPostExecute: cek gagal"+result);
                Toast.makeText(Dashboard.this, "Koneksi gagal coba beberapa saat lagi", Toast.LENGTH_SHORT).show();
                finish();

            }
            else
            {
               // msg("Connected.");
                Toast.makeText(Dashboard.this, "Terhubung", Toast.LENGTH_SHORT).show();
                isBtConnected = true;
            }
            progress.dismiss();
        }
    }

    private static final String TAG = "Dashboard";
    Button on,off,kirim;
    EditText perintah;
    Chip l1,l2,l3,l4;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_dashboard);
//        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
//        View view = binding.getRoot();
//        setContentView(view);
        setContentView(R.layout.activity_dashboard);
        on = findViewById(R.id.on);
        off = findViewById(R.id.off);
        l1 = findViewById(R.id.lampu1);
        l2 = findViewById(R.id.lampu2);
        l3 = findViewById(R.id.lampu3);
        l4 = findViewById(R.id.lampu4);
        kirim = findViewById(R.id.kirim);
        perintah = findViewById(R.id.perintah);

        address=getIntent().getStringExtra(BluetothActivity.EXTRA_ADDRESS);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Log.d(TAG, "cek my uuid: "+myUUID);
        new ConnectBT().execute();


        on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bluetoothSocket!=null)
                {
                    try
                    {
                        bluetoothSocket.getOutputStream().write("1".toString().getBytes());
                    }
                    catch (IOException e)
                    {
                        Toast.makeText(Dashboard.this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bluetoothSocket!=null)
                {
                    try
                    {
                        bluetoothSocket.getOutputStream().write("0".toString().getBytes());
                    }
                    catch (IOException e)
                    {
                        Toast.makeText(Dashboard.this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        l1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bluetoothSocket!=null)
                {
                    try
                    {
                        if(l1.isChecked()){
                            bluetoothSocket.getOutputStream().write("8".toString().getBytes());
                        }else{
                            bluetoothSocket.getOutputStream().write("9".toString().getBytes());
                        }

                    }
                    catch (IOException e)
                    {
                        Toast.makeText(Dashboard.this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        l2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bluetoothSocket!=null)
                {
                    try
                    {
                        if(l2.isChecked()){
                            bluetoothSocket.getOutputStream().write("6".toString().getBytes());
                        }else{
                            bluetoothSocket.getOutputStream().write("7".toString().getBytes());
                        }

                    }
                    catch (IOException e)
                    {
                        Toast.makeText(Dashboard.this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        l3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bluetoothSocket!=null)
                {
                    try
                    {
                        if(l3.isChecked()){
                            bluetoothSocket.getOutputStream().write("4".toString().getBytes());
                        }else{
                            bluetoothSocket.getOutputStream().write("5".toString().getBytes());
                        }

                    }
                    catch (IOException e)
                    {
                        Toast.makeText(Dashboard.this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        l4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bluetoothSocket!=null)
                {
                    try
                    {
                        if(l4.isChecked()){
                            Log.d(TAG, "onClick: "+l2.isChecked());
                            bluetoothSocket.getOutputStream().write("2".toString().getBytes());
                        }else{
                            bluetoothSocket.getOutputStream().write("3".toString().getBytes());
                        }

                    }
                    catch (IOException e)
                    {
                        Toast.makeText(Dashboard.this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        kirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             if(perintah.getText().toString().isEmpty()){
                 perintah.setError("Kosong");
             }else{
                 if (bluetoothSocket!=null)
                 {
                     try
                     {
                         bluetoothSocket.getOutputStream().write(perintah.getText().toString().getBytes());

                     }
                     catch (IOException e)
                     {
                         Toast.makeText(Dashboard.this, "Terjadi kesalahan", Toast.LENGTH_SHORT).show();
                     }
                 }
             }
            }
        });

    }
}