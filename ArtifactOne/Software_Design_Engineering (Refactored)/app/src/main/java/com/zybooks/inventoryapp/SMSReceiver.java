package com.zybooks.inventoryapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;

/** @noinspection ALL*/
// this allow brodcaster listening for sms message
public class SMSReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Object[] pdus = (Object[]) bundle.get("pdus");
            if (pdus != null) {
                for (Object pdu : pdus) {
                    SmsMessage message = SmsMessage.createFromPdu((byte[]) pdu);
                    String sender = message.getOriginatingAddress();
                    String body = message.getMessageBody();
                    Toast.makeText(context, "SMS from " + sender + ": " + body, Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}
