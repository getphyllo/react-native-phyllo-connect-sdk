import { PhylloEnvironment } from 'react-native-phyllo-connect'

const clientId = '<client id here>' // add your Id here
const clientSecret = '<client secret here>' // add your client Secret
const env = PhylloEnvironment.sandbox // add your environment type, sandbox, production are the valid values

export default {
  clientId,
  clientSecret,
  env,
}
