package com.example.movex.model

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.tasks.await

class UsuarioDAO {

    private val db = FirebaseFirestore.getInstance()
    private val usuariosCollection = db.collection("usuarios")

    suspend fun adicionarUsuario(usuario: Usuario) {
        validarUsuario(usuario)

        val querySnapshot = usuariosCollection
            .whereEqualTo("email", usuario.email)
            .get()
            .await()

        if (!querySnapshot.isEmpty) {
            throw IllegalArgumentException("O email já está em uso.")
        }

        val newId = gerarNovoId()
        val usuarioComId = usuario.copy(id = newId)
        usuariosCollection.document(newId.toString()).set(usuarioComId).await()
    }

    suspend fun removerUsuario(id: Long) {
        usuariosCollection.document(id.toString()).delete().await()
    }

    suspend fun autenticarUsuario(email: String, senha: String): Usuario? {
        val querySnapshot: QuerySnapshot = usuariosCollection
            .whereEqualTo("email", email)
            .whereEqualTo("senha", senha)
            .get()
            .await()

        return if (!querySnapshot.isEmpty) {
            querySnapshot.documents[0].toObject<Usuario>()
        } else {
            null
        }
    }

    suspend fun listarUsuarios(): List<Usuario> {
        val querySnapshot = usuariosCollection.get().await()
        return querySnapshot.documents.mapNotNull { it.toObject<Usuario>() }
    }

    suspend fun atualizarUsuario(usuario: Usuario) {
        validarUsuario(usuario)
        usuario.id?.let {
            usuariosCollection.document(it.toString()).set(usuario).await()
        }
    }

    private suspend fun gerarNovoId(): Int {
        val allUsers = listarUsuarios()
        return (allUsers.maxOfOrNull { it.id ?: 0 } ?: 0) + 1
    }

    suspend fun obterUsuarioPorId(id: Int): Usuario? {
        val documentSnapshot = usuariosCollection.document(id.toString()).get().await()
        return documentSnapshot.toObject<Usuario>()
    }

    suspend fun obterIdUsuarioPorEmail(email: String): Int? {
        val querySnapshot: QuerySnapshot = usuariosCollection
            .whereEqualTo("email", email)
            .get()
            .await()

        return if (!querySnapshot.isEmpty) {
            val usuario = querySnapshot.documents[0].toObject<Usuario>()
            usuario?.id
        } else {
            null
        }
    }

    // Função de validação
    private fun validarUsuario(usuario: Usuario) {
        if (usuario.nome.isNullOrBlank()) {
            throw IllegalArgumentException("O nome é obrigatório.")
        }

        if (usuario.email.isNullOrBlank() || !usuario.email.matches("^[A-Za-z0-9+_.-]+@(.+)$".toRegex())) {
            throw IllegalArgumentException("O e-mail é inválido.")
        }

        if (usuario.senha.isNullOrBlank()) {
            throw IllegalArgumentException("A senha é obrigatória.")
        }

        if (usuario.senha.length < 6) {
            throw IllegalArgumentException("A senha deve ter no mínimo 6 caracteres.")
        }
    }
}

