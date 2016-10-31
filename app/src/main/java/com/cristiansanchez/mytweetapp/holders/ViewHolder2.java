package com.cristiansanchez.mytweetapp.holders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cristiansanchez.mytweetapp.R;
import com.cristiansanchez.mytweetapp.models.Tweet;

import java.util.List;

/**
 * Created by kristianss27 on 10/27/16.
 */
public class ViewHolder2 extends RecyclerView.ViewHolder implements View.OnClickListener {

    public ImageView ivUserImg;
    public TextView tvUserName;
    public TextView tvTweetText;
    private Context context;
    private List<Tweet> articles;

    public ViewHolder2(View v, Context context, List<Tweet> articles){
        super(v);
        ivUserImg = (ImageView) v.findViewById(R.id.ivUserImg);
        tvUserName = (TextView) v.findViewById(R.id.tvUserName);
        tvTweetText = (TextView) v.findViewById(R.id.tvTweetText);

        // Store the context
        this.context = context;
        this.articles = articles;
        // Attach a click listener to the entire row view
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        /*int position = getAdapterPosition(); // gets item position
        if (position != RecyclerView.NO_POSITION) { // Check if an item was deleted, but the user clicked it before the UI removed it
            Article article = articles.get(position);
            // We can access the data within the views
            Intent intent = new Intent(context, ArticleActivity.class);
            intent.putExtra("article", Parcels.wrap(article));
            v.getContext().startActivity(intent);
            //Toast.makeText(context, article.getHeadline(), Toast.LENGTH_SHORT).show();
        }*/
    }
}
