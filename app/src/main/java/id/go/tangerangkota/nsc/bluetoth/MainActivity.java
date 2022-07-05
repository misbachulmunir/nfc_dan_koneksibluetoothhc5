package id.go.tangerangkota.nsc.bluetoth;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.mazenrashed.printooth.Printooth;
import com.mazenrashed.printooth.ui.ScanningActivity;

import id.go.tangerangkota.nsc.R;


public class MainActivity extends AppCompatActivity {
//private ActivityMainBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        binding = ActivityMainBinding.inflate(getLayoutInflater());
//        View view = binding.getRoot();
//        setContentView(view);
        setContentView(R.layout.activity_main);

        Printooth.INSTANCE.init(this);
        startActivityForResult(new Intent(this, ScanningActivity.class), ScanningActivity.SCANNING_FOR_PRINTER);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ScanningActivity.SCANNING_FOR_PRINTER && resultCode == Activity.RESULT_OK){
            Toast.makeText(this,  Printooth.INSTANCE.getPairedPrinter().getName(), Toast.LENGTH_SHORT).show();

        }
    }
}