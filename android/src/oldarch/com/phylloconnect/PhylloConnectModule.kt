package com.phylloconnect

import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
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
import com.facebook.react.bridge.WritableArray
import com.facebook.react.bridge.Arguments
import com.facebook.react.bridge.ReadableMap
import com.facebook.react.modules.core.DeviceEventManagerModule
import com.facebook.react.bridge.Callback
import com.facebook.react.bridge.Promise


class PhylloConnectModule(reactContext: ReactApplicationContext) : ReactContextBaseJavaModule(reactContext) {

    override fun getName() = "PhylloConnectModule"

    val logTag: String = "PhylloConnectModule"

    @ReactMethod
    public fun initialize(readableMap: ReadableMap) {
        Handler(Looper.getMainLooper()).post {

            var callback = object : ConnectCallback (){
                override fun onAccountConnected(account_id: String?,work_platform_id: String?, user_id: String?) {
                    val values = Arguments.createArray();
                    values.pushString(account_id);
                    values.pushString(work_platform_id);
                    values.pushString(user_id);
                    sendEvent("onAccountConnected", values);
                }

                override fun onAccountDisconnected(account_id: String?,work_platform_id: String?, user_id: String?) {
                    val values = Arguments.createArray();
                    values.pushString(account_id);
                    values.pushString(work_platform_id);
                    values.pushString(user_id);
                    sendEvent("onAccountDisconnected", values);
                }

                override fun onTokenExpired(user_id: String?) {
                    val values = Arguments.createArray();
                    values.pushString(user_id);
                    sendEvent("onTokenExpired", values);
                }

                override fun onExit(reason:String?, user_id: String?) {
                    val values = Arguments.createArray();
                    values.pushString(reason);
                    values.pushString(user_id);
                    sendEvent("onExit", values);
                }

                override fun onConnectionFailure(reason: String?,work_platform_id: String?,user_id: String?) {
                    val values = Arguments.createArray();
                    values.pushString(reason);
                    values.pushString(work_platform_id);
                    values.pushString(user_id);
                    sendEvent("onConnectionFailure", values);
                }
            }

            var map = hashMapOf<String, Any?>()
            map = readableMap.toHashMap()
            map["environment"] = getPhylloEnvironment(map["environment"] as String)
            map["callback"] = callback
            map["external_sdk_name"] = "reactnative" //for Analytics
            map["external_sdk_version"] = "0.3.7"  // for sdk version
            PhylloConnect.initialize(context = reactApplicationContext, map)
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
            "staging" -> {
                return PhylloConnect.ENVIRONMENT.STAGING
            }
            "production" -> {
                return PhylloConnect.ENVIRONMENT.PRODUCTION
            }
            else -> PhylloConnect.ENVIRONMENT.DEVELOPMENT
        }
    }

    // @ReactMethod
    // public fun getPhylloEnvironmentUrl(env: String, callback: Callback) {
    //   val baseUrl = getPhylloEnvironment(env).baseUrl
    //   callback.invoke(null, baseUrl)
    // }

    @ReactMethod
    fun getPhylloEnvironmentUrl(environment: String, promise: Promise) {
       try {
            val baseUrl = getPhylloEnvironment(environment).baseUrl
            promise.resolve(baseUrl)
        } catch (e: Throwable) {
            promise.reject("Create Event Error", e)
        }
    }

    
    @ReactMethod
    public fun open() {
        Handler(Looper.getMainLooper()).post {
            PhylloConnect.open()
        }
    } 
  
    private fun sendEvent(
                    eventName:String,
                    values:WritableArray?) {
        
        reactApplicationContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java).emit(eventName, values)
    }

      // Required for rn built in EventEmitter Calls. Otherwise it'll show warnings
      @ReactMethod
      public fun addListener(eventName:String) {
        //   Log.d("eventName",eventName);
       }
  
      @ReactMethod
      public fun removeListeners(count:Integer) {
      //Log.d("removeListeners",count);
      }

}