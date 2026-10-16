package com.example.parcialkotlinapp.ui

import androidx.appcompat.app.AppCompatActivity
import com.example.parcialkotlinapp.R
import com.google.android.material.appbar.MaterialToolbar

// =====================================
// 4.8 NAVEGACIÓN UNIFORME
// Todas las pantallas secundarias usan esta función para mostrar
// la flecha superior y regresar a la pantalla anterior.
// La flecha y el botón Atrás del dispositivo hacen exactamente lo mismo.
// =====================================
fun AppCompatActivity.configurarFlechaRegresar(toolbar: MaterialToolbar) {
    toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
    toolbar.setNavigationContentDescription("Regresar")
    toolbar.setNavigationOnClickListener {
        onBackPressedDispatcher.onBackPressed()
    }
}
