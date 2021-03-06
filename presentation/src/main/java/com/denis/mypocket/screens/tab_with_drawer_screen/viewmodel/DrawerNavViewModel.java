package com.denis.mypocket.screens.tab_with_drawer_screen.viewmodel;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.denis.domain.interactor.DefaultSubscriber;
import com.denis.domain.interactor.facades.NavigationDrawerFacade;
import com.denis.domain.models.User;
import com.denis.domain.models.Wallet;
import com.denis.mypocket.internal.di.PerActivity;
import com.denis.mypocket.model.UserModel;
import com.denis.mypocket.model.WalletModel;
import com.denis.mypocket.model.mapper.ModelMapper;
import com.denis.mypocket.model.mapper.UserModelMapper;
import com.denis.mypocket.model.mapper.WalletModelDataMapper;
import com.denis.mypocket.viewmodel.ViewModel;
import com.fernandocejas.frodo.annotation.RxLogSubscriber;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.net.ssl.HttpsURLConnection;

import rx.Subscriber;

/**
 * Created by denis on 4/29/16.
 */
@PerActivity // TODO: 5/26/16 MUltiple instance of this class, after logout
public class DrawerNavViewModel implements ViewModel {

    private NavigationDrawerFacade facadeInterceptor;
    private Context context;
    private ModelMapper<User, UserModel> userModelMapper = new UserModelMapper();
    private ModelMapper<Wallet, WalletModel> modelMapper = new WalletModelDataMapper();

    private UserModel userModel = null;
    private List<WalletModel> walletsList = new ArrayList<>();

    private Subscriber<List<User>> userSubscriber = new DefaultSubscriber<List<User>>() {
        @Override
        public void onNext(List<User> users) {
            if (users != null && !users.isEmpty())
                userModel = userModelMapper.toModel(users.get(0));
        }
    };

    @Inject
    public DrawerNavViewModel(Context context, NavigationDrawerFacade facadeInterceptor) {
        this.facadeInterceptor = facadeInterceptor;
        this.context = context;

        facadeInterceptor.retriveUser(userSubscriber); //retrieve user
        facadeInterceptor.retrieveAllWallets(new WalletsSubscriber()); //retrieve his wallets
    }

    @Override
    public void destroy() {
        userModelMapper = null;
        context = null;
        facadeInterceptor.unSubscribe();
    }

    public void logout() {
        facadeInterceptor.logout(new LogoutSubscriber());
    }

    public UserModel getUser() {
        return userModel;
    }

    public void updateWallets(List<WalletModel> wallets) {
        List<Wallet> walletList = modelMapper.fromModel(wallets);
        facadeInterceptor.updateWallets(new WalletsSubscriber(), walletList);
    }

    @RxLogSubscriber
    private class LogoutSubscriber extends DefaultSubscriber<Integer> {
        @Override
        public void onCompleted() {
            super.onCompleted();
        }

        @Override
        public void onError(Throwable e) {
            super.onError(e);
        }

        @Override
        public void onNext(Integer code) {
            if (code == HttpsURLConnection.HTTP_OK)
                facadeInterceptor.deleteToken(new DeleteTokenSubscriber());

        }
    }

    @RxLogSubscriber
    private class DeleteTokenSubscriber extends DefaultSubscriber<Boolean> {
        @Override
        public void onNext(Boolean isTokenDeleted) {
            if (isTokenDeleted)
                facadeInterceptor.deleteUser(new DeleteUserSubscriber());
        }
    }

    @RxLogSubscriber
    private class DeleteUserSubscriber extends DefaultSubscriber<Boolean> {
        @Override
        public void onNext(Boolean isTokenDeleted) {
            if (isTokenDeleted) {
                String packageName = context.getPackageName();
                Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                ((Activity) context).finish();
            }
        }


    }

    @RxLogSubscriber
    private class WalletsSubscriber extends DefaultSubscriber<List<Wallet>> {
        @Override
        public void onNext(List<Wallet> wallets) {
            if (wallets != null && !wallets.isEmpty()) {
                walletsList.clear();
                walletsList.addAll(modelMapper.toModel(wallets));
            }
        }
    }

    public List<WalletModel> getWalletsList() {
        return walletsList;
    }
}
