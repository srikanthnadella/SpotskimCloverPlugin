package com.sripra.termtegrity.cloverplugin;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.PluginResult;
import org.json.JSONObject;
import org.json.JSONArray;
import org.json.JSONException;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import com.clover.sdk.util.CloverAccount;
import com.clover.sdk.v1.ResultStatus;
import com.clover.sdk.v1.ServiceConnector;
import com.clover.sdk.v1.merchant.Merchant;
import com.clover.sdk.v1.merchant.MerchantAddress;
import com.clover.sdk.v1.merchant.MerchantConnector;
import com.clover.sdk.v1.merchant.MerchantIntent;
import java.text.DateFormat;
import java.util.Date;

public class CloverPlugin extends CordovaPlugin implements MerchantConnector.OnMerchantChangedListener, ServiceConnector.OnServiceConnectedListener{
    public static final String ACTION_GET_MERCHANT = "getMerchant";
    private static final String TAG = "CloverPlugin";
    private CallbackContext notificationCallbackContext = null;
    BroadcastReceiver receiver;
    
    @Override
    public void onMerchantChanged(Merchant merchant) {
        updateMerchant("merchant changed", null, merchant);
    }
    
    @Override
    public void onServiceConnected(ServiceConnector connector) {
        Log.i(TAG, "service connected: " + connector);
    }
    
    @Override
    public void onServiceDisconnected(ServiceConnector connector) {
        Log.i(TAG, "service disconnected: " + connector);
    }
    
 
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        try {
            if (ACTION_GET_MERCHANT.equals(action)) {
                startAccountChooser();
                connect();
                getMerchant();
				JSONObject obj = new JSONObject();
        try {
            obj.put("MerchantID", merchantID);
            obj.put("DeviceID", deviceID);
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage(), e);
        }
        
				PluginResult result = new PluginResult(PluginResult.Status.OK, obj);
            result.setKeepCallback(true);
            callbackContext.sendPluginResult(result);

                return true;
            }
            callbackContext.error("Invalid action");
            return false;
        } catch(Exception e) {
            System.err.println("Exception: " + e.getMessage());
            callbackContext.error("Exception: " + e.getMessage());
            return false;
        }
    }
    
    private static final int REQUEST_ACCOUNT = 0;
    private MerchantConnector merchantConnector;
    private Account account;
    private String merchantID;
    private String deviceID;
    
    private void startAccountChooser() {
        AccountManager accountManager = AccountManager.get(this.cordova.getActivity().getApplicationContext());
        Account[] accounts = accountManager.getAccountsByType(CloverAccount.CLOVER_ACCOUNT_TYPE);
        for (Account acc : accounts){
            account = acc;
            break;
        }
        
    }
    
    
    private void connect() {
        disconnect();
        if (account != null) {
            merchantConnector = new MerchantConnector(cordova.getActivity(), account, this);
            merchantConnector.setOnMerchantChangedListener(this);
            merchantConnector.connect();
        }
    }
    
    private void disconnect() {
        if (merchantConnector != null) {
            merchantConnector.disconnect();
            merchantConnector = null;
        }
    }
    
    
    private void getMerchant() {
        Merchant merchant = null;
        try {
            merchant = merchantConnector.getMerchant();
        }
        catch (Exception e){
            
        }
        merchantID = merchant.getId();
        deviceID = merchant.getDeviceId();
        
    }
    
    private void updateMerchant(String status, ResultStatus resultStatus, Merchant result) {
        merchantID = result.getId();
        deviceID = result.getDeviceId();
    }
}