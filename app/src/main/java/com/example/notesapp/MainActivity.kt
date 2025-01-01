package com.example.notesapp

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {

    // Firebase Database Reference
    private lateinit var database: DatabaseReference

    // UI Elements
    private lateinit var titleEditText: TextInputEditText
    private lateinit var contentEditText: TextInputEditText
    private lateinit var saveButton: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance().reference

        // Initialize UI Elements
        titleEditText = findViewById(R.id.titletext)
        contentEditText = findViewById(R.id.content_text)
        saveButton = findViewById(R.id.mybutton)

        // Save Button Click Listener
        saveButton.setOnClickListener {
            saveNoteToFirebase()
        }
    }

    private fun saveNoteToFirebase() {
        // Get input values from EditTexts
        val title = titleEditText.text.toString().trim()
        val content = contentEditText.text.toString().trim()

        // Validate Inputs
        if (title.isEmpty() || content.isEmpty()) {
            Toast.makeText(this, "Please fill out both fields!", Toast.LENGTH_SHORT).show()
            return
        }

        // Create a Note object
        val note = Note(title, content)

        // Save note to Firebase Realtime Database under "notes"
        val noteId = database.child("notes").push().key // Generate a unique ID for the note
        if (noteId != null) {
            database.child("notes").child(noteId).setValue(note)
                .addOnSuccessListener {
                    Toast.makeText(this, "Note saved successfully!", Toast.LENGTH_SHORT).show()
                    titleEditText.text?.clear()
                    contentEditText.text?.clear()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to save note. Try again.", Toast.LENGTH_SHORT).show()
                }
        }
    }

    // Data Class for Note
    data class Note(val title: String, val content: String)
}
