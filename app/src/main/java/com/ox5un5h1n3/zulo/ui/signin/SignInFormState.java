//package com.ox5un5h1n3.zulo.ui.signin;
//
//import androidx.annotation.Nullable;
//
///**
// * Data validation state of the login form.
// */
//class SignInFormState {
//    @Nullable
//    private final Integer usernameError;
//    @Nullable
//    private final Integer passwordError;
//    private final boolean isDataValid;
//
//    SignInFormState(@Nullable Integer usernameError, @Nullable Integer passwordError) {
//        this.usernameError = usernameError;
//        this.passwordError = passwordError;
//        this.isDataValid = false;
//    }
//
//    SignInFormState(boolean isDataValid) {
//        this.usernameError = null;
//        this.passwordError = null;
//        this.isDataValid = isDataValid;
//    }
//
//    @Nullable
//    Integer getUsernameError() {
//        return usernameError;
//    }
//
//    @Nullable
//    Integer getPasswordError() {
//        return passwordError;
//    }
//
//    boolean isDataValid() {
//        return isDataValid;
//    }
//}