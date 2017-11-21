package com.google.android.gms.drive

import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import com.google.android.gms.common.api.GoogleApiClient

/**
 * Created by Administrator on 2017-11-21.
 */

open class GoogleDriveUtils : Activity(), GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    /**
     * Google API client.
     */
    private var mGoogleApiClient: GoogleApiClient? = null

    /**
     * Called when `mGoogleApiClient` is connected.
     */
    override fun onConnected(connectionHint: Bundle?) {}

    /**
     * Called when `mGoogleApiClient` is disconnected.
     */
    override fun onConnectionSuspended(cause: Int) {}

    /**
     * Called when `mGoogleApiClient` is trying to connect but failed.
     * Handle `result.getResolution()` if there is a resolution is
     * available.
     */
    override fun onConnectionFailed(result: ConnectionResult) {
        if (!result.hasResolution()) {
            // show the localized error dialog.
            GoogleApiAvailability.getInstance().getErrorDialog(this, result.errorCode, 0).show()
            return
        }
        try {
            result.startResolutionForResult(this, REQUEST_CODE_RESOLUTION)
        } catch (e: IntentSender.SendIntentException) {
            e.printStackTrace()
        }

    }

    /**
     * Called when activity gets visible. A connection to Drive services need to
     * be initiated as soon as the activity is visible. Registers
     * `ConnectionCallbacks` and `OnConnectionFailedListener` on the
     * activities itself.
     */
    override fun onResume() {
        super.onResume()
        if (mGoogleApiClient == null) {
            mGoogleApiClient = GoogleApiClient.Builder(this)
                    .addApi(Drive.API)
                    .addScope(Drive.SCOPE_FILE)
                    .addScope(Drive.SCOPE_APPFOLDER) // required for App Folder sample
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build()
        }
        mGoogleApiClient?.connect()
    }

    /**
     * Called when activity gets invisible. Connection to Drive service needs to
     * be disconnected as soon as an activity is invisible.
     */
    override fun onPause() {
        mGoogleApiClient?.let { it.disconnect() }
        super.onPause()
    }

    /**
     * Getter for the `GoogleApiClient`.
     */
    fun getGoogleApiClient(): GoogleApiClient? {
        return mGoogleApiClient
    }

    /**
     * Handles resolution callbacks.
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_RESOLUTION) {
                mGoogleApiClient?.connect()
            } else if (requestCode == REQUEST_CODE_GOOGLE_DRIVE_UPLOAD) {
                finish()
            }
        } else if (resultCode == Activity.RESULT_CANCELED) {
            finish()
        }
    }

    companion object {

        const val REQUEST_CODE_RESOLUTION = 0
        const val REQUEST_CODE_GOOGLE_DRIVE_UPLOAD = 1
        const val REQUEST_CODE_GOOGLE_DRIVE_DOWNLOAD = 2
    }
}