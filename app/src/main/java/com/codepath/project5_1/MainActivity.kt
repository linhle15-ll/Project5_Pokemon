
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import okhttp3.*
import org.json.JSONArray
import java.io.IOException
import java.util.*
import com.codepath.project5_1.R

class MainActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private var imageUrls: List<String> = emptyList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imageView = findViewById(R.id.imageView)
        val button: Button = findViewById(R.id.button)

        button.setOnClickListener {
            loadRandomImage()
        }

        fetchImageUrls()
    }

    private fun fetchImageUrls() {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("https://raw.githubusercontent.com/Biuni/PokemonGO-Pokedex/master/pokedex.json")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Handle failure
            }

            override fun onResponse(call: Call, response: Response) {
                val jsonString = response.body?.string()
                jsonString?.let {
                    imageUrls = parseJson(it)
                    loadRandomImage()
                }
            }
        })
    }

    private fun parseJson(jsonString: String): List<String> {
        val urls = mutableListOf<String>()
        val jsonArray = JSONArray(jsonString)
        for (i in 0 until jsonArray.length()) {
            val jsonObject = jsonArray.getJSONObject(i)
            val imageUrl = jsonObject.getJSONObject("img").getString("url")
            urls.add(imageUrl)
        }
        return urls
    }

    private fun loadRandomImage() {
        if (imageUrls.isEmpty()) {
            // Handle case when imageUrls is empty
            return
        }

        val random = Random()
        val randomIndex = random.nextInt(imageUrls.size)
        val imageUrl = imageUrls[randomIndex]

        Glide.with(this)
            .load(imageUrl)
            .into(imageView)
    }
}
