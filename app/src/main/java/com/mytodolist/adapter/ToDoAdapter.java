package com.mytodolist.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.mytodolist.R;
import com.mytodolist.models.ToDoModel;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ToDoAdapter extends FirestoreRecyclerAdapter<ToDoModel, ToDoAdapter.ProductViewHolder> {

    private Context context;

    private AdapterView.OnItemClickListener onItemClickListener;
    private ActionListener mListener;
    private FirestoreRecyclerOptions<ToDoModel> options;
    private SimpleDateFormat dateFormat;

    public interface ActionListener {

        void onChatClick(ToDoModel toDoModel);

        void onCheckBoxClick(ToDoModel toDoModel);

        void onDeleteClick(ToDoModel toDoModel);
    }

    /**
     * Create a new RecyclerView adapter that listens to a Firestore Query.  See {@link
     * FirestoreRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public ToDoAdapter(@NonNull FirestoreRecyclerOptions<ToDoModel> options, Context context, ActionListener mListener) {
        super(options);
        this.options = options;
        this.context = context;
        this.mListener = mListener;
        dateFormat = new SimpleDateFormat("MMMM dd, yyyy hh:mm a", Locale.getDefault());
    }

    @Override
    protected void onBindViewHolder(@NonNull ProductViewHolder productViewHolder, int i, @NonNull ToDoModel toDoModel) {
        productViewHolder.setData(toDoModel);
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_to_do, parent, false);
        return new ProductViewHolder(view, this);
    }

    @Override
    public void onError(FirebaseFirestoreException e) {
        Log.e("error", e.getMessage());
        // Toast.makeText(ToDoListingActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
    }

    class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.et_title)
        AppCompatTextView tv_title;
        @BindView(R.id.tv_desc)
        AppCompatTextView tv_desc;
        @BindView(R.id.tv_date_time)
        AppCompatTextView tv_date_time;
        @BindView(R.id.cb_completed)
        AppCompatCheckBox cb_completed;
        @BindView(R.id.rl_mail)
        RelativeLayout rl_mail;

        private ToDoAdapter mAdapter;

        ProductViewHolder(View itemView, ToDoAdapter mAdapter) {
            super(itemView);
            this.mAdapter = mAdapter;
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        void setData(ToDoModel productName) {
            tv_title.setText(productName.getTitle());
            if (productName.getDescription() != null && !productName.getDescription().isEmpty()) {
                tv_desc.setVisibility(View.VISIBLE);
                tv_desc.setText(productName.getDescription());
            } else {
                tv_desc.setVisibility(View.GONE);
            }

            if (productName.getTime() != null) {
                tv_date_time.setVisibility(View.VISIBLE);
                tv_date_time.setText(dateFormat.format(new Date(productName.getTime().getSeconds() * 1000L)));
            } else {
                tv_date_time.setVisibility(View.GONE);
            }

            if (productName.isCompleted()) {
                cb_completed.setChecked(true);
                rl_mail.setAlpha(0.5f);
            } else {
                cb_completed.setChecked(false);
                rl_mail.setAlpha(1f);
            }
        }

        @Override
        public void onClick(View view) {
            mAdapter.onItemHolderClick(ProductViewHolder.this);
        }

        @OnClick(R.id.iv_chat)
        void chat() {
            if (mListener != null) {
                mListener.onChatClick(options.getSnapshots().get(getAdapterPosition()));
            }
        }

        @OnClick(R.id.cb_completed)
        void onCheckBoxClick() {
            if (mListener != null) {
                ToDoModel toDoModel = options.getSnapshots().get(getAdapterPosition());
                toDoModel.setCompleted(cb_completed.isChecked());
                mListener.onCheckBoxClick(toDoModel);
            }
        }


        @OnClick(R.id.iv_delete)
        void delete() {
            if (mListener != null) {
                mListener.onDeleteClick(options.getSnapshots().get(getAdapterPosition()));
            }
        }
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    private void onItemHolderClick(ProductViewHolder holder) {
        if (onItemClickListener != null)
            onItemClickListener.onItemClick(null, holder.itemView, holder.getAdapterPosition(), holder.getItemId());
    }
}
