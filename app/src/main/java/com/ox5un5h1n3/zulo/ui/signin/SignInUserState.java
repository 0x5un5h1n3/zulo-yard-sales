package com.ox5un5h1n3.zulo.ui.signin;

/**
 * Class exposing authenticated user details to the UI.
 */
class SignInUserState {
    private final String displayName;
    //... other data fields that may be accessible to the UI

    SignInUserState(String displayName) {
        this.displayName = displayName;
    }

    String getDisplayName() {
        return displayName;
    }
}