package com.cgg.ghmcpollingapp.error_handler;

import android.content.Context;

public interface ErrorHandlerInterface {
    void handleError(Throwable e, Context context);
    void handleError(String e, Context context);
}
