package com.example.facedetectionapp.activities

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.facedetectionapp.R
import com.example.facedetectionapp.models.Student

class StudentAdapter : RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

    private val studentList = mutableListOf<Student>()

    fun setStudents(students: List<Student>) {
        studentList.clear()
        studentList.addAll(students)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_student, parent, false)
        return StudentViewHolder(view)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val student = studentList[position]
        holder.bind(student)
    }

    override fun getItemCount(): Int = studentList.size

    class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvStudentName: TextView = itemView.findViewById(R.id.tvStudentName)
        private val tvStudentID: TextView = itemView.findViewById(R.id.tvStudentID)
        private val imgStudent: ImageView = itemView.findViewById(R.id.imgStudent)

        private val btnEdit: ImageButton = itemView.findViewById(R.id.btnEdit)
        private val btnDelete: ImageButton = itemView.findViewById(R.id.btnDelete)


        fun bind(student: Student) {
            // Afficher le nom et l'ID de l'étudiant
            tvStudentName.text = "${student.firstName} ${student.lastName}"
            tvStudentID.text = student.studentId

            // Construire l'URL complète pour l'image en utilisant l'IP de votre serveur Django
            val imageUrl =
                "http://192.168.1.106:8000${student.image}" // Utilisez "localhost" si vous êtes en mode local

            // Vérifiez l'URL dans les logs
            Log.d("StudentAdapter", "Image URL: $imageUrl")

            // Charger l'image avec Glide
            Glide.with(itemView.context)
                .load(imageUrl) // URL complète de l'image
                .placeholder(android.R.color.darker_gray) // Afficher un placeholder en attendant le chargement
                .into(imgStudent)


            btnEdit.setOnClickListener {
                val intent = Intent(itemView.context, AddEditStudentActivity::class.java)
                intent.putExtra("studentId", student.id) // Passez l'ID pour l'édition
                itemView.context.startActivity(intent)
            }


            btnDelete.setOnClickListener {
                // Utilisez l'ID primaire pour la suppression
                (itemView.context as? ManageStudentsActivity)?.deleteStudent(student.id)
            }


        }
    }
}
