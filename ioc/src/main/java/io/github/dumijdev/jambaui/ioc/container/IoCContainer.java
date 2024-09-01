package io.github.dumijdev.jambaui.ioc.container;

public interface IoCContainer<K, V> {
    void registerFromBase(Class<?> rootClass);

    V resolve(K k);

    void register(K k, V v);
}
