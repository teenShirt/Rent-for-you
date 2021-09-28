package com.example.rentforme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rentforme.Models.BookingDetails;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class AddedBookings extends AppCompatActivity {

    ListView listView;
    List<BookingDetails> user;
    DatabaseReference ref;
    String idd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_added_bookings);

        listView = (ListView) findViewById(R.id.orderlist);
        user = new ArrayList<>();

        ref = FirebaseDatabase.getInstance().getReference("Bookings");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user.clear();

                for (DataSnapshot studentDatasnap : dataSnapshot.getChildren()) {

                    BookingDetails bookingDetails = studentDatasnap.getValue(BookingDetails.class);
                    user.add(bookingDetails);

                }

                MyAdapter adapter = new MyAdapter(AddedBookings.this, R.layout.custom_added_booking_deails, (ArrayList<BookingDetails>) user);
                listView.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    static class ViewHolder {

        ImageButton button1;
        ImageButton button2;
        TextView COL1;
        TextView COL2;
        TextView COL3;
        TextView COL4;
    }

    class MyAdapter extends ArrayAdapter<BookingDetails> {
        LayoutInflater inflater;
        Context myContext;
        List<BookingDetails> user;


        public MyAdapter(Context context, int resource, ArrayList<BookingDetails> objects) {
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
                view = inflater.inflate(R.layout.custom_added_booking_deails, null);

                holder.COL1 = (TextView) view.findViewById(R.id.bbid);
                holder.COL2 = (TextView) view.findViewById(R.id.bbname);
                holder.COL3 = (TextView) view.findViewById(R.id.bbcontact);
                holder.COL4 = (TextView) view.findViewById(R.id.bbaddress);
                holder.button1 = (ImageButton) view.findViewById(R.id.itemedit);
                holder.button2 = (ImageButton) view.findViewById(R.id.itemdelete);


                view.setTag(holder);
            } else {

                holder = (ViewHolder) view.getTag();
            }

            holder.COL1.setText("Booking ID:- "+user.get(position).getId());
            holder.COL2.setText("User Name:- "+user.get(position).getUname());
            holder.COL3.setText("User Contact:- "+user.get(position).getContact());
            holder.COL4.setText("User Address:- "+user.get(position).getAddress());
            System.out.println(holder);

            idd = user.get(position).getId();

            holder.button2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                            .setTitle("Do you want to delete this Booking?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    final String idd = user.get(position).getId();
                                    FirebaseDatabase.getInstance().getReference("Bookings").child(idd).removeValue();
                                    //remove function not written
                                    Toast.makeText(myContext, "Booking deleted successfully", Toast.LENGTH_SHORT).show();

                                }
                            })

                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            })
                            .show();
                }
            });

            holder.button1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
                    View view1 = inflater.inflate(R.layout.custom_update_booking, null);
                    dialogBuilder.setView(view1);

                    final EditText editText1 = (EditText) view1.findViewById(R.id.bbuname);
                    final EditText editText2 = (EditText) view1.findViewById(R.id.bbContact);
                    final EditText editText3 = (EditText) view1.findViewById(R.id.bbaddress);
                    final Button buttonupdate = (Button) view1.findViewById(R.id.update);

                    final AlertDialog alertDialog = dialogBuilder.create();
                    alertDialog.show();

                    final String idd = user.get(position).getId();
                    final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Bookings").child(idd);
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String id = (String) snapshot.child("id").getValue();
                            String name = (String) snapshot.child("uname").getValue();
                            String contact = (String) snapshot.child("contact").getValue();
                            String address = (String) snapshot.child("address").getValue();

                            editText1.setText(name);
                            editText2.setText(contact);
                            editText3.setText(address);

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });


                    buttonupdate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String name = editText1.getText().toString();
                            String contact = editText2.getText().toString();
                            String address = editText3.getText().toString();

                            if (name.equals("")) {
                                editText1.setError("Name is required");
                            }else if (contact.isEmpty()) {
                                editText3.setError("Contact Number is required");
                            }else if (address.isEmpty()) {
                                editText3.setError("Address is required");
                            }else if (contact.length() > 10) {
                                editText2.setError("Enter valid contact number");
                            } else {

                                HashMap map = new HashMap();
                                map.put("uname", name);
                                map.put("contact", contact);
                                map.put("address", address);
                                reference.updateChildren(map);

                                Toast.makeText(AddedBookings.this, "Updated successfully", Toast.LENGTH_SHORT).show();

                                alertDialog.dismiss();
                            }
                        }
                    });
                }
            });

            return view;

        }
    }
}