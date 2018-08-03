package com.viettel.api.memcache;

/**
 * Created by VTN-PTPM-NV19 on 3/24/2018.
 */
public interface Cache {
    void add(String key, Object value, long periodInMillis);

    void remove(String key);

    Object get(String key);

    void clear();

    long size();
}
