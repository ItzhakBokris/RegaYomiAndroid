package com.regayomi.ui.base;

import android.app.Application;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.ViewModel;

public abstract class BaseViewModel extends AndroidViewModel {

    public BaseViewModel(@NonNull Application application) {
        super(application);
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
