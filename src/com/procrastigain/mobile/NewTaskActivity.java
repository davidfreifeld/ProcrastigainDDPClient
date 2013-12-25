package com.procrastigain.mobile;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

//import com.procrastigain.mobile.TaskContract.TaskEntry;

public class NewTaskActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_task);
        // Show the Up button in the action bar.
        setupActionBar();
        
        setupUI(findViewById(R.id.id_new_task_parent_layout));
        
        /******* Set up the hours spinner *********/
        Spinner spinnerHours = (Spinner) findViewById(R.id.id_new_task_hours_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterHours = ArrayAdapter.createFromResource(this,
             R.array.hours_spinner_ints, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterHours.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerHours.setAdapter(adapterHours);
        /******************************************/
        
        /******* Set up the minutes spinner *********/
        Spinner spinnerMinutes = (Spinner) findViewById(R.id.id_new_task_minutes_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapterMinutes = ArrayAdapter.createFromResource(this,
             R.array.minutes_spinner_ints, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapterMinutes.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinnerMinutes.setAdapter(adapterMinutes);
        /******************************************/
        
        TextView dateTextView = (TextView) findViewById(R.id.id_new_task_date);
        final Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_MONTH, 7);
		Date date = c.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
		String dateString = sdf.format(date);
		dateTextView.setText(dateString);
        
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.new_task, menu);
        return true;
    }
    

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // This ID represents the Home or Up button. In the case of this
                // activity, the Up button is shown. Use NavUtils to allow users
                // to navigate up one level in the application structure. For
                // more details, see the Navigation pattern on Android Design:
                //
                // http://developer.android.com/design/patterns/navigation.html#up-vs-back
                //
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    public void showDatePickerDialog(View view) {
    	// add another constructor that sets the date and then calls super()
    	final Calendar c = Calendar.getInstance();
		c.add(Calendar.DAY_OF_MONTH, 7);
        DialogFragment newFragment = new DatePickerFragment(c);
        newFragment.show(getFragmentManager(), "datePicker");
    }
    
    public void saveNewTask(View view) {
    	
    	TextView titleTextView = (TextView) findViewById(R.id.id_new_task_title);
    	String title = titleTextView.getText().toString();
    	
    	if (title.equals(""))
    	{
    		CharSequence text = "Please enter a task title.";
    		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    		return;
    	}
    	
    	Spinner hoursSpinner = (Spinner) findViewById(R.id.id_new_task_hours_spinner);
    	int hours = Integer.parseInt(hoursSpinner.getSelectedItem().toString());
    	
    	Spinner minutesSpinner = (Spinner) findViewById(R.id.id_new_task_minutes_spinner);
    	int minutes = Integer.parseInt(minutesSpinner.getSelectedItem().toString());
    	
    	if (hours == 0 && minutes == 0)
    	{
    		CharSequence text = "Please enter the amount of time needed.";
    		Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    		return;
    	}
    	
    	SeekBar appealSeekBar = (SeekBar) findViewById(R.id.id_new_task_appeal_seekbar);
    	int appeal = appealSeekBar.getProgress();
    	
    	TextView dateTextView = (TextView) findViewById(R.id.id_new_task_date);
    	String dueDate = dateTextView.getText().toString();
    	
    	/************* Now write to the database ***********/
    	
    	/************ Go to MyTasks Activity **************/
    	Intent intent = new Intent(this, MyTasksActivity.class);
        startActivity(intent);
    	
    }
    
    public void hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager)  getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
    }
    
    public void setupUI(View view) {

        //Set up touch listener for non-text box views to hide keyboard.
        if(!(view instanceof EditText)) {

            view.setOnTouchListener(new OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard();
                    return false;
                }

            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {

            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

                View innerView = ((ViewGroup) view).getChildAt(i);

                setupUI(innerView);
            }
        }
    }

}
