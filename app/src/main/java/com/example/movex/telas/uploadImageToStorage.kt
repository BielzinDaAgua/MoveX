import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await



suspend fun uploadImageToStorage(uriString: String): String {
    val storage = FirebaseStorage.getInstance()
    val storageRef = storage.reference.child("perfil/${System.currentTimeMillis()}_profile.jpg")


    val uri = Uri.parse(uriString)


    val uploadTask = storageRef.putFile(uri).await()

    return storageRef.downloadUrl.await().toString()
}
