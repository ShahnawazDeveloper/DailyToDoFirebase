package com.mytodolist.view;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.mytodolist.models.ToDoModel;

public interface ToDoListingView {

    void onGetDataSuccess(FirestoreRecyclerOptions<ToDoModel> list);

    void onGetDataFailure(String message);

    void logoutUser();

}
