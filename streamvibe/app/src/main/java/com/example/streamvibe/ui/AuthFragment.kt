package com.example.streamvibe.ui

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.streamvibe.R
import com.example.streamvibe.repository.AuthRepository

// PUBLIC_INTERFACE
class AuthViewModel : ViewModel() {
    var isLoginMode = true
    var errorMsg: String? = null

    fun login(context: android.content.Context, email: String, password: String): Boolean {
        val ok = AuthRepository.login(context, email, password)
        if (!ok) errorMsg = "Invalid credentials"
        else errorMsg = null
        return ok
    }

    fun signUp(context: android.content.Context, name: String, email: String, password: String): Boolean {
        val ok = AuthRepository.signUp(context, name, email, password)
        if (!ok) errorMsg = "Account already exists"
        else errorMsg = null
        return ok
    }
}

// PUBLIC_INTERFACE
class AuthFragment : Fragment() {
    private lateinit var viewModel: AuthViewModel
    private lateinit var errorTv: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_auth, container, false)
        viewModel = ViewModelProvider(this)[AuthViewModel::class.java]
        val layout = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.VERTICAL
            gravity = android.view.Gravity.CENTER
            setPadding(52, 90, 52, 0)
        }
        errorTv = TextView(requireContext()).apply {
            setTextColor(resources.getColor(R.color.secondary))
        }
        layout.addView(errorTv)

        val etName = EditText(requireContext()).apply {
            hint = "Name"
            setPadding(18, 18, 18, 18)
            visibility = View.GONE
        }
        val etEmail = EditText(requireContext()).apply {
            hint = "Email"
            setPadding(18, 18, 18, 18)
        }
        val etPassword = EditText(requireContext()).apply {
            hint = "Password"
            setPadding(18, 18, 18, 18)
            inputType = android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD or android.text.InputType.TYPE_CLASS_TEXT
        }
        val btnAction = Button(requireContext()).apply {
            text = "Login"
        }
        val tvToggle = TextView(requireContext()).apply {
            text = "No account? Sign up"
            setPadding(0, 28, 0, 0)
            setTextColor(resources.getColor(R.color.secondary))
        }

        layout.addView(etName)
        layout.addView(etEmail)
        layout.addView(etPassword)
        layout.addView(btnAction)
        layout.addView(tvToggle)

        fun updateUi() {
            if (viewModel.isLoginMode) {
                etName.visibility = View.GONE
                btnAction.text = "Login"
                tvToggle.text = "No account? Sign up"
            } else {
                etName.visibility = View.VISIBLE
                btnAction.text = "Sign Up"
                tvToggle.text = "Already signed up? Login"
            }
            errorTv.text = viewModel.errorMsg ?: ""
        }

        btnAction.setOnClickListener {
            val context = requireContext()
            val email = etEmail.text.toString()
            val password = etPassword.text.toString()
            if (viewModel.isLoginMode) {
                val ok = viewModel.login(context, email, password)
                if (ok) {
                    Toast.makeText(context, "Logged in!", Toast.LENGTH_SHORT).show()
                }
            } else {
                val name = etName.text.toString()
                val ok = viewModel.signUp(context, name, email, password)
                if (ok) {
                    Toast.makeText(context, "Signed up!", Toast.LENGTH_SHORT).show()
                }
            }
            updateUi()
        }
        tvToggle.setOnClickListener {
            viewModel.isLoginMode = !viewModel.isLoginMode
            updateUi()
        }
        updateUi()

        (root as? FrameLayout)?.addView(layout)
        return root
    }
}
