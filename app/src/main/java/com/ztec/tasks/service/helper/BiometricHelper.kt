package com.ztec.tasks.service.helper

import android.content.Context

import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG

class BiometricHelper {
    companion object {
        fun isBiometricAvailable(context: Context) = when (BiometricManager.from(context).canAuthenticate(BIOMETRIC_STRONG)) {
                                                            BiometricManager.BIOMETRIC_SUCCESS -> true
                                                            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> false
                                                            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> false
                                                            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> false
                                                            else -> false
                                                     }
    }
}