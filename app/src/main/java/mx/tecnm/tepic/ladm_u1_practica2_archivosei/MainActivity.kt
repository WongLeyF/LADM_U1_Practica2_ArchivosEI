package mx.tecnm.tepic.ladm_u1_practica2_archivosei

import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*

class MainActivity : AppCompatActivity() {
    private var fileN = ""
    private var txt = ""
    private var extdata = listOf<String>()
    private var message = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        save.setOnClickListener {
            fileN = name.text.toString()
            txt = txtFr.text.toString()
            if (rbIn.isChecked){
                if (saveInternal()){
                    message+="Se guardo correctamente"
                    AlertDialog.Builder(this).setTitle("Info").setMessage(message).setPositiveButton("Aceptar"){
                                d, _ ->d.dismiss()
                    }.show()
                    txtFr.setText("")
                    name.setText("")
                }else{
                    message+="No se lograron guardar los datos"
                    AlertDialog.Builder(this).setTitle("Info").setMessage(message).setPositiveButton("Aceptar"){
                                d, _ ->d.dismiss()
                    }.show()
                }
                message=""
            }
            if (rbEx.isChecked){
                if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 0 )
                }
                if (saveExternal()){
                    AlertDialog.Builder(this).setTitle("Info").setMessage(message).setPositiveButton("Aceptar"){
                                d, _ ->d.dismiss()
                    }.show()
                    txtFr.setText("")
                    name.setText("")
                }else{
                    AlertDialog.Builder(this).setTitle("Info").setMessage(message).setPositiveButton("Aceptar"){
                                d, _ ->d.dismiss()
                    }.show()
                }
                message=""
            }
        }


        open.setOnClickListener {
            fileN = name.text.toString()
            var data = openInternal()
            if (rbIn.isChecked){
                if (!data.isNullOrEmpty()){
                    for(i in data){
                        txtFr.setText(txtFr.text.toString()+"\n"+i)
                    }
                    AlertDialog.Builder(this).setTitle("Info").setMessage(message).setPositiveButton("Aceptar"){
                            d, _ ->d.dismiss()
                    }.show()
                }else{
                    AlertDialog.Builder(this).setTitle("Info").setMessage(message).setPositiveButton("Aceptar"){
                            d, _ ->d.dismiss()
                    }.show()
                    txtFr.setText("")
                }
                message=""
            }
            if (rbEx.isChecked){
                if (openExternal()){
                    AlertDialog.Builder(this).setTitle("Info").setMessage(message).setPositiveButton("Aceptar"){
                            d, _ ->d.dismiss()
                    }.show()
                }else{
                    AlertDialog.Builder(this).setTitle("Info").setMessage(message).setPositiveButton("Aceptar"){
                            d, _ ->d.dismiss()
                    }.show()
                    txtFr.setText("")
                }
                message=""
            }
        }
    }

    private fun saveInternal(): Boolean{
        try {
            var fs = OutputStreamWriter(openFileOutput(fileN, MODE_PRIVATE))
            fs.write(txt)
            fs.flush()
            fs.close()
        }catch (io: IOException){
            return false
        }
        return true
    }

    private fun openInternal():List<String>{
        var line= listOf<String>()
        if (fileList().contains(fileN)){
            try {
                val fe = InputStreamReader(openFileInput(fileN))
                val br = BufferedReader(fe)
                line = br.readLines()
                br.close()
                fe.close()
                message="Datos cargados correctamente"
            }catch (io : IOException){
                message="Error al cargar los datos"
                return emptyList()
            }
        }
        return line
    }

    private fun saveExternal():Boolean{
        try
        {
            if(Environment.getExternalStorageState()!=Environment.MEDIA_MOUNTED)
            {
                message="No se detectó memoria externa"
                return false
            }
//            var file = nombrear.text.toString()
//            var texto = frase.text.toString()
            var pathSD=Environment.getExternalStorageDirectory()
            var fileSD=File(pathSD.absolutePath,fileN)
            var fs=OutputStreamWriter(FileOutputStream(fileSD))
            if(fileSD.exists())
            {
                message="El archivo se sobreescribió"
            }
            fs.write(txt)
            fs.flush()
            fs.close()
        }
        catch (io:Exception)
        {
            message="No se pudo guardar"
            return false
        }
        return true
    }

    private fun openExternal():Boolean{
        try
        {
            if(Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED)
            {
                message="Error de lectura de memoria externa"
                return false
            }
            var pathSD=Environment.getExternalStorageDirectory()
            var fileSD=File(pathSD.absolutePath,fileN)
            if(fileSD.exists())
            {
                val fi = InputStreamReader(FileInputStream(fileSD))
                val br = BufferedReader(fi)
                extdata = br.readLines()
                br.close()
                fi.close()
                message="Datos cargados correctamente"
            }
            else
            {
                message="Error al cargar los datos"
            }
        }
        catch (IO: java.lang.Exception)
        {
            message="Error en el archivo"
            return false
        }
        return true
    }
}