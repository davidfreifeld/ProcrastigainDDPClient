package com.procrastigain.mobile;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.keysolutions.ddpclient.android.DDPBroadcastReceiver;
import com.keysolutions.ddpclient.android.DDPStateSingleton;

public class MainActivity extends Activity {
	
	/** handle to Login menu item */
    private MenuItem mMenuLogin;
    
    /** handle to Logout menu item */
    private MenuItem mMenuLogout;

    /** broadcast receiver */
    private BroadcastReceiver mReceiver;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
    }
    
    @Override
    public void onResume() {
    	super.onResume();
    	
    	updateLoginButtons();
    	
    	mReceiver = new DDPBroadcastReceiver(MyDDPState.getInstance(), this) {
            @Override
            protected void onDDPConnect(DDPStateSingleton ddp) {
                super.onDDPConnect(ddp);
                // add our subscriptions needed for the activity here
                //ddp.subscribe("parties", new Object[] {});
                //ddp.subscribe("directory", new Object[] {});
            }
            @Override
            protected void onSubscriptionUpdate(String changeType,
                    String subscriptionName, String docId) {
                if (subscriptionName.equals("parties")) {
                    // show any new parties
                    //showVisibleParties();
                }
            }
            @Override
            protected void onLogin() {
                // update login/logout action button
                invalidateOptionsMenu();
            }
            @Override
            protected void onLogout() {
                // update login/logout action button
                invalidateOptionsMenu();
            }
        };
        MyDDPState.getInstance().connectIfNeeded();    // start connection process if we're not connected
    }

    
    /** used by fragment to force update of login buttons */
    public void updateLoginButtons() {
        if (mMenuLogin != null) {
            MyDDPState ddp = MyDDPState.getInstance();
            mMenuLogin.setVisible(!ddp.isLoggedIn());
            mMenuLogout.setVisible(ddp.isLoggedIn());
        }
    }

    /**
     * Updates login action bar menu buttons
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
      MenuInflater inflater = getMenuInflater();
      inflater.inflate(R.menu.menu_main, menu);
      mMenuLogin = menu.findItem(R.id.action_login);
      mMenuLogout = menu.findItem(R.id.action_logout);
      updateLoginButtons();
      return true;
    }
    
    /**
     * Handles login/logout button presses
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
      MyDDPState ddp = MyDDPState.getInstance();
      switch (item.getItemId()) {
      case R.id.action_login:
          String resumeToken = ddp.getResumeToken();
          if (resumeToken != null) {
              // start DDP login process
              ddp.login(resumeToken);
          } else {
              // fire up login window
              Intent intent = new Intent(this, LoginActivity.class);
              this.startActivity(intent);
          }
        break;
      case R.id.action_logout:
        ddp.logout();
        break;
      default:
        break;
      }
      return true;
    }
    
}
