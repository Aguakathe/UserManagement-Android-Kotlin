package com.example.parcialkotlinapp.utils

import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.Calendar

class FechaUtilsTest {

    // Nota: en Calendar los meses van de 0 (enero) a 11 (diciembre)
    private val hoy = FechaUtils.crearFecha(2026, Calendar.OCTOBER, 7)

    @Test
    fun cumpleanosYaPasoEsteAnio() {
        val nacimiento = FechaUtils.crearFecha(2000, Calendar.MAY, 20)
        assertEquals(26, FechaUtils.calcularEdad(nacimiento, hoy))
    }

    @Test
    fun cumpleanosAunNoLlegaEsteAnio() {
        val nacimiento = FechaUtils.crearFecha(2000, Calendar.DECEMBER, 1)
        assertEquals(25, FechaUtils.calcularEdad(nacimiento, hoy))
    }

    @Test
    fun mismoMesDiaPosterior() {
        val nacimiento = FechaUtils.crearFecha(2000, Calendar.OCTOBER, 8)
        assertEquals(25, FechaUtils.calcularEdad(nacimiento, hoy))
    }

    @Test
    fun cumpleanosEsHoy() {
        val nacimiento = FechaUtils.crearFecha(2000, Calendar.OCTOBER, 7)
        assertEquals(26, FechaUtils.calcularEdad(nacimiento, hoy))
    }

    @Test
    fun formatoApiEsAnioMesDia() {
        val fecha = FechaUtils.crearFecha(2002, Calendar.MAY, 9)
        assertEquals("2002-05-09", FechaUtils.formatoApi(fecha))
    }
}