package com.mytodolist.view;

public interface SignUpView {

    void onSetProgressBarVisibility(int visibility);

    void showValidationErrorEmptyFirstName();

    void showValidationErrorEmptyLastName();

    void showValidationErrorEmptyEmail();

    void showValidationErrorInvalidEmail();

    void showValidationErrorEmptyPassword();

    void showValidationErrorEmptyConfirmPassword();

    void showValidationErrorConfirmPasswordNotMatch();

    void signUpSuccessFully();

    void signUpFail(String message);

}
