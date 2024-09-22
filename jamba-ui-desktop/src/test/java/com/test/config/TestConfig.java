package com.test.config;

import io.github.dumijdev.jambaui.core.annotations.Configuration;
import io.github.dumijdev.jambaui.core.annotations.Injectable;
import io.github.dumijdev.jambaui.core.annotations.Property;

@Configuration
public class TestConfig {


    @Injectable
    public TestInjectable create(@Property("${app.name}") String name) {
        return new TestInjectable(name);
    }
}
