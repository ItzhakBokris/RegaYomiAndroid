package com.regayomi.ui.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

public abstract class BaseFragment<T extends ViewDataBinding, V extends BaseViewModel> extends Fragment {

    // A data-binding object that generated from the view of the fragment.
    private T viewBinding;

    // The persistence view-model of the fragment.
    private V viewModel;

    /**
     * Gets the fragment layout resource id.
     */
    @LayoutRes
    protected abstract int getLayoutId();

    /**
     * Gets the persistence view-model class of the fragment.
     */
    @NonNull
    protected abstract Class<V> getViewModelClass();

    /**
     * Called when the view of the fragment is ready and can be initialized.
     */
    protected abstract void onSetUp(@NonNull T binding, @NonNull V model);

    /**
     * Gets the data-binding object that generated from the view of the fragment.
     */
    protected T getViewBinding() { return viewBinding; }

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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        return viewBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        onSetUp(viewBinding, viewModel);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        viewModel.writeTo(outState);
    }
}
