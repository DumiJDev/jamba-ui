package io.github.dumijdev.jambaui.data.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import io.github.dumijdev.jambaui.context.container.ApplicationContainer;
import io.github.dumijdev.jambaui.context.scanner.ApplicationScanner;
import io.github.dumijdev.jambaui.core.annotations.Configuration;
import io.github.dumijdev.jambaui.core.annotations.Injectable;
import io.github.dumijdev.jambaui.core.annotations.OnDestroy;
import jakarta.data.repository.Repository;
import jakarta.persistence.Entity;
import org.hibernate.StatelessSession;
import org.hibernate.annotations.Generated;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.util.List;
import java.util.Properties;

@Configuration
public class JambaDataConfig {
    private final Logger logger = LoggerFactory.getLogger(JambaDataConfig.class);
    private StatelessSession statelessSession;

    public JambaDataConfig() {
        ApplicationContainer.registerAnnotation(Generated.class);
        ApplicationContainer.registerAnnotation(Repository.class);
    }

    @Injectable
    public StatelessSession statelessSession(JambaUiDataConfigModel model) {
        DataSource dataSource = getDataSource(model);

        // Configurar Hibernate
        Properties hibernateProperties = new Properties();

        if (!"h2".equalsIgnoreCase(model.getPlatform())) {
            hibernateProperties.put(Environment.DIALECT, model.getDialect());
        }

        hibernateProperties.put(Environment.SHOW_SQL, model.getShowSql());
        hibernateProperties.put(Environment.HBM2DDL_AUTO, model.getDdlAuto());

        var configuration = new org.hibernate.cfg.Configuration();
        configuration.setProperties(hibernateProperties);

        configuration.getProperties().put(Environment.JAKARTA_NON_JTA_DATASOURCE, dataSource);

        var scanner = ApplicationScanner.getInstance();

        scanner.getClassesAnnotatedBy(Entity.class)
                .forEach(configuration::addAnnotatedClass);

        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties()).build();

        statelessSession = configuration.buildSessionFactory(serviceRegistry).openStatelessSession();

        return statelessSession;
    }

    private static DataSource getDataSource(JambaUiDataConfigModel model) {
        HikariConfig hikariConfig = new HikariConfig();

        hikariConfig.setJdbcUrl(model.getJdbcUrl());
        hikariConfig.setUsername(model.getUsername());
        hikariConfig.setPassword(model.getPassword());
        hikariConfig.setDriverClassName(model.getJdbcDriver());
        hikariConfig.setAutoCommit(true);
        hikariConfig.setMaximumPoolSize(5);

        int poolSize = model.getPoolSize();

        if (!List.of("h2", "sqlite").contains(model.getPlatform())) {
            poolSize = 1;
        }

        hikariConfig.setMaximumPoolSize(poolSize);
        return new HikariDataSource(hikariConfig);
    }

    @OnDestroy
    public void onDestroy() {
        logger.info("Closing connection to database...");
        statelessSession.close();
    }


}
