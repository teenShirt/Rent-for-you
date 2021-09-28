package com.example.rentforme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rentforme.Models.BookingDetails;
import com.example.rentforme.Models.Vehicles;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ManageBookings extends AppCompatActivity {

    Button button;
    ListView listView;
    List<Vehicles> user;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_bookings);

        listView = (ListView) findViewById(R.id.listviewdel);
        button = (Button) findViewById(R.id.gotobooklist);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManageBookings.this, AddedBookings.class);
                startActivity(intent);
            }
        });

        user = new ArrayList<>();

        ref = FirebaseDatabase.getInstance().getReference("Vehicles");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user.clear();

                for (DataSnapshot studentDatasnap : dataSnapshot.getChildren()) {

                    Vehicles vehicles = studentDatasnap.getValue(Vehicles.class);
                    user.add(vehicles);
                }

                MyAdapter adapter = new MyAdapter(ManageBookings.this, R.layout.custom_booking_vehicles, (ArrayList<Vehicles>) user);
                listView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    static class ViewHolder {

        TextView COL1;
        TextView COL2;
        TextView COL3;
        TextView COL4;
        Button button;
    }

    class MyAdapter extends ArrayAdapter<Vehicles> {
        LayoutInflater inflater;
        Context myContext;
        List<Vehicles> user;


        public MyAdapter(Context context, int resource, ArrayList<Vehicles> objects) {
            super(context, resource, objects);
            myContext = context;
            user = objects;
            inflater = LayoutInflater.from(context);
            int y;
            String barcode;
        }

        @SuppressLint("SetTextI18n")
        @Override
        public View getView(int position, View view, ViewGroup parent) {
            final ViewHolder holder;
            if (view == null) {
                holder = new ViewHolder();
                view = inflater.inflate(R.layout.custom_booking_vehicles, null);

                holder.COL1 = (TextView) view.findViewById(R.id.caname);
                holder.COL2 = (TextView) view.findViewById(R.id.caday);
                holder.COL3 = (TextView) view.findViewById(R.id.cmilage);
                holder.COL4 = (TextView) view.findViewById(R.id.cloocation);
                holder.button = (Button) view.findViewById(R.id.booknow);

                view.setTag(holder);
            } else {

                holder = (ViewHolder) view.getTag();
            }

            holder.COL1.setText("Vehicle Name:- "+user.get(position).getName());
            holder.COL2.setText("Vehicle PerDay:- "+user.get(position).getPerday());
            holder.COL3.setText("Vehicle Mileage:- "+user.get(position).getMilage());
            holder.COL4.setText("Vehicle Location:- "+user.get(position).getLocation());

            System.out.println(holder);

            holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
                    View view = inflater.inflate(R.layout.custom_insert_booking, null);
                    dialogBuilder.setView(view);

                    final EditText editText1 = (EditText) view.findViewById(R.id.buname);
                    final EditText editText2 = (EditText) view.findViewById(R.id.cContact);
                    final EditText editText3 = (EditText) view.findViewById(R.id.baddress);
                    final Button buttonAdd = (Button) view.findViewById(R.id.book);

                    final AlertDialog alertDialog = dialogBuilder.create();
                    alertDialog.show();

                    final String idd = user.get(position).getId();
                    final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Vehicles").child(idd);
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String id = (String) snapshot.child("id").getValue();


                            buttonAdd.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Bookings");

                                    final String name = editText1.getText().toString();
                                    final String contact = editText2.getText().toString();
                                    final String address = editText3.getText().toString();



                                    if (name.isEmpty()) {
                                        editText1.setError("Name is required");
                                    }else if (contact.isEmpty()) {
                                        editText2.setError("Contact is required");
                                    }else if (address.isEmpty()) {
                                        editText3.setError("Address is required");
                                    } else if (contact.length() > 10) {
                                        editText2.setError("Enter valid contact number is required");
                                    }else {

                                        BookingDetails bookingDetails = new BookingDetails(id,name,contact,address);
                                        reference.child(id).setValue(bookingDetails);

                                        Toast.makeText(ManageBookings.this, "Successfully added", Toast.LENGTH_SHORT).show();

                                        alertDialog.dismiss();
                                    }

                                }
                            });

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }

                    });

                }

            });

            return view;

        }
    }

}