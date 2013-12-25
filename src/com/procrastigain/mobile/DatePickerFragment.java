package com.procrastigain.mobile;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;

public class DatePickerFragment extends DialogFragment 
							implements DatePickerDialog.OnDateSetListener {

	private Calendar defaultDay;
	
	public DatePickerFragment(Calendar day) {
		super();
		defaultDay = day;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		// Use one week from today as the default date in the picker
		//final Calendar c = Calendar.getInstance();
		//c.add(Calendar.DAY_OF_MONTH, 7);
		int year = defaultDay.get(Calendar.YEAR);
		int month = defaultDay.get(Calendar.MONTH);
		int day = defaultDay.get(Calendar.DAY_OF_MONTH);
		
		// Create a new instance of DatePickerDialog and return it
		return new DatePickerDialog(getActivity(), this, year, month, day);
	}

	public void onDateSet(DatePicker view, int year, int month, int day) {
		// Do something with the date chosen by the user
		TextView dateTextView = (TextView) getActivity().findViewById(R.id.id_new_task_date);
		
		Calendar cal = Calendar.getInstance();
		cal.set(year, month, day);
		Date date = cal.getTime();

		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
		
		String dateString = sdf.format(date);
		
		dateTextView.setText(dateString);
	}

}