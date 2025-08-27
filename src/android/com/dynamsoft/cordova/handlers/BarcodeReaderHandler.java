package com.dynamsoft.cordova.handlers;

import android.app.AlertDialog;
import android.util.Base64;
import android.util.Log;

import com.dynamsoft.core.basic_structures.CompletionListener;
import com.dynamsoft.core.basic_structures.EnumErrorCode;
import com.dynamsoft.cvr.CaptureVisionRouter;
import com.dynamsoft.cvr.CaptureVisionRouterException;
import com.dynamsoft.cvr.CapturedResultReceiver;
import com.dynamsoft.cvr.EnumPresetTemplate;

import com.dynamsoft.dbr.BarcodeResultItem;
import com.dynamsoft.dbr.DecodedBarcodesResult;
import com.dynamsoft.cvr.SimplifiedCaptureVisionSettings;
import com.dynamsoft.dbr.EnumBarcodeFormat;
import com.dynamsoft.dce.CameraEnhancer;
import com.dynamsoft.utility.UtilityModule;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Locale;

public class BarcodeReaderHandler {
    public CaptureVisionRouter mReader;
    public  CapturedResultReceiver capturedResultReceiver;

    public CordovaInterface cordova;
    private AlertDialog mAlertDialog;

    public BarcodeReaderHandler(CordovaInterface cordova) {
        this.cordova = cordova;
    }
    public void createDbrInstance(CallbackContext callbackContext) {
      cordova.getThreadPool().execute(() -> {
        try {
          if (mReader == null) {
            mReader = new CaptureVisionRouter(this.cordova.getContext());
          }
          callbackContext.success("DBR instance created");
        } catch (Exception e) {
          callbackContext.error("Failed to create DBR instance: " + e.getMessage());
        }
      });
    }

    public void getVersion(CallbackContext callbackContext) {
        callbackContext.success(UtilityModule.getVersion());
    }

    public void startScanning() {
        cordova.getThreadPool().execute(() -> {
            mReader.startCapturing(EnumPresetTemplate.PT_READ_SINGLE_BARCODE, new CompletionListener() {
                @Override
                public void onSuccess() {
                }

                @Override
                public void onFailure(int errorCode, String errorString) {
                    if(errorCode == EnumErrorCode.EC_CALL_REJECTED_WHEN_CAPTURING) {
                        // If an error occurs because capturing is in progress, stop the current capturing first, and then start capturing again.
                        mReader.stopCapturing();
                        startScanning();
                    } else {
                        cordova.getActivity().runOnUiThread(() -> showDialog("Error", String.format(Locale.getDefault(), "ErrorCode: %d %nErrorMessage: %s", errorCode, errorString)));
                    }
                }
            });
        });
    }

    public void stopScanning() {
        cordova.getThreadPool().execute(() -> {
            mReader.removeResultReceiver(capturedResultReceiver);
            mReader.stopCapturing();
        });
    }

    public void getRuntimeSettings(CallbackContext callbackContext) {
        try {
            SimplifiedCaptureVisionSettings settings = mReader.getSimplifiedSettings(EnumPresetTemplate.PT_READ_SINGLE_BARCODE);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("barcodeFormatIds", settings.barcodeSettings.barcodeFormatIds);
            jsonObject.put("expectedBarcodesCount", settings.barcodeSettings.expectedBarcodesCount);
            jsonObject.put("timeout", settings.timeout);
            callbackContext.success(jsonObject);
        } catch (JSONException | CaptureVisionRouterException e) {
            callbackContext.error(e.getMessage());
            e.printStackTrace();
        }
    }

    public void outputSettingsToString(CallbackContext callbackContext) {
        try {
            String settings = mReader.outputSettings(EnumPresetTemplate.PT_READ_SINGLE_BARCODE, true);
            callbackContext.success(settings);
        } catch (CaptureVisionRouterException e) {
            throw new RuntimeException(e);
        }
    }

    public void updateRuntimeSettings(JSONArray args, CallbackContext callbackContext) throws JSONException {
      Object updateSettings = args.get(0);

      if (updateSettings instanceof JSONObject) {
        // Optionally load license or set settings
        SimplifiedCaptureVisionSettings settings = null;
        try {
          settings = mReader.getSimplifiedSettings(EnumPresetTemplate.PT_READ_SINGLE_BARCODE);

          if (!((JSONObject) updateSettings).isNull("barcodeFormatIds")) {
            settings.barcodeSettings.barcodeFormatIds = ((JSONObject) updateSettings).getInt("barcodeFormatIds");
          }

          if (!((JSONObject) updateSettings).isNull("expectedBarcodesCount")) {
            settings.barcodeSettings.expectedBarcodesCount = ((JSONObject) updateSettings).getInt("expectedBarcodesCount");
          }

          if (!((JSONObject) updateSettings).isNull("timeout")) {
            settings.timeout = ((JSONObject) updateSettings).getInt("timeout");
          }

          mReader.updateSettings(EnumPresetTemplate.PT_READ_SINGLE_BARCODE, settings);
        } catch (CaptureVisionRouterException e) {
          if (e.getErrorCode() == EnumErrorCode.EC_CALL_REJECTED_WHEN_CAPTURING) {
            // If an error occurs because capturing is in progress, stop the current capturing first, then call this function again, and then start capturing again if you want.
            mReader.stopCapturing();
            try {
                mReader.updateSettings(EnumPresetTemplate.PT_READ_SINGLE_BARCODE, settings);
            } catch (CaptureVisionRouterException ex) {
                e.printStackTrace();
            }
            startScanning();
          }
        }
      }

      callbackContext.success();
    }

