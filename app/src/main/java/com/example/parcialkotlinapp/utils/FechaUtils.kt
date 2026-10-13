package com.example.parcialkotlinapp.utils

import java.util.Calendar
import java.util.Locale

object FechaUtils {

        // =====================================
        // CALCULAR EDAD
        // Considera día, mes y año: si en el año actual
        // aún no ha llegado el cumpleaños, se resta 1.
        // =====================================
        fun calcularEdad(
                nacimiento: Calendar,
                hoy: Calendar = Calendar.getInstance()
                    ): Int {

                var edad =
                        hoy.get(Calendar.YEAR) - nacimiento.get(Calendar.YEAR)

                val mesHoy = hoy.get(Calendar.MONTH)
                val mesNacimiento = nacimiento.get(Calendar.MONTH)

                val aunNoCumple =
                        mesHoy < mesNacimiento ||
                                    (mesHoy == mesNacimiento &&
                                                hoy.get(Calendar.DAY_OF_MONTH) <
                                                nacimiento.get(Calendar.DAY_OF_MONTH))

                if (aunNoCumple) {
                        edad--
                    }

                return edad
            }

        // Formato que se muestra al usuario: dd/MM/yyyy
        fun formatoVisual(fecha: Calendar): String =
               String.format(
                        Locale.getDefault(),
                        "%02d/%02d/%04d",
                        fecha.get(Calendar.DAY_OF_MONTH),
                        fecha.get(Calendar.MONTH) + 1,
                        fecha.get(Calendar.YEAR)
                           )

        // Formato que se envía a la API: yyyy-MM-dd
       fun formatoApi(fecha: Calendar): String =
                String.format(
                        Locale.US,
                        "%04d-%02d-%02d",
                        fecha.get(Calendar.YEAR),
                        fecha.get(Calendar.MONTH) + 1,
                        fecha.get(Calendar.DAY_OF_MONTH)
                            )

        fun crearFecha(anio: Int, mes: Int, dia: Int): Calendar =
                Calendar.getInstance().apply {
                        clear()
                        set(anio, mes, dia)
                    }

}