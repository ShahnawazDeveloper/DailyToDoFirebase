package com.mytodolist.presenter;

import android.content.Context;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.mytodolist.R;
import com.mytodolist.models.UserModel;
import com.mytodolist.utility.SharedPreference;
import com.mytodolist.utility.ValidationUtil;
import com.mytodolist.view.LoginView;

public class LoginPresenter {

    private LoginView loginView;
    private FirebaseFirestore mFirestore;
    private SharedPreference preference;

    private Context context;

    public LoginPresenter(Context context, LoginView loginView) {
        this.context = context;
        this.loginView = loginView;
        mFirestore = FirebaseFirestore.getInstance();
        preference = SharedPreference.getInstance();
    }

    public void doLogin(EditText email, EditText pwd) {
        if (isValid(email, pwd)) {
            mFirestore.collection("users")
                    .whereEqualTo("email", email.getText().toString().trim())
                    .whereEqualTo("password", pwd.getText().toString().trim())
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            loginView.onSetProgressBarVisibility(View.GONE);

                            if (!queryDocumentSnapshots.getDocuments().isEmpty() &&
                                    queryDocumentSnapshots.getDocuments().size() > 0) {
                                preference.setUserDetails(queryDocumentSnapshots.getDocuments().get(0).toObject(UserModel.class));
                                loginView.loginSuccessFully();
                            } else {
                                loginView.loginFail((String) context.getText(R.string.auth_failed));
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            loginView.loginFail(e.getMessage());
                            loginView.onSetProgressBarVisibility(View.GONE);
                        }
                    });
        }
    }

    public void checkForAlreadyLogin() {
        if (preference.getUserDetails() != null
                && preference.getUserDetails().getUid() != null
                && !preference.getUserDetails().getUid().isEmpty()) {
            loginView.loginSuccessFully();
        }
    }

    /*validate user input*/
    private boolean isValid(EditText email, EditText pwd) {

        if (ValidationUtil.validateEmptyEditText(email)) {
            loginView.showValidationErrorEmptyEmail();
            return false;
        }
        if (ValidationUtil.validateEmail(email)) {
            loginView.showValidationErrorInvalidEmail();
            return false;
        }

        if (ValidationUtil.validateEmptyEditText(pwd)) {
            loginView.showValidationErrorEmptyPassword();
            return false;
        }

        loginView.onSetProgressBarVisibility(View.VISIBLE);
        return true;
    }
}
