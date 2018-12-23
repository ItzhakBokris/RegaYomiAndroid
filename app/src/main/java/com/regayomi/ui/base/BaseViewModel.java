package com.regayomi.ui.base;

import android.app.Application;
import android.os.Bundle;

import com.regayomi.utils.SingleLiveEvent;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

public abstract class BaseViewModel extends AndroidViewModel {

    // Single-live-event of message-id that should be displayed in a snackbar.
    private final SingleLiveEvent<Integer> snackbarMessage;

    public BaseViewModel(@NonNull Application application) {
        super(application);

        snackbarMessage = new SingleLiveEvent<>();
    }

    /**
     * Gets single-live-event of message-id that should be displayed in a snackbar.
     */
    public SingleLiveEvent<Integer> getSnackbarMessage() {
        return snackbarMessage;
    }

    /**
     * Writes the view-model contents to a bundle.
     */
    public abstract void writeTo(@NonNull Bundle bundle);

    /**
     * Reads the bundle contents into this view-model.
     */
    public abstract void readFrom(@NonNull Bundle bundle);
}
