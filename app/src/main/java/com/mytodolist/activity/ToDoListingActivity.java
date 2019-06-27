package com.mytodolist.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mytodolist.R;
import com.mytodolist.adapter.ToDoAdapter;
import com.mytodolist.models.ToDoModel;
import com.mytodolist.presenter.ToDoListingPresenter;
import com.mytodolist.utility.SharedPreference;
import com.mytodolist.view.ToDoListingView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ToDoListingActivity extends AppCompatActivity implements ToDoAdapter.ActionListener, AdapterView.OnItemClickListener, ToDoListingView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rv_todo)
    RecyclerView rvTodo;
    @BindView(R.id.fab_add_todo)
    FloatingActionButton fabAddTodo;

    FirebaseFirestore mFirestore;
    SharedPreference preference;

    FirestoreRecyclerOptions<ToDoModel> options;
    ToDoAdapter toDoAdapter;
    ToDoAdapter.ActionListener listener;
    ToDoListingPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_listing);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        setupToolbar();
        listener = this;
        presenter = new ToDoListingPresenter(ToDoListingActivity.this, this);
        presenter.prepareQuery();
    }

    private void setupToolbar() {
        toolbar.setTitle("ToDo");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @OnClick(R.id.fab_add_todo)
    public void addToDo() {
        startActivity(new Intent(ToDoListingActivity.this, CreateToDoActivity.class));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.logout_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.menu_logout:
                presenter.logout();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onChatClick(ToDoModel toDoModel) {
        Intent intent = new Intent(ToDoListingActivity.this, CommentsActivity.class);
        intent.putExtra("todo_id", toDoModel.getTodo_id());
        intent.putExtra("title", toDoModel.getTitle());
        startActivity(intent);
    }

    @Override
    public void onCheckBoxClick(ToDoModel toDoModel) {
        presenter.setCompleted(toDoModel);
    }

    @Override
    public void onDeleteClick(ToDoModel toDoModel) {
        presenter.deleteToDo(toDoModel);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (toDoAdapter != null) {
            toDoAdapter.startListening();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (toDoAdapter != null) {
            toDoAdapter.stopListening();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(ToDoListingActivity.this, CreateToDoActivity.class);
        intent.putExtra("todo_id", options.getSnapshots().get(position).getTodo_id());
        intent.putExtra("isEdit", true);
        startActivity(intent);
    }

    @Override
    public void onGetDataSuccess(FirestoreRecyclerOptions<ToDoModel> list) {
        options = list;
        setToDoAdapter();
    }

    @Override
    public void onGetDataFailure(String message) {
        Log.e("Error => ", message);
    }

    @Override
    public void logoutUser() {
        Intent intent = new Intent(ToDoListingActivity.this, LoginActivity.class);
        startActivity(intent);
        finishAffinity();
    }

    public void setToDoAdapter() {
        if (toDoAdapter == null) {
            toDoAdapter = new ToDoAdapter(options, ToDoListingActivity.this, listener);
            toDoAdapter.setOnItemClickListener(this);
        }

        if (rvTodo.getAdapter() == null) {
            rvTodo.setHasFixedSize(false);
            rvTodo.setLayoutManager(new LinearLayoutManager(this));
            rvTodo.setAdapter(toDoAdapter);
            rvTodo.setFocusable(false);
            rvTodo.setNestedScrollingEnabled(false);
        }
        toDoAdapter.notifyDataSetChanged();
    }
}
