package dev.rupeshdeshmukh.locsationapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import dev.rupeshdeshmukh.locsationapp.R
import dev.rupeshdeshmukh.locsationapp.constant.Constant
import dev.rupeshdeshmukh.locsationapp.databinding.ItemMovieBinding
import dev.rupeshdeshmukh.locsationapp.network.data.Movie

class MovieAdapter(private var movies: List<Movie>) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    fun setMovies(newMoviesList: List<Movie>){
        movies=newMoviesList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]
        holder.bind(movie)
    }

    override fun getItemCount(): Int {
        return movies.size
    }

    inner class MovieViewHolder(private val binding: ItemMovieBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Movie) {
            binding.txtMovieName.text = movie.title
            binding.txtReleasedOn.text ="Released on :${movie.release_date}"
            binding.txtOverview.text =movie.overview
            Picasso.get().load(Constant.IMAGE_BASEURL+movie.poster_path).placeholder(R.drawable.ic_launcher_background).into(binding.imgPoster)
        }
    }
}
