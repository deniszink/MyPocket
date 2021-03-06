package com.denis.data.local_store;

import java.util.Collection;
import java.util.List;

import rx.Observable;

/**
 * Created by denis on 1/5/16.
 */
public interface RealmStore<T>  {
    Observable<T> get(final String id);
    Observable<T> put(T item);
    Observable<List<T>> put(List<T> list);
    Observable<T> update(T item);
    Observable<List<T>> getList();
    Observable<Boolean> delete(T item);
}
