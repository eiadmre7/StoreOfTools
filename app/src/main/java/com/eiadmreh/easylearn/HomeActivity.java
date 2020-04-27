package com.eiadmreh.easylearn;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private Button btnAdd,  btnShow,btnShowCart;
    private EditText editTextName,editTextprice,editTextcode,editTextQuntity;
    private ImageView loadedImage;
    private Uri imageUri;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    public static ArrayList<ToolinCart> thisCart;
    public static double Total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_home );
        btnAdd = (Button) findViewById( R.id.btnAdd );
        editTextName = (EditText) findViewById( R.id.editTextFileName );
        editTextprice=(EditText)findViewById(R.id.editTextPrice);
        editTextcode=(EditText)findViewById(R.id.editTextCode);
        editTextQuntity=(EditText)findViewById(R.id.editTexQuantity);
        loadedImage = (ImageView) findViewById( R.id.imgView );
        btnShow = (Button) findViewById( R.id.btnShow );
        btnShowCart=(Button)findViewById(R.id.btnDetails);
        thisCart=new ArrayList<ToolinCart>();
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
                thisCart.add(t);
                Total+=TotalPrice;
                clear();
            }
        });
        btnShowCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeActivity.this,DetailsActivity.class));
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
                                Toast.makeText( HomeActivity.this, exception.getMessage(), Toast.LENGTH_LONG ).show();

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult( requestCode, resultCode, data );
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            loadedImage.setImageURI( imageUri );
           // Picasso.get().load(imageUri).into(loadedImage);


        }
    }

    public void clear(){
        editTextName.setText("");
        editTextcode.setText("");
        editTextprice.setText("");
        editTextQuntity.setText("");
    }
}

