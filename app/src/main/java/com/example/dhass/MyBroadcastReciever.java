package com.example.dhass;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import androidx.legacy.content.WakefulBroadcastReceiver;

public class MyBroadcastReciever extends WakefulBroadcastReceiver {

    // Default constructor
    public MyBroadcastReciever() {
        // Initialization code here, if needed
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("MySharedPreferences", Context.MODE_PRIVATE);

        // Your task to be performed every 30 seconds
        String name = sharedPreferences.getString("name", "");
        int age = sharedPreferences.getInt("age", 0);
        boolean isStudent = sharedPreferences.getBoolean("isStudent", false);

        Toast.makeText(context, name, Toast.LENGTH_SHORT).show();
        Toast.makeText(context, String.valueOf(age), Toast.LENGTH_SHORT).show();
        age = age + 1;
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("age", age);
        editor.apply();
        age = sharedPreferences.getInt("age", 0);
        Log.d("executing alarm","Value change "+String.valueOf(age));

        Toast.makeText(context, String.valueOf(age), Toast.LENGTH_SHORT).show();

    }
}
