package com.mytodolist.presenter;

import android.content.Context;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.mytodolist.R;
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
    private String token;

    public SignUpPresenter(Context context, SignUpView signUpView) {
        this.context = context;
        this.signUpView = signUpView;
        mFirestore = FirebaseFirestore.getInstance();
        preference = SharedPreference.getInstance();
        getFCMToken();
    }

    public void doRequestForSignUp(EditText etLastName, EditText etFirstName, EditText etEmail, EditText etPassword, EditText etConfirmPassword) {
        if (isValid(etLastName, etFirstName, etEmail, etPassword, etConfirmPassword)) {
            CheckForExistingUser(etLastName, etFirstName, etEmail, etPassword);
            //signUpUser(etLastName, etFirstName, etEmail, etPassword);
        }
    }


    private void CheckForExistingUser(EditText etLastName, EditText etFirstName, EditText etEmail, EditText etPassword) {
        mFirestore.collection("users")
                .whereEqualTo("email", etEmail.getText().toString().trim())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if (!queryDocumentSnapshots.getDocuments().isEmpty() &&
                                queryDocumentSnapshots.getDocuments().size() > 0) {
                            signUpView.onSetProgressBarVisibility(View.GONE);
                            signUpView.signUpFail((String) context.getText(R.string.auth_failed_email_user));
                        } else {
                            signUpUser(etLastName, etFirstName, etEmail, etPassword);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        signUpUser(etLastName, etFirstName, etEmail, etPassword);
                    }
                });

    }

    //create user
    private void signUpUser(EditText etFirstName, EditText etLastName, EditText etEmail, EditText etPassword) {

        DocumentReference documentReference = mFirestore.collection("users").document();
        UserModel user = new UserModel(documentReference.getId(),
                etFirstName.getText().toString().trim(),
                etLastName.getText().toString().trim(),
                etEmail.getText().toString().trim(),
                etPassword.getText().toString().trim(),
                token,
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
        if (ValidationUtil.validatePassword(etPassword)) {
            signUpView.showValidationErrorInvalidPassword();
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

    private void getFCMToken() {
        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (!task.isSuccessful()) {
                    //Logger.e("getInstanceId failed", task.getException());
                    return;
                }

                // Get new Instance ID token
                if (task.getResult() != null) {
                     token = task.getResult().getToken();
                    SharedPreference.getInstance().setStringInPref("FCM_Token", token);
                    //Logger.e(token);
                }
            }
        });
    }

}
