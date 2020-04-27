package com.eiadmreh.easylearn;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity {
    EditText emailId,password,name,phone;
    Button btnSignUp;
    TextView tvSignIn;
    FirebaseAuth mFirebaseAuth;
    private String Email,Password,Name,Phone;
    private long MaxId=0;
    public static String CustomerName;
    FirebaseDatabase  mdatabase;
    DatabaseReference mDatabaseRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mFirebaseAuth=FirebaseAuth.getInstance();
        emailId=(EditText)findViewById(R.id.editText);
        password=(EditText)findViewById(R.id.editText2);
        name=(EditText)findViewById(R.id.etName);
        phone=(EditText)findViewById(R.id.etPhone);
        btnSignUp=(Button)findViewById(R.id.button);
        tvSignIn=(TextView)findViewById(R.id.textView);
        mdatabase = FirebaseDatabase.getInstance();
        mDatabaseRef = mdatabase.getReference("Customers");
        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                    MaxId=dataSnapshot.getChildrenCount();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Email=emailId.getText().toString();
                Password=password.getText().toString();
                if(Email.isEmpty()){
                    emailId.setError("Please enter a valid email..");
                    emailId.requestFocus();
                }
                else if(Password.isEmpty()){
                    password.setError("Please enter your password !");
                    password.requestFocus();
                }
                else if(Email.isEmpty()&&Password.isEmpty()){
                    Toast.makeText(MainActivity.this,"Fields are empty !",Toast.LENGTH_SHORT).show();
                }
                else if(!(Email.isEmpty()&&Password.isEmpty()) && chkNamePhone()){
                    mFirebaseAuth.createUserWithEmailAndPassword(Email,Password).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(MainActivity.this,"Sign up not successful ! ,Please try again",Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Email=Email.toLowerCase();
                                AddNewUser();
                                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                            }
                        }
                    });
                }
                else
                    Toast.makeText(MainActivity.this,"Error ocurred !",Toast.LENGTH_SHORT).show();
            }
        });
        tvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(MainActivity.this,LoginActivity.class);
                startActivity(i);
            }
        });
    }

    private boolean chkNamePhone() {
        Name=name.getText().toString();
        Phone=phone.getText().toString();
        if(Name.isEmpty()){
            name.setError("Please enter your name");
            name.requestFocus();
        }
        else if(Phone.isEmpty()){
            phone.setError("Please enter your phone number !");
            phone.requestFocus();
        }
        else if(Name.isEmpty()&&Phone.isEmpty()){
            Toast.makeText(MainActivity.this,"Fields Name & Phone are empty !",Toast.LENGTH_SHORT).show();
        }
        else if(!(Name.isEmpty()&&Phone.isEmpty()))
            return true;

        return false;
    }

    private void AddNewUser() {
        Customer customer = new Customer();
        customer.setName(Name);
        customer.setPhone(Long.valueOf(Phone));
        customer.setEmail(Email);
        final Long uploadId = MaxId + 1;
        mDatabaseRef.child(String.valueOf(uploadId)).setValue(customer);
    }

}
