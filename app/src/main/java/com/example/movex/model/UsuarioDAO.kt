package com.example.movex.model

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.tasks.await

class UsuarioDAO {

    private val db = FirebaseFirestore.getInstance()
    private val usuariosCollection = db.collection("usuarios")

    // Função para adicionar um novo usuário
    suspend fun adicionarUsuario(usuario: Usuario) {
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

    // Função para remover um usuário pelo ID
    suspend fun removerUsuario(id: Long) {
        usuariosCollection.document(id.toString()).delete().await()
    }

    // Função para autenticar um usuário (verificar email e senha)
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

    // Função para listar todos os usuários
    suspend fun listarUsuarios(): List<Usuario> {
        val querySnapshot = usuariosCollection.get().await()
        return querySnapshot.documents.mapNotNull { it.toObject<Usuario>() }
    }

    // Função para atualizar um usuário existente
    suspend fun atualizarUsuario(usuario: Usuario) {
        usuario.id?.let {
            usuariosCollection.document(it.toString()).set(usuario).await()
        }
    }

    // Função para gerar um novo ID único
    private suspend fun gerarNovoId(): Int {
        val allUsers = listarUsuarios()
        return (allUsers.maxOfOrNull { it.id ?: 0 } ?: 0) + 1
    }

    // Função para obter um usuário pelo ID
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

}
