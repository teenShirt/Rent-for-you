package com.example.rentforme;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rentforme.Models.Vehicles;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddVehicle extends AppCompatActivity {

    EditText editText1;
    EditText editText2;
    EditText editText3;
    EditText editText4;
    EditText editText5;
    EditText editText6;
    EditText editText7;
    EditText editText8;
    Button button;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehicle);

        editText1=(EditText)findViewById(R.id.carrID);
        editText2=(EditText)findViewById(R.id.carrName);
        editText3=(EditText)findViewById(R.id.carrNumber);
        editText4=(EditText)findViewById(R.id.carrPerday);
        editText5=(EditText)findViewById(R.id.carrMilage);
        editText6=(EditText)findViewById(R.id.carrLocation);
        editText7=(EditText)findViewById(R.id.carrContact);
        editText8=(EditText)findViewById(R.id.carrDescription);
        button=(Button)findViewById(R.id.carradd);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                reference = FirebaseDatabase.getInstance().getReference("Vehicles");

                final String id = editText1.getText().toString();
                final String name = editText2.getText().toString();
                final String number = editText3.getText().toString();
                final String perday = editText4.getText().toString();
                final String milage = editText5.getText().toString();
                final String location = editText6.getText().toString();
                final String phone = editText7.getText().toString();
                final String description = editText7.getText().toString();

                if (id.isEmpty()) {
                    editText1.setError("ID is required");
                } else if (name.isEmpty()) {
                    editText2.setError("Name is required");
                }  else if (number.isEmpty()) {
                    editText3.setError("Car number is required");
                } else if (perday.isEmpty()) {
                    editText4.setError("KM per day is required");
                } else if (milage.isEmpty()) {
                    editText5.setError("Mileage is required");
                } else if (location.isEmpty()) {
                    editText6.setError("Location is required");
                }else if (phone.isEmpty()) {
                    editText7.setError("Contact number is required");
                }else if (description.isEmpty()) {
                    editText8.setError("Description is required");
                }else if(phone.length() > 10){
                    editText7.setError("Enter correct number number is required");
                }else {

                    Vehicles vehicles = new Vehicles(id,name,number,perday,milage,location,phone,description);
                    reference.child(id).setValue(vehicles);

                    Toast.makeText(AddVehicle.this, "Vehicle added successfully", Toast.LENGTH_SHORT).show();


                }
            }
        });
    }
}