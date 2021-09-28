package com.example.rentforme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rentforme.Models.Ratings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.zip.Inflater;

public class FeedBackManagement extends AppCompatActivity {

    TextView showrateing,ratecount,commen;
    EditText review;
    Button submit,privous;
    RatingBar ratingBar;
    float rateValue;
    String temp;
    String comment;
    DatabaseReference reference;
    FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back_management);

        ratecount = (TextView)findViewById(R.id.rateCount);
        review = (EditText)findViewById(R.id.review);
        submit = (Button)findViewById(R.id.subm);
        privous = (Button)findViewById(R.id.see);
        ratingBar = (RatingBar)findViewById(R.id.ratingBar);

        firebaseAuth=FirebaseAuth.getInstance();

        privous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(FeedBackManagement.this);
                View view1 = getLayoutInflater().inflate(R.layout.privious_ratings,null);
                dialogBuilder.setView(view1);

                final TextView textView1 = (TextView) view1.findViewById(R.id.showRating);
                final TextView textView2 = (TextView) view1.findViewById(R.id.Comment);
                Button button = (Button)view1.findViewById(R.id.ok);

                final AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                final String userid = user.getUid();

                DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("Ratings").child(userid);
                myRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        String rate = snapshot.child("rate").getValue().toString();
                        String comment = snapshot.child("comment").getValue().toString();

                        textView1.setText(rate);
                        textView2.setText(comment);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        alertDialog.dismiss();
                    }
                });

            }
        });


        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                rateValue = ratingBar.getRating();

                if (rateValue<=1 && rateValue>0){
                    ratecount.setText("Bad " +rateValue + "/5");
                }else if (rateValue<=2 && rateValue>1){
                    ratecount.setText("OK " +rateValue + "/5");
                }else if (rateValue<=3 && rateValue>2){
                    ratecount.setText("Good " +rateValue + "/5");
                }else if (rateValue<=4 && rateValue>3){
                    ratecount.setText("Very Good " +rateValue + "/5");
                }else if (rateValue<=5 && rateValue>4){
                    ratecount.setText("Best " +rateValue + "/5");
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                reference = FirebaseDatabase.getInstance().getReference("Ratings");

                temp = ratecount.getText().toString();
                comment = review.getText().toString();

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String userid = user.getUid();

                Ratings ratings = new Ratings(temp, comment);
                reference.child(userid).setValue(ratings);

                System.out.println(ratings);

                Toast.makeText(FeedBackManagement.this, "Rate added successfully", Toast.LENGTH_SHORT).show();
            }
        });

    }
}