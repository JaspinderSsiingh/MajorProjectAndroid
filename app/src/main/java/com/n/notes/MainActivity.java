package com.n.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Dialog;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.n.notes.adapter.CategoryAdapter;
import com.n.notes.utils.DataBase;

import org.w3c.dom.Text;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextView addTV;
    DataBase dataBase;
    RecyclerView categoryRV;
    List<String> dataList;
    CategoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    private void init() {
        dataBase = new DataBase(this);
        addTV = findViewById(R.id.addTV);
        categoryRV = findViewById(R.id.categoryRV);
        addTV.setOnClickListener(this);
        checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE);
//        checkPermission(Manifest.permission.ACCESS_COARSE_LOCATION, LOCATION_PERMISSION);

        setAdapter();
    }


    private void setAdapter() {
        categoryRV.setLayoutManager(new LinearLayoutManager(this));
        dataList = dataBase.getAllCategories();
        adapter = new CategoryAdapter(this, dataList);
        categoryRV.setAdapter(adapter);
    }


    private void addNoteDialog() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.add_category_dialog);
        TextView cancelTV = dialog.findViewById(R.id.cancelTV);
        TextView okTV = dialog.findViewById(R.id.okTV);
        EditText categoryET = dialog.findViewById(R.id.categoryET);

        okTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (categoryET.getText().toString().length() > 0) {
                    dataBase.insertCategory(categoryET.getText().toString());
                    dialog.dismiss();
                    setAdapter();
                    dataSaved();
                } else {
                    Toast.makeText(MainActivity.this, "Please Enter Category", Toast.LENGTH_LONG).show();
                }
            }
        });


        cancelTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    private void dataSaved() {
        Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.data_saved_success);
        TextView okTV = dialog.findViewById(R.id.okTV);

        okTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addTV:
                addNoteDialog();
                break;
        }
    }


    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final int STORAGE_PERMISSION_CODE = 101;
    private static final int LOCATION_PERMISSION = 102;

    public void checkPermission(String permission, int requestCode) {
        // Checking if permission is not granted
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{permission}, requestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == CAMERA_PERMISSION_CODE) {

            // Checking whether user granted the permission or not.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Showing the toast message
                Toast.makeText(MainActivity.this, "Camera Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Camera Permission Denied", Toast.LENGTH_SHORT).show();
            }
        } else if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this, "Storage Permission Granted", Toast.LENGTH_SHORT).show();
                checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, LOCATION_PERMISSION);

            } else {
                Toast.makeText(MainActivity.this, "Storage Permission Denied", Toast.LENGTH_SHORT).show();
                checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, LOCATION_PERMISSION);

            }
        } else if (requestCode == LOCATION_PERMISSION) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this, "Location Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(MainActivity.this, "Location Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

}