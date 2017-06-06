package com.example.isjahan.sinchcalltest.model;

import io.realm.RealmObject;
import io.realm.annotations.Required;

/**
 * Created by shadman.rahman on 04-Jun-17.
 */

public class CallDetails extends RealmObject{
    @Required
    private String CallingBy;
    @Required
    private String CallingTo;
    @Required
    private String CallDuration;
    @Required
    private String CallInTime;
    @Required
    private String CallType;

    public CallDetails() {
    }

    public CallDetails(String callingBy, String callingTo, String callDuration, String callInTime, String callType) {
        CallingBy = callingBy;
        CallingTo = callingTo;
        CallDuration = callDuration;
        CallInTime = callInTime;
        CallType = callType;
    }

    public String getCallingBy() {
        return CallingBy;
    }

    public void setCallingBy(String callingBy) {
        CallingBy = callingBy;
    }

    public String getCallingTo() {
        return CallingTo;
    }

    public void setCallingTo(String callingTo) {
        CallingTo = callingTo;
    }

    public String getCallDuration() {
        return CallDuration;
    }

    public void setCallDuration(String callDuration) {
        CallDuration = callDuration;
    }

    public String getCallInTime() {
        return CallInTime;
    }

    public void setCallInTime(String callInTime) {
        CallInTime = callInTime;
    }

    public String getCallType() {
        return CallType;
    }

    public void setCallType(String callType) {
        CallType = callType;
    }

}
