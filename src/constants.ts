export interface ICallbacks {
  callbackName: string;
  argsArr: Array<string>;
  argsLength: number;
}

export interface Version {
  connect_reactnative_sdk_version: string;
  connect_ios_sdk_version: string;
  min_supported_ios_version: string;
  max_supported_ios_version: string;
  connect_android_sdk_version: string;
  min_supported_android_version: string;
  max_supported_android_version: string;
}

export const reactNativeVersionObj: Version = {
  connect_reactnative_sdk_version: "0.3.7",
  connect_ios_sdk_version: "0.3.6",
  min_supported_ios_version: "12.0",
  max_supported_ios_version: "17.0*",
  connect_android_sdk_version: "0.3.6",
  min_supported_android_version: "21",
  max_supported_android_version: "34",
};

export const PHYLLO_ACCOUNT_CONNECTED_KEY: ICallbacks = {
  callbackName: "accountConnected",
  argsArr: ["accountId", "workplatformId", "userId"],
  argsLength: 3,
};

export const PHYLLO_ACCOUNT_DISCONNECTED_KEY: ICallbacks = {
  callbackName: "accountDisconnected",
  argsArr: ["accountId", "workplatformId", "userId"],
  argsLength: 3,
};

export const PHYLLO_ON_TOKEN_EXPIRED_KEY: ICallbacks = {
  callbackName: "tokenExpired",
  argsArr: ["userId"],
  argsLength: 1,
};

export const PHYLLO_ON_EXIT_KEY: ICallbacks = {
  callbackName: "exit",
  argsArr: ["reason", "userId"],
  argsLength: 2,
};

export const PHYLLO_ON_CONNECTION_FAILURE_KEY: ICallbacks = {
  callbackName: "connectionFailure",
  argsArr: ["reason", "workplatformId", "userId"],
  argsLength: 3,
};

interface ICallbackDefinitions {
  mandatoryCallbacks: Array<ICallbacks>;
  optionalCallbacks: Array<ICallbacks>;
}

export const callBacksDefinitionArr: ICallbackDefinitions = {
  mandatoryCallbacks: new Array(
    PHYLLO_ACCOUNT_CONNECTED_KEY,
    PHYLLO_ACCOUNT_DISCONNECTED_KEY,
    PHYLLO_ON_TOKEN_EXPIRED_KEY,
    PHYLLO_ON_EXIT_KEY,
  ),
  optionalCallbacks: new Array(PHYLLO_ON_CONNECTION_FAILURE_KEY),
};
