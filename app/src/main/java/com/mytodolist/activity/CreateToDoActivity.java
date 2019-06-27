package com.mytodolist.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;

import com.mytodolist.R;
import com.mytodolist.models.ToDoModel;
import com.mytodolist.presenter.CreateToDoPresenter;
import com.mytodolist.utility.DataTypeUtil;
import com.mytodolist.utility.ValidationUtil;
import com.mytodolist.view.CreateToDoView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateToDoActivity extends AppCompatActivity implements CreateToDoView {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.cb_completed)
    CheckBox cbCompleted;
    @BindView(R.id.tv_date_time)
    AppCompatTextView tvDateTime;
    @BindView(R.id.et_title)
    AppCompatEditText etTitle;
    @BindView(R.id.et_description)
    AppCompatEditText etDescription;

    DataTypeUtil dataTypeUtil;
    SimpleDateFormat dateFormat;
    CreateToDoPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_to_do);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        dataTypeUtil = DataTypeUtil.getInstance();
        dateFormat = new SimpleDateFormat("MMMM dd, yyyy hh:mm a", Locale.getDefault());
        presenter = new CreateToDoPresenter(CreateToDoActivity.this, this, getIntent());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.save_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.menu_save:
                presenter.createToDo(etTitle, etDescription, cbCompleted);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @OnClick(R.id.tv_date_time)
    public void setDateTime() {
        presenter.setDatePicker();
        //setDatePicker();
    }

    @Override
    public void onSetProgressBarVisibility(int visibility) {

    }

    @Override
    public void showValidationErrorEmptyTitle() {
        dataTypeUtil.showToast(CreateToDoActivity.this, getString(R.string.err_msg_enter_to_do));
        ValidationUtil.requestFocus(CreateToDoActivity.this, etTitle);
    }

    @Override
    public void showValidationErrorInvalidDate() {
        dataTypeUtil.showToast(CreateToDoActivity.this, getString(R.string.err_msg_invalid_date));
    }

    @Override
    public void setDateTime(String dateTime) {
        tvDateTime.setText(dateTime);
    }

    @Override
    public void toDoCreatedSuccessFully() {
        finish();
    }

    @Override
    public void toDoCreationFail(String message) {
        dataTypeUtil.showToast(CreateToDoActivity.this, message);
    }

    @Override
    public void setToolbarTitle(String title) {
        toolbar.setTitle(title);//"Create ToDo");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void setToDoData(ToDoModel toDoModel) {
        if (toDoModel != null) {
            etTitle.setText(toDoModel.getTitle());
            etDescription.setText(toDoModel.getDescription());
            tvDateTime.setText(dateFormat.format(new Date(toDoModel.getTime().getSeconds() * 1000L)));

            cbCompleted.setVisibility(View.VISIBLE);
            if (toDoModel.isCompleted()) {
                cbCompleted.setChecked(true);
            } else {
                cbCompleted.setChecked(false);
            }
        }
    }
}
