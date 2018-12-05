package com.regayomi.ui.splashScreen;

import android.content.Intent;
import android.os.Handler;

import com.regayomi.R;
import com.regayomi.databinding.ActivitySplashScreenBinding;
import com.regayomi.ui.article.ArticleMainActivity;
import com.regayomi.ui.article.ArticleViewModel;
import com.regayomi.ui.base.BaseActivity;

import androidx.annotation.NonNull;

public class SplashScreenActivity extends BaseActivity<ActivitySplashScreenBinding, ArticleViewModel> {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_splash_screen;
    }

    @NonNull
    @Override
    protected Class<ArticleViewModel> getViewModelClass() {
        return ArticleViewModel.class;
    }

    @Override
    protected void onSetUp(@NonNull ActivitySplashScreenBinding binding, @NonNull ArticleViewModel model) {
        model.updateArticles();
        model.getArticles().observe(this, articles -> {
            if (!articles.isEmpty()) {
                new Handler().post(this::goToMainScreen);
            }
            else {
                binding.setNoArticles(true);
            }
        });
    }

    /**
     * Redirects to the articles main screen.
     */
    private void goToMainScreen() {
        startActivity(new Intent(this, ArticleMainActivity.class));
        finish();
    }
}
