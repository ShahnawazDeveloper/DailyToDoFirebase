package com.mytodolist.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mytodolist.R;
import com.mytodolist.adapter.CommentsAdapter;
import com.mytodolist.models.CommentModel;
import com.mytodolist.presenter.CommentPresenter;
import com.mytodolist.utility.DataTypeUtil;
import com.mytodolist.utility.SharedPreference;
import com.mytodolist.utility.ValidationUtil;
import com.mytodolist.view.CommentView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CommentsActivity extends AppCompatActivity implements CommentView {


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

    CommentPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        mFirestore = FirebaseFirestore.getInstance();
        preference = SharedPreference.getInstance();
        dataTypeUtil = DataTypeUtil.getInstance();
        presenter = new CommentPresenter(CommentsActivity.this, this, getIntent());
        presenter.getComments();
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
        presenter.addComment(etComment);
    }

    @Override
    public void setToolbarTitle(String title) {
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onGetDataSuccess(FirestoreRecyclerOptions<CommentModel> list) {
        options = list;
        setCommentsAdapter();
    }

    @Override
    public void onGetDataFailure(String message) {
        Log.e("onGetCommentFailure =>", message);
    }

    @Override
    public void showValidationErrorEmptyComment() {
        dataTypeUtil.showToast(CommentsActivity.this, getString(R.string.err_msg_enter_comment));
        ValidationUtil.requestFocus(CommentsActivity.this, etComment);
    }

    @Override
    public void clearCommentText() {
        etComment.setText("");
    }

    @Override
    public void onSendCommentSuccess() {
        scrollToBottom();
    }

    @Override
    public void onSendCommentFailure(String message) {
        Log.e("onSendCommentFailure =>", message);
    }

    private void scrollToBottom() {
        if (options.getSnapshots().size() > 0) {
            rvComment.scrollToPosition(options.getSnapshots().size() - 1);
        }
    }
}
