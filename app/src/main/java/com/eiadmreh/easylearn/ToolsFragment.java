package com.eiadmreh.easylearn;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class ToolsFragment extends Fragment {
    private Button   btnShow,btnShowCart;
    private ImageButton btnAdd;
    private EditText editTextName,editTextprice,editTextcode,editTextQuntity;
    private ImageView loadedImage;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    View view;
    public ToolsFragment(){

    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view= inflater.inflate(R.layout.fragment_tools, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        btnAdd = (ImageButton) view.findViewById( R.id.btnAdd );
        editTextName = (EditText) view.findViewById( R.id.editTextFileName );
        editTextprice=(EditText)view.findViewById(R.id.editTextPrice);
        editTextcode=(EditText)view.findViewById(R.id.editTextCode);
        editTextQuntity=(EditText)view.findViewById(R.id.editTexQuantity);
        loadedImage = (ImageView) view.findViewById( R.id.imgView );
        btnShow = (Button) view.findViewById( R.id.btnShow );
        btnShowCart=(Button)view.findViewById(R.id.btnDetails);
        mStorageRef = FirebaseStorage.getInstance().getReference("Tools");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Tools");
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    editTextcode.setText("123001");
                    ShowSelectedTool();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowSelectedTool();
            }
        });
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Name;
                long Code;
                double Price;
                double TotalPrice;
                int Quantity;
                Name=editTextName.getText().toString().trim();
                Code=Long.parseLong(editTextcode.getText().toString().trim());
                Price=Double.parseDouble(editTextprice.getText().toString().trim());
                Quantity=Integer.parseInt(editTextQuntity.getText().toString().trim());
                TotalPrice=Price*Quantity;
                ToolinCart t=new ToolinCart(Name,Code,Price,Quantity,TotalPrice);
                ApplicationClass.thisCart.add(t);
                ApplicationClass.Total+=TotalPrice;
                clear();
            }
        });
        btnShowCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startActivity(new Intent(view.getContext(),DetailsActivity.class));
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new DetailsFragment())
                        .commit();
            }
        });
    }

    private void ShowSelectedTool() {
        final long toolCode= Long.parseLong(editTextcode.getText().toString());
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("Tools");

        Query q =myRef.orderByChild( "tCode" ).equalTo( toolCode );
        q.addListenerForSingleValueEvent( new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    Tool tool = ds.getValue(Tool.class);
                    editTextName.setText(tool.gettName());
                    editTextprice.setText("" + tool.gettPrice());

                    // Toast.makeText(MainActivity.this,"No Tool Found !!!,Try again.",Toast.LENGTH_SHORT).show();
                    try {
                        StorageReference riversRef = mStorageRef.child(tool.getImageUrl());
                        final File localFile = File.createTempFile("Tools", getImageType( tool.getImageUrl( )));
                        riversRef.getFile(localFile)
                                .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                        // Successfully downloaded data to local file
                                        // ...
                                        Bitmap bitmap = BitmapFactory.decodeFile( localFile.getAbsolutePath() );
                                        loadedImage.setImageBitmap( bitmap );


                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle failed download
                                // ...
                                Toast.makeText(view.getContext(), exception.getMessage(), Toast.LENGTH_SHORT).show();

                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {


            }
        } );


    }
    private String getImageType(String name){
        String[] type = name.split( "\\." );
        return type[1];
    }


    public void clear(){
        editTextName.setText("");
        editTextprice.setText("");
        editTextQuntity.setText("");
        editTextcode.setText("123001");
        editTextcode.requestFocus();
    }
}


