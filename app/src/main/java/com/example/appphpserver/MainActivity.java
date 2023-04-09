package com.example.appphpserver;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {


    public static EditText txt_id, txt_name, txt_address, txt_data;
    public static Button btn_add, btn_show, btn_update, btn_delete, btn_clear, merge_btn;
    public static Switch db_switch;
    public static TextView db_mode_text;
    public static LocalDBHelper localDBHelper;
    public static ServerDBHelper serverDBHelper;
    public static DBHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // this will hide the action bar
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}

        setContentView(R.layout.activity_main);

        // create the database helpers
        String server_addr = getIntent().getStringExtra("server");
        serverDBHelper = new ServerDBHelper(MainActivity.this, server_addr);
        localDBHelper = new LocalDBHelper(MainActivity.this);
        dbHelper = localDBHelper; // set offline by default


        txt_id = findViewById(R.id.txt_id);
        txt_name = findViewById(R.id.txt_name);
        txt_address = findViewById(R.id.txt_address);
        txt_data = findViewById(R.id.txt_data);
        db_mode_text = findViewById(R.id.db_mode_label);

        btn_add = findViewById(R.id.btn_add);
        btn_update = findViewById(R.id.btn_update);
        btn_delete = findViewById(R.id.btn_delete);
        btn_show = findViewById(R.id.btn_show);
        btn_clear = findViewById(R.id.btn_clear);
        merge_btn = findViewById(R.id.merge_btn);


        db_switch = findViewById(R.id.db_switch);
        db_switch.setTextOn("Online");
        db_switch.setTextOff("Offline");
        db_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // switch is ON
                    dbHelper = serverDBHelper;
                    db_mode_text.setText("Viewing Online Data");
                } else {
                    // switch is OFF
                    dbHelper = localDBHelper;
                    db_mode_text.setText("Viewing Offline Data");
                }
                List();
            }
        });


        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendTransaction("insert");
            }
        });

        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txt_id.setText("");
                txt_address.setText("");
                txt_name.setText("");
                List();
            }
        });

        List();

        btn_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendTransaction("show");
            }
        });

        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendTransaction("update");
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                sendTransaction("delete");
            }
        });
        merge_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendTransaction("merge");
            }
        });

    }

    public void sendTransaction(String op) {


        switch (op) {
            case "insert":
                Add(txt_name.getText().toString(), txt_address.getText().toString());
                break;
            case "update":
                if (txt_id.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please submit a valid ID !", Toast.LENGTH_SHORT).show();
                    return;
                }
                Update(txt_id.getText().toString(), txt_name.getText().toString(), txt_address.getText().toString());
                break;
            case "show":
                if (txt_id.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please submit a valid ID !", Toast.LENGTH_SHORT).show();
                    return;
                }
                Show(txt_id.getText().toString());
                break;
            case "delete":
                if (txt_id.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please submit a valid ID !", Toast.LENGTH_SHORT).show();
                    return;
                }
                Delete(txt_id.getText().toString());
                break;
            case "merge":
                Merge();
                break;
        }
        List();
        ((InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE))
                .toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0);
    }

    public void Add(String name, String address) {
        dbHelper.Add(name, address);
    }

    public void Show(String id) {
        dbHelper.Show(id);
    }

    public void Update(String id, String new_name, String new_address) {
        dbHelper.Update(id, new_name, new_address);
    }

    public void Delete(String id) {
        dbHelper.Delete(id);
    }

    public void List() {
        dbHelper.List();
    }
    public void Merge(){
        serverDBHelper.Merge(localDBHelper);
    }
    @Override
    protected void onDestroy() {
        dbHelper.close();
        super.onDestroy();
    }


}