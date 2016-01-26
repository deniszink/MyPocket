package com.denis.mypocket.internal.di.modules;

import android.util.Log;

import com.denis.data.entity.mapper.WalletDataMapper;
import com.denis.data.local_store.RealmStore;
import com.denis.data.local_store.WalletRealmStore;
import com.denis.data.repository.WalletDataRepository;
import com.denis.data.repository.datasource.interfaces.WalletDataStore;
import com.denis.data.repository.datasource.WalletLocalDataStore;
import com.denis.domain.executor.PostExecutionThread;
import com.denis.domain.executor.ThreadExecutor;
import com.denis.domain.interactor.AddWalletUseCase;
import com.denis.domain.interactor.UseCase;
import com.denis.domain.models.Wallet;
import com.denis.domain.repository.WalletRepository;
import com.denis.mypocket.internal.di.PerActivity;
import com.denis.mypocket.utils.PLTags;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;
import io.realm.Realm;

@Module
public class WalletModule {
    public WalletModule() {
        Log.d(PLTags.INSTANCE_TAG,"Wallet Module, "+hashCode());
    }
    //region Wallet Use Case dependencies

    @Provides
    @PerActivity
    WalletDataMapper getWalletEntityDataMapper(){
        return new WalletDataMapper();
    }

    @Provides @PerActivity
    RealmStore getRealmStore(Realm realm){
        return new WalletRealmStore(realm);
    }

    @Provides @PerActivity
    WalletDataStore walletDataStore(RealmStore store){
        return new WalletLocalDataStore(store);
    }

    @Provides @PerActivity
    WalletRepository provideUserRepository(WalletDataMapper mapper, WalletDataStore walletDataStore) {
        return new WalletDataRepository(mapper,walletDataStore);
    }

    @Provides @PerActivity @Named("addWallet")
    UseCase<Wallet> getAddWalletUseCase(ThreadExecutor threadExecutor, PostExecutionThread postExecutionThread, WalletRepository repository){
        return new AddWalletUseCase(threadExecutor,postExecutionThread,repository);
    }
    //endregion
}
