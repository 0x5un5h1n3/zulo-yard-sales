package com.ox5un5h1n3.zulo.ui.signin;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.util.Patterns;

import com.ox5un5h1n3.zulo.data.LoginRepository;
import com.ox5un5h1n3.zulo.data.Result;
import com.ox5un5h1n3.zulo.data.model.LoggedInUser;
import com.ox5un5h1n3.zulo.R;

public class SignInViewModel extends ViewModel {

    private final MutableLiveData<SignInFormState> loginFormState = new MutableLiveData<>();
    private final MutableLiveData<SignInResult> loginResult = new MutableLiveData<>();
    private final LoginRepository loginRepository;

    SignInViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    LiveData<SignInFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<SignInResult> getLoginResult() {
        return loginResult;
    }

    public void login(String username, String password) {
        // can be launched in a separate asynchronous job
        Result<LoggedInUser> result = loginRepository.login(username, password);

        if (result instanceof Result.Success) {
            LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
            loginResult.setValue(new SignInResult(new SignInUserState(data.getDisplayName())));
        } else {
            loginResult.setValue(new SignInResult(R.string.sign_in_failed));
        }
    }

    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new SignInFormState(R.string.sign_in_invalid_user, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new SignInFormState(null, R.string.sign_in_invalid_pass));
        } else {
            loginFormState.setValue(new SignInFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        } else if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }
}