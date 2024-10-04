package com.example.movex.telas


import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await

// Função para fazer o upload da imagem para o Firebase Storage
suspend fun uploadImageToStorage(uri: String): String {
    val storage = FirebaseStorage.getInstance()
    val storageRef = storage.reference.child("perfil/${System.currentTimeMillis()}_profile.jpg")

    val uploadTask = storageRef.putFile(android.net.Uri.parse(uri)).await()

    // Após o upload, obter a URL pública da imagem
    return storageRef.downloadUrl.await().toString()
}
