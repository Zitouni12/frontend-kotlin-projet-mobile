package com.example.facedetectionapp.utils

import android.content.ContentUris
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Log
import android.os.Build
import android.provider.DocumentsContract
import java.io.File
import java.io.FileOutputStream

object FileUtils {

    fun getPath(context: Context, uri: Uri): String? {
        val isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT

        // Si l'URI est un fichier local, renvoyer directement le chemin
        if ("file" == uri.scheme) {
            return uri.path
        }

        // Pour les documents de type "content://"
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // Si c'est un document de type externe
            if (isExternalStorageDocument(uri)) {
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).toTypedArray()
                val type = split[0]
                if ("primary".equals(type, ignoreCase = true)) {
                    return context.getExternalFilesDir(null)?.absolutePath + "/" + split[1]
                }
            } else if (isDownloadsDocument(uri)) {
                // Cas des documents téléchargés
                val id = DocumentsContract.getDocumentId(uri)
                val contentUri = Uri.parse("content://downloads/public_downloads")
                val contentUriWithId = Uri.withAppendedPath(contentUri, id)
                return getDataColumn(context, contentUriWithId, null, null)
            } else if (isMediaDocument(uri)) {
                // Cas des médias
                val docId = DocumentsContract.getDocumentId(uri)
                val split = docId.split(":".toRegex()).toTypedArray()
                val type = split[0]
                var contentUri: Uri? = null
                if ("image" == type) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                } else if ("video" == type) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                } else if ("audio" == type) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI
                }
                val selection = "_id=?"
                val selectionArgs = arrayOf(split[1])
                return contentUri?.let { getDataColumn(context, it, selection, selectionArgs) }
            }
        } else if ("content" == uri.scheme) {
            // Cas généraux de "content://" qui ne sont pas des documents
            return getDataColumn(context, uri, null, null)
        }
        return null
    }

    private fun getDataColumn(context: Context, uri: Uri, selection: String?, selectionArgs: Array<String>?): String? {
        try {
            // Utiliser openInputStream pour ouvrir un flux d'entrée si c'est un fichier
            val inputStream = context.contentResolver.openInputStream(uri)

            // Vérifiez si le flux d'entrée n'est pas nul et traitez-le
            inputStream?.use {
                // Par exemple, vous pourriez enregistrer l'image temporairement
                val file = File(context.cacheDir, "temp_image.jpg")
                val outputStream = FileOutputStream(file)

                // Copier le contenu du flux d'entrée dans un fichier temporaire
                inputStream.copyTo(outputStream)

                // Retourner le chemin du fichier temporaire
                return file.absolutePath
            }
        } catch (e: Exception) {
            Log.e("FileUtils", "Error retrieving file path: ${e.message}")
        }

        return null
    }


    // Vérifie si l'URI appartient à un stockage externe
    private fun isExternalStorageDocument(uri: Uri): Boolean {
        return "com.android.externalstorage.documents" == uri.authority
    }

    // Vérifie si l'URI appartient aux téléchargements
    private fun isDownloadsDocument(uri: Uri): Boolean {
        return "com.android.providers.downloads.documents" == uri.authority
    }

    // Vérifie si l'URI appartient aux documents médias
    private fun isMediaDocument(uri: Uri): Boolean {
        return "com.android.providers.media.documents" == uri.authority
    }
}
