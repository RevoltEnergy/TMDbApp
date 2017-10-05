package com.pk.tmdbapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.github.ybq.android.spinkit.style.ThreeBounce;
import com.pk.tmdbapp.R;
import com.pk.tmdbapp.activities.DetailActivity;
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
                        .listener(new RequestListener<Drawable>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                viewHolder.progressBar.setVisibility(View.GONE);
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                viewHolder.progressBar.setVisibility(View.GONE);
                                return false;
                            }
                        })
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
        ProgressBar progressBar;
        ThreeBounce threeBounce;

        MyViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            userRating = (TextView) itemView.findViewById(R.id.user_rating);
            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            progressBar = (ProgressBar) itemView.findViewById(R.id.progress);
            threeBounce = new ThreeBounce();
            progressBar.setIndeterminateDrawable(threeBounce);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    MovieModel clickedDataItem = movieList.get(position);
                    Intent intent = new Intent(mContext, DetailActivity.class);
                    intent.putExtra("title", movieList.get(position).getTitle());
                    intent.putExtra("original_title", movieList.get(position).getOriginalTitle());
                    intent.putExtra("poster_path", movieList.get(position).getPosterPath());
                    intent.putExtra("overview", movieList.get(position).getOverview());
                    intent.putExtra("vote_average", Double.toString(movieList.get(position).getVoteAverage()));
                    intent.putExtra("release_date", movieList.get(position).getReleaseDate());
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                    Toast.makeText(v.getContext(), "You clicked " + clickedDataItem.getOriginalTitle(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
