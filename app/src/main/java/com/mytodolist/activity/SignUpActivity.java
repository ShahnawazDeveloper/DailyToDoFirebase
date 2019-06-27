package com.mytodolist.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import com.google.firebase.firestore.FirebaseFirestore;
import com.mytodolist.R;
import com.mytodolist.presenter.SignUpPresenter;
import com.mytodolist.utility.DataTypeUtil;
import com.mytodolist.utility.SharedPreference;
import com.mytodolist.utility.ValidationUtil;
import com.mytodolist.view.SignUpView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SignUpActivity extends AppCompatActivity implements SignUpView {

    @BindView(R.id.et_first_name)
    AppCompatEditText etFirstName;
    @BindView(R.id.et_last_name)
    AppCompatEditText etLastName;
    @BindView(R.id.et_email)
    AppCompatEditText etEmail;
    @BindView(R.id.et_password)
    AppCompatEditText etPassword;
    @BindView(R.id.et_confirm_password)
    AppCompatEditText etConfirmPassword;

    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    DataTypeUtil dataTypeUtil;
    FirebaseFirestore mFirestore;
    SharedPreference preference;
    SignUpPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        dataTypeUtil = DataTypeUtil.getInstance();
        mFirestore = FirebaseFirestore.getInstance();
        preference = SharedPreference.getInstance();
        presenter = new SignUpPresenter(SignUpActivity.this, this);
    }

    @OnClick(R.id.btn_sign_up)
    public void signUp() {
        presenter.doRequestForSignUp(etFirstName, etLastName, etEmail, etPassword, etConfirmPassword);
    }

    private void redirectOnToDoListing() {
        Intent intent = new Intent(SignUpActivity.this, ToDoListingActivity.class);
        startActivity(intent);
        finishAffinity();
    }

    @OnClick(R.id.btn_login)
    public void login() {
        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onSetProgressBarVisibility(int visibility) {
        progressBar.setVisibility(visibility);
    }

    @Override
    public void showValidationErrorEmptyFirstName() {
        dataTypeUtil.showToast(SignUpActivity.this, getString(R.string.err_msg_enter_first_name));
        ValidationUtil.requestFocus(SignUpActivity.this, etFirstName);
    }

    @Override
    public void showValidationErrorEmptyLastName() {
        dataTypeUtil.showToast(SignUpActivity.this, getString(R.string.err_msg_enter_last_name));
        ValidationUtil.requestFocus(SignUpActivity.this, etLastName);
    }

    @Override
    public void showValidationErrorEmptyEmail() {
        dataTypeUtil.showToast(SignUpActivity.this, getString(R.string.err_msg_enter_email));
        ValidationUtil.requestFocus(SignUpActivity.this, etEmail);
    }

    @Override
    public void showValidationErrorInvalidEmail() {
        dataTypeUtil.showToast(SignUpActivity.this, getString(R.string.err_msg_enter_proper_email));
        ValidationUtil.requestFocus(SignUpActivity.this, etEmail);
    }

    @Override
    public void showValidationErrorEmptyPassword() {
        dataTypeUtil.showToast(SignUpActivity.this, getString(R.string.err_msg_enter_password));
        ValidationUtil.requestFocus(SignUpActivity.this, etPassword);
    }

    @Override
    public void showValidationErrorInvalidPassword() {
        dataTypeUtil.showToast(SignUpActivity.this, getString(R.string.minimum_password));
        ValidationUtil.requestFocus(SignUpActivity.this, etPassword);
    }

    @Override
    public void showValidationErrorEmptyConfirmPassword() {
        dataTypeUtil.showToast(SignUpActivity.this, getString(R.string.err_msg_enter_confirm_password));
        ValidationUtil.requestFocus(SignUpActivity.this, etConfirmPassword);
    }

    @Override
    public void showValidationErrorConfirmPasswordNotMatch() {
        dataTypeUtil.showToast(SignUpActivity.this, getString(R.string.err_msg_passwords_do_not_match));
        ValidationUtil.requestFocus(SignUpActivity.this, etConfirmPassword);
    }

    @Override
    public void signUpSuccessFully() {
        redirectOnToDoListing();
    }

    @Override
    public void signUpFail(String message) {
        dataTypeUtil.showToast(SignUpActivity.this, message);
    }
}
