package com.mamboflix.ui.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;


import androidx.annotation.RequiresApi;

import com.mamboflix.prefs.UserPref;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import in.aabhasjindal.otptextview.OtpTextView;

public class OTPReceiver extends BroadcastReceiver {
    private static OtpTextView otpTextView;
    String code = "";
    private UserPref userPref;


    public void setEditText(OtpTextView editText) {
        OTPReceiver.otpTextView = editText;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onReceive(Context context, Intent intent) {
        userPref = new UserPref(context);
        SmsMessage[] smsMessages = Telephony.Sms.Intents.getMessagesFromIntent(intent);
        for (SmsMessage message : smsMessages) {
            String getMessage = message.getMessageBody();
            Pattern pattern = Pattern.compile("(\\d{4})");


//   \d is for a digit
//   {} is the number of digits here 4.

            Matcher matcher = pattern.matcher(getMessage);
            String val = "";
            if (matcher.find()) {
                val = matcher.group(0);  // 4 digit number
                Log.e("val", val);
                if (!userPref.isLogin()) {
                    if (val != null) {
                        otpTextView.setOTP(val);
                    } else {
                        otpTextView.setOTP("");
                    }
                }
            }
        }
    }
}
