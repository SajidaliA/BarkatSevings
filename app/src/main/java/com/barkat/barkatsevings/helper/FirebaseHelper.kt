package com.barkat.barkatsevings.helper

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.barkat.barkatsevings.SAVING_ID
import com.barkat.barkatsevings.USER_ID
import com.barkat.barkatsevings.data.Saving
import java.io.File
import javax.inject.Inject
import com.barkat.barkatsevings.data.User
import com.barkat.barkatsevings.utils.PreferenceProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

/**
 * Created by Sajid Ali Suthar.
 */
class FirebaseHelper @Inject constructor(private val mPreferenceManager: PreferenceProvider) {
    private var storage: FirebaseStorage? = null
    private var storageReference: StorageReference? = null
    private var fileReference: StorageReference? = null
    private var firebaseDatabase: FirebaseDatabase? = null
    private var databaseReference: DatabaseReference? = null
    private lateinit var auth: FirebaseAuth

    fun checkUserLoggedIn() : FirebaseUser? {
        auth = FirebaseAuth.getInstance()
        return auth.currentUser
    }

    fun login(email: String, password: String) : LiveData<FirebaseUser>{
        val firebaseUserLiveData = MutableLiveData<FirebaseUser>()
        auth = FirebaseAuth.getInstance()
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful){
                    Log.d("TAG", "signInWithEmail:success")
                    firebaseUserLiveData.postValue(auth.currentUser)
                }else{
                    Log.w("TAG", "signInWithEmail:failure", task.exception)
                    firebaseUserLiveData.postValue(null)
                }
            }
        return firebaseUserLiveData
    }

    fun uploadProfileImage(user: User) {
        storage = FirebaseStorage.getInstance()
        storageReference = storage?.reference
        fileReference = storageReference?.child("profileImages/" + user.id)
        fileReference?.putFile(Uri.fromFile(File(user.profileImage)))
            ?.addOnSuccessListener {
                fileReference?.downloadUrl?.addOnSuccessListener {
                    user.profileImage = it.toString()
                }
            }
            ?.addOnFailureListener { e -> // Error, Image not uploaded
                Log.e("TAG", "Recording file save FAILED")
            }
            ?.addOnProgressListener { taskSnapshot ->
                // Progress Listener for loading percentage on the dialog box
                val progress = (100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount)

            }
    }

    private fun postSavingToFB(saving: Saving) {
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase?.getReference("Savings")
        val savingId = mPreferenceManager.getValue(SAVING_ID, "")
        savingId?.let {
            databaseReference?.child(it)?.child(saving.id.toString())
                ?.setValue(saving)?.addOnSuccessListener {
            }?.addOnFailureListener {
                Log.e("TAG", "Saving save FAILED")
            }
        }
    }

    fun getSavingsFromServer(): LiveData<ArrayList<Saving>> {
        val savingsLiveData: MutableLiveData<ArrayList<Saving>> = MutableLiveData()
        val savingList: ArrayList<Saving> = ArrayList()
        firebaseDatabase = FirebaseDatabase.getInstance()
        databaseReference = firebaseDatabase?.getReference("Savings")

        databaseReference?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    Log.e("TAG", "task SUCCESS")
                    savingList.clear()
                    savingsLiveData.value?.clear()
                    for (postSnapshot in snapshot.children) {
                        postSnapshot.getValue(Saving::class.java)?.let {
                            savingList.add(it)
                        }
                    }
                    savingsLiveData.postValue(savingList)
                } catch (e: Exception) {
                    Log.e("TAG", "task in EXCEPTION")
                    e.printStackTrace()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("TAG", "task CANCELED")
            }
        })
        return savingsLiveData
    }

    fun deleteRecordingPermanently(saving: Saving) {
        databaseReference?.child(mPreferenceManager.getValue(USER_ID, "").toString())
            ?.child(saving.id.toString())?.removeValue()?.addOnCompleteListener {
            storageReference?.child("profileImages/" + saving.id)?.delete()
                ?.addOnSuccessListener {
                    Log.e("TAG", "Profile Image deleted")
                }?.addOnFailureListener {
                Log.e("TAG", "Profile image delete FAILED")
            }
        }?.addOnFailureListener {
            Log.e("TAG", "Recording delete FAILED")
        }

    }
}