import { PhylloEnvironment } from "react-native-phyllo-connect"

const clientId = '0a62ced6-3b00-4112-9c6b-7e7e61db02d0' // add your Id here
const clientSecret = '6260ff9d-1026-4b75-8d81-b33e317b4581' // add your client Secret
const env = PhylloEnvironment.sandbox // add your environment type, sandbox, production are the valid values

export default {
  clientId,
  clientSecret,
  env,
}
