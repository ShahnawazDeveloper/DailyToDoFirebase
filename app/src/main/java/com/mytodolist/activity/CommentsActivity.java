package com.mytodolist.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.mytodolist.R;
import com.mytodolist.adapter.CommentsAdapter;
import com.mytodolist.models.CommentModel;
import com.mytodolist.utility.DataTypeUtil;
import com.mytodolist.utility.SharedPreference;
import com.mytodolist.utility.ValidationUtil;

import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CommentsActivity extends AppCompatActivity {


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv_comment)
    RecyclerView rvComment;
    @BindView(R.id.et_comment)
    AppCompatEditText etComment;

    FirebaseFirestore mFirestore;
    SharedPreference preference;
    FirestoreRecyclerOptions<CommentModel> options;

    String todoId, title;
    CommentsAdapter commentsAdapter;
    DataTypeUtil dataTypeUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        ButterKnife.bind(this);
        init();
    }

    private void getDataFromIntent() {
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            todoId = extras.getString("todo_id");
            title = extras.getString("title");
        }
    }

    private void init() {
        mFirestore = FirebaseFirestore.getInstance();
        preference = SharedPreference.getInstance();
        dataTypeUtil = DataTypeUtil.getInstance();
        getDataFromIntent();
        setupToolbar();
        getComments();
    }

    private void setupToolbar() {
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void getComments() {
        Query query = mFirestore.collection("todo/" + todoId + "/comments")
                .orderBy("created_time", Query.Direction.ASCENDING);

        options = new FirestoreRecyclerOptions.Builder<CommentModel>()
                .setQuery(query, CommentModel.class)
                .build();

        setCommentsAdapter();
    }

    private void setCommentsAdapter() {
        if (commentsAdapter == null) {
            commentsAdapter = new CommentsAdapter(options, CommentsActivity.this);
            //mAdapter.setHeaderDecoration();
        }

        if (rvComment.getAdapter() == null) {
            rvComment.setHasFixedSize(false);
            rvComment.setLayoutManager(new LinearLayoutManager(this));
            rvComment.setAdapter(commentsAdapter);
            rvComment.setFocusable(false);
            rvComment.setNestedScrollingEnabled(false);
        }
        commentsAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        commentsAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (commentsAdapter != null) {
            commentsAdapter.stopListening();
        }
    }

    @OnClick(R.id.iv_send)
    public void comment() {
        if (isValid()) {
            DocumentReference documentReference = mFirestore.collection("todo/" + todoId + "/comments").document();
            CommentModel user = new CommentModel(documentReference.getId(),
                    preference.getUserDetails().getUid(),
                    etComment.getText().toString().trim(),
                    preference.getUserDetails().getFirstName(),
                    preference.getUserDetails().getLastName(),
                    new Timestamp(new Date())
            );
            etComment.setText("");
            documentReference.set(user)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            etComment.setText("");
                            Toast.makeText(CommentsActivity.this, "Authentication Success.",
                                    Toast.LENGTH_LONG).show();
                            //finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(CommentsActivity.this, "Something went wrong please try again.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    });
        }
    }

    private boolean isValid() {
        if (ValidationUtil.validateEmptyEditText(etComment)) {
            // TODO: 2019-06-26  change message
            dataTypeUtil.showToast(CommentsActivity.this, getString(R.string.err_msg_enter_comment));
            ValidationUtil.requestFocus(CommentsActivity.this, etComment);
            return false;
        }
        return true;
    }

}
