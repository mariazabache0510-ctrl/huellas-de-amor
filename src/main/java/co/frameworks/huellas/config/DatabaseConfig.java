package co.frameworks.huellas.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Convierte DATABASE_URL de Render (postgres://...) a JDBC si está presente.
 */
@Configuration
public class DatabaseConfig {

    @Bean
    @Primary
    @ConditionalOnExpression("T(System).getenv('DATABASE_URL') != null && !T(System).getenv('DATABASE_URL').isBlank()")
    DataSource renderDataSource() throws URISyntaxException {
        String databaseUrl = System.getenv("DATABASE_URL");
        URI uri = new URI(databaseUrl);

        String userInfo = uri.getUserInfo();
        String username = userInfo.split(":", 2)[0];
        String password = userInfo.split(":", 2)[1];
        String jdbcUrl = "jdbc:postgresql://" + uri.getHost()
                + (uri.getPort() > 0 ? ":" + uri.getPort() : "")
                + uri.getPath()
                + (uri.getQuery() != null ? "?" + uri.getQuery() : "");

        if (!jdbcUrl.contains("sslmode=")) {
            jdbcUrl = jdbcUrl + (jdbcUrl.contains("?") ? "&" : "?") + "sslmode=require";
        }

        return DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .url(jdbcUrl)
                .username(username)
                .password(password)
                .build();
    }
}
