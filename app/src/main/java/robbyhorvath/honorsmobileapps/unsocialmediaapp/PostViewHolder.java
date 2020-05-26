package robbyhorvath.honorsmobileapps.unsocialmediaapp;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

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
            String username = FirebaseDatabase.getInstance().getReference("Users").child(post.getPosterId()).child("username").toString();
            mUsernameTextView.setText(username);
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
