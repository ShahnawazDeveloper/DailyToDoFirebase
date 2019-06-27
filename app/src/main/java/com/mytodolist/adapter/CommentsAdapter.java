package com.mytodolist.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.mytodolist.R;
import com.mytodolist.models.CommentModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommentsAdapter extends FirestoreRecyclerAdapter<CommentModel, CommentsAdapter.CommentViewHolder> {

    private Context context;

    private AdapterView.OnItemClickListener onItemClickListener;
    SimpleDateFormat dateFormat;

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public CommentsAdapter(@NonNull FirestoreRecyclerOptions<CommentModel> options, Context context) {
        super(options);
        this.context = context;
        dateFormat = new SimpleDateFormat("MMMM dd, yyyy hh:mm a", Locale.getDefault());
    }

    @Override
    protected void onBindViewHolder(@NonNull CommentViewHolder productViewHolder, int i, @NonNull CommentModel commentModel) {
        productViewHolder.setData(commentModel);
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_comment, parent, false);
        return new CommentViewHolder(view, this);
    }

    @Override
    public void onError(FirebaseFirestoreException e) {
        Log.e("error", e.getMessage());
        // Toast.makeText(ToDoListingActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    class CommentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.tv_user_name)
        AppCompatTextView tv_user_name;
        @BindView(R.id.tv_user_comment)
        AppCompatTextView tv_user_comment;
        @BindView(R.id.tv_time)
        AppCompatTextView tv_time;

        private CommentsAdapter mAdapter;

        CommentViewHolder(View itemView, CommentsAdapter mAdapter) {
            super(itemView);
            this.mAdapter = mAdapter;
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        void setData(CommentModel productName) {
            tv_user_name.setText(productName.getFirstName() + " " + productName.getLastName());
            tv_user_comment.setText(productName.getComment());
            tv_time.setText(dateFormat.format(new Date(productName.getCreated_time().getSeconds() * 1000L)));
        }

        @Override
        public void onClick(View view) {
            mAdapter.onItemHolderClick(CommentViewHolder.this);
        }

    }

    void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private void onItemHolderClick(CommentViewHolder holder) {
        if (onItemClickListener != null)
            onItemClickListener.onItemClick(null, holder.itemView, holder.getAdapterPosition(), holder.getItemId());
    }
}
