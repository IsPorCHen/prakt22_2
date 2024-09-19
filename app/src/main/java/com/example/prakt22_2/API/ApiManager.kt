package com.example.prakt22_2.API

import android.content.Context
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley

class ApiManager(context: Context) {

    private val requestQueue = Volley.newRequestQueue(context)

    fun fetchDataFromApi(input: String, onSuccess: (String) -> Unit, onError: (String) -> Unit) {
        val url = getApiUrl(input)

        val stringRequest = StringRequest(
            Request.Method.GET, url,
            { response ->
                onSuccess(response)
            },
            { error ->
                onError(error.localizedMessage ?: "Ошибка сети")
            }
        )

        requestQueue.add(stringRequest)
    }

    private fun getApiUrl(input: String): String {
        return "https://pokeapi.co/api/v2/pokemon/$input"
    }
}