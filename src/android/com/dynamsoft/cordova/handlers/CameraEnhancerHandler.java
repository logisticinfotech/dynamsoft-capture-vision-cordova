package com.dynamsoft.cordova.handlers;

import android.os.Handler;
import android.os.Looper;

import com.dynamsoft.core.basic_structures.DSRect;
import com.dynamsoft.dce.CameraEnhancer;
import com.dynamsoft.dce.CameraEnhancerException;
import com.dynamsoft.dce.EnumEnhancerFeatures;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CameraEnhancerHandler {
    public CordovaInterface cordova;
    public CordovaWebView webView;

    public CameraEnhancer mCamera;
    public CameraViewHandler mCameraViewHandler;
    Handler mUiHandler = new Handler(Looper.getMainLooper());

    public CameraEnhancerHandler(CordovaInterface cordova, CordovaWebView webView) {
        this.cordova = cordova;
        this.webView = webView;
        mCameraViewHandler = new CameraViewHandler(this);
    }

    public void createDceInstance(CallbackContext callbackContext) {
        try {
            mCamera = new CameraEnhancer(cordova.getActivity());
            if(mCameraViewHandler.mCameraView != null) {
                mCamera.setCameraView(mCameraViewHandler.mCameraView);
            }
            mCameraViewHandler.mCamera = this.mCamera;
            callbackContext.success();
        } catch (Exception e) {
            callbackContext.error(e.getMessage());
            e.printStackTrace();
        }
    }

    public void open(CallbackContext callbackContext) {
        mUiHandler.post(() -> {
            try {
                mCamera.open();
                callbackContext.success();
            } catch (CameraEnhancerException e) {
                callbackContext.error(e.getMessage());
                e.printStackTrace();
            }
        });
    }

    public void close(CallbackContext callbackContext) {
        mUiHandler.post(() -> {
            try {
                mCamera.close();
                callbackContext.success();
            } catch (CameraEnhancerException e) {
                callbackContext.error(e.getMessage());
                e.printStackTrace();
            }
        });
    }

    public void setTorchState(JSONArray args, CallbackContext callbackContext) throws JSONException {
        try {
            if (args.getInt(0) == 1) {
                mCamera.turnOnTorch();
            } else {
                mCamera.turnOffTorch();
            }
            callbackContext.success();
        } catch (CameraEnhancerException e) {
            callbackContext.error(e.getMessage());
        }
    }

    public void setScanRegion(JSONArray args, CallbackContext callbackContext) throws JSONException {
        try {
            if (args.get(0).equals(null)) {
                mCamera.setScanRegion(null);
            } else {
                JSONObject region = args.getJSONObject(0);
                DSRect regionDefinition = new DSRect();
                regionDefinition.left = region.isNull("regionLeft") ? 0 : region.getInt("regionLeft");
                regionDefinition.top = region.isNull("regionTop") ? 0 : region.getInt("regionTop");
                regionDefinition.right = region.isNull("regionRight") ? 100 : region.getInt("regionRight");
                regionDefinition.bottom = region.isNull("regionBottom") ? 100 : region.getInt("regionBottom");
                regionDefinition.measuredInPercentage = !region.isNull("regionMeasuredByPercentage") && 
                    region.getInt("regionMeasuredByPercentage") > 0;
                
                mCamera.setScanRegion(regionDefinition);
            }
            callbackContext.success();
        } catch (CameraEnhancerException e) {
            callbackContext.error(e.getMessage());
        }
    }

    public void setScanRegionVisible(JSONArray args) throws JSONException {
        boolean isVisible = args.getBoolean(0);
        mCameraViewHandler.mCameraView.setScanRegionMaskVisible(isVisible);
        mCameraViewHandler.mCameraView.setScanLaserVisible(isVisible);
    }
}
