package com.google.android.gms.drive

import android.content.IntentSender
import android.os.Bundle
import com.google.android.gms.common.api.ResultCallback
import io.github.hanjoongcho.easypassword.persistence.DatabaseHelper
import org.apache.commons.io.FileUtils
import utils.DateUtils
import java.io.File
import java.io.IOException

/**
 * Created by Administrator on 2017-11-21.
 */

class GoogleDriveUploader : GoogleDriveUtils() {

    override fun onConnected(connectionHint: Bundle?) {
        super.onConnected(connectionHint)
        Drive.DriveApi.newDriveContents(getGoogleApiClient())
                .setResultCallback(driveContentsCallback)
    }

    private val driveContentsCallback: ResultCallback<DriveApi.DriveContentsResult> = ResultCallback { result ->
        val driveContents = result.driveContents
        val outputStream = driveContents.outputStream
        val backupFile = File(DatabaseHelper.getInstance(this@GoogleDriveUploader).getDatabasePath())
        try {
            FileUtils.copyFile(backupFile, outputStream)
        } catch (e: IOException) {
            e.printStackTrace()
        }

        val metadataChangeSet = MetadataChangeSet.Builder()
                .setTitle(DatabaseHelper.DIARY_DB_NAME + "_" + DateUtils.getDateStringFromTimeMillis(System.currentTimeMillis(), DateUtils.SIMPLE_DATE_FORMAT_FULL))
                .setMimeType(DatabaseHelper.getInstance(this@GoogleDriveUploader).getMimeType()).build()
        val intentSender = Drive.DriveApi
                .newCreateFileActivityBuilder()
                .setInitialMetadata(metadataChangeSet)
                .setInitialDriveContents(result.driveContents)
                .build(getGoogleApiClient())
        try {
            startIntentSenderForResult(
                    intentSender, REQUEST_CODE_GOOGLE_DRIVE_UPLOAD, null, 0, 0, 0)
        } catch (e: IntentSender.SendIntentException) {
            e.printStackTrace()
        }
    }

}