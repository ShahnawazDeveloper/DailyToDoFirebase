package com.mytodolist.presenter;

import android.content.Context;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
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
    private String token;

    public LoginPresenter(Context context, LoginView loginView) {
        this.context = context;
        this.loginView = loginView;
        mFirestore = FirebaseFirestore.getInstance();
        preference = SharedPreference.getInstance();
        getFCMToken();
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
                                UserModel userModel = queryDocumentSnapshots.getDocuments().get(0).toObject(UserModel.class);
                                if (userModel != null) {
                                    userModel.setFcm_token(token);
                                    updateFCMToken(userModel);
                                    preference.setUserDetails(userModel);
                                    loginView.loginSuccessFully();
                                }
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

    private void updateFCMToken(UserModel userModel) {
        mFirestore.collection("users").document(userModel.getUid()).set(userModel);
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
