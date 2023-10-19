package com.example.dhass;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;




public class SecondFragment extends Fragment {

    private Spinner dateSpinner;
    private Calendar selectedDate;

    private Button setAlarmButton;
    private Button stopAlarmButton;
    private Button checkAlarmButton;

    private  Button onceAlarmButton;

    private TextView displayActionTImeTextView;




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_second, container, false);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MySharedPreferences", Context.MODE_PRIVATE);

        Spinner alarmSelectSpinner = rootView.findViewById(R.id.alarmSelectSpinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(requireContext(), R.array.on_off_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        alarmSelectSpinner.setAdapter(adapter);

        selectedDate = Calendar.getInstance();

        TimePicker timePicker = rootView.findViewById(R.id.timePicker);
        timePicker.setIs24HourView(true); // Set this to true for 24-hour format
        timePicker.setCurrentHour(selectedDate.get(Calendar.HOUR_OF_DAY));
        timePicker.setCurrentMinute(selectedDate.get(Calendar.MINUTE));
        setAlarmButton = rootView.findViewById(R.id.setAlarmButton);
        stopAlarmButton = rootView.findViewById(R.id.stopAlarmButton);
        //checkAlarmButton = rootView.findViewById(R.id.checkAlarmButton);
        //onceAlarmButton = rootView.findViewById(R.id.onceAlarmButton);
        dateSpinner = rootView.findViewById(R.id.dateSpinner);
        displayActionTImeTextView = rootView.findViewById((R.id.displayActionTImeTextView));

        int alarmday = sharedPreferences.getInt("alarmday", 0);
        int alarmmonth = sharedPreferences.getInt("alarmmonth", 0);
        int alarmyear = sharedPreferences.getInt("alarmyear", 0);
        int alarmhour = sharedPreferences.getInt("alarmhour", 0);
        int alarmminute = sharedPreferences.getInt("alarmminute", 0);
        int alarmsecond = sharedPreferences.getInt("alarmsecond", 0);
        String action = sharedPreferences.getString("alarmaction", "");
        String alarmstr = "Alarm set "+alarmyear + "-" + (alarmmonth+1) + "-" + alarmday + " " + alarmhour + ":" + alarmminute + ":" + alarmsecond + " to " + action;
        displayActionTImeTextView.setText(alarmstr);


        final SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.US);

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.spinner_entries,
                android.R.layout.simple_spinner_item
        );
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dateSpinner.setAdapter(adapter1);

        dateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("Tag ","position " + position);
                if (position == 1) {
                    showDatePickerDialog();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
                Log.d("Nothing selected ","nothing selected ");

            }
        });

        setAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Schedule the repeating alarm
                // Update the selectedDateTime with the values from date and time pickers
                selectedDate.set(
                        selectedDate.get(Calendar.YEAR),
                        selectedDate.get(Calendar.MONTH),
                        selectedDate.get(Calendar.DATE),
                        timePicker.getCurrentHour(),
                        timePicker.getCurrentMinute()
                );

                // Format the selected date and time
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
                String formattedDateTime = sdf.format(selectedDate.getTime());

                // Display the selected date and time
                Toast.makeText(getView().getContext(), "Selected Date and Time: " + formattedDateTime, Toast.LENGTH_LONG).show();

                String actionValue = alarmSelectSpinner.getSelectedItem().toString();

                Toast.makeText(getView().getContext(), "Selected value of action: " + actionValue, Toast.LENGTH_LONG).show();

                int dayOfMonth = selectedDate.get(Calendar.DAY_OF_MONTH);

                // Get the current month (0-11, where 0 represents January)
                int month = selectedDate.get(Calendar.MONTH);

                // Get the current year (e.g., 2023)
                int year = selectedDate.get(Calendar.YEAR);

                // Get the current hour in 24-hour format (0-23)
                int hourOfDay = selectedDate.get(Calendar.HOUR_OF_DAY);

                // Get the current minute (0-59)
                int minute = selectedDate.get(Calendar.MINUTE);

                // Get the current second (0-59)
                int second = selectedDate.get(Calendar.SECOND);

                Log.d("Day Now: ","Day Now: " + dayOfMonth);
                Log.d("Month Now: ","Month Now: " + month);
                Log.d("Year Now: ","Year Now: " + year);
                Log.d("Hour Now: ","Hour Now: " + hourOfDay);
                Log.d("Minute Now: ","Minute Now: " + minute);
                Log.d("Seconds Now: ","Seconds Now: " + second);

                SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MySharedPreferences", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("alarmday", dayOfMonth);
                editor.putInt("alarmmonth", month);
                editor.putInt("alarmyear", year);
                editor.putInt("alarmhour", hourOfDay);
                editor.putInt("alarmminute", minute);
                editor.putInt("alarmsecond", second);
                editor.putString("alarmaction", actionValue);
                editor.apply();

                displayActionTImeTextView.setText("Alarm set " +formattedDateTime+" to "+ actionValue );

