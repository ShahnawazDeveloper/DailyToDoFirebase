package com.mytodolist.view;

import com.mytodolist.models.ToDoModel;

public interface CreateToDoView {

    void onSetProgressBarVisibility(int visibility);

    void showValidationErrorEmptyTitle();

    void showValidationErrorEmptyDate();

    void showValidationErrorInvalidDate();

    void setDateTime(String dateTime);

    void toDoCreatedSuccessFully();

    void toDoCreationFail(String message);

    void setToolbarTitle(String title);

    void setToDoData(ToDoModel toDoModel);
}
