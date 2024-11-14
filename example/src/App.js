import React, { useEffect, useState } from 'react'
import { View, StyleSheet, TouchableOpacity, Alert, Text } from 'react-native'
import PhylloConnect from 'react-native-phyllo-connect'
import AsyncStorage from '@react-native-async-storage/async-storage'
import BouncyCheckbox from 'react-native-bouncy-checkbox'

import { createUser, createUserToken } from './APIHandler'
import { generateRandomString } from './randomGenerator'
import clientConfig from './config'
import Toast from 'react-native-simple-toast'

export default function ExampleApp() {
  const [existingUser, setExistingUser] = useState(false)
  const [userId, setUserId] = useState('')
  const [userToken, setUserToken] = useState('')

  useEffect(() => {
    const getUserFromStorage = async () => {
      const userId = await AsyncStorage.getItem('user-id')
      const token = await AsyncStorage.getItem('user-token')

      if (!userId || !token) {
        return
      }
      setUserId(userId)
      setUserToken(token)
    }

    getUserFromStorage()
  }, [])

  // A callback function called upon event
  const onExitCallBack = (reason, userId) => {
    console.log(`onExit reason: ${reason}, userId: ${userId}`)
    Toast.show(`onExit reason: ${reason}, userId: ${userId}`)
  }
  const onAccountConnectedCallBack = (accountId, workplatformId, userId) => {
    console.log(
      `onAccountConnected accountId: ${accountId}, workplatformId: ${workplatformId}, userId: ${userId}`,
    )
    Toast.show(
      `onAccountConnected accountId: ${accountId}, workplatformId: ${workplatformId}, userId: ${userId}`,
    )
  }
  const onAccountDisconnectedCallBack = (accountId, workplatformId, userId) => {
    console.log(
      `onAccountDisconnected accountId: ${accountId}, workplatformId: ${workplatformId}, userId: ${userId}`,
    )
    Toast.show(
      `onAccountDisconnected accountId: ${accountId}, workplatformId: ${workplatformId}, userId: ${userId}`,
    )
  }
  const onTokenExpiredCallBack = (userId) => {
    console.log(`onTokenExpired userId: ${userId}`)
    Toast.show(`onTokenExpired userId: ${userId}`)
  }

  const onConnectionFailure = (reason, workplatformId, userId) => {
    console.log(
      `onConnectionFailure reason: ${reason}, workplatformId: ${workplatformId}, userId: ${userId}`,
    )
    Toast.show(
      `onConnectionFailure reason: ${reason}, workplatformId: ${workplatformId}, userId: ${userId}`,
    )
  }

  const onPressButton = async (workPlatformId) => {
    const clientDisplayName = 'Example'
    const externalId = generateRandomString(20)
    const environment = clientConfig.env
    const baseURL = await PhylloConnect.getEnvBaseURl(environment.toString())
    let id, token
    try {
      // Create a user, SDK Token if the user is new user
      if (existingUser) {
        id = userId
        token = userToken
      } else {
        id = await createUser(generateRandomString(8), externalId, baseURL)
        token = await createUserToken(id, baseURL)
        await AsyncStorage.setItem('user-id', id)
        await AsyncStorage.setItem('user-token', token)
        setUserId(id)
        setUserToken(token)
      }

      // set config
      const config = {
        clientDisplayName,
        token,
        userId: id,
        environment,
        workPlatformId,
      }

      const phylloConnect = PhylloConnect.initialize(config)
      phylloConnect.on('exit', onExitCallBack)
      phylloConnect.on('tokenExpired', onTokenExpiredCallBack)
      phylloConnect.on('accountConnected', onAccountConnectedCallBack)
      phylloConnect.on('accountDisconnected', onAccountDisconnectedCallBack)
      phylloConnect.on('connectionFailure', onConnectionFailure)

      phylloConnect.open()

      console.log('Version Object', phylloConnect.version())
    } catch (e) {
      Toast.show(e.message)
    }
  }
  return (
    <View style={styles.containerStyle}>
      <TouchableOpacity
        onPress={() => onPressButton('')}
        style={styles.buttonStyle}
      >
        <Text style={styles.buttonText}>Connect Platform Account(s)</Text>
      </TouchableOpacity>
      <TouchableOpacity
        onPress={() => onPressButton('9bb8913b-ddd9-430b-a66a-d74d846e6c66')}
        style={styles.buttonStyle}
      >
        <Text style={styles.buttonText}>Connect Instagram using Phyllo</Text>
      </TouchableOpacity>
      <TouchableOpacity
        onPress={() => onPressButton('14d9ddf5-51c6-415e-bde6-f8ed36ad7054')}
        style={styles.buttonStyle}
        underlayColor='#fff'
      >
        <Text style={styles.buttonText}>Connect YouTube using Phyllo</Text>
      </TouchableOpacity>

      <BouncyCheckbox
        fillColor='green'
        onPress={(isChecked) => {
          setExistingUser(isChecked)
        }}
        text='Existing user'
        isChecked={existingUser}
        textStyle={{
          textDecorationLine: 'none',
        }}
        style={styles.checkboxStyle}
        disabled={!userId || !userToken}
      />
    </View>
  )
}

const styles = StyleSheet.create({
  buttonStyle: {
    marginVertical: 5,
    backgroundColor: '#524FA1',
    borderColor: '#524FA1',
    height: 40,
    display: 'flex',
    justifyContent: 'center',
    borderRadius: 5,
    shadowColor: 'black',
    shadowOpacity: 0.26,
    shadowOffset: { width: 0, height: 2 },
    shadowRadius: 5,
    elevation: 3,
  },

  buttonText: {
    color: '#fff',
    textAlign: 'center',
    fontWeight: 'bold',
  },
  containerStyle: {
    display: 'flex',
    flex: 0.8,
    marginHorizontal: 35,
    justifyContent: 'center',
  },
  checkboxStyle: {
    marginTop: 20,
  },
})
