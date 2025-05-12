package com.example.testapplication.ui.theme.model

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import org.json.JSONObject

class ContestDataProvider : ContentProvider() {

    override fun onCreate(): Boolean {
        // Initialization (e.g., setup databases, etc.)
        return true
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        // Example query logic (this is where you fetch your data, e.g., JSON string)
        val result = "{\"randomText\": {\"value\": \"GeneratedString123\", \"created\": \"2025-05-12\"}}"
        val jsonObject = JSONObject(result)
        val randomText = jsonObject.getJSONObject("randomText")
        val value = randomText.getString("value")
        val created = randomText.getString("created")

        // Create a cursor with your data
        val matrixCursor = MatrixCursor(arrayOf("data"))
        matrixCursor.addRow(arrayOf(result))
        return matrixCursor
    }

    override fun getType(uri: Uri): String? {
        return "vnd.android.cursor.item/vnd.com.iav.contestdataprovider.text"
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return null
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        return 0
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        return 0
    }
}