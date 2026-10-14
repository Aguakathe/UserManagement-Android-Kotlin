    package com.example.parcialkotlinapp

    import android.content.Intent
    import android.os.Bundle
    import android.widget.Button
    import android.widget.TextView
    import androidx.activity.enableEdgeToEdge
    import androidx.appcompat.app.AppCompatActivity
    import androidx.core.view.ViewCompat
    import androidx.core.view.WindowInsetsCompat
    import com.google.android.material.appbar.MaterialToolbar
    import com.google.android.material.dialog.MaterialAlertDialogBuilder
    import java.util.Calendar

    class MainActivity : AppCompatActivity() {

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)

            enableEdgeToEdge()

            setContentView(R.layout.activity_main)

            val tvSaludo =findViewById<TextView>(R.id.tvSaludo)

            // Nombre recibido desde LoginActivity
            val  nombreUsuario = intent.getStringExtra(
                "nombreUsuario"
            ) ?: "Usuario"

            //Hora Actual del dispositivo
            val horaActual =
                Calendar.getInstance().get(Calendar.HOUR_OF_DAY)

            //Determinar saludo
            var saludo = when(horaActual){

                in 5..11 ->
                    "Buenos días"

                in 12..17 ->
                    "Buenas tardes"

                else ->
                    "Buenas noches"
            }

            //Mostrar saludo personalizado

            tvSaludo.text = "$saludo, $nombreUsuario"

            // Manejo de los bordes de la pantalla
            ViewCompat.setOnApplyWindowInsetsListener(
                findViewById(R.id.main)
            ) { v, insets ->

                val systemBars =
                    insets.getInsets(
                        WindowInsetsCompat.Type.systemBars()
                    )

                v.setPadding(
                    systemBars.left,
                    systemBars.top,
                    systemBars.right,
                    systemBars.bottom
                )

                insets
            }

            // =====================================
            // BOTÓN EJERCICIO PAR O IMPAR
            // =====================================

            val btnParImpar =
                findViewById<Button>(R.id.btnParImpar)

            btnParImpar.setOnClickListener {

                val intent = Intent(
                    this,
                    ParImparActivity::class.java
                )

                startActivity(intent)
            }

            // =====================================
            // CERRAR SESIÓN (ícono superior)
            // =====================================

            val toolbar =
             findViewById<MaterialToolbar>(R.id.toolbarMain)

            toolbar.setOnMenuItemClickListener { item ->

                when (item.itemId) {

                    R.id.action_cerrar_sesion -> {
                        confirmarCierreSesion()
                        true
                    }

                    else -> false
                }
            }
        }

        // =====================================
        // ALERTDIALOG DE CONFIRMACIÓN
        // =====================================
        private fun confirmarCierreSesion() {

            MaterialAlertDialogBuilder(this)
                .setTitle("Cerrar sesión")
                .setMessage("¿Está seguro de que desea cerrar sesión?")

                // CANCELAR: solo cierra el diálogo
                .setNegativeButton("CANCELAR") { dialog, _ ->
                    dialog.dismiss()
                }

                // SALIR: finaliza la sesión
                .setPositiveButton("SALIR") { _, _ ->
                    cerrarSesion()
                }
                .show()
        }

        private fun cerrarSesion() {

            val intent = Intent(
                this,
                LoginActivity::class.java
            )

            // Limpia la pila de Activities: el botón Atrás
            // ya no puede regresar a pantallas de la sesión anterior
            intent.flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or
                        Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(intent)

            finish()
        }
    }