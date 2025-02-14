package IntegrationTest.testcontainers;

import java.util.Map;
import java.util.stream.Stream;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.lifecycle.Startables;

@ContextConfiguration(initializers = AbstractIntegrationTest.Initializer.class)
public class AbstractIntegrationTest {

	static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext>{
			 static MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0.29");
			
			
			private static void startContainers() {
				Startables.deepStart(Stream.of(mysql)).join();
			}

			
			private static Map<String, String> createConnectionConfiguratiton() {
				return Map.of(
						"spring.datasource.url", " jdbc:mysql://localhost:3306/api_with_spring",
						"spring.datasource.username", "root",
						"spring.datasource.password", "Kelowna"
						);
			}
			
			@SuppressWarnings({ "unchecked", "rawtypes" })
			@Override
			public void initialize(ConfigurableApplicationContext applicationContext) {
				startContainers();
				ConfigurableEnvironment environment = applicationContext.getEnvironment();
				MapPropertySource testContainers = new MapPropertySource
						("testContainers", (Map) createConnectionConfiguratiton());
				environment.getPropertySources().addFirst(testContainers);
			}


	
	}

}
