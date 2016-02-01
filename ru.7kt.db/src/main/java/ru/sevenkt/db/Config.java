package ru.sevenkt.db;

import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.instrument.classloading.InstrumentationLoadTimeWeaver;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.hibernate4.HibernateExceptionTranslator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@ComponentScan(basePackages = { "ru.sevenkt.db.services" })
//@EnableJpaRepositories(basePackages = "ru.sevenkt.db.repositories")
@PropertySource(value = "META-INF/application.properties")
public class Config {

	@Autowired

	private Environment env;

	@Value("${init-db:false}")
	private String initDatabase;

	@PostConstruct
	public void init() {
		System.out.println("config!");
	}

	@Bean
	public PlatformTransactionManager transactionManager()

	{

		EntityManagerFactory factory = entityManagerFactory().getObject();

		return new JpaTransactionManager(factory);

	}

	@Bean

	public LocalContainerEntityManagerFactoryBean entityManagerFactory()

	{

		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();

		EclipseLinkJpaVendorAdapter vendorAdapter = new EclipseLinkJpaVendorAdapter();

		vendorAdapter.setGenerateDdl(Boolean.TRUE);

		vendorAdapter.setShowSql(Boolean.TRUE);

		factory.setDataSource(dataSource());

		factory.setJpaVendorAdapter(vendorAdapter);

		factory.setPersistenceUnitName("ru.7kt.db");;

		Properties jpaProperties = new Properties();

		jpaProperties.put("eclipselink.ddl-generation", env.getProperty("eclipselink.ddl-generation"));
		jpaProperties.put("eclipselink.ddl-generation.output-mode", env.getProperty("eclipselink.ddl-generation.output-mode"));
		jpaProperties.put("eclipselink.target-database", env.getProperty("eclipselink.target-database"));
		jpaProperties.put("eclipselink.weaving", env.getProperty("eclipselink.weaving"));
		jpaProperties.put("eclipselink.logging.level", env.getProperty("eclipselink.logging.level"));
		jpaProperties.put("eclipselink.logging.parameters", env.getProperty("eclipselink.logging.parameters"));

		factory.setJpaProperties(jpaProperties);

		factory.afterPropertiesSet();

		factory.setLoadTimeWeaver(new InstrumentationLoadTimeWeaver());

		return factory;

	}

	@Bean

	public HibernateExceptionTranslator hibernateExceptionTranslator()

	{

		return new HibernateExceptionTranslator();

	}

	@Bean
	public DataSource dataSource() {

		BasicDataSource dataSource = new BasicDataSource();

		dataSource.setDriverClassName(env.getProperty("jdbc.driverClassName"));

		dataSource.setUrl(env.getProperty("jdbc.url"));

		dataSource.setUsername(env.getProperty("jdbc.username"));

		dataSource.setPassword(env.getProperty("jdbc.password"));

		return dataSource;

	}

	@Bean
	public DataSourceInitializer dataSourceInitializer(DataSource dataSource)

	{

		DataSourceInitializer dataSourceInitializer = new DataSourceInitializer();

		dataSourceInitializer.setDataSource(dataSource);

		ResourceDatabasePopulator databasePopulator = new ResourceDatabasePopulator();

		databasePopulator.addScript(new ClassPathResource("db.sql"));

		dataSourceInitializer.setDatabasePopulator(databasePopulator);

		dataSourceInitializer.setEnabled(Boolean.parseBoolean(initDatabase));

		return dataSourceInitializer;

	}

}
