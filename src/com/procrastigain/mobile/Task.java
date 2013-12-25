package com.procrastigain.mobile;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class Task {

	private Map<String, Object> mFields;
    /** This is our object ID */
    private String mDocId;
    
    /** Last state of current user ID so we can figure out if we need to refresh fields */
    private String mLastMyUserId;
	
    /**
     * Gets Meteor object ID
     * @return object ID string
     */
    public String getId() {
        return mDocId;
    }
    
    /**
     * Gets title for task
     * @return title of task
     */
    public String getTitle() {
        return ((String) mFields.get("title"));
    }
    
    /**
     * Gets duration for this task
     * @return duration of task
     */
    public int getDuration() {
        return ((Double) mFields.get("duration")).intValue();
    }
    
    public int getHours() {
    	return ((Double) mFields.get("duration")).intValue() / 3600;
    }
    
    public int getMinutes() {
    	return (((Double) mFields.get("duration")).intValue() % 3600) / 60;
    }
    
    /**
     * Gets appeal for task
     * @return appeal of task
     */
    public int getAppeal() {
        return Integer.parseInt((String) mFields.get("appeal")) - 1;
    }
    
    /**
     * Gets title for task
     * @return title of task
     */
    public Date getDueDate() {
    	
    	// parses json date of the form {$date=1.38494839E12}, there
    	// might be a better way to do this
    	
    	String dateString = mFields.get("dateDue").toString();
    	int length = dateString.length();
    	
    	String dateSubstring = dateString.substring(7, length - 1);
    	Date date = new Date((long) Double.parseDouble(dateSubstring));
    	
        return date;
    }
    
    /**
     * Constructor for Party object
     * @param docId Meteor's object ID for this Party
     * @param fields field/value map reference
     */
    public Task(String docId, Map<String, Object> fields) {
        this.mFields = fields;
        this.mDocId = docId;
    }
    
    /**
     * used to figure out if user ID has changed so we can refresh user ID dependent fields
     * @return true if changed
     */
    @SuppressWarnings("unused")
    private boolean hasUserIdChanged() {
        String myUserId = MyDDPState.getInstance().getUserId();
        if (((myUserId == null) && (mLastMyUserId != null))
                || ((myUserId != null) && (mLastMyUserId == null))
                || (!myUserId.equals(mLastMyUserId))) {
            return true;
        }
        return false;
    }
    
    /**
     * Override so we can print party info easily
     */
    @Override
    public String toString() {
        return getTitle();
    }
    
    
}
