package ru.sevenkt.db;

import java.util.Properties;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.support.JpaRepositoryFactory;
import org.springframework.instrument.classloading.InstrumentationLoadTimeWeaver;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.EclipseLinkJpaVendorAdapter;

import ru.sevenkt.db.repositories.DeviceRepo;
import ru.sevenkt.db.repositories.ErrorRepo;
import ru.sevenkt.db.repositories.JournalRepo;
import ru.sevenkt.db.repositories.MeasuringRepo;
import ru.sevenkt.db.repositories.SchedulerGroupRepo;
import ru.sevenkt.db.repositories.ParamsRepo;
import ru.sevenkt.db.repositories.ReportRepo;
import ru.sevenkt.db.services.IDBService;
import ru.sevenkt.db.services.impl.DBService;

@Configuration
//@EnableTransactionManagement
@PropertySource(value = "META-INF/application.properties")
public class Config {

	@Autowired

	private Environment env;

	@Value("${init-db:false}")
	private String initDatabase;

	

	

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory()

	{

		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();

		EclipseLinkJpaVendorAdapter vendorAdapter = new EclipseLinkJpaVendorAdapter();

		vendorAdapter.setGenerateDdl(Boolean.TRUE);

		vendorAdapter.setShowSql(Boolean.TRUE);

		factory.setDataSource(dataSource());

		factory.setJpaVendorAdapter(vendorAdapter);

		factory.setPersistenceUnitName("ru.7kt.db");

		Properties jpaProperties = new Properties();

		jpaProperties.put("eclipselink.ddl-generation", env.getProperty("eclipselink.ddl-generation"));
		jpaProperties.put("eclipselink.ddl-generation.output-mode",
				env.getProperty("eclipselink.ddl-generation.output-mode"));
		jpaProperties.put("eclipselink.target-database", env.getProperty("eclipselink.target-database"));
		jpaProperties.put("eclipselink.weaving", env.getProperty("eclipselink.weaving"));
		jpaProperties.put("eclipselink.logging.level.sql", env.getProperty("eclipselink.logging.level.sql"));
		jpaProperties.put("eclipselink.logging.parameters", env.getProperty("eclipselink.logging.parameters"));
		jpaProperties.put("eclipselink.logging.file", env.getProperty("eclipselink.logging.file"));

		factory.setJpaProperties(jpaProperties);

		factory.afterPropertiesSet();

		factory.setLoadTimeWeaver(new InstrumentationLoadTimeWeaver());

		return factory;

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
	

	@Bean
	public  DeviceRepo deviceRepo(EntityManager em){
		JpaRepositoryFactory jpaRepositoryFactory=new JpaRepositoryFactory(em);
		return jpaRepositoryFactory.getRepository(DeviceRepo.class);
	}
	@Bean
	public  MeasuringRepo measuringRepo(EntityManager em){
		JpaRepositoryFactory jpaRepositoryFactory=new JpaRepositoryFactory(em);
		return jpaRepositoryFactory.getRepository(MeasuringRepo.class);
	}
	@Bean
	public  SchedulerGroupRepo nodeRepo(EntityManager em){
		JpaRepositoryFactory jpaRepositoryFactory=new JpaRepositoryFactory(em);
		return jpaRepositoryFactory.getRepository(SchedulerGroupRepo.class);
	}
	@Bean
	public  JournalRepo journalRepo(EntityManager em){
		JpaRepositoryFactory jpaRepositoryFactory=new JpaRepositoryFactory(em);
		return jpaRepositoryFactory.getRepository(JournalRepo.class);
	}
	@Bean
	public  ParamsRepo paramsRepo(EntityManager em){
		JpaRepositoryFactory jpaRepositoryFactory=new JpaRepositoryFactory(em);
		return jpaRepositoryFactory.getRepository(ParamsRepo.class);
	}
	@Bean
	public  ErrorRepo errorRepo(EntityManager em){
		JpaRepositoryFactory jpaRepositoryFactory=new JpaRepositoryFactory(em);
		return jpaRepositoryFactory.getRepository(ErrorRepo.class);
	}
	@Bean
	public  ReportRepo reportRepo(EntityManager em){
		JpaRepositoryFactory jpaRepositoryFactory=new JpaRepositoryFactory(em);
		return jpaRepositoryFactory.getRepository(ReportRepo.class);
	}
	
	@Bean
	public IDBService dbService(){
		return new DBService();
	}
	
	@Bean
	public EntityManager entityManager(EntityManagerFactory emf){
		EntityManager em = emf.createEntityManager();
		return em;
	}

	
}
