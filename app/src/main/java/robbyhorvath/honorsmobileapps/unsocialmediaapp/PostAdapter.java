package robbyhorvath.honorsmobileapps.unsocialmediaapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PostAdapter extends RecyclerView.Adapter<PostViewHolder> {
    private Post[] mPosts;

    public PostAdapter(Post[] posts){
        mPosts = posts;
    }
    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context;
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View rootView = layoutInflater.inflate(R.layout.post_item, parent, false);
        PostViewHolder newPostViewHolder = new PostViewHolder(rootView);
        return newPostViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        holder.bindPost(mPosts[position]);
    }

    @Override
    public int getItemCount() {
        return mPosts.length;
    }
}
