package com.phylloconnect

import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import java.util.Map
import java.util.HashMap
import android.util.Log
import com.getphyllo.*
import android.os.Looper
import android.os.Handler
import java.lang.Runnable
import com.facebook.react.bridge.UiThreadUtil
import android.view.WindowManager
import android.view.Window
import android.view.View
import android.content.Intent
import com.facebook.react.bridge.WritableMap
import com.facebook.react.bridge.Arguments
import com.facebook.react.modules.core.DeviceEventManagerModule

class PhylloConnectModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {

    override fun getName() = "PhylloConnectModule"

    val logTag: String = "PhylloConnectModule"

    //TODO remove
    override fun getConstants(): MutableMap<String, Any> {
        return hashMapOf("count" to 1)
    }

    @ReactMethod
    public fun initialize(name: String, token: String, userId: String, environment: String, platformId: String) {       
        Handler(Looper.getMainLooper()).post {
            PhylloConnect.initialize(context = reactApplicationContext,
            name = name,
            userId = userId,
            token = token,
            platformId = platformId,
            environment = getPhylloEnvironment(environment), // TODO remove
            callback = object : ConnectCallback {
                override fun onAccountConnected(accountId: String?,platformId: String?, userId: String?) {
                
                    val params = Arguments.createMap();
                    params.putString("account_id", accountId);
                    params.putString("user_id", userId);
                    params.putString("work_platform_id", platformId);
                    sendEvent("onAccountConnected", params);
                }

                override fun onAccountDisconnected(accountId: String?,platformId: String?, userId: String?) {
                    val params = Arguments.createMap();
                    params.putString("account_id", accountId);
                    params.putString("user_id", userId);
                    params.putString("work_platform_id", platformId);
                    sendEvent("onAccountDisconnected", params);
                }

                override fun onError(errorMsg: String?) {
                    //Log.d(logTag, "on Error  errorMsg")
                }

                override fun onTokenExpired(userId: String?) {
                    val params = Arguments.createMap();
                    params.putString("user_id", userId);
                    sendEvent("onTokenExpired", params);
                }

                override fun onEvent(event: PhylloConnect.EVENT) {
                    //Log.d(logTag, "onEvent  $event")
                }

                override fun onExit(reason:String?,userId: String?) {
                    val params = Arguments.createMap();
                    params.putString("user_id", userId);
                    params.putString("reason", reason);
                    sendEvent("onExit", params);
                }
            })
        }
    }

    private fun getPhylloEnvironment(env: String): PhylloConnect.ENVIRONMENT {
        return when (env) {
            "development" -> {
                return PhylloConnect.ENVIRONMENT.DEVELOPMENT
            }
            "sandbox" -> {
                return PhylloConnect.ENVIRONMENT.SANDBOX
            }
            "production" -> {
                return PhylloConnect.ENVIRONMENT.PRODUCTION
            }
            else -> PhylloConnect.ENVIRONMENT.DEVELOPMENT
        }
    }

    @ReactMethod
    public fun getPhylloEnvironmentUrl(env:String, callBack:Callback) {
        val envUrl = getPhylloEnvironment(env).baseUrl
        callBack.invoke(envUrl)
    }

    @ReactMethod
    public fun open() {
        Log.d(logTag,"Open Phyllo Connect Sdk")
        PhylloConnect.open()
    } 
  
    private fun sendEvent(
                    eventName:String,
                    params:WritableMap?) {
        //reactApplicationContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(eventName, params);
        reactApplicationContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java).emit(eventName, params)
    }
  
     // Required for rn built in EventEmitter Calls.
    @ReactMethod
    public fun addListener(eventName:String) {
        Log.d("eventName",eventName);
     }

    @ReactMethod
    public fun removeListeners(count:Integer) {
    //Log.d("removeListeners",count);
    }

}