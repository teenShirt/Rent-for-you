package com.example.rentforme;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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

import com.example.rentforme.Models.Vehicles;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManageVehicles extends AppCompatActivity {

    FloatingActionButton button;
    ListView listView;
    private List<Vehicles> user;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_vehicles);

        button = (FloatingActionButton)findViewById(R.id.addbtn);
        listView = (ListView)findViewById(R.id.listview);

        user = new ArrayList<>();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManageVehicles.this, AddVehicle.class);
                startActivity(intent);
            }
        });

        ref = FirebaseDatabase.getInstance().getReference("Vehicles");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user.clear();

                for (DataSnapshot taskDatasnap : dataSnapshot.getChildren()){

                    Vehicles vehicles = taskDatasnap.getValue(Vehicles.class);
                    user.add(vehicles);
                }

                MyAdapter adapter = new MyAdapter(ManageVehicles.this, R.layout.custom_vehiclelist, (ArrayList<Vehicles>) user);
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
        ImageButton imageButton1;
        ImageButton imageButton2;

    }

    class MyAdapter extends ArrayAdapter<Vehicles> {
        LayoutInflater inflater;
        Context myContext;
        List<Map<String, String>> newList;
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
                view = inflater.inflate(R.layout.custom_vehiclelist, null);

                holder.COL1 = (TextView) view.findViewById(R.id.caridd);
                holder.COL2 = (TextView) view.findViewById(R.id.carnamee);
                holder.COL3 = (TextView) view.findViewById(R.id.carlocation);
                holder.COL4 = (TextView) view.findViewById(R.id.carmilage);
                holder.imageButton1=(ImageButton)view.findViewById(R.id.delete);
                holder.imageButton2=(ImageButton)view.findViewById(R.id.edit);

                view.setTag(holder);
            } else {

                holder = (ViewHolder) view.getTag();
            }

            holder.COL1.setText("Car ID: -"+user.get(position).getId());
            holder.COL2.setText("Car Name: -"+user.get(position).getName());
            holder.COL3.setText("Car Location: -"+user.get(position).getLocation());
            holder.COL4.setText("Car Milage: -"+user.get(position).getMilage());

            System.out.println(holder);

            final String idd = user.get(position).getId();

            holder.imageButton1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                            .setTitle("Do you want to delete this Vehicle?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {

                                    String userid = user.get(position).getId();

                                    FirebaseDatabase.getInstance().getReference("Vehicles").child(idd).removeValue();
                                   //remove function not written
                                    Toast.makeText(myContext, "Deleted successfully", Toast.LENGTH_SHORT).show();

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

            holder.imageButton2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext());
                    View view1 = inflater.inflate(R.layout.custom_updatevehicle,null);
                    dialogBuilder.setView(view1);

                    final EditText editText1 = (EditText)view1.findViewById(R.id.cName);
                    final EditText editText2 = (EditText)view1.findViewById(R.id.cNumber);
                    final EditText editText3 = (EditText)view1.findViewById(R.id.cPerday);
                    final EditText editText4 = (EditText)view1.findViewById(R.id.cMilage);
                    final EditText editText5 = (EditText)view1.findViewById(R.id.cLocation);
                    final EditText editText6 = (EditText)view1.findViewById(R.id.cContact);
                    final EditText editText7 = (EditText)view1.findViewById(R.id.cDescription);
                    final Button button = (Button)view1.findViewById(R.id.Cupdate);

                    final AlertDialog alertDialog = dialogBuilder.create();
                    alertDialog.show();

                    final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Vehicles").child(idd);
                    reference.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String id = (String) snapshot.child("id").getValue();
                            String name = (String) snapshot.child("name").getValue();
                            String number = (String) snapshot.child("number").getValue();
                            String perday = (String) snapshot.child("perday").getValue();
                            String milage = (String) snapshot.child("milage").getValue();
                            String location = (String) snapshot.child("location").getValue();
                            String phone = (String) snapshot.child("phone").getValue();
                            String description = (String) snapshot.child("description").getValue();

                            editText1.setText(name);
                            editText2.setText(number);
                            editText3.setText(perday);
                            editText4.setText(milage);
                            editText5.setText(location);
                            editText6.setText(phone);
                            editText7.setText(description);
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                        }
                    });

                    button.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String name = editText1.getText().toString();
                            String number = editText2.getText().toString();
                            String perday =editText3.getText().toString();
                            String milage = editText4.getText().toString();
                            String location = editText5.getText().toString();
                            String phone = editText6.getText().toString();
                            String description = editText7.getText().toString();


                            if (name.isEmpty()) {
                                editText1.setError("Name is required");
                            }  else if (number.isEmpty()) {
                                editText2.setError("Car number is required");
                            } else if (perday.isEmpty()) {
                                editText3.setError("KM per day is required");
                            } else if (milage.isEmpty()) {
                                editText4.setError("Mileage is required");
                            } else if (location.isEmpty()) {
                                editText5.setError("Location is required");
                            }else if (phone.isEmpty()) {
                                editText6.setError("Contact number is required");
                            }else if (description.isEmpty()) {
                                editText7.setError("Description is required");
                            }else if(phone.length() > 10){
                                editText6.setError("Enter correct number number is required");
                            }else {
//
                                HashMap map = new HashMap();
                                map.put("name",name);
                                map.put("number",number);
                                map.put("perday",perday);
                                map.put("milage",milage);
                                map.put("location",location);
                                map.put("phone",phone);
                                map.put("description",description);
                                reference.updateChildren(map);

                                Toast.makeText(ManageVehicles.this, "Vehicle updated successfully", Toast.LENGTH_SHORT).show();

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