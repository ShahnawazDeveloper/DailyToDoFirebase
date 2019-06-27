package com.mytodolist.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;

import com.mytodolist.R;
import com.mytodolist.presenter.LoginPresenter;
import com.mytodolist.utility.DataTypeUtil;
import com.mytodolist.utility.ValidationUtil;
import com.mytodolist.view.LoginView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity implements LoginView {

    @BindView(R.id.et_email)
    AppCompatEditText etEmail;
    @BindView(R.id.et_password)
    AppCompatEditText etPassword;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;

    DataTypeUtil dataTypeUtil;
    private LoginPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        dataTypeUtil = DataTypeUtil.getInstance();
        presenter = new LoginPresenter(LoginActivity.this, this);
        presenter.checkForAlreadyLogin();
    }

    @OnClick(R.id.btn_login)
    public void login() {
        presenter.doLogin(etEmail, etPassword);
    }

    private void redirectOnToDoListing() {
        Intent intent = new Intent(LoginActivity.this, ToDoListingActivity.class);
        startActivity(intent);
        finishAffinity();
    }

    @OnClick(R.id.btn_sign_up)
    public void signUp() {
        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(intent);
    }

    @Override
    public void onSetProgressBarVisibility(int visibility) {
        progressBar.setVisibility(visibility);
    }

    @Override
    public void showValidationErrorEmptyEmail() {
        dataTypeUtil.showToast(LoginActivity.this, getString(R.string.err_msg_enter_email));
        ValidationUtil.requestFocus(LoginActivity.this, etEmail);
    }

    @Override
    public void showValidationErrorInvalidEmail() {
        dataTypeUtil.showToast(LoginActivity.this, getString(R.string.err_msg_enter_proper_email));
        ValidationUtil.requestFocus(LoginActivity.this, etEmail);
    }

    @Override
    public void showValidationErrorEmptyPassword() {
        dataTypeUtil.showToast(LoginActivity.this, getString(R.string.err_msg_enter_password));
        ValidationUtil.requestFocus(LoginActivity.this, etPassword);
    }

    @Override
    public void loginSuccessFully() {
        redirectOnToDoListing();
    }

    @Override
    public void loginFail(String message) {
        dataTypeUtil.showToast(LoginActivity.this, message);
    }
}
