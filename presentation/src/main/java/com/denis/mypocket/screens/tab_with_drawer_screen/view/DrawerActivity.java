package com.denis.mypocket.screens.tab_with_drawer_screen.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

import com.denis.mypocket.R;
import com.denis.mypocket.databinding.ActivityDrawerBinding;
import com.denis.mypocket.databinding.NavDrawerHeaderBinding;
import com.denis.mypocket.internal.di.components.DaggerDrawerComponent;
import com.denis.mypocket.internal.di.modules.drawer.DrawerModule;
import com.denis.mypocket.model.UserModel;
import com.denis.mypocket.model.WalletModel;
import com.denis.mypocket.screens.add_transaction_screen.view.AddTransactionActivity;
import com.denis.mypocket.screens.tab_with_drawer_screen.viewmodel.DrawerNavViewModel;
import com.denis.mypocket.screens.wallets_screen.view.WalletActivity;
import com.denis.mypocket.utils.PLConstants;
import com.denis.mypocket.view.activity.BaseActivity;
import com.denis.mypocket.view.fragments.CycleOperationFragment;
import com.denis.mypocket.view.fragments.DebtsFragment;
import com.denis.mypocket.view.fragments.TransactionsFragment;

import java.util.List;

import javax.inject.Inject;

public class DrawerActivity extends BaseActivity implements
        NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private DrawerLayout mDrawer;
    private FloatingActionButton fabTrans, fabCyclic, fabSaves, fabStats, fabAddIncome, fabAddExpense;

    private Animation rotate_backward, fab_close, fab_open, rotate_forward;
    private boolean isTransFabOpen;
    private Interpolator interpolatorOpen = new AccelerateInterpolator();
    private Interpolator interpolatorClose = new DecelerateInterpolator();
    private boolean[] currentlyPageShown = {true, false, false, false}; //need to indicate which button is showing
    private boolean[] switchStates = {false, false, false, false, false};
    private FloatingActionButton[] fabMassive;
    private NavigationView navigationView;


    @Inject
    DrawerNavViewModel viewModel;

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, DrawerActivity.class);
    }

    //region tab event listener

    /**
     * Listener which need to subscribe to tab selection events
     */
    private class OnTabSelectedListener extends TabLayout.ViewPagerOnTabSelectedListener {

        int previousFabIdx = 0;

        public OnTabSelectedListener(ViewPager viewPager) {
            super(viewPager);
        }

        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            super.onTabSelected(tab);
            Log.d("myTag", "onTabSelected: ");
            if (currentlyPageShown[previousFabIdx]) { //previous fab is showing
                if (isTransFabOpen) //fabTrans is showing
                    animateFAB();
                playCloseAnimation(fabMassive[previousFabIdx], tab.getPosition()); //close fab with animation
                currentlyPageShown[previousFabIdx] = false;// fab isn't showing
            }

            currentlyPageShown[tab.getPosition()] = true;
            previousFabIdx = tab.getPosition(); //save current idx to previous
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
            Log.d("myTag", "onTabUnselected: ");
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {
            Log.d("myTag", "onTabReselected: ");
        }

        private void playCloseAnimation(View view, int nextBtIdx) {

            AnimatorSet fabCloseSet = new AnimatorSet();

            Animator scaleAnimatorX = ObjectAnimator.ofFloat(view, "scaleX", 1f, 0.0f)
                    .setDuration(300);
            scaleAnimatorX.setInterpolator(interpolatorClose);

            Animator scaleAnimatorY = ObjectAnimator.ofFloat(view, "scaleY", 1f, 0.0f)
                    .setDuration(300);
            scaleAnimatorY.setInterpolator(interpolatorClose);

            Animator alpha = ObjectAnimator.ofFloat(view, "alpha", 1f, 0.0f)
                    .setDuration(300);
            alpha.setInterpolator(interpolatorClose);

            fabCloseSet.playTogether(scaleAnimatorX, scaleAnimatorY, alpha);

            fabCloseSet.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    view.setVisibility(View.GONE);
                    view.setClickable(false);
                    playOpenAnimation(fabMassive[nextBtIdx]); //open actual fab with animation
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });

            fabCloseSet.start();
        }

        private void playOpenAnimation(View view) {
            AnimatorSet fabOpenSet = new AnimatorSet();

            Animator scaleAnimatorX = ObjectAnimator.ofFloat(view, "scaleX", 0f, 1f)
                    .setDuration(300);

            scaleAnimatorX.setInterpolator(interpolatorOpen);

            Animator scaleAnimatorY = ObjectAnimator.ofFloat(view, "scaleY", 0f, 1f)
                    .setDuration(300);
            scaleAnimatorY.setInterpolator(interpolatorOpen);

            Animator alpha = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f)
                    .setDuration(300);
            alpha.setInterpolator(interpolatorOpen);

            fabOpenSet.playTogether(scaleAnimatorX, scaleAnimatorY, alpha);

            fabOpenSet.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    if (fabMassive[previousFabIdx].getVisibility() == View.VISIBLE)
                        fabMassive[previousFabIdx].setVisibility(View.GONE);
                    view.setClickable(true);
                    view.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animator animation) {

                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            fabOpenSet.start();


        }

    }
    //endregion


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityDrawerBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_drawer);
        binding.setViewModel(viewModel);

        Toolbar toolbar = binding.content.toolbar;

        View view = binding.navView.getHeaderView(0);
        NavDrawerHeaderBinding headerBinding = NavDrawerHeaderBinding.bind(view);
        UserModel userModel = viewModel.getUser();

        navigationView = binding.navView;
        headerBinding.setUser(userModel);
        headerBinding.walletImgNDH.setOnClickListener(this);

        configireToolbar(toolbar, R.string.app_name, false);

        initAnimations();

        bindFabs(binding);
        bindDrawer(binding, toolbar);
        bindTabs(binding);

    }

    public void initAnimations() {
        rotate_backward = AnimationUtils.loadAnimation(this, R.anim.rotate_backward);
        rotate_forward = AnimationUtils.loadAnimation(this, R.anim.rotate_forward);
        fab_close = AnimationUtils.loadAnimation(this, R.anim.fab_close);
        fab_open = AnimationUtils.loadAnimation(this, R.anim.fab_open);
    }

    private void bindFabs(ActivityDrawerBinding binding) {
        fabTrans = binding.content.fabTrans;
        fabCyclic = binding.content.fabCyclic;
        fabSaves = binding.content.fabSaves;
        fabStats = binding.content.fabStats;

        fabAddExpense = binding.content.addExpenseTrans;
        fabAddIncome = binding.content.addIncomeTrans;

        fabTrans.setOnClickListener(this);
        fabStats.setOnClickListener(this);
        fabSaves.setOnClickListener(this);
        fabCyclic.setOnClickListener(this);

        fabAddExpense.setOnClickListener(this);
        fabAddIncome.setOnClickListener(this);

        fabMassive = new FloatingActionButton[]{fabTrans, fabCyclic, fabSaves, fabStats};
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addExpenseTrans:
                startAddTransactionAct(false);
                break;
            case R.id.addIncomeTrans:
                startAddTransactionAct(true);
                break;
            case R.id.fabTrans:
                animateFAB();
                break;
            case R.id.fabCyclic:
                Snackbar.make(fabCyclic, "Cyclic", Snackbar.LENGTH_SHORT).show();
                break;
            case R.id.fabSaves:
                Snackbar.make(fabSaves, "Saves", Snackbar.LENGTH_SHORT).show();
                break;
            case R.id.fabStats:
                Snackbar.make(fabStats, "Stats", Snackbar.LENGTH_SHORT).show();
                break;
            case R.id.walletImg_NDH:
                showWallets(v);
                break;


            default:
                throw new IllegalArgumentException("Can't recognize incoming ID");
        }
    }

    private void showWallets(View v) {
        navigationView.getMenu().clear();
        List<WalletModel> walletsList;
        SwitchCompat[] switchCompats;

        if (!v.isSelected()) {
            v.setSelected(true);
            navigationView.inflateMenu(R.menu.menu_drawer_wallets);
            walletsList = viewModel.getWalletsList();
            switchCompats = new SwitchCompat[walletsList.size()];

            SubMenu subMenu = navigationView.getMenu().getItem(0).getSubMenu();
            for (int i = 0; i < 5; i++) {
                if(i < walletsList.size()) {
                    String name = walletsList.get(i).getName();
                    subMenu.getItem(i).setTitle(name);

                    SwitchCompat actionView = (SwitchCompat) subMenu.getItem(i).getActionView();
                    actionView.setTag(walletsList.get(i));
                    actionView.setChecked(switchStates[i]);
                    switchCompats[i] = actionView;

                    int finalI = i;
                    actionView.setOnCheckedChangeListener((buttonView, isChecked) -> {
                        for (SwitchCompat switchCompat : switchCompats) {
                            switchCompat.setChecked(false);
                        }

                        ((WalletModel) buttonView.getTag()).setActive(isChecked);
                        switchStates[finalI] = isChecked;
                        buttonView.setChecked(isChecked);

                    });
                }else{
                    subMenu.getItem(i).setVisible(false);
                }
            }
        } else {
            v.setSelected(false);
            navigationView.inflateMenu(R.menu.menu_drawer);
        }
    }

    public void startAddTransactionAct(boolean isIncome) {
        Intent intent = new Intent(this, AddTransactionActivity.class);
        intent.putExtra(PLConstants.INTENT_INCOME_FLAG, isIncome);

        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(this, fabAddIncome, "reveal");
        ActivityCompat.startActivity(this, intent, optionsCompat.toBundle());
    }


    public void animateFAB() {
        if (isTransFabOpen) {
            fabTrans.startAnimation(rotate_backward);
            fabAddIncome.startAnimation(fab_close);
            fabAddExpense.startAnimation(fab_close);
            fabAddIncome.setClickable(false);
            fabAddExpense.setClickable(false);
            isTransFabOpen = false;
        } else {
            fabTrans.startAnimation(rotate_forward);
            fabAddIncome.startAnimation(fab_open);
            fabAddExpense.startAnimation(fab_open);
            fabAddIncome.setClickable(true);
            fabAddExpense.setClickable(true);
            isTransFabOpen = true;
        }
    }


    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_options_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return id == R.id.action_settings || super.onOptionsItemSelected(item);

    }

    private void bindTabs(ActivityDrawerBinding binding) {
        TabLayout tabLayout = binding.content.tabs;

        ViewPager viewPager = binding.content.viewPagerContainer;
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setOnTabSelectedListener(new OnTabSelectedListener(viewPager));
    }

    private void bindDrawer(ActivityDrawerBinding binding, Toolbar toolbar) {
        NavigationView navigationView = binding.navView;
        navigationView.setNavigationItemSelectedListener(this);

        mDrawer = binding.drawerLayout;
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.setDrawerListener(toggle);
        toggle.syncState();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_wallets) {
            Intent callingIntent = WalletActivity.getCallingIntent(this, false);
            startActivity(callingIntent);
        } else if (id == R.id.nav_categories) {

        } else if (id == R.id.nav_debts) {
            replaceFragment(R.id.containerDrawer, DebtsFragment.newInstance());
        } else if (id == R.id.travel_mode) {

        } else if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_tutorials) {

        } else if (id == R.id.nav_cycle_operations) {
            replaceFragment(R.id.containerDrawer, CycleOperationFragment.newInstance());
        } else if (id == R.id.nav_transactions) {
            //         replaceFragment(R.id.containerDrawer, TransactionsFragment.newInstance());
        } else if (id == R.id.nav_logout_MD) {
            viewModel.logout();
        }

        mDrawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void initDIComponent() {
        DaggerDrawerComponent.builder()
                .applicationComponent(getApplicationComponent())
                .drawerModule(new DrawerModule())
                .activityModule(getActivityModule())
                .build().inject(this);
    }

    /**
     * A {@link FragmentStatePagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public static class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return TransactionsFragment.newInstance();
                case 1:
                    return CycleOperationFragment.newInstance();
                case 2:
                    return CycleOperationFragment.newInstance();
                case 3:
                    return CycleOperationFragment.newInstance();
                default:
                    throw new IllegalStateException("Can't recognize which fragment I should give to");
            }
        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {

            switch (position) {
                case 0:
                    return "Trans";
                case 1:
                    return "Cyclic";
                case 2:
                    return "Saves";
                case 3:
                    return "Stats";
                default:
                    throw new IllegalStateException("Can't find title.");
            }
        }
    }

    @Override
    protected void onDestroy() {
        viewModel.destroy();
        super.onDestroy();
    }
}
