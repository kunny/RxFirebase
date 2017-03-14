package com.androidhuman.rxfirebase.common.model;

import android.support.annotation.Nullable;

@Deprecated
public final class TaskResult {

    private final boolean success;

    @Nullable
    private final Throwable exception;

    public static TaskResult success() {
        return new TaskResult(true, null);
    }

    public static TaskResult failure(@Nullable Throwable exception) {
        return new TaskResult(false, exception);
    }

    private TaskResult(boolean success, @Nullable Throwable exception) {
        this.success = success;
        this.exception = exception;
    }

    public boolean isSuccess() {
        return success;
    }

    @Nullable
    public Throwable getException() {
        return exception;
    }

}
