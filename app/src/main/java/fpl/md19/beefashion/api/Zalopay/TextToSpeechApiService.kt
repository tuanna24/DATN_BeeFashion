package fpl.md19.beefashion.api.Zalopay


import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface OpenAIApi {
    @Headers("Content-Type: application/json")
    @POST("v1/audio/speech")
    fun convertTextToSpeech(
        @Header("Authorization") apiKey: String,
        @Body requestBody: TextToSpeechRequest
    ): Call<TextToSpeechResponse>
}

object ApiTTSService {
    private const val OPENAI_URL = "https://api.openai.com/"

    val api: OpenAIApi by lazy {
        Retrofit.Builder()
            .baseUrl(OPENAI_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OpenAIApi::class.java)
    }
}

data class TextToSpeechRequest(
    val input: String,
    val voice: String = "alloy",
    val model: String = "tts-1"
)

data class TextToSpeechResponse(
    val audio_url: String?
)