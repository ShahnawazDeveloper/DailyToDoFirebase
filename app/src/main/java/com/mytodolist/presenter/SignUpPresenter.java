package com.mytodolist.presenter;

import android.content.Context;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.mytodolist.models.UserModel;
import com.mytodolist.utility.SharedPreference;
import com.mytodolist.utility.ValidationUtil;
import com.mytodolist.view.SignUpView;

import java.util.Date;

public class SignUpPresenter {

    private SignUpView signUpView;
    private FirebaseFirestore mFirestore;
    private SharedPreference preference;
    private Context context;

    public SignUpPresenter(Context context, SignUpView signUpView) {
        this.context = context;
        this.signUpView = signUpView;
        mFirestore = FirebaseFirestore.getInstance();
        preference = SharedPreference.getInstance();
    }

    public void doRequestForSignUp(EditText etLastName, EditText etFirstName, EditText etEmail, EditText etPassword, EditText etConfirmPassword) {
        if (isValid(etLastName, etFirstName, etEmail, etPassword, etConfirmPassword)) {
            signUpUser(etLastName, etFirstName, etEmail, etPassword);
        }
    }

    //create user
    private void signUpUser(EditText etFirstName, EditText etLastName, EditText etEmail, EditText etPassword) {

        DocumentReference documentReference = mFirestore.collection("users").document();
        UserModel user = new UserModel(documentReference.getId(),
                etFirstName.getText().toString().trim(),
                etLastName.getText().toString().trim(),
                etEmail.getText().toString().trim(),
                etPassword.getText().toString().trim(),
                new Timestamp(new Date())
        );

        documentReference.set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        preference.setUserDetails(user);
                        signUpView.onSetProgressBarVisibility(View.GONE);
                        signUpView.signUpSuccessFully();
                        /*Toast.makeText(SignUpActivity.this, "Authentication Success.",
                                Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                        //TODO  store user object in prefrence
                        redirectOnToDoListing();*/
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        signUpView.signUpFail(e.getMessage());
                        signUpView.onSetProgressBarVisibility(View.GONE);
                      /*  Toast.makeText(SignUpActivity.this, "Authentication Fail.",
                                Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.GONE);*/
                    }
                });

    }

    /*validate user input*/
    private boolean isValid(EditText etFirstName, EditText etLastName, EditText etEmail, EditText etPassword, EditText etConfirmPassword) {
        if (ValidationUtil.validateEmptyEditText(etFirstName)) {
            signUpView.showValidationErrorEmptyFirstName();
            return false;
        }
        if (ValidationUtil.validateEmptyEditText(etLastName)) {
            signUpView.showValidationErrorEmptyLastName();
            return false;
        }
        if (ValidationUtil.validateEmptyEditText(etEmail)) {
            signUpView.showValidationErrorEmptyEmail();
            return false;
        }
        if (ValidationUtil.validateEmail(etEmail)) {
            signUpView.showValidationErrorInvalidEmail();
            return false;
        }
        if (ValidationUtil.validateEmptyEditText(etPassword)) {
            signUpView.showValidationErrorEmptyPassword();
            return false;
        }
        if (ValidationUtil.validateEmptyEditText(etConfirmPassword)) {
            signUpView.showValidationErrorEmptyConfirmPassword();
            return false;
        }
        if (ValidationUtil.validateConfirmPassword(etPassword, etConfirmPassword)) {
            signUpView.showValidationErrorConfirmPasswordNotMatch();
            return false;
        }

        signUpView.onSetProgressBarVisibility(View.VISIBLE);
        return true;
    }

}
