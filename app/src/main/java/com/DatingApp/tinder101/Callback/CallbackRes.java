package com.DatingApp.tinder101.Callback;


import androidx.annotation.NonNull;

import lombok.Getter;

public class CallbackRes<T> {
    public CallbackRes() {}

    @NonNull
    @Override
    public String toString() {
        if (this instanceof CallbackRes.Success) {
            CallbackRes.Success success = (CallbackRes.Success) this;
            return "Success[data=" + success.getData().toString() + "]";
        } else if (this instanceof CallbackRes.Error) {
            CallbackRes.Error error = (CallbackRes.Error) this;
            return "Error[exception=" + error.getError().toString() + "]";
        }
        return "";
    }

    // Success sub-class
    @Getter
    public static final class Success<T> extends CallbackRes {
        private final T data;

        public Success(T data) {
            this.data = data;
        }

    }

    // Error sub-class
    @Getter
    public static final class Error extends CallbackRes {
        private final Exception error;

        public Error(Exception error) {
            this.error = error;
        }

    }
}