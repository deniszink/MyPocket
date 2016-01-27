package com.denis.mypocket.view.activity;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.MenuItem;

import com.denis.mypocket.R;
import com.denis.mypocket.databinding.ActivityAddTransactionBinding;
import com.denis.mypocket.internal.di.components.DaggerAddTransactionComponent;
import com.denis.mypocket.internal.di.modules.AddTransactionModule;
import com.denis.mypocket.viewmodel.AddTransactionViewModel;

import javax.inject.Inject;


public class AddTransactionActivity extends BaseActivity {

    @Inject
    public AddTransactionViewModel viewModel;
    private ActivityAddTransactionBinding binding;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_transaction);
        binding.setViewModel(viewModel);
        binding.addTrans.setOnClickListener(viewModel.addOnClick);
        configireToolbar(binding.toolbarAddTrans.toolbar, R.string.toolbar_add_trans, true);

    }


    @Override
    protected void initDIComponent() {
        DaggerAddTransactionComponent.builder()
                .applicationComponent(getApplicationComponent())
                .addTransactionModule(new AddTransactionModule())
                .activityModule(getActivityModule())
                .build().inject(this);
    }


    @Override
    public void onDestroy() {
        viewModel.destroy();
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
