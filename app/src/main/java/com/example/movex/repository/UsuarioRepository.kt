import androidx.compose.runtime.mutableStateListOf
import com.example.movex.models.Usuario

object UsuarioRepository {
    private val usuarios = mutableStateListOf<Usuario>()

    fun adicionarUsuario(usuario: Usuario){
        usuarios.add(usuario)
    }

    fun listarUsuarios(): List<Usuario>{
        return usuarios;
    }

    fun atualizarUsuario(usuario: Usuario) {
        val index = usuarios.indexOfFirst { it.id == usuario.id }
        if (index != -1) {
            usuarios[index] = usuario
        }
    }

    fun removerUsuario(id: Int){
        usuarios.removeAll{it.id == id}
    }
}