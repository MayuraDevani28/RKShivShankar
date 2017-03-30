package com.shivshankar.utills;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

/**
 * Created by Mayura on 10/24/2016.
 */

public class SmsReceiver extends BroadcastReceiver {

    private static SmsListener mListener;
    public static final String SMS_BUNDLE = "pdus";

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            Bundle intentExtras = intent.getExtras();
            if (intentExtras != null) {
                Object[] sms = (Object[]) intentExtras.get(SMS_BUNDLE);
                String smsMessageStr = "";
                for (int i = 0; i < sms.length; ++i) {
                    SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) sms[i]);

                    String messageBody = smsMessage.getMessageBody().toString();
                    String address = smsMessage.getOriginatingAddress();

                    if (messageBody.contains("") && messageBody.contains("Your OTP")){
                        try {
                            synchronized (address) {
                                smsMessageStr = messageBody.split("Your OTP is ")[1];
                                smsMessageStr = smsMessageStr.substring(0, 4);
                                if (!smsMessageStr.isEmpty())
                                        mListener.messageReceived(smsMessageStr);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void bindListener(SmsListener listener) {
        mListener = listener;
    }
}