package cz.lamorak.wordgame.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cz.lamorak.wordgame.R;
import cz.lamorak.wordgame.model.Highscore;

/**
 * Created by ovancak on 06.04.2017.
 */

public class HighscoreAdapter extends RecyclerView.Adapter<HighscoreAdapter.HighscoreVH> {

    private final List<Highscore> highscores;

    public HighscoreAdapter(final List<Highscore> highscores) {
        this.highscores = highscores;
    }

    @Override
    public HighscoreVH onCreateViewHolder(final ViewGroup parent, final int viewType) {
        final View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_highscore, parent, false);
        return new HighscoreVH(layout);
    }

    @Override
    public void onBindViewHolder(final HighscoreVH holder, final int position) {
        holder.bind(highscores.get(position));
    }

    @Override
    public int getItemCount() {
        return highscores.size();
    }

    static class HighscoreVH extends RecyclerView.ViewHolder{

        private final TextView name;
        private final TextView score;

        public HighscoreVH(final View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.highscore_name);
            score = (TextView) itemView.findViewById(R.id.highscore_value);
        }

        public void bind(final Highscore highscore) {
            name.setText(highscore.getName());
            score.setText(String.valueOf(highscore.getValue()));
        }
    }
}
