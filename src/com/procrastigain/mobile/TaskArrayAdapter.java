package com.procrastigain.mobile;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class TaskArrayAdapter extends ArrayAdapter<Task> {
	
	public final static String TASK_ID = "com.procrastigain.mobile.TASK_ID";
	public final static String TITLE = "com.procrastigain.mobile.TITLE";
	public final static String HOURS = "com.procrastigain.mobile.HOURS";
	public final static String MINUTES = "com.procrastigain.mobile.MINUTES";
	public final static String APPEAL = "com.procrastigain.mobile.APPEAL";
	public final static String DUE_DATE = "com.procrastigain.mobile.DUE_DATE";
	
	private List<Task> mTasks;
	private static SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
	
	public TaskArrayAdapter(Context context, int resource, List<Task> objects) {
		super(context, resource, objects);
		mTasks = objects;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View listViewItem = convertView;

		if (listViewItem == null) {
	        LayoutInflater vi;
	        vi = LayoutInflater.from(getContext());
	        listViewItem = vi.inflate(R.layout.task_template, null);
	    }

	    final Task p = mTasks.get(position);
		
	    if (p != null) {

	        TextView titleTextView = (TextView) listViewItem.findViewById(R.id.id_task_title);
	        TextView dateTextView = (TextView) listViewItem.findViewById(R.id.id_task_date);

	        if (titleTextView != null) {
	            titleTextView.setText(p.getTitle());
	        }
	        if (dateTextView != null) {
	            dateTextView.setText(sdf.format(p.getDueDate()));
	        }
	    }
	    
	    final View finalListViewItem = listViewItem;
	    
	    /******** Click button to work on a task: ********/
		Button workButton = (Button) listViewItem.findViewById(R.id.id_task_work_button);
		workButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				
				/* Implement working on a task in here, remove below code */
				
            	TextView titleTextView = (TextView) finalListViewItem.findViewById(R.id.id_task_title);
				CharSequence text = "Work on task: " + titleTextView.getText();
         		Toast.makeText(v.getContext(), text, Toast.LENGTH_SHORT).show();
             }
         });
		/*************************************************/
	    
	    TextView editTextView = (TextView) listViewItem.findViewById(R.id.id_task_edit);
		editTextView.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				
				/* Implement editing a task in here, remove below code */
				
				Intent intent = new Intent(finalListViewItem.getContext(), EditTaskActivity.class);

				String taskId = p.getId();
				String title = p.getTitle();
				int hours = p.getHours();
				int minutes = p.getMinutes();
				int appeal = p.getAppeal();
				String dueDate = sdf.format(p.getDueDate());
				
				intent.putExtra(TASK_ID, taskId);
				intent.putExtra(TITLE, title);
				intent.putExtra(HOURS, hours);
				intent.putExtra(MINUTES, minutes);
				intent.putExtra(APPEAL, appeal);
				intent.putExtra(DUE_DATE, dueDate);
				
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				v.getContext().startActivity(intent);
			}
		});

	    return listViewItem;

	}

}
