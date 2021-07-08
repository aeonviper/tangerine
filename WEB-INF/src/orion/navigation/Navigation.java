package orion.navigation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import orion.annotation.Cookie;
import orion.annotation.Request;
import orion.annotation.Response;
import orion.annotation.Session;
import orion.core.Constant;
import orion.view.View;
import core.persistence.Transactional;
import core.utility.Logger;
import orion.core.Utility;

public class Navigation {

	private static Configuration[] configurationArray = null;

	private static Configuration[] configurationFile() {
		List<Configuration> configurationList = new ArrayList<>();

		List<String> lines = Utility.readFileResource(Constant.navigationConfigurationFile);
		for (String line : lines) {
			line = line.trim();
			if (line.length() == 0 || line.startsWith("#")) {
				continue;
			}
			String[] fields = line.split(Constant.navigationConfigurationFileDelimiter, 4);
			Configuration configuration = new Configuration();
			configuration.setControllerName(fields[0]);
			configuration.setPathMethodName(fields[1]);
			String[] parameters = fields[2].split(",");
			for (String parameter : parameters) {
				configuration.getPathParameterList().add(parameter);
			}
			configuration.setPath(fields[3]);
			configuration.setPathPattern(Pattern.compile(configuration.getPath()));

			configurationList.add(configuration);
		}

		return configurationList.toArray(new Configuration[0]);
	}

	public static void controllerPathMethod(Configuration[] configurationArray) {
		Map<String, Boolean> controllerMap = new HashMap<>();

		for (Configuration configuration : configurationArray) {

			String controllerClassName = configuration.getControllerName();
			if (!controllerMap.containsKey(controllerClassName)) {

				controllerMap.put(controllerClassName, Boolean.TRUE);

				Class controllerClass = null;
				try {
					controllerClass = Class.forName(controllerClassName);
				} catch (ClassNotFoundException e) {
					Logger.getLogger().warn(e.getMessage());
				}

				if (controllerClass != null) {

					Controller controller = new Controller(controllerClass);

					for (Configuration config : configurationArray) {
						if (config.getControllerName().equals(controllerClassName)) {
							config.setController(controller);
						}
					}

					Map<Annotation, List<Method>> annotationMethodMap = new HashMap<>();

					for (Method method : controllerClass.getMethods()) {

						Annotation annotation = null;
						if (method.getParameterCount() == 1) {
							if ((annotation = method.getAnnotation(Request.class)) != null) {
							} else if ((annotation = method.getAnnotation(Response.class)) != null) {
							} else if ((annotation = method.getAnnotation(Session.class)) != null) {
							} else if ((annotation = method.getAnnotation(Cookie.class)) != null) {
							}
						}

						if (method.getParameterCount() == 0 && View.class.equals(method.getReturnType())) {
							for (Configuration config : configurationArray) {
								if (config.getControllerName().equals(controllerClassName)) {
									if (config.getPathMethodName().equals(method.getName())) {
										config.setPathMethod(method);
									}
								}
							}
						} else if (annotation != null) {
							List<Method> methodList = annotationMethodMap.get(annotation);
							if (methodList == null) {
								methodList = new ArrayList<>();
							}
							methodList.add(method);
							annotationMethodMap.put(annotation, methodList);
						}
					}

					controller.setAnnotationMethodMap(annotationMethodMap);
				}
			}
		}
	}

	public static void configuration() {

		configurationArray = configurationFile();
		controllerPathMethod(configurationArray);

	}

	public static Handle controllerFor(String path) {
		Matcher matcher;
		for (Configuration configuration : configurationArray) {
			if ((matcher = configuration.getPathPattern().matcher(path)).matches()) {
				Handle handle = new Handle(configuration.getController(), configuration.getPathMethod());
				for (int i = 1; i <= matcher.groupCount(); i++) {
					handle.getParameterMap().put(configuration.getPathParameterList().get(i - 1), new String[] { matcher.group(i) });
				}
				return handle;
			}
		}
		return null;
	}

}
