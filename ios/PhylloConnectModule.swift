//
//  PhyConnectModule.swift
//  PhyConnectModule
//
//  Copyright © 2022 Phyllo. All rights reserved.
//


import Foundation
import PhylloConnect


@objc(PhylloConnectModule)
public class PhylloConnectModule: RCTEventEmitter {
    
    var hasObservers:Bool?
    
    override public func supportedEvents() -> [String]! {
        return ["onAccountConnected","onAccountDisconnected","onTokenExpired","onExit","onConnectionFailure"]
    }
    
    public override init() {
        super.init()
    }
    
    public override func startObserving() {
        self.hasObservers = true
        super.startObserving()
    }
    
    public override func stopObserving() {
        self.hasObservers = false
        super.stopObserving()
    }
    
    
    @objc(initialize:)
    func initialize(config:[String:Any]) {
        DispatchQueue.main.async {

         var phylloConfig = [String:Any]()
         phylloConfig = config
         phylloConfig["environment"] = self.getEnvironment(env: config["environment"] as? String ?? "")
         phylloConfig["delegate"] = self
         phylloConfig["external_sdk_name"] = "reactnative" //for Analytics
         phylloConfig["external_sdk_version"] = "0.3.7"  // for sdk version
         PhylloConnect.shared.initialize(config: phylloConfig)

        }
    }

   @objc(open)
    func open() {
        DispatchQueue.main.async {
            PhylloConnect.shared.open()
        }
    }
    
@objc func getPhylloEnvironmentUrl(_ environment:String ,resolve: @escaping RCTPromiseResolveBlock, reject: @escaping RCTPromiseRejectBlock) -> Void {
     let baseUrl = getEnvironment(env:environment).rawValue
     resolve(baseUrl)
   }

func getEnvironment(env:String) -> PhylloEnvironment {
    switch env {
      case "development":
        return PhylloEnvironment.dev
      case "sandbox":
        return PhylloEnvironment.sandbox
      case "staging":
        return PhylloEnvironment.staging
      case "production":
        return PhylloEnvironment.prod
      default:
        return PhylloEnvironment.sandbox
      }
}

@objc public override static func requiresMainQueueSetup() -> Bool {
      return true
}
    
// @objc public func resolvePromise(
//     _ resolve: RCTPromiseResolveBlock,
//     rejecter reject: RCTPromiseRejectBlock
//   ) -> Void {
//     resolve(hasBaseUrl)
//   }
    
//  @objc override public func constantsToExport() -> [AnyHashable : Any]! {
//    return ["baseUrl": hasBaseUrl]
//  }

//   @objc
//   func getBaseUrl(_ callback: RCTResponseSenderBlock) {
//     callback([hasBaseUrl])
//   }
//  func _getphylloBaseUrl(name: String) -> Void {
//    hasBaseUrl = getEnvironment(env:name).rawValue
//  }
//  @objc(getphylloBaseUrl:)
//  func getphylloBaseUrl(_ name: String) -> Void {
//     DispatchQueue.main.async {
//       self._getphylloBaseUrl(name: name)
//     }
//   }

}

extension PhylloConnectModule : PhylloConnectDelegate {
  
  public func onAccountConnected(account_id: String, work_platform_id: String, user_id: String) {
    //Event Sent After Get Connect
    DispatchQueue.main.async {
      var values : [String] = [account_id, work_platform_id, user_id]
      if self.hasObservers ?? false {
            self.sendEvent(withName: "onAccountConnected", body: values)
      }
    }
    
  }
  
  public func onAccountDisconnected(account_id: String, work_platform_id: String, user_id: String) {
    //Event Sent After Get Connect
    DispatchQueue.main.async {
      var values : [String] = [account_id, work_platform_id, user_id]
      if self.hasObservers ?? false {
            self.sendEvent(withName: "onAccountDisconnected", body: values)
      }
    }
  }
  
  public func onTokenExpired(user_id: String) {
    //Event Sent After Get Connect
    DispatchQueue.main.async {
      var values : [String] = [user_id]
      if self.hasObservers ?? false {
            self.sendEvent(withName: "onTokenExpired", body: values)
      }
    }
  }

  
  public func onExit(reason: String, user_id: String) {
    //Event Sent After Get Connect
    DispatchQueue.main.async {
      var values = [reason, user_id]
      if self.hasObservers ?? false {
            self.sendEvent(withName: "onExit", body: values)
      }
    }
  }

  public func onConnectionFailure(reason: String,work_platform_id:String ,user_id: String) { 
    //Event Sent After Get Connect
    DispatchQueue.main.async {
      var values = [reason,work_platform_id,user_id]
      if self.hasObservers ?? false {
            self.sendEvent(withName: "onConnectionFailure", body: values)
      }
    }
  }
}
