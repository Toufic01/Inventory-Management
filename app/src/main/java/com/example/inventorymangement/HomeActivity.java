package com.example.inventorymangement;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;

import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends AppCompatActivity {
Spinner spinner;
FirebaseFirestore firebaseFirestore;
EditText company_name,admin_name,product;
CircleImageView image;
ImageButton logOutB,backB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ///Declaring the variables

        firebaseFirestore =FirebaseFirestore.getInstance();
        company_name = findViewById(R.id.company_name);
        admin_name = findViewById(R.id.admin_name);
        product = findViewById(R.id.product);
        image = findViewById(R.id.image);
        logOutB = findViewById(R.id.logOutB);
        backB = findViewById(R.id.backB);




        ///Category item select dropdown menu

        spinner = findViewById(R.id.spinner);
        String select [] ={"Select category","Pc","Mobile","Food","Other"};

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(HomeActivity.this,android.R.layout.simple_spinner_dropdown_item,select);
        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String value = parent.getItemAtPosition(position).toString();
                Toast.makeText(getApplicationContext(),"selected"+value,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        
        ///logout from app

        logOutB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog = new AlertDialog.Builder(HomeActivity.this)
                        .setTitle("Exit")
                        .setMessage("Are you want to exit from this application")
                        .setPositiveButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                finishAffinity();
                            }
                        }).create();
            }
        });

        /// backpress
        backB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),LoginActivity.class));
            }
        });
    }
}