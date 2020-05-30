package robbyhorvath.honorsmobileapps.unsocialmediaapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private List<Post> mPosts;

    public PostAdapter(List<Post> posts) {
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
        holder.bindPost(mPosts.get(position));
    }

    public void setPosts(List<Post> posts) {
        mPosts = posts;
    }

    @Override
    public int getItemCount() {
        if (mPosts != null)
            return mPosts.size();
        return 0;
    }

    public class PostViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView mPostImageView;
        private ImageView mProfilePictureImageView;
        private TextView mUsernameTextView;
        private TextView mPostDescription;
        private TextView mDateTextView;

        private Context context;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            context = itemView.getContext();
            mPostImageView = itemView.findViewById(R.id.post_image);
            mProfilePictureImageView = itemView.findViewById(R.id.post_user_profile_picture);
            mUsernameTextView = itemView.findViewById(R.id.post_username);
            mPostDescription = itemView.findViewById(R.id.post_description);
            mDateTextView = itemView.findViewById(R.id.post_date);
        }

        public void bindPost(Post post) {
            try {
                GlideApp.with(context)
                        .load(FirebaseStorage.getInstance().getReferenceFromUrl("gs://androidfinalproject-e27f1.appspot.com/posts/").child(post.getPostId() + ".jpg"))
                        .into(mPostImageView);
                GlideApp.with(context)
                        .load(FirebaseStorage.getInstance().getReferenceFromUrl("gs://androidfinalproject-e27f1.appspot.com/profile pictures/").child(post.getPosterId() + ".jpg"))
                        .into(mProfilePictureImageView);
                FirebaseDatabase.getInstance().getReference("Users").child(post.getPosterId()).child("username").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String username = dataSnapshot.getValue(String.class);
                        mUsernameTextView.setText(username);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        mUsernameTextView.setText("Error Loading Username");
                    }
                });
                mPostDescription.setText(post.getDescription());
                mDateTextView.setText(post.getTimePosted());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onClick(View view) {

        }
    }
}
