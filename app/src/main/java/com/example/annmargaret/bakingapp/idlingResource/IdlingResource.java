package com.example.annmargaret.bakingapp.idlingResource;

import android.support.annotation.Nullable;

import java.util.concurrent.atomic.AtomicBoolean;

public class IdlingResource implements android.support.test.espresso.IdlingResource {

    @Nullable
    private volatile ResourceCallback mCallback;

    private AtomicBoolean currentlyIdle = new AtomicBoolean(true);

    @Override
    public String getName() {
        return this.getClass().getName();
    }

    @Override
    public boolean isIdleNow() {
        return currentlyIdle.get();
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        mCallback = callback;
    }

    public void setIdleState(boolean isIdleNow) {
        currentlyIdle.set(isIdleNow);
        if(isIdleNow && mCallback != null) {
            mCallback.onTransitionToIdle();
        }
    }
}
