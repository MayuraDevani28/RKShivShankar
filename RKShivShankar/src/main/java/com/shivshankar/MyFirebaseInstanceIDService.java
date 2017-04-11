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
import android.net.Uri;
import android.os.Build;
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

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // TODO: Implement this method to send any registration to your app's servers.
        AppPreferences.getPrefs().edit().putBoolean(commonVariables.PREFERENCE_FIRST_RUN, true);
        callGetfcmDetailsAPI(refreshedToken);
    }

    private void callGetfcmDetailsAPI(String regid) {
        try {
            String strModelName = Build.MODEL;
            String strOSVersion = Build.VERSION.RELEASE;
            String strDeviceUUID = commonVariables.uuid;
            String strDeviceType = "Android" + strDeviceUUID;
            Uri uri = new Uri.Builder().scheme("http")
                    .authority(commonVariables.STRING_SERVER_URL_FOR_GET_METHOD)
                    .path("Mobile/GetfcmDetails")
                    .appendQueryParameter("strDeviceType", strDeviceType)
                    .appendQueryParameter("strDeviceUUID", strDeviceUUID)
                    .appendQueryParameter("strModelName", strModelName)
                    .appendQueryParameter("strOSVersion", strOSVersion)
                    .appendQueryParameter("GCMRegistraionId", regid)
                    .build();
            String query = uri.toString();
            Log.v("TAG", "Calling With:" + query);
//            new ServerAPICAll(null, this).execute(query);
            APIs.callAPI(null, this, query);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // [END refresh_token]

    /**
     * Persist token to third-party servers.
     * <p/>
     * Modify this method to associate the user's FCM InstanceID token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
    private void sendRegistrationToServer(String token) {
        // Add custom implementation, as needed.
    }

    @Override
    public void onResult(JSONObject jobjWhole) {
        if (jobjWhole != null) {
            try {
                String strApiName = jobjWhole.optString("api");
                if (strApiName.equalsIgnoreCase("GetfcmDetails")) {
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