    public void resetRuntimeSettings() {
        try {
            mReader.resetSettings();
        } catch (CaptureVisionRouterException e) {
            e.printStackTrace();
        }
    }

    public void setTextResultListener(CallbackContext callbackContext) {
        mReader.addResultReceiver(capturedResultReceiver = new CapturedResultReceiver() {
            @Override
            // Implement the callback method to receive DecodedBarcodesResult.
            // The method returns a DecodedBarcodesResult object that contains an array of BarcodeResultItems.
            // BarcodeResultItems is the basic unit from which you can get the basic info of the barcode like the barcode text and barcode format.
            public void onDecodedBarcodesReceived(DecodedBarcodesResult result) {
                if (result != null && result.getItems() != null && result.getItems().length > 0) {
                    try {
                        PluginResult pluginResult = null;
                        pluginResult = new PluginResult(PluginResult.Status.OK, handleResults(result.getItems()));
                        pluginResult.setKeepCallback(true);
                        callbackContext.sendPluginResult(pluginResult);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }

//    public void setMinImageReadingInterval(JSONArray args) throws JSONException {
//        mReader.setMinImageReadingInterval(args.getInt(0));
//    }
//
//    public void getMinImageReadingInterval(CallbackContext callbackContext) throws JSONException {
//        callbackContext.success(mReader.getMinImageReadingInterval());
//    }

    private JSONArray handleResults(BarcodeResultItem[] barcodes) throws JSONException {

        JSONArray barcodesList = new JSONArray();

        for (int i = 0; i < barcodes.length; i++) {
            BarcodeResultItem barcode = barcodes[i];
            JSONObject jsonBarcode = new JSONObject();

//            if (barcode.barcodeFormat_2 != EnumBarcodeFormat_2.BF2_NULL) {
//                jsonBarcode.put("barcodeFormatString", barcode.barcodeFormatString_2);
//            } else {
//                jsonBarcode.put("barcodeFormatString", barcode.barcodeFormatString);
//            }
            jsonBarcode.put("barcodeFormatString", barcode.getFormatString());
            jsonBarcode.put("barcodeText", barcode.getText());
            String bytesBase64 = Base64.encodeToString(barcode.getBytes(), Base64.DEFAULT);
            jsonBarcode.put("barcodeBytes", bytesBase64);
//            jsonBarcode.put("barcodeLocation", handleLocationResult(barcode.localizationResult));
            barcodesList.put(jsonBarcode);
        }
        return barcodesList;
    }

//    private JSONObject handleLocationResult(LocalizationResult result) throws JSONException {
//        if (result == null) {
//            return null;
//        }
//        JSONObject location = new JSONObject();
//        location.put("angle", result.angle);
//        location.put("quadrilateral", handleQuadrilateral(result.resultPoints));
//        return location;
//    }

//    private JSONObject handleQuadrilateral(Point[] points) throws JSONException {
//        if (points == null) {
//            return null;
//        }
//        JSONObject quadrilateral = new JSONObject();
//        quadrilateral.put("points", handlePoints(points));
//        return quadrilateral;
//    }

//    private JSONArray handlePoints(Point[] points) throws JSONException {
//        if (points == null) {
//            return null;
//        }
//        JSONArray pointArray = new JSONArray();
//        for (int i = 0; i < 4; i++) {
//            pointArray.put(handleSinglePoint(points[i]));
//        }
//        return pointArray;
//    }

//    private JSONObject handleSinglePoint(Point point) throws JSONException {
//        JSONObject pointMap = new JSONObject();
//        pointMap.put("x", point.x);
//        pointMap.put("y", point.y);
//        return pointMap;
//    }

    public void setCameraEnhancer(CameraEnhancer cameraEnhancer) {
        if(cameraEnhancer != null) {
//            mReader.setCameraEnhancer(cameraEnhancer);
            try {
                mReader.setInput(cameraEnhancer);
            } catch (CaptureVisionRouterException e) {
                if(e.getErrorCode() == EnumErrorCode.EC_CALL_REJECTED_WHEN_CAPTURING) {
                    // If an error occurs because capturing is in progress, stop the current capturing first, then call this function again, and then start capturing again if you want.
                    mReader.stopCapturing();
                    try {
                        mReader.setInput(cameraEnhancer);
                    } catch (CaptureVisionRouterException ex) {
                        e.printStackTrace();
                    }
                    startScanning();
                }
            }
        }
    }

    private void showDialog(String title, String message) {
        if (mAlertDialog == null) {
            // Restart the capture when the dialog is closed
            mAlertDialog = new AlertDialog.Builder(this.cordova.getContext()).setCancelable(true).setPositiveButton("OK", null)
                    .setOnDismissListener(dialog -> mReader.startCapturing(EnumPresetTemplate.PT_READ_SINGLE_BARCODE, null))
                    .create();
        }
        mAlertDialog.setTitle(title);
        mAlertDialog.setMessage(message);
        mAlertDialog.show();
    }
}
