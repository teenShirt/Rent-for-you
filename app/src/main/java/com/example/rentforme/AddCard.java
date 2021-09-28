package com.example.rentforme;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rentforme.Models.CardDetails;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddCard extends AppCompatActivity {

    EditText editText1;
    EditText editText2;
    EditText editText3;
    EditText editText4;
    EditText editText5;
    Button button;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_card);

        ref = FirebaseDatabase.getInstance().getReference("CardDetails");

        editText1 = (EditText)findViewById(R.id.ownername);
        editText2 = (EditText)findViewById(R.id.cardnum);
        editText3 = (EditText)findViewById(R.id.expiredate);
        editText4 = (EditText)findViewById(R.id.cardcsv);
        editText5 = (EditText)findViewById(R.id.bankname);
        button = (Button)findViewById(R.id.addbtn);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String Ownername = editText1.getText().toString();
                final String cardNum = editText2.getText().toString();
                final String expireDate = editText3.getText().toString();
                final String Csv = editText4.getText().toString();
                final String bank = editText5.getText().toString();

                if (Ownername.isEmpty()) {
                    editText1.setError("Name is required");
                }else if (cardNum.isEmpty()) {
                    editText2.setError("Card Number is required");
                }else if (expireDate.isEmpty()) {
                    editText3.setError("Expire date Number is required");
                }else if (Csv.isEmpty()) {
                    editText4.setError("Csv is required");
                }else if (bank.isEmpty()) {
                    editText5.setError("Bank name is required");
                }else {

                    CardDetails cardDetails = new CardDetails(Ownername,cardNum,expireDate,Csv,bank);
                    ref.child(cardNum).setValue(cardDetails);

                    Toast.makeText(AddCard.this, "Successfully added", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(AddCard.this, CustomerAccountCard.class);
                    startActivity(intent);
                }
            }
        });
    }
}