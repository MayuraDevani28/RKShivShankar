/**
 * Copyright 2016 Google Inc. All Rights Reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.shivshankar;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.shivshankar.ServerCall.APIs;
import com.shivshankar.utills.AppPreferences;
import com.shivshankar.utills.OnResult;
import com.shivshankar.utills.commonVariables;

import org.json.JSONObject;


public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService implements OnResult {

    private static final String TAG = "MyFirebaseIIDService";

    @Override
    public void onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
        AppPreferences.getPrefs().edit().putBoolean(commonVariables.PREFERENCE_FIRST_RUN, true);
        APIs.UpdatefcmDetails_Buyer(this);
    }

    @Override
    public void onResult(JSONObject jobjWhole) {
        if (jobjWhole != null) {
            try {
                String strApiName = jobjWhole.optString("api");
                if (strApiName.equalsIgnoreCase("UpdatefcmDetails_Buyer")) {
                    SharedPreferences.Editor editor = AppPreferences.getPrefs().edit();
                    editor.putBoolean(commonVariables.PREFERENCE_FIRST_RUN, false);
                    editor.apply();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
