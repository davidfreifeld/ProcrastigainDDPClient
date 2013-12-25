package com.procrastigain.mobile;

import java.util.ArrayList;
import java.util.List;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.keysolutions.ddpclient.android.DDPBroadcastReceiver;
import com.keysolutions.ddpclient.android.DDPStateSingleton;

//import com.procrastigain.mobile.TaskContract.TaskEntry;

public class MyTasksActivity extends Activity {

	/** broadcast receiver */
    private BroadcastReceiver mReceiver;
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
    	setContentView(R.layout.activity_my_tasks);
    	
//    	// Show the Up button in the action bar.
//        setupActionBar();  	        
    }

    public void onResume() {
    	super.onResume();
    	
    	mReceiver = new DDPBroadcastReceiver(MyDDPState.getInstance(), this) {
    		protected void onDDPConnect(DDPStateSingleton ddp) {
    			super.onDDPConnect(ddp);
    			// add our subscriptions needed for the activity here
                ddp.subscribe("tasks", new Object[] {});
                
    		}
    		protected void onSubscriptionUpdate(String changeType,
    				String subscriptionName, String docId) {
    			if (subscriptionName.equals("tasks")) {
    				showMyTasks();          		
    			}
    		}
    		protected void onLogin() {
    			// something in here?
    		}
    		protected void onLogout() {
    			// something in here?
    		}
    	};
    	MyDDPState.getInstance().connectIfNeeded();
    }
    
    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void setupActionBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
        getActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.my_tasks, menu);
        return super.onCreateOptionsMenu(menu);
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
            case R.id.action_new_task:
Intent intent = new Intent(this, NewTaskActivity.class);
            	
            	/* From the tutorial: shows how to attach data to an Intent instance
            	 * EditText editText = (EditText) findViewById(R.id.edit_message);
            	 * String message = editText.getText().toString();
            	 * intent.putExtra(EXTRA_MESSAGE, message);*/
            	
            	startActivity(intent);
            	return true;
        }
        return super.onOptionsItemSelected(item);
    }
    
    private void showMyTasks() {
    	List<Task> myTasks = new ArrayList<Task>();
    	
    	for (Task task : MyDDPState.getInstance().getTasks().values()) {
    		myTasks.add(task);
    	}
    	
    	TaskArrayAdapter adapter = new TaskArrayAdapter(getApplicationContext(), R.layout.task_template, myTasks);
    	ListView listView = (ListView) findViewById(R.id.id_my_tasks_list_view);
    	listView.setAdapter(adapter);  
    }

}
