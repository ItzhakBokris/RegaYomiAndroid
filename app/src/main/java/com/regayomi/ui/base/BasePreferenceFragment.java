package com.regayomi.ui.base;


import android.content.SharedPreferences;
import android.os.Bundle;

import com.regayomi.ui.common.TimePreference;
import com.regayomi.ui.common.TimePreferenceDialog;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.XmlRes;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

public abstract class BasePreferenceFragment<V extends BaseViewModel>
        extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {

    // The persistence view-model of the fragment.
    private V viewModel;

    /**
     * Gets the persistence view-model of the fragment.
     */
    protected V getViewModel() { return viewModel; }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = ViewModelProviders.of(requireActivity()).get(getViewModelClass());
        if (savedInstanceState != null) {
            viewModel.readFrom(savedInstanceState);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        viewModel.writeTo(outState);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(getPreferenceResId(), rootKey);
    }

    @Override
    public void onDisplayPreferenceDialog(Preference preference) {
        if (preference instanceof TimePreference) {
            DialogFragment dialogFragment = TimePreferenceDialog.newInstance(preference.getKey());
            dialogFragment.setTargetFragment(this, 0);
            dialogFragment.show(requireFragmentManager(), "TimePreferenceDialog");
        } else {
            super.onDisplayPreferenceDialog(preference);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceManager().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

    }

    @Override
    public void onPause() {
        getPreferenceManager().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
        super.onPause();
    }

    /**
     * Gets the resource-id of the preference hierarchy xml.
     */
    @XmlRes
    protected abstract int getPreferenceResId();

    /**
     * Gets the persistence view-model class of the fragment.
     */
    @NonNull
    protected abstract Class<V> getViewModelClass();
}
