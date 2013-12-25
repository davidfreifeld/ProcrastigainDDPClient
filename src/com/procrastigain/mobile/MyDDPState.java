/*
* (c)Copyright 2013 Ken Yee, KEY Enterprise Solutions 
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
* http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package com.procrastigain.mobile;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import android.content.Context;

import com.keysolutions.ddpclient.DDPClient.DdpMessageType;
import com.keysolutions.ddpclient.android.DDPStateSingleton;

/**
 * Implements specific DDP state/commands for this application
 * @author kenyee
 *
 * This is a singleton class that should be initialized in your MyApplication singleton.
 */
public class MyDDPState extends DDPStateSingleton {
    /** collection of Parties */
    private Map<String, Task> mTasks;
    
    /**
     * Constructor for this singleton (private because it's a singleton)
     * @param context Android application context
     */
    private MyDDPState(Context context) {
        // Constructor hidden because this is a singleton
        super(context);
        mTasks = new ConcurrentHashMap<String, Task>();
    }

    /**
     * Used by MyApplication singleton to initialize this singleton
     * @param context Android application context
     */
    public static void initInstance(Context context) {
        // only called by MyApplication
        if (mInstance == null) {
            // Create the instance
            mInstance = new MyDDPState(context);
        }
    }
    
    /**
     * Gets only instance of this singleton
     * @return this singleton object
     */
    public static MyDDPState getInstance() {
        // Return the instance
        return (MyDDPState) mInstance;
    }
    
    /**
     * Gets current collection of Parties
     * @return parties collection
     */
    public Map<String, Task> getTasks() {
        return mTasks;
    }
    
    /**
     * Gets specified Party object
     * @param partyId Meteor ID of party
     * @return Party object if found
     */
    public Task getTask(String taskId) {
        if (!mTasks.containsKey(taskId)) {
            return null;
        }
        return mTasks.get(taskId);
    }
    
    /**
     * Lets us lightly wrapper default implementation's objects
     * instead of using our own DB if we had to override 
     * the add/remove/updateDoc methods
     */
    @Override
    public void broadcastSubscriptionChanged(String collectionName,
            String changetype, String docId) {
        if (collectionName.equals("tasks")) {
            if (changetype.equals(DdpMessageType.ADDED)) {
                mTasks.put(docId, new Task(docId, (Map<String, Object>) getCollection(collectionName).get(docId)));
            } else if (changetype.equals(DdpMessageType.REMOVED)) {
                mTasks.remove(docId);
            } else if (changetype.equals(DdpMessageType.UPDATED)) {
            	
            	// TO DO: in here, need to do real time updating???
            	
                //mTasks.get(docId).refreshFields();
            }
        }
        // do the broadcast after we've taken care of our parties wrapper
        super.broadcastSubscriptionChanged(collectionName, changetype, docId);
    }
    
    ////// Meteor methods on the server for this application
    public void updateTask(String taskId, String title, int duration,
            int appeal, Date dueDate) {
    	
    	Map<String, Object> updateParams = new ConcurrentHashMap<String, Object>();
    	updateParams.put("title", title);
    	updateParams.put("duration", duration);
    	
    	getDDP().collectionUpdate("tasks", taskId, updateParams);
    	
    }
}
