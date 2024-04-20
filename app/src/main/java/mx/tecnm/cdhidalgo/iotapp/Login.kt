package mx.tecnm.cdhidalgo.iotapp

import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class Login : AppCompatActivity() {

    //Declarar variables
    lateinit var etLoginUser:EditText
    lateinit var etLoginPassword:EditText
    //var etLoginUser: EditText? = null -> ejemplo de otra
    lateinit var btnLoginStart: Button
    lateinit var sesion: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Match con las variables
        etLoginUser = findViewById(R.id.etLoginUser)
        etLoginPassword = findViewById(R.id.etLoginPassword)
        btnLoginStart = findViewById(R.id.btnLoginStart)
        sesion = getSharedPreferences("sesion", 0)

        btnLoginStart.setOnClickListener { login() }

    }

    private fun login() {
        //Como buena practica debe ser así
        val url = Uri.parse(Config.URL + "login")
            .buildUpon()
            .build().toString()

        val peticion = object:StringRequest(Request.Method.POST, url,{
            response -> with(sesion.edit()){ //guarda un par de datos
            putString("jwt", response)
            putString("username",etLoginUser.text.toString())
            apply()

            /* val edicion = sesion.edit()
            * edicion.putString("jwt", response)
            * edicion.putString("username",etLoginUser.text.toString())
            * edicion.apply() */
        }
            startActivity(Intent(this, MainActivity2::class.java))
        },{
            error -> Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show()
        }){
            override fun getParams(): Map<String,String>{
                val body: MutableMap<String,String> = HashMap()
                body["username"] = etLoginUser.text.toString()
                body.put(("password"),etLoginPassword.text.toString())
                return body
            }
        }
        MySingleton.getInstance(applicationContext).addToRequestQueue(peticion)
    }
}