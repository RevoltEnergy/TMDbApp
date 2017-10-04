package com.pk.tmdbapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.pk.tmdbapp.activities.DetailActivity;
import com.pk.tmdbapp.R;
import com.pk.tmdbapp.mvp.model.MovieModel;

import java.util.List;

/**
 * Created by ace on 10/02/2017.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MyViewHolder> {

    private Context mContext;
    private List<MovieModel> movieList;

    public MoviesAdapter(Context mContext, List<MovieModel> movieList) {
        this.mContext = mContext;
        this.movieList = movieList;
    }

    @Override
    public MoviesAdapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.movie_card, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MoviesAdapter.MyViewHolder viewHolder, int position) {
        viewHolder.title.setText(movieList.get(position).getOriginalTitle());
        String vote = Double.toString(movieList.get(position).getVoteAverage());
        viewHolder.userRating.setText(vote);
        String baseImageUrl = "https://image.tmdb.org/t/p/w500";

        Glide.with(mContext)
                .load(baseImageUrl + movieList.get(position).getPosterPath())
                .apply(new RequestOptions()
                        .placeholder(R.drawable.load)
                        .error(R.drawable.load))
                .into(viewHolder.thumbnail);
    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView userRating;
        ImageView thumbnail;

        MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            userRating = (TextView) itemView.findViewById(R.id.user_rating);
            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        MovieModel clickedDataItem = movieList.get(position);
                        Intent intent = new Intent(mContext, DetailActivity.class);
                        intent.putExtra("original_title", movieList.get(position).getOriginalTitle());
                        intent.putExtra("poster_path", movieList.get(position).getPosterPath());
                        intent.putExtra("overview", movieList.get(position).getOverview());
                        intent.putExtra("vote_average", Double.toString(movieList.get(position).getVoteAverage()));
                        intent.putExtra("release_date", movieList.get(position).getReleaseDate());
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        mContext.startActivity(intent);
                        Toast.makeText(v.getContext(), "You clicked " + clickedDataItem.getOriginalTitle(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}
