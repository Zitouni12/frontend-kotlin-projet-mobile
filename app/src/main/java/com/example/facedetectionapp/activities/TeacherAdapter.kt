package com.example.facedetectionapp.activities

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.facedetectionapp.R
import com.example.facedetectionapp.models.Teacher

class TeacherAdapter(private var teachers: List<Teacher>, private val listener: OnItemClickListener) :
    RecyclerView.Adapter<TeacherAdapter.TeacherViewHolder>() {

    interface OnItemClickListener {
        fun onEditClick(teacher: Teacher)
        fun onDeleteClick(teacher: Teacher)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeacherViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_teacher, parent, false)
        return TeacherViewHolder(view)
    }

    override fun onBindViewHolder(holder: TeacherViewHolder, position: Int) {
        val teacher = teachers[position]
        holder.bind(teacher)
    }

    override fun getItemCount(): Int = teachers.size

    fun updateTeachers(newTeachers: List<Teacher>) {
        teachers = newTeachers
        notifyDataSetChanged()
    }

    inner class TeacherViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val btnEdit: Button = itemView.findViewById(R.id.btnEdit)
        private val btnDelete: Button = itemView.findViewById(R.id.btnDelete)
        private val txtNomPrenom: TextView = itemView.findViewById(R.id.txtName)
        private val txtDateEmbauche: TextView = itemView.findViewById(R.id.txtDateEmbauche)
        private val txtMatiere: TextView = itemView.findViewById(R.id.txtMatiere)
        private val txtFiliere: TextView = itemView.findViewById(R.id.txtFiliere)

        fun bind(teacher: Teacher) {
            // Affichage des informations de l'enseignant
            txtNomPrenom.text = "${teacher.nom} ${teacher.prenom}"
            txtDateEmbauche.text = "Date Embauche: ${teacher.dateEmbauche ?: "Non spécifiée"}"
            txtMatiere.text = "Matière: ${teacher.matiere ?: "Non spécifiée"}"
            txtFiliere.text = "Filière: ${teacher.filiere ?: "Non spécifiée"}"

            // Ajouter des actions pour les boutons
            btnEdit.setOnClickListener { listener.onEditClick(teacher) }
            btnDelete.setOnClickListener { listener.onDeleteClick(teacher) }
        }
    }
}

