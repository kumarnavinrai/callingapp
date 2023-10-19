package com.example.dhass;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class PlaceholderFragment extends Fragment {

    private Button buttonSave;
    private EditText upperLimitEditText;
    private EditText lowerLimitEditText;
    public PlaceholderFragment() {
        // Required empty public constructor
    }

    private static final int REQUEST_CALL_PERMISSION = 1;

    private String phoneNumber;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MySharedPreferences", Context.MODE_PRIVATE);
        View view = inflater.inflate(R.layout.fragment_placeholder, container, false);
        // Customize the fragment's layout and functionality here
        buttonSave = view.findViewById(R.id.saveButton);
        upperLimitEditText = view.findViewById(R.id.upperLimitEditText);
        lowerLimitEditText = view.findViewById(R.id.lowerLimitEditText);
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check for CALL_PHONE permission
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                    makePhoneCall();
                } else {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CALL_PHONE}, REQUEST_CALL_PERMISSION);
                }
//                String ul = upperLimitEditText.getText().toString();
//                String ll = lowerLimitEditText.getText().toString();
//                ul = ul.trim();
//                ll = ll.trim();
//                if(!ul.isEmpty() && Integer.parseInt(ul) <= 100){
//                    if(!ll.isEmpty() && Integer.parseInt(ll) >=1){
//                        if(Integer.parseInt(ul) > Integer.parseInt(ll)) {
//                            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MySharedPreferences", Context.MODE_PRIVATE);
//                            SharedPreferences.Editor editor = sharedPreferences.edit();
//                            editor.putInt("upperlimit", Integer.parseInt(ul));
//                            editor.putInt("lowerlimit", Integer.parseInt(ll));
//                            editor.apply();
//                            Toast.makeText(view.getContext(), "Successfully Saved !!", Toast.LENGTH_SHORT).show();
//                        }else{
//                            Toast.makeText(view.getContext(),"Upper limit should be more than lower limit !", Toast.LENGTH_SHORT).show();
//                        }
//                    }else{
//                        Toast.makeText(view.getContext(),"Lower Limit Cannot be empty or less than 1 !", Toast.LENGTH_SHORT).show();
//                    }
//                }else{
//                    Toast.makeText(view.getContext(),"Upper Limit Cannot be empty or greater than 100 !", Toast.LENGTH_SHORT).show();
//                }
            }
        });



        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CALL_PERMISSION && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            makePhoneCall();
        } else {
            Log.e("PhoneCall", "Call permission denied");
        }
    }

    private void makePhoneCall() {
        // Use Retrofit to fetch phone number from the API
        phoneNumber = "+60362091000";
        dialPhoneNumber();



//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("https://your_api_base_url.com/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        ApiService apiService = retrofit.create(ApiService.class);
//        Call<PhoneNumberResponse> call = apiService.getPhoneNumber();
//        call.enqueue(new Callback<PhoneNumberResponse>() {
//            @Override
//            public void onResponse(Call<PhoneNumberResponse> call, Response<PhoneNumberResponse> response) {
//                if (response.isSuccessful()) {
//                    phoneNumber = response.body().getPhoneNumber();
//                    dialPhoneNumber();
//                } else {
//                    Log.e("PhoneCall", "Failed to retrieve phone number from API");
//                }
//            }
//
//            @Override
//            public void onFailure(Call<PhoneNumberResponse> call, Throwable t) {
//                Log.e("PhoneCall", "API request failed: " + t.getMessage());
//            }
//        });
    }

    private void dialPhoneNumber() {
        Intent intent = new Intent(Intent.ACTION_CALL);
        intent.setData(Uri.parse("tel:" + phoneNumber));

        try {
            startActivity(intent);
            Log.d("PhoneCall", "Call initiated");

            Toast.makeText(getContext(),"Call initiated", Toast.LENGTH_SHORT).show();
            // Start listening for call state
            handleCallState();
        } catch (SecurityException e) {
            Log.d("PhoneCall", "Call permission denied");
            Toast.makeText(getContext(),"Call permission denied: ", Toast.LENGTH_SHORT).show();
        }
    }

    private void handleCallState() {
        TelephonyManager telephonyManager = (TelephonyManager) getActivity().getSystemService(getActivity().TELEPHONY_SERVICE);

        PhoneStateListener phoneStateListener = new PhoneStateListener() {
            @Override
            public void onCallStateChanged(int state, String phoneNumber) {
                switch (state) {
                    case TelephonyManager.CALL_STATE_OFFHOOK:

                        Log.d("PhoneCall", "Call picked up");
                        Toast.makeText(getContext(),"Call picked up", Toast.LENGTH_SHORT).show();
                        break;
                    case TelephonyManager.CALL_STATE_IDLE:
                        Log.d("PhoneCall", "Call dropped");
                        Toast.makeText(getContext(),"Call dropped", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };

        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
    }
}
