package com.dynamsoft.cordova.handlers;

import android.app.AlertDialog;
import android.util.Base64;

import com.dynamsoft.core.basic_structures.CompletionListener;
import com.dynamsoft.cvr.CaptureVisionRouter;
import com.dynamsoft.cvr.CaptureVisionRouterException;
import com.dynamsoft.cvr.CapturedResultReceiver;
import com.dynamsoft.cvr.EnumPresetTemplate;

import com.dynamsoft.dbr.BarcodeResultItem;
import com.dynamsoft.dbr.DecodedBarcodesResult;
import com.dynamsoft.cvr.SimplifiedCaptureVisionSettings;
import com.dynamsoft.dce.CameraEnhancer;
import com.dynamsoft.utility.UtilityModule;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;

public class BarcodeReaderHandler {
    public CaptureVisionRouter mReader;

    public CordovaInterface cordova;
    private AlertDialog mAlertDialog;

    public BarcodeReaderHandler(CordovaInterface cordova) {
      this.cordova = cordova;
    }
    public void createDbrInstance(CallbackContext callbackContext) {
        if (mReader == null) {
//            try {
                mReader = new CaptureVisionRouter(this.cordova.getContext());
//            } catch (CaptureVisionRouterException e) {
//                callbackContext.error(e.getMessage());
//                e.printStackTrace();
//            }
        }
        callbackContext.success();
    }

    public void getVersion(CallbackContext callbackContext) {
        callbackContext.success(UtilityModule.getVersion());
    }

    public void startScanning() {

      mReader.startCapturing(EnumPresetTemplate.PT_READ_SINGLE_BARCODE, new CompletionListener() {
        @Override
        public void onSuccess() {

        }

        @Override
        public void onFailure(int errorCode, String errorString) {
           cordova.getActivity().runOnUiThread(() -> showDialog("Error", String.format(Locale.getDefault(), "ErrorCode: %d %nErrorMessage: %s", errorCode, errorString)));
        }
      });
    }

    public void stopScanning() {
        mReader.stopCapturing();
    }

    public void getRuntimeSettings(CallbackContext callbackContext) {
        try {
            SimplifiedCaptureVisionSettings settings = mReader.getSimplifiedSettings(EnumPresetTemplate.PT_READ_SINGLE_BARCODE);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("barcodeFormatIds", settings.barcodeSettings.barcodeFormatIds);
//            jsonObject.put("barcodeFormatIds_2", settings.barcodeSettings.barcodeFormatIds_2);
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
            String settings = mReader.outputSettings(EnumPresetTemplate.PT_READ_SINGLE_BARCODE);
            callbackContext.success(settings);
        } catch (CaptureVisionRouterException e) {
          throw new RuntimeException(e);
        }
    }

    public void updateRuntimeSettings(JSONArray args, CallbackContext callbackContext) throws JSONException {
        Object settings = args.get(0);
        try {
            if (settings instanceof String) {
//                mReader.initSettings((String) settings, EnumConflictMode.CM_OVERWRITE);
//                mReader.initSettings((String) settings);
            } else if (settings instanceof Integer) {
//                mReader.updateRuntimeSettings(EnumPresetTemplate.fromValue((int) settings));
//                mReader.updateSettings();
            } else if (settings instanceof JSONObject) {
                SimplifiedCaptureVisionSettings s = mReader.getSimplifiedSettings(EnumPresetTemplate.PT_READ_SINGLE_BARCODE);
                if (!((JSONObject) settings).isNull("barcodeFormatIds")) {
                    s.barcodeSettings.barcodeFormatIds = ((JSONObject) settings).getInt("barcodeFormatIds");
                }
//                if (!((JSONObject) settings).isNull("barcodeFormatIds_2")) {
//                    s.barcodeSettings.barcodeFormatIds_2 = ((JSONObject) settings).getInt("barcodeFormatIds_2");
//                }
                if (!((JSONObject) settings).isNull("expectedBarcodesCount")) {
                    s.barcodeSettings.expectedBarcodesCount = ((JSONObject) settings).getInt("expectedBarcodesCount");
                }
                if (!((JSONObject) settings).isNull("timeout")) {
                    s.timeout = ((JSONObject) settings).getInt("timeout");
                }
                mReader.updateSettings(EnumPresetTemplate.PT_READ_SINGLE_BARCODE, s);
            }
        } catch (CaptureVisionRouterException e) {
          callbackContext.error(e.getMessage());
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
      mReader.addResultReceiver(new CapturedResultReceiver() {
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
            throw new RuntimeException(e);
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
