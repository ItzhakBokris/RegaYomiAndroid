package com.regayomi.ui.base;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;
import com.regayomi.databinding.ActivityArticleMainBinding;
import com.regayomi.ui.article.ArticleViewModel;
import com.regayomi.utils.ConfigUtils;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.lifecycle.ViewModelProviders;

public abstract class BaseActivity<T extends ViewDataBinding, V extends BaseViewModel> extends AppCompatActivity {

    // A data-binding object that generated from the view of the activity.
    private T viewBinding;

    // The persistence view-model of the activity.
    private V viewModel;

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.updateTitleLocale();
        viewBinding = DataBindingUtil.setContentView(this, getLayoutId());
        viewModel = ViewModelProviders.of(this).get(getViewModelClass());
        if (savedInstanceState != null) {
            viewModel.readFrom(savedInstanceState);
        } else if (getIntent() != null && getIntent().getExtras() != null) {
            viewModel.readFrom(getIntent().getExtras());
        }
        initSnackbar();
        onSetUp(viewBinding, viewModel);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        viewModel.writeTo(outState);
    }

    @Override
    protected void attachBaseContext(Context base) { super.attachBaseContext(ConfigUtils.updateBaseContextLocale(base)); }

    /**
     * Update the title of the activity according to the current locale.
     */
    private void updateTitleLocale() {
        try {
            int flags = PackageManager.GET_META_DATA;
            int label = getPackageManager().getActivityInfo(getComponentName(), flags).labelRes;
            if (label == 0) {
                label = getPackageManager().getApplicationInfo(getPackageName(), flags).labelRes;
            }
            if (label != 0) {
                setTitle(label);
            }
        }
        catch (PackageManager.NameNotFoundException ignored) {}
    }

    /**
     * Called when the view of the activity is ready and can be initialized.
     */
    protected abstract void onSetUp(@NonNull T binding, @NonNull V model);

    /**
     * Gets the activity layout resource id.
     */
    @LayoutRes
    protected abstract int getLayoutId();

    /**
     * Gets the data-binding object that generated from the view of the activity.
     */
    protected T getViewBinding() { return viewBinding; }

    /**
     * Gets the persistence view-model class of the activity.
     */
    @NonNull
    protected abstract Class<V> getViewModelClass();

    /**
     * Gets the persistence view-model of the activity.
     */
    protected V getViewModel() { return viewModel; }

    /**
     * Listens to Snackbar messages that could displayed at some scenarios.
     */
    private void initSnackbar() {
        viewModel.getSnackbarMessage().observe(this,
                messageId -> Snackbar.make(viewBinding.getRoot(), messageId, Snackbar.LENGTH_LONG).show());
    }
}
