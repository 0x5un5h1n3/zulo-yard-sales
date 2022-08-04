package com.ox5un5h1n3.zulo.ui.signin;

import androidx.annotation.Nullable;

/**
 * Authentication result : success (user details) or error message.
 */
class SignInResult {
    @Nullable
    private SignInUserState success;
    @Nullable
    private Integer error;

    SignInResult(@Nullable Integer error) {
        this.error = error;
    }

    SignInResult(@Nullable SignInUserState success) {
        this.success = success;
    }

    @Nullable
    SignInUserState getSuccess() {
        return success;
    }

    @Nullable
    Integer getError() {
        return error;
    }
}