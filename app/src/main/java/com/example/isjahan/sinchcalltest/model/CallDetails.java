package com.example.isjahan.sinchcalltest.model;


/**
 * Created by shadman.rahman on 04-Jun-17.
 */

public class CallDetails{


    private String CallingTo;

    private long CallInTime;

    private String CallType;

    public CallDetails(String callingTo, long callInTime, String callType) {
        CallingTo = callingTo;
        CallInTime = callInTime;
        CallType = callType;
    }

    public CallDetails() {
    }

    public String getCallingTo() {
        return CallingTo;
    }

    public void setCallingTo(String callingTo) {
        CallingTo = callingTo;
    }

    public long getCallInTime() {
        return CallInTime;
    }

    public void setCallInTime(long callInTime) {
        CallInTime = callInTime;
    }

    public String getCallType() {
        return CallType;
    }

    public void setCallType(String callType) {
        CallType = callType;
    }
}