//                if (AlarmPermissionHelper.isAlarmPermissionGranted(getActivity())) {
//                    // Permission is already granted.
//                    // You can proceed with setting alarms or other actions.
//                    Toast.makeText(getView().getContext(), "Repeating Alarm Set", Toast.LENGTH_SHORT).show();
//                    AlarmScheduler.scheduleRepeatingAlarm(getActivity());
//                } else {
//                    // Permission has not been granted, request it.
//                    Toast.makeText(getView().getContext(), "Asking Permission", Toast.LENGTH_SHORT).show();
//
//                    AlarmPermissionHelper.requestAlarmPermission(getActivity());
//                    AlarmScheduler.scheduleRepeatingAlarm(getActivity());
//
//                }

            }
        });

        stopAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getView().getContext(), "Cancel Alarm", Toast.LENGTH_SHORT).show();
                clearAlarm();
                displayActionTImeTextView.setText("Alarm not set !");

                //AlarmScheduler.cancelRepeatingAlarm(getActivity());
//                String name = sharedPreferences.getString("name", "");
//                int age = sharedPreferences.getInt("age", 0);
//                boolean isStudent = sharedPreferences.getBoolean("isStudent", false);
//
//                Toast.makeText(getView().getContext(), name, Toast.LENGTH_SHORT).show();
//                Toast.makeText(getView().getContext(), String.valueOf(age), Toast.LENGTH_SHORT).show();
//                age = age + 1;
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                editor.putInt("age", age);
//                editor.apply();
//                age = sharedPreferences.getInt("age", 0);
//                Toast.makeText(getView().getContext(), String.valueOf(age), Toast.LENGTH_SHORT).show();

            }
        });

//        onceAlarmButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(getView().getContext(), "Alarm Set", Toast.LENGTH_SHORT).show();
//                AlarmScheduler.scheduleAlarmOnce(getActivity());
//
//            }
//        });

//        checkAlarmButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //checking alarm is set or not
//                boolean isAlarmSet = AlarmScheduler.isRepeatingAlarmSet(requireContext(), 0);
//
//                if (isAlarmSet) {
//                    // Alarm is already set
//                    Toast.makeText(requireContext(), "Alarm Already Set"+isAlarmSet, Toast.LENGTH_SHORT).show();
//
//                } else {
//                    // Alarm is not set
//                    Toast.makeText(requireContext(), "Alarm Not Set", Toast.LENGTH_SHORT).show();
//
//                }
//            }
//        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MySharedPreferences", Context.MODE_PRIVATE);

        int alarmday = sharedPreferences.getInt("alarmday", 0);
        int alarmmonth = sharedPreferences.getInt("alarmmonth", 0);
        int alarmyear = sharedPreferences.getInt("alarmyear", 0);
        int alarmhour = sharedPreferences.getInt("alarmhour", 0);
        int alarmminute = sharedPreferences.getInt("alarmminute", 0);
        int alarmsecond = sharedPreferences.getInt("alarmsecond", 0);
        String action = sharedPreferences.getString("alarmaction", "");
        String alarmstr = "";
        if(alarmyear == 0){
            alarmstr = "Alarm not set !";
        }else{
            alarmstr = "Alarm set "+alarmyear + "-" + (alarmmonth+1) + "-" + alarmday + " " + alarmhour + ":" + alarmminute + ":" + alarmsecond + " to " + action;
        }
        displayActionTImeTextView.setText(alarmstr);


        // Code to be executed when the fragment comes into focus
        // This code will run each time the fragment becomes visible to the user
        // For example, you can update UI elements, perform data refresh, etc.
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

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                requireContext(),
                dateSetListener,
                selectedDate.get(Calendar.YEAR),
                selectedDate.get(Calendar.MONTH),
                selectedDate.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            selectedDate.set(year, monthOfYear, dayOfMonth);
            updateSpinner();
        }
    };

    private void updateSpinner() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.US);
        String formattedDate = dateFormat.format(selectedDate.getTime());
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                new String[]{formattedDate, "Open Calander"}
        );
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dateSpinner.setAdapter(spinnerAdapter);
        dateSpinner.setSelection(0);

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



//        binding.buttonSecond.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                NavHostFragment.findNavController(SecondFragment.this)
//                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
//            }
//        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}