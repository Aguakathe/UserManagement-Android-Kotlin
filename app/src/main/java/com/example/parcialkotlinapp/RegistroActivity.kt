package com.example.parcialkotlinapp

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.parcialkotlinapp.api.ApiClient
import com.example.parcialkotlinapp.models.CrearUsuarioRequest
import com.example.parcialkotlinapp.models.Usuario
import com.example.parcialkotlinapp.utils.FechaUtils
import com.google.android.material.appbar.MaterialToolbar
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Calendar

class RegistroActivity : AppCompatActivity() {

    // COMPONENTES
    private lateinit var etNombre: EditText
    private lateinit var etApellido: EditText
    private lateinit var etCorreo: EditText
    private lateinit var etPassword: EditText
    private lateinit var etFechaNacimiento: EditText
    private lateinit var tvEdadCalculada: TextView
    private lateinit var etUniversidad: EditText
    private lateinit var spSemestre: Spinner
    private lateinit var btnRegistrar: Button
    private lateinit var tvMensaje: TextView

    // Fecha seleccionada en el DatePicker (null = no seleccionada)
    private var fechaNacimiento: Calendar? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_registro)

        etNombre = findViewById(R.id.etNombre)
        etApellido = findViewById(R.id.etApellido)
        etCorreo = findViewById(R.id.etCorreoRegistro)
        etPassword = findViewById(R.id.etPasswordRegistro)
        etFechaNacimiento = findViewById(R.id.etFechaNacimiento)
        tvEdadCalculada = findViewById(R.id.tvEdadCalculada)
        etUniversidad = findViewById(R.id.etUniversidad)
        spSemestre = findViewById(R.id.spSemestre)
        btnRegistrar = findViewById(R.id.btnRegistrar)
        tvMensaje = findViewById(R.id.tvMensajeRegistro)


        // =====================================
        // FLECHA SUPERIOR: regresar a la pantalla anterior
        // =====================================
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbarRegistro)

        toolbar.setNavigationOnClickListener {
            finish()
        }

        configurarSemestres()

        // =====================================
        // FECHA DE NACIMIENTO -> DatePicker
        // =====================================
        etFechaNacimiento.setOnClickListener {
            mostrarDatePicker()
        }

        btnRegistrar.setOnClickListener {
            registrarUsuario()
        }
    }

    // =====================================
    // SEMESTRES VÁLIDOS: 1 a 10
    // La posición del Spinner coincide con el semestre
    // (posición 0 = "Seleccione el semestre")
    // =====================================
    private fun configurarSemestres() {

        val semestres =
            listOf("Seleccione el semestre") +
                    (1..10).map { "Semestre $it" }

        val adaptador = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            semestres
        )

        adaptador.setDropDownViewResource(
            android.R.layout.simple_spinner_dropdown_item
        )

        spSemestre.adapter = adaptador

    }

    // =====================================
    // DATEPICKER
    // =====================================
    private fun mostrarDatePicker() {

        // Si no hay fecha, se abre 18 años atrás como sugerencia
        val base = fechaNacimiento
            ?: Calendar.getInstance().apply {
                add(Calendar.YEAR, -18)
            }

        val datePicker = DatePickerDialog(
            this,
            { _, anio, mes, dia ->

                val seleccion =
                    FechaUtils.crearFecha(anio, mes, dia)

                fechaNacimiento = seleccion

                etFechaNacimiento.setText(
                    FechaUtils.formatoVisual(seleccion)
                )

                etFechaNacimiento.error = null

                // La edad se calcula, no se ingresa
                val edad = FechaUtils.calcularEdad(seleccion)

                tvEdadCalculada.text = "Edad: $edad años"
            },
            base.get(Calendar.YEAR),
            base.get(Calendar.MONTH),
            base.get(Calendar.DAY_OF_MONTH)
        )

        // No se permiten fechas futuras
        datePicker.datePicker.maxDate =
            System.currentTimeMillis()

        datePicker.show()
    }

    // =====================================
    // REGISTRAR
    // =====================================
    private fun registrarUsuario() {

        val nombre = etNombre.text.toString().trim()
        val apellido = etApellido.text.toString().trim()
        val correo = etCorreo.text.toString().trim()
        val password = etPassword.text.toString().trim()
        val universidad = etUniversidad.text.toString().trim()
        val semestre = spSemestre.selectedItemPosition
        val fecha = fechaNacimiento

        // VALIDACIONES
        if (nombre.isBlank()) {
            etNombre.error = "Ingrese el nombre"
            return
        }

        if (apellido.isBlank()) {
            etApellido.error = "Ingrese el apellido"
            return
        }

        if (correo.isBlank()) {
            etCorreo.error = "Ingrese el correo"
            return
        }

        if (!android.util.Patterns.EMAIL_ADDRESS
                .matcher(correo)
                .matches()) {

            etCorreo.error = "Correo no válido"
            return
        }

        if (password.isBlank()) {
            etPassword.error = "Ingrese la contraseña"
            return
        }

        // La API exige mínimo 6 caracteres
        if (password.length < 6) {
            etPassword.error = "La contraseña debe tener mínimo 6 caracteres"
            return
        }

        if (fecha == null) {
            etFechaNacimiento.error = "Seleccione la fecha de nacimiento"
            mostrarToast("Seleccione la fecha de nacimiento")
            return
        }

        if (universidad.isBlank()) {
            etUniversidad.error = "Ingrese la universidad"
            return
        }

        if (semestre !in 1..10) {
            mostrarToast("Seleccione un semestre entre 1 y 10")
            return
        }

        // OBJETO QUE ENVIAREMOS AL BACKEND
        val nuevoUsuario = CrearUsuarioRequest(
            nombre = nombre,
            apellido = apellido,
            correo = correo,
            password = password,
            fechaNacimiento = FechaUtils.formatoApi(fecha),
            universidad = universidad,
            semestre = semestre
        )

        tvMensaje.text = "Registrando usuario..."

        // Evitar varios clics mientras responde la API
        btnRegistrar.isEnabled = false

        // LLAMADA AL BACKEND
        ApiClient.usuarioApi
            .crearUsuario(nuevoUsuario)
            .enqueue(object : Callback<Usuario> {

                override fun onResponse(
                    call: Call<Usuario>,
                    response: Response<Usuario>
                ) {

                    btnRegistrar.isEnabled = true

                    if (response.isSuccessful) {

                        val usuarioCreado = response.body()

                        tvMensaje.text =
                            "Usuario registrado correctamente"

                        Toast.makeText(
                            this@RegistroActivity,
                            "Usuario creado. ID: ${usuarioCreado?.id}",
                            Toast.LENGTH_LONG
                        ).show()

                    } else {

                        tvMensaje.text =
                            "Error del servidor: ${response.code()}"
                    }
                }

                override fun onFailure(
                    call: Call<Usuario>,
                    t: Throwable
                ) {

                    btnRegistrar.isEnabled = true

                    tvMensaje.text =
                        "Error de conexión: ${t.message}"
                }
            })
    }

    private fun mostrarToast(mensaje: String) {
        Toast.makeText(this, mensaje, Toast.LENGTH_SHORT).show()
    }
}