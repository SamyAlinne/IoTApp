package mx.tecnm.cdhidalgo.iotapp

import android.app.AlertDialog
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.StringRequest

class MainActivity2 : AppCompatActivity(), ItemListener {
    private lateinit var rvList: RecyclerView
    lateinit var sesion: SharedPreferences
    lateinit var lista: Array<Array<String?>>
    lateinit var btnAdd: Button
    lateinit var btnRefresh: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        rvList = findViewById(R.id.rvList)
        btnAdd = findViewById(R.id.btnAdd)
        btnRefresh = findViewById(R.id.btnRefresh)

        sesion = getSharedPreferences("sesion", 0)

        rvList.setHasFixedSize(true)
        rvList.itemAnimator = DefaultItemAnimator()
        rvList.layoutManager = LinearLayoutManager(this)

        fill()

        btnAdd.setOnClickListener { startActivity(Intent(this, MainActivity3::class.java)) }
        btnRefresh.setOnClickListener { fill() }
    }
        private fun fill() {
        //Como buena practica debe ser así
        val url = Uri.parse(Config.URL + "sensors")
            .buildUpon()
            .build().toString()

        val peticion = object : JsonObjectRequest(Request.Method.GET, url, null, {
                response -> val data = response.getJSONArray("data")
                //Log.e("Lista Sensores", response.toString()) --> Otra forma
                lista = Array(data.length()){ arrayOfNulls<String>(5)}
            for (i in 0 until data.length()){
                lista[i][0] = data.getJSONObject(i).getString("id")
                lista[i][1] = data.getJSONObject(i).getString("name")
                lista[i][2] = data.getJSONObject(i).getString("type")
                lista[i][3] = data.getJSONObject(i).getString("value")
                lista[i][4] = data.getJSONObject(i).getString("date")
            }
            rvList.adapter = MyAdapter(lista, this)

        }, {
                error -> Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show()
        }) {
            override fun getHeaders(): MutableMap<String, String> {
                val body: MutableMap<String, String> = HashMap()
                body["Authorization"] = sesion.getString("jwt", "").toString()
                return body
            }
        }
        MySingleton.getInstance(applicationContext).addToRequestQueue(peticion)
    }

    override fun onClick(v: View?, position: Int) {
        Toast.makeText(this, "Click en $position", Toast.LENGTH_SHORT).show()
    }

    override fun onEdit(v: View?, position: Int) {
        val i = Intent(this, MainActivity3::class.java)
        i.putExtra("id", lista[position][0])
        i.putExtra("name", lista[position][1])
        i.putExtra("type", lista[position][2])
        i.putExtra("value", lista[position][3])
        startActivity(i)
    }

    override fun onDel(v: View?, position: Int) {
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("Eliminar")
            .setMessage("¿Está seguro de eliminar ${lista[position][1]}?")

            .setPositiveButton("Sí"){ dialog, which ->
                val url = Uri.parse(Config.URL + "sensors/" + lista[position][0])
                    .buildUpon()
                    .build().toString()
                val peticion = object:StringRequest(Request.Method.DELETE, url,{
                        response -> fill()
                },{
                        error -> Toast.makeText(this, error.toString(), Toast.LENGTH_LONG).show()
                        fill()
                }){
                    override fun getHeaders(): MutableMap<String, String> {
                        val body: MutableMap<String, String> = HashMap()
                        body["Authorization"] = sesion.getString("jwt", "").toString()
                        return body
                    }
                }
                MySingleton.getInstance(applicationContext).addToRequestQueue(peticion)
            }

            .setNegativeButton("No", null)
            .show()

    }
}
