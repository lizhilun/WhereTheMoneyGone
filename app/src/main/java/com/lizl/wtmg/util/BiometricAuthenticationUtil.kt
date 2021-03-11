package com.lizl.wtmg.util

import android.content.Context
import android.hardware.biometrics.BiometricManager
import android.hardware.biometrics.BiometricPrompt
import android.os.CancellationSignal
import android.util.Log
import androidx.core.content.ContextCompat
import com.blankj.utilcode.util.StringUtils
import com.blankj.utilcode.util.Utils
import com.lizl.wtmg.R

object BiometricAuthenticationUtil
{
    private val TAG = "BiometricAuthenticationUtil"

    private val mBiometricPrompt: BiometricPrompt by lazy {
        BiometricPrompt.Builder(Utils.getApp()).setTitle(StringUtils.getString(R.string.fingerprint_authentication_dialog_title))
            .setDescription(StringUtils.getString(R.string.fingerprint_authentication_dialog_description))
            .setNegativeButton(StringUtils.getString(R.string.cancel), ContextCompat.getMainExecutor(Utils.getApp()), { _, _ -> }).build()
    }

    private val mBiometricManager: BiometricManager by lazy { Utils.getApp().getSystemService(Context.BIOMETRIC_SERVICE) as BiometricManager }

    fun isFingerprintSupport() = mBiometricManager.canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS

    fun authenticate(callback: (result: Boolean, error: String) -> Unit)
    {
        mBiometricPrompt.authenticate(CancellationSignal(), ContextCompat.getMainExecutor(Utils.getApp()), object : BiometricPrompt.AuthenticationCallback()
        {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence?)
            {
                Log.d(TAG, "onAuthenticationError() called with: errorCode = [$errorCode], errString = [$errString]")
                super.onAuthenticationError(errorCode, errString)
                callback.invoke(false, errString.toString())
            }

            override fun onAuthenticationFailed()
            {
                Log.d(TAG, "onAuthenticationFailed")
                super.onAuthenticationFailed()
                callback.invoke(false, "")
            }

            override fun onAuthenticationHelp(helpCode: Int, helpString: CharSequence?)
            {
                Log.d(TAG, "onAuthenticationHelp() called with: helpCode = [$helpCode], helpString = [$helpString]")
                super.onAuthenticationHelp(helpCode, helpString)
                callback.invoke(false, helpString.toString())
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult?)
            {
                Log.d(TAG, "onAuthenticationSucceeded() called with: result = [$result]")
                super.onAuthenticationSucceeded(result)
                callback.invoke(true, "")
            }
        })
    }
}