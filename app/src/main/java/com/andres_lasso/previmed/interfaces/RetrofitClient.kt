package com.andres_lasso.previmed.interfaces

import com.andres_lasso.previmed.network.PagosApi
import com.andres_lasso.previmed.network.PagosApiService
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit



object RetrofitClient {

    // 🌐 URL base del backend Previmed
    private const val BASE_URL = "https://previmedbackend-q73n.onrender.com/"

    // 🧾 Interceptor de logs detallado (solo en modo debug)
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    // ⚙️ Cliente HTTP con configuración de tiempo de espera y logs
    private val okHttpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true) // 🔁 se reintenta si falla conexión
            .build()
    }

    // 🧠 Configuración de Gson (snake_case ↔ camelCase)
    private val gson = GsonBuilder()
        .setLenient() // Permite leer JSON flexible
        .serializeNulls() // Acepta valores nulos sin fallar
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES) // Convierte idMedico ↔ id_medico automáticamente
        .create()


    // 🚀 Instancia principal de Retrofit
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    // ----------------------------------------------------------------
    // 🔹 APIs centralizadas: acceso único desde RetrofitClient
    // ----------------------------------------------------------------
    val loginApi: LoginApi by lazy { retrofit.create(LoginApi::class.java) }
    val planesApi: PlanesApi by lazy { retrofit.create(PlanesApi::class.java) }
    val pacienteApi: PacienteApi by lazy { retrofit.create(PacienteApi::class.java) }
    val epsApi: EpsApi by lazy { retrofit.create(EpsApi::class.java) }
    val membresiaApi: MembresiaApi by lazy { retrofit.create(MembresiaApi::class.java) }
    val visitasApi: VisitaService by lazy { retrofit.create(VisitaService::class.java) }
    val pagoApi: PagoApi by lazy { retrofit.create(PagoApi::class.java) }
    val formaPagoApi: FormaPagoApi by lazy {
        retrofit.create(FormaPagoApi::class.java)
    }
    val registerApi: RegisterApi by lazy { retrofit.create(RegisterApi::class.java) }
    val medicoApi: MedicoApi by lazy { retrofit.create(MedicoApi::class.java) }

    val pagosApi: PagosApiService by lazy { retrofit.create(PagosApiService::class.java)
    }
    val beneficiarioApi: BeneficiarioService by lazy {
        retrofit.create(BeneficiarioService::class.java)
    }

}
