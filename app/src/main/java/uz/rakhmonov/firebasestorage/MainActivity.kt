package uz.rakhmonov.firebasestorage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import uz.rakhmonov.firebasestorage.databinding.ActivityMainBinding
import kotlin.jvm.internal.MutablePropertyReference0

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    lateinit var firebaseStorage:FirebaseStorage
    lateinit var reference: StorageReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        firebaseStorage= FirebaseStorage.getInstance()
        reference=firebaseStorage.getReference("MeningStoragim")

        binding.btnLoad.setOnClickListener {
            getImageContent.launch("image/*")
//            getImageContent.launch("video/*")   video yuklashda

        }
    }

       val getImageContent= registerForActivityResult(ActivityResultContracts.GetContent()){uri->

           if (uri!=null){
               val task = reference.child("rasmchalar").putFile(uri)
               binding.myProgress.visibility= View.VISIBLE
               task.addOnSuccessListener {

               binding.myProgress.visibility= View.GONE
                   binding.myImage.setImageURI(uri)

                   it.metadata?.reference?.downloadUrl?.addOnSuccessListener{
                       println(it)
                       Toast.makeText(this, "${it}", Toast.LENGTH_SHORT).show()

                   }

//                   binding.myVideo.setVideoURI(uri)   video yuklashda
//                   binding.myVideo.start()            video yuklashda   va UI da xam <VideoView bo'lishi kerak
               }
               task.addOnFailureListener {
                   Toast.makeText(this, "yuklab bo'lmadi", Toast.LENGTH_SHORT).show()

               binding.myProgress.visibility= View.GONE
               }

           }else{
               Toast.makeText(this, "Rasm tanlanmadi", Toast.LENGTH_SHORT).show()
           }


    }
}