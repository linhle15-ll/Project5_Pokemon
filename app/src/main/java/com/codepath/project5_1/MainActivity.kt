import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.JsonHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import android.util.Log
import com.codepath.project5_1.R

class MainActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize views
        imageView = findViewById(R.id.imageView)
        val button: Button = findViewById(R.id.button)

        // Load random Pokemon image on activity creation
        loadRandomPokemonImage()

        // Set button click listener
        button.setOnClickListener {
            loadRandomPokemonImage()
        }
    }

    private fun loadRandomPokemonImage() {
        val client = AsyncHttpClient()
        val apiUrl = "https://pokeapi.co/api/v2/pokemon-species/${(1..807).random()}"
        client.get(apiUrl, object : JsonHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, response: JSONObject?) {
                super.onSuccess(statusCode, headers, response)
                response?.let {
                    if (it.has("sprites")) {
                        val sprites = it.getJSONObject("sprites")
                        val imageUrl = sprites.optString("front_default", "")
                        if (imageUrl.isNotEmpty()) {
                            Log.d("Pokemon", "Random Pokemon image URL: $imageUrl")
                            Glide.with(this@MainActivity)
                                .load(imageUrl)
                                .fitCenter()
                                .into(imageView)
                        } else {
                            Log.e("Pokemon", "No image URL found in response")
                            Toast.makeText(this@MainActivity, "No image found", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Log.e("Pokemon", "Sprites object not found in response")
                        Toast.makeText(this@MainActivity, "No sprites found", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, throwable: Throwable?, errorResponse: JSONObject?) {
                super.onFailure(statusCode, headers, throwable, errorResponse)
                Log.e("Pokemon Error", errorResponse?.toString() ?: "Unknown error")
                Toast.makeText(this@MainActivity, "Failed to load image", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
