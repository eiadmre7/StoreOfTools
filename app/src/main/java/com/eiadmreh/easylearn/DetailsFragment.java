package com.eiadmreh.easylearn;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class DetailsFragment extends Fragment {
    View view;
    ListView myList;
    TextView etTotal;
    Calendar c=Calendar.getInstance();
    private Button btnAccept,btnBack;
    private DatabaseReference mDatabaseRef;
    private long MaxId=0,SerialCode;
    private int OrderLine=1;
    private String currentDateString;
    private ArrayList<ToolinCart> Cart;
    public DetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_details, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        myList=(ListView)view.findViewById(R.id.list);
        etTotal=(TextView)view.findViewById(R.id.tvTotal);
        btnAccept=(Button)view.findViewById(R.id.btnAccept);
        btnBack=(Button)view.findViewById(R.id.btnBack);
        Cart=ApplicationClass.thisCart;
        currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Orders");
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                    MaxId=dataSnapshot.getChildrenCount();
                SerialCode=10000+MaxId+1;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        ArrayAdapter<ToolinCart> myAdapter =
                new ArrayAdapter<ToolinCart>(view.getContext(), android.R.layout.simple_list_item_1,Cart );
        myList.setAdapter(myAdapter);
        final String total=""+ApplicationClass.Total;
        etTotal.setText(total);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ToolsFragment())
                        .commit();
            }
        });

        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Orders order=new Orders();
                order.setOrderNumber(String.valueOf(SerialCode));
                order.setClientName(LoginActivity.custName);
                order.setDate(currentDateString);
                order.setOrderPrice(etTotal.getText().toString()+"(N.Sh)");
                mDatabaseRef.child( String.valueOf(SerialCode) ).setValue(order);
                mDatabaseRef = FirebaseDatabase.getInstance().getReference("Orders").child(String.valueOf(SerialCode));
                for (ToolinCart postSnapshot : Cart) {
                    ToolinCart tool = postSnapshot;
                    mDatabaseRef.child( String.valueOf(OrderLine) ).setValue(tool);
                    OrderLine++;
                }
                Toast.makeText( view.getContext(), "your order was Uploaded", Toast.LENGTH_SHORT ).show();
                ApplicationClass.thisCart.clear();
                ApplicationClass.Total=0;
            }
        });
    }


}
