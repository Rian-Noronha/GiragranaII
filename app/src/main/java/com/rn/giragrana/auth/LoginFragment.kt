package com.rn.giragrana.auth

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInStatusCodes
import com.google.android.gms.common.api.ApiException
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.auth
import com.rn.giragrana.R
import com.rn.giragrana.databinding.FragmentLoginBinding

@Suppress("DEPRECATION")
class LoginFragment : Fragment() {

    companion object {
        const val GOOGLE_SIGN_IN = 1903
    }

    private lateinit var binding: FragmentLoginBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }


    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        auth = Firebase.auth
        googleSignInClient = GoogleSignIn.getClient(requireContext(), getGoogleSignInOptions())
        binding.btnSignIn.setOnClickListener { signIn() }
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, GOOGLE_SIGN_IN)
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GOOGLE_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                handleSignInError(e)
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    navigateToProductListFragment()
                } else {
                    showAuthErrorToast()
                }
            }
    }

    private fun getGoogleSignInOptions(): GoogleSignInOptions {
        return GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
    }

    private fun navigateToProductListFragment() {
        Navigation.findNavController(requireActivity(), R.id.navHostFragment)
            .navigate(R.id.action_loginFragment_to_fragmentListProduct)
    }

    private fun showAuthErrorToast() {
        Toast.makeText(requireContext(), getString(R.string.auth_error_message), Toast.LENGTH_LONG).show()
    }

    private fun handleSignInError(exception: Exception) {
        when (exception) {
            is ApiException -> {
                when (exception.statusCode) {
                    GoogleSignInStatusCodes.SIGN_IN_CANCELLED -> {
                        Toast.makeText(requireContext(), R.string.login_cancelled, Toast.LENGTH_LONG).show()
                    }
                    GoogleSignInStatusCodes.SIGN_IN_FAILED -> {
                        Toast.makeText(requireContext(), R.string.login_fail, Toast.LENGTH_LONG).show()
                    }
                    else -> {
                        Toast.makeText(requireContext(), R.string.unknown_error, Toast.LENGTH_LONG).show()
                    }
                }
            }
            else -> {
                Toast.makeText(requireContext(), "Erro de autenticação: ${exception.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

}