package com.example.facedetectionapp.activities

import android.Manifest
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.facedetectionapp.R
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.example.facedetectionapp.api.ApiService
import com.example.facedetectionapp.api.RetrofitClient
import com.example.facedetectionapp.models.Student
import com.example.facedetectionapp.models.StudentResponse
import com.example.facedetectionapp.utils.FileUtils
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream

class AddEditStudentActivity : AppCompatActivity() {

    private lateinit var etStudentID: EditText
    private lateinit var etFirstName: EditText
    private lateinit var etLastName: EditText
    private lateinit var etBirthDate: EditText
    private lateinit var spStatus: Spinner
    private lateinit var btnSaveStudent: Button
    private lateinit var btnChooseImage: Button
    private lateinit var imgStudentPreview: ImageView

    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_student)

        // Initialisation des vues
        etStudentID = findViewById(R.id.etStudentID)
        etFirstName = findViewById(R.id.etFirstName)
        etLastName = findViewById(R.id.etLastName)
        etBirthDate = findViewById(R.id.etBirthDate)
        spStatus = findViewById(R.id.spStatus)
        btnSaveStudent = findViewById(R.id.btnSaveStudent)
        btnChooseImage = findViewById(R.id.btnChooseImage)
        imgStudentPreview = findViewById(R.id.imgStudentPreview)

        // Populate the status spinner
        val statusOptions = arrayOf("Present", "Absent")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, statusOptions)
        spStatus.adapter = adapter

        // Vérifiez si un ID est passé pour l'édition
        val studentId = intent.getIntExtra("studentId", -1)
        if (studentId != -1) {
            loadStudentDetails(studentId)
        }

        // Choisir une image
        btnChooseImage.setOnClickListener {
            selectImage()
        }

        // Sauvegarder l'étudiant
        btnSaveStudent.setOnClickListener {
            saveStudent()
        }
    }

    // Handle image selection
    private fun selectImage() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            selectedImageUri = data?.data
            Glide.with(this).load(selectedImageUri).into(imgStudentPreview)
        }
    }

    private fun saveStudent() {
        val studentId = etStudentID.text.toString().trim()
        val firstName = etFirstName.text.toString().trim()
        val lastName = etLastName.text.toString().trim()
        val birthDate = etBirthDate.text.toString().trim()
        val status = spStatus.selectedItem.toString()

        if (studentId.isEmpty() || firstName.isEmpty() || lastName.isEmpty() || birthDate.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Convert data to RequestBody
        val studentIdPart = RequestBody.create("text/plain".toMediaTypeOrNull(), studentId)
        val firstNamePart = RequestBody.create("text/plain".toMediaTypeOrNull(), firstName)
        val lastNamePart = RequestBody.create("text/plain".toMediaTypeOrNull(), lastName)
        val birthDatePart = RequestBody.create("text/plain".toMediaTypeOrNull(), birthDate)
        val statusPart = RequestBody.create("text/plain".toMediaTypeOrNull(), status)

        // If image is selected, prepare it
        var imagePart: MultipartBody.Part? = null
        if (selectedImageUri != null) {
            val filePath = FileUtils.getPath(this, selectedImageUri!!)
            val file = File(filePath)

            if (!file.exists()) {
                Toast.makeText(this, "File not found", Toast.LENGTH_SHORT).show()
                return
            }

            // Compress the image before uploading
            val compressedFile = filePath?.let { compressImage(it) }

            if (compressedFile == null) {
                Toast.makeText(this, "Error compressing the image", Toast.LENGTH_SHORT).show()
                return
            }

            val requestFile = RequestBody.create("image/*".toMediaTypeOrNull(), compressedFile)
            imagePart = MultipartBody.Part.createFormData("image", compressedFile.name, requestFile)
        }

        // Check if the studentId is being edited or newly created
        val studentIdFromIntent = intent.getIntExtra("studentId", -1)
        val call: Call<StudentResponse>

        if (studentIdFromIntent != -1) {
            // Update existing student
            call = RetrofitClient.instance.create(ApiService::class.java).updateStudent(
                studentIdFromIntent, studentIdPart, firstNamePart, lastNamePart, birthDatePart, statusPart, imagePart
            )
        } else {
            // Add new student
            call = RetrofitClient.instance.create(ApiService::class.java).addStudent(
                studentIdPart, firstNamePart, lastNamePart, birthDatePart, statusPart, imagePart
            )
        }

        // Envoi de la requête API
        call.enqueue(object : Callback<StudentResponse> {
            override fun onResponse(call: Call<StudentResponse>, response: Response<StudentResponse>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@AddEditStudentActivity, "Student saved successfully", Toast.LENGTH_SHORT).show()
                    finish() // Go back to previous activity
                } else {
                    Toast.makeText(this@AddEditStudentActivity, "Failed to save student", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<StudentResponse>, t: Throwable) {
                Toast.makeText(this@AddEditStudentActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun loadStudentDetails(studentId: Int) {
        val apiService = RetrofitClient.instance.create(ApiService::class.java)

        apiService.getStudentById(studentId).enqueue(object : Callback<Student> {
            override fun onResponse(call: Call<Student>, response: Response<Student>) {
                if (response.isSuccessful) {
                    val student = response.body()
                    if (student != null) {
                        // Préremplir le formulaire
                        etStudentID.setText(student.studentId)
                        etFirstName.setText(student.firstName)
                        etLastName.setText(student.lastName)
                        etBirthDate.setText(student.birthDate)
                        spStatus.setSelection(if (student.status == "Present") 0 else 1)

                        // Charger l'image de l'étudiant
                        val imageUrl = "http://192.168.1.106:8000${student.image}"
                        Glide.with(this@AddEditStudentActivity)
                            .load(imageUrl)
                            .placeholder(android.R.color.darker_gray)
                            .into(imgStudentPreview)
                    }
                } else {
                    Toast.makeText(this@AddEditStudentActivity, "Failed to load student details", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Student>, t: Throwable) {
                Toast.makeText(this@AddEditStudentActivity, "Error: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun compressImage(imagePath: String): File? {
        val originalFile = File(imagePath)
        if (!originalFile.exists()) return null

        val bitmap = BitmapFactory.decodeFile(imagePath)
        val compressedFile = File(filesDir, "${originalFile.name}_compressed.jpg")
        val outputStream = FileOutputStream(compressedFile)

        // Compress the image to 70% quality
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, outputStream)
        outputStream.flush()
        outputStream.close()

        return compressedFile
    }
}
