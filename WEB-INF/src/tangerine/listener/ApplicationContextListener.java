package tangerine.listener;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import orion.core.Constant;
import orion.core.Core;
import orion.navigation.Navigation;
import tangerine.mapper.AdministratorMapper;
import tangerine.mapper.AnswerMapper;
import tangerine.mapper.ConversationMapper;
import tangerine.mapper.DraftQuestionMapper;
import tangerine.mapper.ShowMapper;
import tangerine.mapper.QuestionMapper;
import tangerine.mapper.SequenceMapper;
import tangerine.mapper.UserExpenseMapper;
import tangerine.mapper.UserMapper;
import tangerine.mapper.UserSubscriptionMapper;
import tangerine.mapper.UserVoucherMapper;
import tangerine.mapper.VerificationMapper;
import tangerine.mapper.VoucherMapper;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;

import core.persistence.PersistenceModule;

public class ApplicationContextListener implements ServletContextListener {

	public void contextInitialized(ServletContextEvent servletContextEvent) {

		Constant.setContextPath(servletContextEvent.getServletContext().getContextPath());

		Injector injector = Guice.createInjector(new AbstractModule() {
			protected void configure() {
				Properties readOnlyProperties = new Properties();
				readOnlyProperties.put("connection.url", tangerine.core.Constant.databaseUrl);
				readOnlyProperties.put("connection.username", tangerine.core.Constant.databaseUsername);
				readOnlyProperties.put("connection.password", tangerine.core.Constant.databasePassword);

				Properties readWriteProperties = new Properties();
				readWriteProperties.put("connection.url", tangerine.core.Constant.databaseUrl);
				readWriteProperties.put("connection.username", tangerine.core.Constant.databaseUsername);
				readWriteProperties.put("connection.password", tangerine.core.Constant.databasePassword);

				List<Class> mapperClassList = new ArrayList<Class>();

				mapperClassList.add(UserMapper.class);

				mapperClassList.add(UserExpenseMapper.class);
				mapperClassList.add(UserSubscriptionMapper.class);

				mapperClassList.add(VoucherMapper.class);
				mapperClassList.add(UserVoucherMapper.class);

				mapperClassList.add(QuestionMapper.class);
				mapperClassList.add(DraftQuestionMapper.class);
				mapperClassList.add(ConversationMapper.class);
				mapperClassList.add(AnswerMapper.class);
				mapperClassList.add(VerificationMapper.class);
				mapperClassList.add(SequenceMapper.class);
				
				mapperClassList.add(AdministratorMapper.class);

				mapperClassList.add(ShowMapper.class);

				install(new PersistenceModule(mapperClassList, readWriteProperties, readOnlyProperties));
			}
		});
		Core.setInjector(injector);

		Navigation.configuration();

	}

	public void contextDestroyed(ServletContextEvent servletContextEvent) {

	}

}
