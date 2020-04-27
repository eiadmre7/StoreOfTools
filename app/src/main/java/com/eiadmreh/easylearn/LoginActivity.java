package com.eiadmreh.easylearn;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {
    EditText emailId,password;
    Button btnSignIn;
    TextView tvSignUp;
    FirebaseAuth mFirebaseAuth;
    FirebaseUser mFirebaseUser;
    private FirebaseDatabase mDatabase;
    public static String custName;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mFirebaseAuth=FirebaseAuth.getInstance();
        mFirebaseUser=null;
        mDatabase = FirebaseDatabase.getInstance();
        emailId=(EditText)findViewById(R.id.editText);
        password=(EditText)findViewById(R.id.editText2);
        btnSignIn=(Button)findViewById(R.id.button);
        tvSignUp=(TextView)findViewById(R.id.textView);

        mAuthStateListener=new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                mFirebaseUser=mFirebaseAuth.getCurrentUser();
              /*  if(mFirebaseUser!=null){
                    Toast.makeText(LoginActivity.this,"You are logged in",Toast.LENGTH_SHORT).show();
                    Intent i=new Intent(LoginActivity.this,HomeActivity.class);
                    startActivity(i);
                }
                else*/
                    Toast.makeText(LoginActivity.this,"Login Please !",Toast.LENGTH_SHORT).show();
            }
        };
        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String Email=emailId.getText().toString();
                String Password=password.getText().toString();
                if(Email.isEmpty()){
                    emailId.setError("Please enter a valid email..");
                    emailId.requestFocus();
                }
                else if(Password.isEmpty()){
                    password.setError("Please enter your password !");
                    password.requestFocus();
                }
                else if(Email.isEmpty()&&Password.isEmpty()){
                    Toast.makeText(LoginActivity.this,"Fields are empty !",Toast.LENGTH_SHORT).show();
                }
                else if(!(Email.isEmpty()&&Password.isEmpty())){
                    mFirebaseAuth.signInWithEmailAndPassword(Email,Password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            {
                                if(!task.isSuccessful()){
                                    Toast.makeText(LoginActivity.this,"Login error ! ,Please try again",Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Query query = mDatabase.getReference("Customers")
                                            .orderByChild("email")
                                            .equalTo(Email);
                                    query.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.exists())
                                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                    Customer customer = snapshot.getValue(Customer.class);
                                                    custName=customer.getName();
                                                }

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                    password.setText("");
                                    emailId.setText("");
                                    startActivity(new Intent(LoginActivity.this,NavigateActivity.class));
                                }
                            }
                        }
                    });
                }
                else
                    Toast.makeText(LoginActivity.this,"Error ocurred !",Toast.LENGTH_SHORT).show();
            }
        });
        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intSignUp=new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intSignUp);
            }
        });

    }
    @Override
    protected void onStart(){
        super.onStart();
        mFirebaseAuth.addAuthStateListener(mAuthStateListener);
    }
}
