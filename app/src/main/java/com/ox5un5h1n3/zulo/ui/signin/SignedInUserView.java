package com.ox5un5h1n3.zulo.ui.signin;

/**
 * Class exposing authenticated user details to the UI.
 */
class SignedInUserView {
    private String displayName;
    //... other data fields that may be accessible to the UI

    SignedInUserView(String displayName) {
        this.displayName = displayName;
    }

    String getDisplayName() {
        return displayName;
    }
}