package io.github.dumijdev.jambaui.context.container;

import java.util.List;

public interface IoCContainer<K, V> {
    void registerFromBase(Class<?> rootClass);

    V resolve(K k);

    void register(K k, V v);

    List<V> resolveAll();
}
