package com.mytodolist.presenter;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.mytodolist.models.ToDoModel;
import com.mytodolist.utility.SharedPreference;
import com.mytodolist.utility.ValidationUtil;
import com.mytodolist.view.CreateToDoView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CreateToDoPresenter {

    private CreateToDoView createToDoView;
    private FirebaseFirestore mFirestore;
    private SharedPreference preference;

    private Context context;
    Calendar calendar;
    Intent intent;

    String todoId;
    boolean isEdit;

    public CreateToDoPresenter(Context context, CreateToDoView createToDoView, Intent intent) {
        this.context = context;
        this.createToDoView = createToDoView;
        this.intent = intent;
        mFirestore = FirebaseFirestore.getInstance();
        preference = SharedPreference.getInstance();
        calendar = Calendar.getInstance();
        getDataFromIntent();
    }

    /*validate user input*/
    private boolean isValid(EditText etTitle) {

        if (ValidationUtil.validateEmptyEditText(etTitle)) {
            // TODO: 2019-06-26  change message
            createToDoView.showValidationErrorEmptyTitle();
            return false;
        }

        /*if (ValidationUtil.validateEmail(etDescription)) {
            // TODO: 2019-06-26  change message
            dataTypeUtil.showToast(CreateToDoActivity.this, getString(R.string.err_msg_enter_password));
            ValidationUtil.requestFocus(CreateToDoActivity.this, etDescription);
            return false;
        }*/

        if (calendar.getTime().before(new Date())) {
            // TODO: 2019-06-26  change message
            createToDoView.showValidationErrorInvalidDate();
            return false;
        }

        return true;
    }

    public void setDatePicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        setTimePicker();
                    }
                }, calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.getDatePicker().setMinDate(new Date().getTime());
        datePickerDialog.show();
    }

    private void setTimePicker() {
        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        calendar.set(Calendar.MINUTE, minute);
                        SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy hh:mm a", Locale.getDefault());
                        createToDoView.setDateTime(dateFormat.format(calendar.getTime()));
                        //tvDateTime.setText(dateFormat.format(calendar.getTime()));
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false);
        timePickerDialog.show();
    }


    private void getDataFromIntent() {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            todoId = extras.getString("todo_id");
            isEdit = extras.getBoolean("isEdit");
            createToDoView.setToolbarTitle("Edit ToDo");
            //setupToolbar("Edit ToDo");
            getToDo();
        } else {
            createToDoView.setToolbarTitle("Create ToDo");
            //setupToolbar("Create ToDo");
        }
    }

    private void getToDo() {
        mFirestore.collection("todo")
                .whereEqualTo("todo_id", todoId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult() != null && !task.getResult().getDocuments().isEmpty()) {
                                ToDoModel toDoModel = task.getResult().getDocuments().get(0).toObject(ToDoModel.class);
                                if (toDoModel != null) {
                                    createToDoView.setToDoData(toDoModel);
                                    calendar.setTimeInMillis(toDoModel.getTime().getSeconds() * 1000);
                                }

                                /*if (toDoModel != null) {
                                    etTitle.setText(toDoModel.getTitle());
                                    etDescription.setText(toDoModel.getDescription());
                                    tvDateTime.setText(dateFormat.format(new Date(toDoModel.getTime().getSeconds() * 1000L)));
                                    cbCompleted.setVisibility(View.VISIBLE);
                                    if (toDoModel.isCompleted()) {
                                        cbCompleted.setChecked(true);
                                    } else {
                                        cbCompleted.setChecked(false);
                                    }
                                }*/
                            }
                        } else {
                            Log.e("Err getting documents: ", String.valueOf(task.getException()));
                        }
                    }
                });
    }

    //create user
    private void addToDo(EditText title, EditText desc, CheckBox isSelected) {
        DocumentReference documentReference;
        ToDoModel toDoModel;
        if (isEdit) {
            documentReference = mFirestore.collection("todo").document(todoId);
            toDoModel = new ToDoModel(documentReference.getId(),
                    preference.getUserDetails().getUid(),
                    title.getText().toString().trim(),
                    desc.getText().toString().trim(),
                    new Timestamp(calendar.getTime()),
                    new Timestamp(new Date()),
                    isSelected.isChecked()
            );
        } else {
            documentReference = mFirestore.collection("todo").document();
            toDoModel = new ToDoModel(documentReference.getId(),
                    preference.getUserDetails().getUid(),
                    title.getText().toString().trim(),
                    desc.getText().toString().trim(),
                    new Timestamp(calendar.getTime()),
                    new Timestamp(new Date()),
                    false
            );
        }


        documentReference.set(toDoModel)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                       /* Toast.makeText(CreateToDoActivity.this, "Authentication Success.",
                                Toast.LENGTH_LONG).show();*/
                        createToDoView.toDoCreatedSuccessFully();
                        //finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        createToDoView.toDoCreationFail(e.getMessage());
                        /*Toast.makeText(CreateToDoActivity.this, "Something went wrong please try again.",
                                Toast.LENGTH_SHORT).show();*/
                    }
                });
    }


    public void createToDo(EditText etTitle, EditText etDesc, CheckBox checkBox) {
        if (isValid(etTitle)) {
            addToDo(etTitle, etDesc, checkBox);
        }
    }

}
