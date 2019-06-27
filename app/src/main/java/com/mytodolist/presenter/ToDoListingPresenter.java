package com.mytodolist.presenter;

import android.content.Context;

import androidx.annotation.NonNull;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mytodolist.models.ToDoModel;
import com.mytodolist.utility.SharedPreference;
import com.mytodolist.view.ToDoListingView;

import java.util.Objects;

public class ToDoListingPresenter {

    private ToDoListingView toDoListingView;
    private FirebaseFirestore mFirestore;
    private SharedPreference preference;
    private Context context;

    public ToDoListingPresenter(Context context, ToDoListingView toDoListingView) {
        this.context = context;
        this.toDoListingView = toDoListingView;
        mFirestore = FirebaseFirestore.getInstance();
        preference = SharedPreference.getInstance();
    }

    public void prepareQuery() {
        Query query = mFirestore.collection("todo")
                .whereEqualTo("uid", preference.getUserDetails().getUid())
                .orderBy("time", Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<ToDoModel> options = new FirestoreRecyclerOptions.Builder<ToDoModel>()
                .setQuery(query, ToDoModel.class)
                .build();
        toDoListingView.onGetDataSuccess(options);

    }

    public void setCompleted(ToDoModel toDoModel) {
        mFirestore.collection("todo").document(toDoModel.getTodo_id()).set(toDoModel);
    }

    public void deleteToDo(ToDoModel toDoModel) {
        mFirestore.collection("todo").document(toDoModel.getTodo_id()).delete();
    }


    private void getToDo() {
        mFirestore.collection("todo")
                .whereEqualTo("uid", preference.getUserDetails().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                //   Log.d(TAG, document.getId() + " => " + document.getData());
                                //todoList.add(document.toObject(ToDoModel.class));
                            }
                        } else {
                            // Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    public void logout() {
        preference.clearPreferences();
        toDoListingView.logoutUser();
    }

}
