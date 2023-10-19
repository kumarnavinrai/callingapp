package com.example.dhass;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.dhass.databinding.FragmentFirstBinding;

import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    private TextView batteryLevelTextView;
    private TextView chargingStatusTextView;

    private TextView upperLimitDisp;
    private TextView lowerLimitDisp;

    private Button trunOn;
    private Button trunOff;
    private int upperlimit = 0;
    private int lowerlimit = 0;

    private BroadcastReceiver batteryReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            float batteryLevel = (level / (float) scale) * 100;

            Log.d("BatteryLevel", "Battery Level: " + batteryLevel + "%");
        }
    };

    private Handler handler = new Handler(Looper.getMainLooper());
    private Runnable batteryRunnable = new Runnable() {
        @Override
        public void run() {
            // Request battery level
            requestBatteryLevel();

            // Schedule the next run after 30 seconds
            handler.postDelayed(this, 30000);
        }
    };

    @Nullable
    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        View view = inflater.inflate(R.layout.fragment_first, container, false);
        batteryLevelTextView = view.findViewById(R.id.chargingPercentage);
        chargingStatusTextView = view.findViewById(R.id.chargingStatus);
        trunOn = view.findViewById(R.id.turnOnButton);
        trunOff = view.findViewById(R.id.turnOffButton);
        upperLimitDisp = view.findViewById(R.id.upperLimitDisp);
        lowerLimitDisp = view.findViewById(R.id.lowerLimitDisp);


        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MySharedPreferences", Context.MODE_PRIVATE);
        upperlimit = sharedPreferences.getInt("upperlimit", 0);
        lowerlimit = sharedPreferences.getInt("lowerlimit", 0);
        upperLimitDisp.setText("Max: " + String.valueOf(upperlimit)+"%");
        lowerLimitDisp.setText("Min: " + String.valueOf(lowerlimit) + "%");

        SharedPreferences.Editor editor = sharedPreferences.edit();
        //        editor.putString("name", "navin");
        //        editor.putInt("age", 30);
        //        editor.putBoolean("isStudent", false);
        //        editor.apply();



        trunOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String urlon = "http://192.168.137.150/lappyoffd2";
                new HttpRequestTask().execute(urlon);
                Toast.makeText(getView().getContext(), "Turn on url: " + urlon, Toast.LENGTH_SHORT).show();
            }
        });

        trunOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String urloff = "http://192.168.137.150/lappyond2";
                new HttpRequestTask().execute(urloff);
                Toast.makeText(getView().getContext(), "Turn off url: " + urloff, Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Context context = requireContext();

        // Register the battery receiver
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        requireActivity().registerReceiver(batteryReceiver, filter);

        // Start requesting battery level every 30 seconds
        handler.postDelayed(batteryRunnable, 30000);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Unregister the battery receiver when the fragment's view is destroyed
        requireActivity().unregisterReceiver(batteryReceiver);
        // Remove the battery level request callback
        handler.removeCallbacks(batteryRunnable);
    }

    private void clearAlarm() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MySharedPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("alarmday", 0);
        editor.putInt("alarmmonth", 0);
        editor.putInt("alarmyear", 0);
        editor.putInt("alarmhour", 0);
        editor.putInt("alarmminute", 0);
        editor.putInt("alarmsecond", 0);
        editor.putString("alarmaction", "");
        editor.apply();
    }

    private void requestBatteryLevel() {
        Context context = requireContext();
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = context.registerReceiver(null, filter);

        if (batteryStatus != null) {
            int level = batteryStatus.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = batteryStatus.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            float batteryLevel = (level / (float) scale) * 100;

            // Update the TextView with the battery level
            batteryLevelTextView.setText("Battery Level: " + batteryLevel + "%");

            // Check charging status
            int chargingStatus = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            boolean isCharging = chargingStatus == BatteryManager.BATTERY_STATUS_CHARGING || chargingStatus == BatteryManager.BATTERY_STATUS_FULL;

            // Update the TextView with charging status
            chargingStatusTextView.setText("Charging Status: " + (isCharging ? "Charging" : "Not Charging"));

            String urlon = "http://192.168.137.150/lappyoffd2"; // Your desired URL
            String urloff = "http://192.168.137.150/lappyond2"; // Your desired URL

            if(isCharging && upperlimit < batteryLevel){
                Toast.makeText(context,"Turn off charging", Toast.LENGTH_SHORT);
                Log.d("Turn off charging", "url: " + urloff);
                new HttpRequestTask().execute(urloff);
            }

            if(!isCharging && lowerlimit > batteryLevel){
                Toast.makeText(context,"Turn on charging", Toast.LENGTH_SHORT);
                Log.d("Turn on charging", "url: " + urlon);
                new HttpRequestTask().execute(urlon);
            }

            Log.d("BatteryLevel", "Battery Level: " + batteryLevel + "%");

            //this code will execute alarm set by user
            // Get the current date and time
            Calendar calendar = Calendar.getInstance();

            // Get the current date in yyyy-MM-dd format
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            String currentDate = dateFormat.format(calendar.getTime());

            // Get the current time in HH:mm format
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.US);
            String currentTime = timeFormat.format(calendar.getTime());

            // Display the current date and time
            Log.d("Date Now: ","Current Date: " + currentDate);
            Log.d("Time Now: ","Current Time: " + currentTime);

            int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

            // Get the current month (0-11, where 0 represents January)
            int month = calendar.get(Calendar.MONTH);

            // Get the current year (e.g., 2023)
            int year = calendar.get(Calendar.YEAR);

            // Get the current hour in 24-hour format (0-23)
            int hourOfDay = calendar.get(Calendar.HOUR_OF_DAY);

            // Get the current minute (0-59)
            int minute = calendar.get(Calendar.MINUTE);

            // Get the current second (0-59)
            int second = calendar.get(Calendar.SECOND);

            Log.d("Day Now: ","Day Now: " + dayOfMonth);
            Log.d("Month Now: ","Month Now: " + month);
            Log.d("Year Now: ","Year Now: " + year);
            Log.d("Hour Now: ","Hour Now: " + hourOfDay);
            Log.d("Minute Now: ","Minute Now: " + minute);
            Log.d("Seconds Now: ","Seconds Now: " + second);


            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MySharedPreferences", Context.MODE_PRIVATE);

            int alarmday = sharedPreferences.getInt("alarmday", 0);
            int alarmmonth = sharedPreferences.getInt("alarmmonth", 0);
            int alarmyear = sharedPreferences.getInt("alarmyear", 0);
            int alarmhour = sharedPreferences.getInt("alarmhour", 0);
            int alarmminute = sharedPreferences.getInt("alarmminute", 0);
            int alarmsecond = sharedPreferences.getInt("alarmsecond", 0);
            String action = sharedPreferences.getString("alarmaction", "");

            Log.d("Alarm Day: ","Alarm Day: " + alarmday);
            Log.d("Alarm Month: ","Alarm Month: " + alarmmonth);
            Log.d("Alarm Year : ","Alarm Year: " + alarmyear);
            Log.d("Alarm Hour: ","Alarm Hour: " + alarmhour);
            Log.d("Alarm Minute: ","Alarm Minute: " + alarmminute);
            Log.d("Alarm Seconds: ","Alarm Seconds: " + alarmsecond);
            Log.d("Alarm action", "Alarm action" + action);

            if(alarmyear == year){
                Log.d("Alarm year matched", "Alarm year matched");
                if(alarmmonth == month){
                    Log.d("Alarm month matched", "Alarm month matched");
                    if(alarmday == dayOfMonth){
                        Log.d("Alarm day matched", "Alarm day matched");
                        if(alarmhour == hourOfDay){
                            Log.d("Alarm hour matched", "Alarm hour matched");
                            if(alarmminute <= minute){
                                Log.d("Alarm minute matched", "Alarm minute matched");
                                if(alarmsecond < second){
                                    Log.d("Alarm seconds matched", "Alarm seconds matched");
                                    Log.d("action.toLowerCase()", action.toLowerCase());
                                    //Log.d("action.toLowerCase() == off", );

                                    String checkon = "on";
                                    String checkoff = "off";
                                    boolean isOn = action.toLowerCase().equals(checkon);
                                    boolean isOff = action.toLowerCase().equals(checkoff);

                                    if(isOn){
                                        Log.d("Turn on charging", "url: " + urlon);
                                        new HttpRequestTask().execute(urlon);
                                        clearAlarm();
                                    }

                                    if(isOff){
                                        Log.d("Turn off charging", "url: " + urloff);
                                        new HttpRequestTask().execute(urloff);
                                        clearAlarm();
                                    }

                                }
                            }
                        }
                    }
                }
            }

        }
    }

    private class HttpRequestTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            HttpURLConnection urlConnection = null;

            try {
                URL url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();

                InputStream in = urlConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String line;
                while ((line = reader.readLine()) != null) {
                    result += line;
                }

            } catch (MalformedURLException e) {
                //throw new RuntimeException(e);
            } catch (IOException e) {
                //throw new RuntimeException(e);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            Log.d("Api Response", "Result of api response: " + result );
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            // Handle the result, update UI, etc.
            // This method runs on the UI thread.
        }
    }

}