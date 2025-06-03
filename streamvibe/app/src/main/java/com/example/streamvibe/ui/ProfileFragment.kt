package com.example.streamvibe.ui

import android.app.AlertDialog
import android.os.Bundle
import android.text.InputType
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.streamvibe.R
import com.example.streamvibe.repository.AuthRepository
import com.example.streamvibe.model.User

// PUBLIC_INTERFACE
class ProfileViewModel : ViewModel() {
    var user: User? = AuthRepository.getCurrentUser()

    fun updateProfile(name: String, email: String, avatar: String?) {
        AuthRepository.updateProfile(name, email, avatar)
        user = AuthRepository.getCurrentUser()
    }
}

// PUBLIC_INTERFACE
class ProfileFragment : Fragment() {
    private lateinit var viewModel: ProfileViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_profile, container, false)
        viewModel = ViewModelProvider(this)[ProfileViewModel::class.java]

        val layout = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(36, 90, 36, 16)
            gravity = android.view.Gravity.CENTER_HORIZONTAL
        }
        val user = viewModel.user
        val avatar = ImageView(requireContext()).apply {
            setImageResource(R.drawable.ic_launcher_round)
            layoutParams = LinearLayout.LayoutParams(220, 220)
        }
        val tvName = TextView(requireContext()).apply {
            text = user?.name ?: ""
            textSize = 22f
        }
        val tvEmail = TextView(requireContext()).apply {
            text = user?.email ?: ""
            setTextColor(resources.getColor(R.color.text_secondary))
        }
        val btnEdit = Button(requireContext()).apply {
            text = "Edit Profile"
            setOnClickListener {
                showEditDialog(viewModel, tvName, tvEmail)
            }
        }

        layout.addView(avatar)
        layout.addView(tvName)
        layout.addView(tvEmail)
        layout.addView(btnEdit)
        (root as? FrameLayout)?.addView(layout)
        return root
    }

    private fun showEditDialog(viewModel: ProfileViewModel, tvName: TextView, tvEmail: TextView) {
        val user = viewModel.user
        if (user == null) return
        val view = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
        }
        val etName = EditText(requireContext())
        etName.setText(user.name)
        val etEmail = EditText(requireContext())
        etEmail.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        etEmail.setText(user.email)
        view.addView(etName)
        view.addView(etEmail)
        AlertDialog.Builder(context)
            .setTitle("Edit Profile")
            .setView(view)
            .setPositiveButton("Save") { _, _ ->
                viewModel.updateProfile(etName.text.toString(), etEmail.text.toString(), null)
                tvName.text = etName.text.toString()
                tvEmail.text = etEmail.text.toString()
                Toast.makeText(context, "Profile updated!", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
