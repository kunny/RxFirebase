package com.androidhuman.rxfirebase.common.model;

import android.support.annotation.Nullable;

public final class TaskResult {

    private final boolean success;

    @Nullable
    private final Exception exception;

    public static TaskResult success() {
        return new TaskResult(true, null);
    }

    public static TaskResult failure(@Nullable Exception exception) {
        return new TaskResult(false, exception);
    }

    private TaskResult(boolean success, @Nullable Exception exception) {
        this.success = success;
        this.exception = exception;
    }

    public boolean isSuccess() {
        return success;
    }

    @Nullable
    public Exception getException() {
        return exception;
    }

}
