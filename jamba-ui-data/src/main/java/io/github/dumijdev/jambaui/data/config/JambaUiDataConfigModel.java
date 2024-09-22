package io.github.dumijdev.jambaui.data.config;

import io.github.dumijdev.jambaui.core.annotations.Injectable;
import io.github.dumijdev.jambaui.core.annotations.Property;

@Injectable
public class JambaUiDataConfigModel {

    @Property("${jambaui.data.jpa.dialect}")
    private String dialect;
    @Property("${jambaui.data.jpa.driver_class}")
    private String jdbcDriver;
    @Property("${jambaui.data.jpa.url}")
    private String jdbcUrl;
    @Property("${jambaui.data.jpa.username}")
    private String username;
    @Property("${jambaui.data.jpa.password}")
    private String password;
    @Property("${jambaui.data.jpa.ddl_auto}")
    private String ddlAuto;
    @Property("${jambaui.data.jpa.show_sql}")
    private String showSql;
    @Property("${jambaui.data.jpa.pool_size}")
    private int poolSize;

    public JambaUiDataConfigModel() {

    }

    public String getDialect() {
        return dialect;
    }

    public String getJdbcDriver() {
        return jdbcDriver;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getDdlAuto() {
        return ddlAuto;
    }

    public String getShowSql() {
        return showSql;
    }

    @Override
    public String toString() {
        return "JambaUiDataConfigModel{" +
                "dialect='" + dialect + '\'' +
                ", jdbcDriver='" + jdbcDriver + '\'' +
                ", jdbcUrl='" + jdbcUrl + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", ddlAuto='" + ddlAuto + '\'' +
                ", showSql='" + showSql + '\'' +
                ", poolSize=" + poolSize +
                '}';
    }

    public int getPoolSize() {
        return poolSize;
    }
}
