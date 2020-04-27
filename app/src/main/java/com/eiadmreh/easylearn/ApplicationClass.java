package com.eiadmreh.easylearn;

import android.app.Application;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import androidx.annotation.NonNull;

public class ApplicationClass extends Application {

    public static ArrayList<ToolinCart>thisCart;
    public static double Total;
    @Override
    public void onCreate() {
        super.onCreate();
        thisCart=new ArrayList<ToolinCart>();

    }
}
