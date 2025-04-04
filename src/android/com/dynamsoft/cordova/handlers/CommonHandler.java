package com.dynamsoft.cordova.handlers;


import com.dynamsoft.license.LicenseManager;
import com.dynamsoft.license.LicenseVerificationListener;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.json.JSONArray;
import org.json.JSONException;

public class CommonHandler {
    public  static void initLicense(JSONArray args, CallbackContext callbackContext, CordovaInterface cordova) throws JSONException {
        String license = args.getString(0);
        cordova.getThreadPool().execute(() -> {
            if (license != null) {
              LicenseManager.initLicense(license, cordova.getContext(), new LicenseVerificationListener() {
                @Override
                public void onLicenseVerified(boolean isSuccess, Exception error) {
                  if(!isSuccess){
                    callbackContext.error(error.getMessage());
                  } else {
                    callbackContext.success();
                  }
                }
              });
            } else {
                callbackContext.error("Expected one non-null string argument.");
            }
        });
    }
}
