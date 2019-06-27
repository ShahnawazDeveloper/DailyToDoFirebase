package com.mytodolist.presenter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.mytodolist.models.CommentModel;
import com.mytodolist.utility.SharedPreference;
import com.mytodolist.utility.ValidationUtil;
import com.mytodolist.view.CommentView;

import java.util.Date;

public class CommentPresenter {

    private CommentView commentView;
    private FirebaseFirestore mFirestore;
    private SharedPreference preference;
    private Context context;
    Intent intent;

    String todoId, title;

    public CommentPresenter(Context context, CommentView commentView, Intent intent) {
        this.context = context;
        this.commentView = commentView;
        mFirestore = FirebaseFirestore.getInstance();
        preference = SharedPreference.getInstance();
        this.intent = intent;
        getDataFromIntent();
    }

    private void getDataFromIntent() {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            todoId = extras.getString("todo_id");
            title = extras.getString("title");
            commentView.setToolbarTitle(title);
        }
    }

    public void getComments() {
        Query query = mFirestore.collection("todo/" + todoId + "/comments")
                .orderBy("created_time", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<CommentModel> options = new FirestoreRecyclerOptions.Builder<CommentModel>()
                .setQuery(query, CommentModel.class)
                .build();

        // setCommentsAdapter();
        commentView.onGetDataSuccess(options);
    }

    public void addComment(EditText etComment) {
        if (isValid(etComment)) {
            doRequestForAddComment(etComment);
        }
    }


    private boolean isValid(EditText etComment) {
        if (ValidationUtil.validateEmptyEditText(etComment)) {
            // TODO: 2019-06-26  change message
            commentView.showValidationErrorEmptyComment();
            return false;
        }
        return true;
    }


    private void doRequestForAddComment(EditText etComment) {
        DocumentReference documentReference = mFirestore.collection("todo/" + todoId + "/comments").document();
        CommentModel user = new CommentModel(documentReference.getId(),
                preference.getUserDetails().getUid(),
                etComment.getText().toString().trim(),
                preference.getUserDetails().getFirstName(),
                preference.getUserDetails().getLastName(),
                new Timestamp(new Date())
        );
        commentView.clearCommentText();
        documentReference.set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        commentView.clearCommentText();
                        Log.e("Comment sent = >", " Success");
                        commentView.onSendCommentSuccess();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("Comment sent = >", " Failure");
                        commentView.onSendCommentFailure(e.getMessage());
                    }

                });
    }
}
