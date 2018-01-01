package com.google.android.gms.drive

import android.content.Intent
import android.content.IntentSender
import android.os.Bundle
import com.google.android.gms.common.api.ResultCallback
import io.github.hanjoongcho.commons.helpers.TransitionHelper
import io.github.hanjoongcho.easypassword.activities.IntroActivity
import io.github.hanjoongcho.easypassword.persistence.DatabaseHelper
import org.apache.commons.io.IOUtils
import java.io.FileOutputStream

/**
 * Created by Administrator on 2017-11-21.
 */

class GoogleDriveDownloader : GoogleDriveUtils() {

    override fun onConnected(connectionHint: Bundle?) {
        super.onConnected(connectionHint)
        // If there is a selected file, open its contents.
        if (mSelectedFileDriveId != null) {
            open()
            return
        }

        val intentSender = Drive.DriveApi
                .newOpenFileActivityBuilder()
                .setMimeType(DatabaseHelper.getInstance(this@GoogleDriveDownloader).getMimeTypeAll())
                .build(getGoogleApiClient())

        try {
            startIntentSenderForResult(
                    intentSender, REQUEST_CODE_GOOGLE_DRIVE_DOWNLOAD, null, 0, 0, 0)
        } catch (e: IntentSender.SendIntentException) {
            e.printStackTrace()
        }
    }

    private fun open() {
        val driveFile = mSelectedFileDriveId?.asDriveFile()
        val listener = DriveFile.DownloadProgressListener { bytesDownloaded, bytesExpected ->
            // Update progress dialog with the latest progress.
            val progress = (bytesDownloaded * 100 / bytesExpected).toInt()
        }
        driveFile?.open(getGoogleApiClient(), DriveFile.MODE_READ_ONLY, listener)?.setResultCallback(driveContentsCallback)
    }

    private val driveContentsCallback = ResultCallback<DriveApi.DriveContentsResult> { result ->
        if (result.status.isSuccess) {
            try {
                val driveContents = result.driveContents.inputStream
                val outputStream = FileOutputStream(DatabaseHelper.getInstance(this@GoogleDriveDownloader).getDatabasePath())
                IOUtils.copy(driveContents, outputStream)
                IOUtils.closeQuietly(outputStream)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        val intent = Intent(this@GoogleDriveDownloader, IntroActivity::class.java).apply {
            putExtra(IntroActivity.INTRO_MODE, IntroActivity.INTRO_MODE_RESTART)
        }
        TransitionHelper.startActivityWithTransition(this@GoogleDriveDownloader, intent)
    }
}