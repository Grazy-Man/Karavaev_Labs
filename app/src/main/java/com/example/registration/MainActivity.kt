package com.example.registration

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.registration.R.drawable.dula
import com.example.registration.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var bindingClass: ActivityMainBinding
    private var login: String = "empty"
    private var password: String = "empty"
    private var name: String = "empty"
    private var name2: String = "empty"
    private var name3: String = "empty"
    private var avatarImageId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingClass = ActivityMainBinding.inflate(layoutInflater)
        setContentView(bindingClass.root)

    }

    @Deprecated("This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n      with the appropriate {@link ActivityResultContract} and handling the result in the\n      {@link ActivityResultCallback#onActivityResult(Object) callback}.")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == Constance.REQUEST_CODE_SIGN_IN){
            val l = data?.getStringExtra(Constance.LOGIN)
            val p = data?.getStringExtra(Constance.PASSWORD)
            if(login == l && password == p){

                bindingClass.imAvatar.visibility = View.VISIBLE
                bindingClass.imAvatar.setImageResource(avatarImageId)
                val textInfo = "$name $name2 $name3"
                bindingClass.tvInfo.text = textInfo
                bindingClass.bHide.visibility = View.GONE
                bindingClass.bExit.text = "Вийти"

            } else {

                bindingClass.imAvatar.visibility = View.VISIBLE
                bindingClass.imAvatar.setImageResource(dula)
                bindingClass.tvInfo.text = "Такого акаунта не існує!"

            }

        } else if(requestCode == Constance.REQUEST_CODE_SIGN_UP){

            login = data?.getStringExtra(Constance.LOGIN)!!
            password = data.getStringExtra(Constance.PASSWORD)!!
            name = data.getStringExtra(Constance.NAME)!!
            name2 = data.getStringExtra(Constance.NAME2)!!
            name3 = data.getStringExtra(Constance.NAME3)!!
            avatarImageId = data.getIntExtra(Constance.AVATAR_ID, 0)
            bindingClass.imAvatar.visibility = View.VISIBLE
            bindingClass.imAvatar.setImageResource(avatarImageId)
            val textInfo = "$name $name2 $name3"
            bindingClass.tvInfo.text = textInfo
            bindingClass.bHide.visibility = View.GONE
            bindingClass.bExit.text = "Вийти"

        }
    }

    @Suppress("DEPRECATION")
    fun onClickSignIn(view: View){

        if(bindingClass.imAvatar.isVisible && bindingClass.tvInfo.text.toString() != "Такого акаунта не існує!"){

            bindingClass.imAvatar.visibility = View.INVISIBLE
            bindingClass.tvInfo.text = ""
            bindingClass.bHide.visibility = View.VISIBLE
            bindingClass.bExit.text = getString(R.string.sign_in)

        } else {

            val intent = Intent(this, SignInUpAct::class.java)
            intent.putExtra(Constance.SIGN_STATE, Constance.SIGN_IN_STATE)
            startActivityForResult(intent, Constance.REQUEST_CODE_SIGN_IN)

        }

    }

    @Suppress("DEPRECATION")
    fun onClickSignUp(view: View){

        val intent = Intent(this, SignInUpAct::class.java)
        intent.putExtra(Constance.SIGN_STATE, Constance.SIGN_UP_STATE)
        startActivityForResult(intent, Constance.REQUEST_CODE_SIGN_UP)

    }


}