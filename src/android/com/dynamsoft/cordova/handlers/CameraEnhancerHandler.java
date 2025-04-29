package com.dynamsoft.cordova.handlers;

import android.os.Handler;
import android.os.Looper;

import com.dynamsoft.core.basic_structures.DSRect;
import com.dynamsoft.dce.CameraEnhancer;
import com.dynamsoft.dce.CameraEnhancerException;
import com.dynamsoft.dce.utils.PermissionUtil;

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
        // init CameraEnhancer and DCECameraView
        mCamera = new CameraEnhancer(cordova.getActivity());
        if(mCameraViewHandler.mCameraView != null) {
            mUiHandler.post(() -> mCamera.setCameraView(mCameraViewHandler.mCameraView));
        }
        try {
            mCameraViewHandler.mCamera = this.mCamera;
        } catch (Exception e) {
            callbackContext.error(e.getMessage());
            e.printStackTrace();
        }
        callbackContext.success();
    }

    public void open(CallbackContext callbackContext) {
        PermissionUtil.requestCameraPermission(cordova.getActivity());
        mUiHandler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    mCamera.open();
                } catch (CameraEnhancerException e) {
                    callbackContext.error(e.getMessage());
                    e.printStackTrace();
                }
                callbackContext.success();
            }
        });
    }

    public void close(CallbackContext callbackContext) {
        mUiHandler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    mCamera.close();
                } catch (CameraEnhancerException e) {
                    callbackContext.error(e.getMessage());
                    e.printStackTrace();
                }
                callbackContext.success();
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
        } catch (CameraEnhancerException e) {
            callbackContext.error(e.getMessage());
        }
        callbackContext.success();
    }

    public void setScanRegion(JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (args.get(0).equals(null)) {
            try {
                mCamera.setScanRegion(null);
            } catch (CameraEnhancerException e) {
                callbackContext.error(e.getMessage());
            }
        } else {
            JSONObject region = args.getJSONObject(0);
            int regionLeft = 0, regionTop = 0, regionRight = 100, regionBottom = 100, regionMeasuredByPercentage = 1;
            if (!region.isNull("regionLeft")) {
                regionLeft = region.getInt("regionLeft");
            }
            if (!region.isNull("regionTop")) {
                regionTop = region.getInt("regionTop");
            }
            if (!region.isNull("regionRight")) {
                regionRight = region.getInt("regionRight");
            }
            if (!region.isNull("regionBottom")) {
                regionBottom = region.getInt("regionBottom");
            }
            if (!region.isNull("regionMeasuredByPercentage")) {
                regionMeasuredByPercentage = region.getInt("regionMeasuredByPercentage");
            }

            DSRect regionDefinition = new DSRect();
            if(regionMeasuredByPercentage > 0) {
                regionDefinition.left = regionLeft / 100f;
                regionDefinition.right = regionRight / 100f;
                regionDefinition.top = regionTop / 100f;
                regionDefinition.bottom = regionBottom / 100f;
                regionDefinition.measuredInPercentage = true;
            } else {
                regionDefinition.left = regionLeft;
                regionDefinition.right = regionRight;
                regionDefinition.top = regionTop;
                regionDefinition.bottom = regionBottom;
                regionDefinition.measuredInPercentage = false;
            }
            try {
                mCamera.setScanRegion(regionDefinition);
            } catch (CameraEnhancerException e) {
                callbackContext.error(e.getMessage());
            }
        }
        callbackContext.success();
    }

    public void setScanRegionVisible(JSONArray args) throws JSONException {
        boolean isVisible = args.getBoolean(0);
        // mCamera.setScanRegionVisible(isVisible);
        mCameraViewHandler.mCameraView.setScanRegionMaskVisible(isVisible);
        // mCameraViewHandler.mCameraView.setScanLaserVisible(isVisible);
        mCameraViewHandler.mCameraView.setScanLaserVisible(false);
    }
}
