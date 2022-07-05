package id.go.tangerangkota.nsc.bluetoth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.Set;

import id.go.tangerangkota.nsc.R;


public class BluetothActivity extends AppCompatActivity {
    ListView devicelist;
    SwipeRefreshLayout sweprefresh;
    FloatingActionButton cariBluetoth;
    //Bluetooth
    private BluetoothAdapter myBluetooth = null;
    private Set<BluetoothDevice> pairedDevices;
    public static String EXTRA_ADDRESS = "device_address";
    private ArrayList<M_perangkat> mPerangkats = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_bluetoth);
      setContentView(R.layout.activity_bluetoth);
        setTitle("Bluetooth Terpasang");
        sweprefresh = findViewById(R.id.sweprefresh);
        cariBluetoth = findViewById(R.id.cari_bluetoth);

        sweprefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                sweprefresh.setRefreshing(true);
                pairedDevicesList();
                sweprefresh.setRefreshing(false);
            }
        });

        devicelist = (ListView)findViewById(R.id.listView);



        //if the device has bluetooth
        myBluetooth = BluetoothAdapter.getDefaultAdapter();

        pairedDevicesList();

        if(myBluetooth == null)
        {
            //Show a mensag. that the device has no bluetooth adapter
            Toast.makeText(getApplicationContext(), "Bluetooth Device Not Available", Toast.LENGTH_LONG).show();

            //finish apk
            finish();
        }
        //    else if(!myBluetooth.isEnabled())
        {
            //Ask to the user turn the bluetooth on
            Intent turnBTon = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(turnBTon,1);
        }

        cariBluetoth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentOpenBluetoothSettings = new Intent();
                intentOpenBluetoothSettings.setAction(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS);
                startActivity(intentOpenBluetoothSettings);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        unregisterReceiver(receiver);
        finish();
    }

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                // Discovery has found a device. Get the BluetoothDevice
                // object and its info from the Intent.
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                String deviceName = device.getName();
                String deviceHardwareAddress = device.getAddress(); // MAC address
            }
        }
    };

    private void pairedDevicesList()
    {
        pairedDevices = myBluetooth.getBondedDevices();
        ArrayList list = new ArrayList();
        mPerangkats.clear();


        if (pairedDevices.size()>0)
        {
            for(BluetoothDevice bt : pairedDevices)
            {

                M_perangkat m_perangkat = new M_perangkat(bt.getName(),bt.getAddress());
                mPerangkats.add(m_perangkat);
                list.add(bt.getName() + "\n" + bt.getAddress()); //Get the device's name and the address
            }
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Tidak ada bluetooth terpasang.", Toast.LENGTH_LONG).show();
        }

        final ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1, list);
        devicelist.setAdapter(adapter);
        devicelist.setOnItemClickListener(myListClickListener); //Method called when the device from the list is clicked

    }

    private AdapterView.OnItemClickListener myListClickListener = new AdapterView.OnItemClickListener()
    {
        public void onItemClick (AdapterView<?> av, View v, int arg2, long arg3)
        {
            // Get the device MAC address, the last 17 chars in the View
            String namaperangkat =mPerangkats.get(arg2).nama;
            String info = ((TextView) v).getText().toString();
            String address = info.substring(info.length() - 17);
           // Toast.makeText(BluetothActivity.this,namaperangkat+ address, Toast.LENGTH_SHORT).show();

            // Make an intent to start next activity.

            Intent i = new Intent(BluetothActivity.this, Dashboard.class);

            //Change the activity.
            i.putExtra(EXTRA_ADDRESS, address); //this will be received at ledControl (class) Activity
          //  i.putExtra(EXTRA_ADDRESS, address); //this will be received at ledControl (class) Activity
            startActivity(i);
        }
    };


}