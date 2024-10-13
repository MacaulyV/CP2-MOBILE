package com.daniel.cineverse

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

class DetalheSalvos : AppCompatActivity() {

    companion object {
        val savedMovies = mutableListOf<Movie>()
    }

    private val selectedMovies = mutableListOf<Movie>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalhe_salvos)

        val movie: Movie? = intent.getParcelableExtra("movie")

        val movieNameTextView: TextView = findViewById(R.id.movie_name)
        val movieImageView: ImageView = findViewById(R.id.movie_image)
        val btnSalvar: Button = findViewById(R.id.btnSalvar)
        val btnVoltar: Button = findViewById(R.id.btnVoltar)
        val btnExcluir: Button = findViewById(R.id.btnExcluir)
        val btnSendFormData: Button = findViewById(R.id.btnFormulario)
        val editTextUserComment: EditText = findViewById(R.id.editTextUserComment)
        val spinnerMovieGenre: Spinner = findViewById(R.id.spinnerMovieGenre)

        val genres = arrayOf("Ação", "Comédia", "Drama", "Ficção Científica", "Terror")
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, genres)
        spinnerMovieGenre.adapter = spinnerAdapter

        movie?.let { selectedMovie ->
            movieNameTextView.text = selectedMovie.name
            movieImageView.setImageResource(selectedMovie.imageResId)

            btnSalvar.setOnClickListener {
                val movieAlreadySaved = savedMovies.any { savedMovie -> savedMovie.name == selectedMovie.name }
                if (!movieAlreadySaved) {
                    selectedMovie.comment = editTextUserComment.text.toString()
                    savedMovies.add(selectedMovie)
                    addMovieToSavedList(selectedMovie)
                    Toast.makeText(this, "Filme salvo com comentário!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Esse filme já está salvo!", Toast.LENGTH_SHORT).show()
                }
            }
        }

        spinnerMovieGenre.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedGenre = genres[position]
                Toast.makeText(this@DetalheSalvos, "Gênero selecionado: $selectedGenre", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        btnSendFormData.setOnClickListener {
            val comment = editTextUserComment.text.toString().trim()
            if (comment.isNotEmpty()) {
                Toast.makeText(this, "Comentário enviado: $comment", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Por favor, insira um comentário.", Toast.LENGTH_SHORT).show()
            }
        }

        btnExcluir.setOnClickListener {
            deleteSelectedMovies()
        }

        btnVoltar.setOnClickListener {
            finish()
        }

        showSavedMovies()
    }

    private fun showSavedMovies() {
        val savedMoviesLayout: LinearLayout = findViewById(R.id.saved_movies_layout)
        savedMoviesLayout.removeAllViews()

        for (movie in savedMovies) {
            addMovieToSavedList(movie)
        }
    }

    private fun addMovieToSavedList(movie: Movie) {
        val savedMoviesLayout: LinearLayout = findViewById(R.id.saved_movies_layout)

        val movieLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(16, 0, 16, 0)
            }
        }

        val movieImageView = ImageView(this).apply {
            setImageResource(movie.imageResId)
            layoutParams = LinearLayout.LayoutParams(200, 300)
        }

        val movieNameTextView = TextView(this).apply {
            text = movie.name
            setTextColor(resources.getColor(android.R.color.white))
            textAlignment = TextView.TEXT_ALIGNMENT_CENTER
        }

        val movieCommentTextView = TextView(this).apply {
            text = movie.comment ?: "Sem comentário"
            setTextColor(resources.getColor(android.R.color.darker_gray))
            textAlignment = TextView.TEXT_ALIGNMENT_CENTER
        }

        val movieCheckBox = CheckBox(this).apply {
            setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    selectedMovies.add(movie)
                } else {
                    selectedMovies.remove(movie)
                }
            }
        }

        movieLayout.addView(movieImageView)
        movieLayout.addView(movieNameTextView)
        movieLayout.addView(movieCommentTextView)
        movieLayout.addView(movieCheckBox)

        savedMoviesLayout.addView(movieLayout)
    }

    private fun deleteSelectedMovies() {
        if (selectedMovies.isEmpty()) {
            Toast.makeText(this, "Nenhum filme selecionado para exclusão.", Toast.LENGTH_SHORT).show()
            return
        }

        savedMovies.removeAll(selectedMovies)
        selectedMovies.clear()
        showSavedMovies() // Atualiza a lista visual
        Toast.makeText(this, "Filmes excluídos com sucesso!", Toast.LENGTH_SHORT).show()
    }
}
