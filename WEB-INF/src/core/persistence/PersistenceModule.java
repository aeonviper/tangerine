package core.persistence;

import static com.google.inject.matcher.Matchers.annotatedWith;
import static com.google.inject.matcher.Matchers.any;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.aopalliance.intercept.MethodInterceptor;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;

import com.google.inject.AbstractModule;

import core.mybatis.MapperProvider;
import core.mybatis.SqlSessionHolderProvider;

public class PersistenceModule extends AbstractModule {

	private final Properties readWriteProperties;
	private final Properties readOnlyProperties;
	private final List<Class> mapperClassList = new ArrayList<Class>();

	public PersistenceModule(List<Class> mapperClassList, Properties... propertiesList) {
		if (propertiesList != null && propertiesList.length == 1) {
			readWriteProperties = propertiesList[0];
			readOnlyProperties = null;
		} else if (propertiesList != null && propertiesList.length == 2) {
			readWriteProperties = propertiesList[0];
			readOnlyProperties = propertiesList[1];
		} else {
			readWriteProperties = null;
			readOnlyProperties = null;
		}
		for (Class mapperClass : mapperClassList) {
			this.mapperClassList.add(mapperClass);
		}
	}

	@Override
	protected final void configure() {

		Configuration configurationReadWrite = new Configuration();
		Configuration configurationReadOnly = new Configuration();
		SqlSessionFactory sqlSessionFactoryReadWrite = null;
		SqlSessionFactory sqlSessionFactoryReadOnly = null;

		if (readWriteProperties != null) {
			/* read write */
			PoolProperties poolPropertiesReadWrite = new PoolProperties();
			poolPropertiesReadWrite.setDriverClassName("org.postgresql.Driver");
			poolPropertiesReadWrite.setMaxActive(25);
			poolPropertiesReadWrite.setMaxIdle(25);
			poolPropertiesReadWrite.setInitialSize(5);
			poolPropertiesReadWrite.setUrl(readWriteProperties.getProperty("connection.url"));
			poolPropertiesReadWrite.setUsername(readWriteProperties.getProperty("connection.username"));
			poolPropertiesReadWrite.setPassword(readWriteProperties.getProperty("connection.password"));

			DataSource dataSourceReadWrite = new DataSource();
			dataSourceReadWrite.setPoolProperties(poolPropertiesReadWrite);
			Environment environmentReadWrite = new Environment("production", new JdbcTransactionFactory(), dataSourceReadWrite);

			configurationReadWrite.setEnvironment(environmentReadWrite);
			sqlSessionFactoryReadWrite = new SqlSessionFactoryBuilder().build(configurationReadWrite);
		}

		if (readOnlyProperties != null) {
			/* read only */
			PoolProperties poolPropertiesReadOnly = new PoolProperties();
			poolPropertiesReadOnly.setDriverClassName("org.postgresql.Driver");
			poolPropertiesReadOnly.setMaxActive(25);
			poolPropertiesReadOnly.setMaxIdle(25);
			poolPropertiesReadOnly.setInitialSize(5);
			poolPropertiesReadOnly.setUrl(readOnlyProperties.getProperty("connection.url"));
			poolPropertiesReadOnly.setUsername(readOnlyProperties.getProperty("connection.username"));
			poolPropertiesReadOnly.setPassword(readOnlyProperties.getProperty("connection.password"));

			DataSource dataSourceReadOnly = new DataSource();
			dataSourceReadOnly.setPoolProperties(poolPropertiesReadOnly);
			Environment environmentReadOnly = new Environment("production", new JdbcTransactionFactory(), dataSourceReadOnly);

			configurationReadOnly.setEnvironment(environmentReadOnly);
			sqlSessionFactoryReadOnly = new SqlSessionFactoryBuilder().build(configurationReadOnly);
		} else {
			sqlSessionFactoryReadOnly = sqlSessionFactoryReadWrite;
		}

		/* module */
		bind(Configuration.class).toInstance(configurationReadWrite);
		bind(PersistenceService.class).toInstance(new PersistenceService(sqlSessionFactoryReadWrite, sqlSessionFactoryReadOnly));
		bind(SqlSession.class).toProvider(SqlSessionHolderProvider.class);

		MethodInterceptor transactionInterceptor = new TransactionInterceptor();
		requestInjection(transactionInterceptor);

		// class-level @Transactional
		// bindInterceptor(annotatedWith(Transactional.class), any(), getTransactionInterceptor());
		// method-level @Transacational
		bindInterceptor(any(), annotatedWith(Transactional.class), transactionInterceptor);

		for (Class mapperClass : mapperClassList) {
			addMapperClass(configurationReadWrite, configurationReadOnly, mapperClass);
		}

		System.out.println(this.getClass().getSimpleName() + "> configurationReadWrite:" + configurationReadWrite + " configurationReadOnly:" + configurationReadOnly);
	}

	protected final void addMapperClass(Configuration configurationReadWrite, Configuration configurationReadOnly, Class<?> mapperClass) {
		configurationReadWrite.addMapper(mapperClass);
		configurationReadOnly.addMapper(mapperClass);
		// is scope correct ?
		bind(mapperClass).toProvider(new MapperProvider(mapperClass));
	}

}
