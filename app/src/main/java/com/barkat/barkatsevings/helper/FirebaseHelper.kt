package com.barkat.barkatsevings.helper

import android.content.Context
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.barkat.barkatsevings.utils.USER_ID
import com.barkat.barkatsevings.data.Saving
import java.io.File
import javax.inject.Inject
import com.barkat.barkatsevings.data.User
import com.barkat.barkatsevings.utils.PreferenceProvider
import com.barkat.barkatsevings.utils.SAVING_BUCKET
import com.barkat.barkatsevings.utils.USER_BUCKET
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped

/**
 * Created by Sajid Ali Suthar.
 */

class FirebaseHelper @Inject constructor(
    private val mPreferenceManager: PreferenceProvider
) {
    private val TAG = "FIREBASE_HELPER"
    private var storage: FirebaseStorage? = null
    private var storageReference: StorageReference? = null
    private var fileReference: StorageReference? = null
    private var firebaseDatabase: FirebaseDatabase? = null
    private var userReference: DatabaseReference? = null
    private lateinit var auth: FirebaseAuth

    fun checkUserLoggedIn(): FirebaseUser? {
        auth = FirebaseAuth.getInstance()
        return auth.currentUser
    }

    fun login(email: String, password: String): LiveData<FirebaseUser> {
        val firebaseUserLiveData = MutableLiveData<FirebaseUser>()
        auth = FirebaseAuth.getInstance()
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithEmail:success")
                    firebaseUserLiveData.postValue(auth.currentUser)
                } else {
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    firebaseUserLiveData.postValue(null)
                }
            }
        return firebaseUserLiveData
    }

    fun uploadProfileImage(user: User) {
        storage = FirebaseStorage.getInstance()
        storageReference = storage?.reference
        fileReference = storageReference?.child("profileImages/" + user.id)
        fileReference?.putFile(Uri.fromFile(File(user.profileImage.toString())))
            ?.addOnSuccessListener {
                fileReference?.downloadUrl?.addOnSuccessListener {
                    user.profileImage = it.toString()
                }
            }
            ?.addOnFailureListener { e -> // Error, Image not uploaded
                Log.e(TAG, "Recording file save FAILED")
            }
            ?.addOnProgressListener { taskSnapshot ->
                // Progress Listener for loading percentage on the dialog box
                val progress = (100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount)

            }
    }

    fun addSaving(selectedUserId: String?, saving: Saving, context: Context) {
        firebaseDatabase = FirebaseDatabase.getInstance()
        userReference = firebaseDatabase?.getReference(SAVING_BUCKET)
        selectedUserId?.let { it ->
            userReference?.child(it)?.child(saving.month.toString() + saving.year.toString())
                ?.setValue(saving)?.addOnSuccessListener {
                    Toast.makeText(context, "Saving added", Toast.LENGTH_SHORT).show()
                }?.addOnFailureListener {
                    Log.e(TAG, it.message.toString())
                }
        }
    }

    fun updateUser(user: User) {
        firebaseDatabase = FirebaseDatabase.getInstance()
        userReference = firebaseDatabase?.getReference(USER_BUCKET)
        val userId = mPreferenceManager.getValue(USER_ID, "")
        userId?.let { it ->
            userReference?.child(it)
                ?.setValue(user)?.addOnSuccessListener {
                    Log.e(TAG, "User Saved")
                }?.addOnFailureListener {
                    Log.e(TAG, it.message.toString())
                }
        }
    }

    fun getUserSavingsFromServer(): LiveData<ArrayList<Saving>> {
        val savingsLiveData: MutableLiveData<ArrayList<Saving>> = MutableLiveData()
        val savingList: ArrayList<Saving> = ArrayList()
        firebaseDatabase = FirebaseDatabase.getInstance()
        userReference = firebaseDatabase?.getReference(SAVING_BUCKET)
        val userIdReference =
            userReference?.child(mPreferenceManager.getValue(USER_ID, "").toString())

        userIdReference?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    savingList.clear()
                    savingsLiveData.value?.clear()
                    for (months in snapshot.children) {
                        months.getValue(Saving::class.java)?.let {
                            savingList.add(it)
                        }
                    }
                    if (savingList.isNotEmpty()) {
                        savingsLiveData.postValue(savingList)
                    }
                } catch (e: Exception) {
                    Log.e(TAG, e.message.toString())
                    e.printStackTrace()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(TAG, error.message)
            }
        })
        return savingsLiveData
    }

    fun getTotalSavingsFromServer(): LiveData<ArrayList<Saving>> {
        val allSavingsLiveData: MutableLiveData<ArrayList<Saving>> = MutableLiveData()
        val allSavingList: ArrayList<Saving> = ArrayList()
        firebaseDatabase = FirebaseDatabase.getInstance()
        userReference = firebaseDatabase?.getReference(SAVING_BUCKET)
        userReference?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    allSavingList.clear()
                    allSavingsLiveData.value?.clear()
                    for (user in snapshot.children) {
                        for (month in user.children) {
                            month.getValue(Saving::class.java)?.let {
                                allSavingList.add(it)
                            }
                        }
                    }
                    if (allSavingList.isNotEmpty()) {
                        allSavingsLiveData.postValue(allSavingList)
                    }
                } catch (e: Exception) {
                    Log.e(TAG, e.message.toString())
                    e.printStackTrace()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(TAG, error.message)
            }
        })
        return allSavingsLiveData
    }

    fun getUserListFromServer(): LiveData<ArrayList<User>> {
        val allUserListLiveData: MutableLiveData<ArrayList<User>> = MutableLiveData()
        val allUserList: ArrayList<User> = ArrayList()
        firebaseDatabase = FirebaseDatabase.getInstance()
        userReference = firebaseDatabase?.getReference(USER_BUCKET)
        userReference?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    allUserList.clear()
                    allUserListLiveData.value?.clear()
                    for (user in snapshot.children) {
                        user.getValue(User::class.java)?.let {
                            allUserList.add(it)
                        }
                    }
                    allUserListLiveData.postValue(allUserList)
                } catch (e: Exception) {
                    Log.e(TAG, e.message.toString())
                    e.printStackTrace()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(TAG, error.message)
            }
        })
        return allUserListLiveData
    }

    fun deleteProfileImage(saving: Saving) {
        userReference?.child(mPreferenceManager.getValue(USER_ID, "").toString())
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

    fun updateNameAndMobileNumber(name: String, photoUrl: String, context: Context) {
        auth = FirebaseAuth.getInstance()
        val profileUpdates = userProfileChangeRequest {
            displayName = name
            photoUri = Uri.parse(photoUrl)
        }
        auth.currentUser!!.updateProfile(profileUpdates)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context, "Data updated", Toast.LENGTH_SHORT).show()
                    Log.d(TAG, "User profile updated.")
                }
            }
    }

    fun logout() {
        Firebase.auth.signOut()
    }
}