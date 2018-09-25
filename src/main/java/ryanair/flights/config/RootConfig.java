package ryanair.flights.config;



import java.nio.charset.StandardCharsets;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@ComponentScan("${app.base.package}")
@PropertySource(name="appConfig", value="classpath:appConfig.properties")
public class RootConfig {


	@Bean
	public static PropertySourcesPlaceholderConfigurer properties() {
		PropertySourcesPlaceholderConfigurer placeHolderConfig = new PropertySourcesPlaceholderConfigurer();
		placeHolderConfig.setFileEncoding(StandardCharsets.UTF_8.displayName());
		placeHolderConfig.setIgnoreUnresolvablePlaceholders(true);
		return placeHolderConfig;
	}


}
