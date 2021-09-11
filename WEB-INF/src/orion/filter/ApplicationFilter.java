package orion.filter;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.FileCleanerCleanup;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileCleaningTracker;

import orion.annotation.Cookie;
import orion.annotation.Request;
import orion.annotation.Response;
import orion.annotation.Session;
import orion.configuration.Configuration;
import orion.controller.Attachment;
import orion.core.Constant;
import orion.core.Core;
import orion.core.Utility;
import orion.navigation.Handle;
import orion.navigation.Navigation;
import orion.view.View;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import core.utility.BeanUtility;
import core.utility.Logger;

public class ApplicationFilter implements Filter {

	protected String characterEncoding = "UTF-8";
	protected Gson gson = new Gson();
	protected Gson gsonEnumBean = new GsonBuilder().registerTypeAdapterFactory(new GsonEnumTypeAdapterFactory()).create();

	public void init(FilterConfig config) throws ServletException {
		String characterEncoding = config.getInitParameter("characterEncoding");
		if (characterEncoding != null) {
			this.characterEncoding = characterEncoding;
		}
	}

	public void destroy() {
	}

	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain) throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;

		String path = Utility.getPath(request);

		Handle handle = Navigation.controllerFor(path);
		if (handle == null) {

			chain.doFilter(servletRequest, servletResponse);

		} else {

			if (request.getCharacterEncoding() == null) {
				request.setCharacterEncoding(characterEncoding);
			}

			Object instance = Core.getInjector().getInstance(handle.getController().getControllerClass());

			for (Map.Entry<Annotation, List<Method>> entry : handle.getController().getAnnotationMethodMap().entrySet()) {
				try {

					if (entry.getKey() instanceof Request) {
						Request annotation = (Request) entry.getKey();
						for (Method method : entry.getValue()) {
							Class[] parameterTypes = method.getParameterTypes();
							if (parameterTypes.length == 1) {
								if (annotation.attribute() == null || annotation.attribute().isEmpty()) {
									Class parameterType = parameterTypes[0];
									if (parameterType.isInstance(request)) {
										method.invoke(instance, new Object[] { request });
									} else if (request.getAttribute(parameterType.getCanonicalName()) != null) {
										method.invoke(instance, new Object[] { request.getAttribute(parameterType.getCanonicalName()) });
									}
								} else {
									method.invoke(instance, new Object[] { request.getAttribute(annotation.attribute()) });
								}
							}
						}
					} else if (entry.getKey() instanceof Response) {
						for (Method method : entry.getValue()) {
							method.invoke(instance, new Object[] { response });
						}
					} else if (entry.getKey() instanceof Session) {
						Session annotation = (Session) entry.getKey();
						for (Method method : entry.getValue()) {
							Class[] parameterTypes = method.getParameterTypes();
							if (parameterTypes.length == 1) {
								HttpSession session = request.getSession(false);
								if (session != null) {
									if (annotation.attribute() == null || annotation.attribute().isEmpty()) {
										Class parameterType = parameterTypes[0];
										if (parameterType.isInstance(session)) {
											method.invoke(instance, new Object[] { session });
										} else if (session.getAttribute(parameterType.getCanonicalName()) != null) {
											method.invoke(instance, new Object[] { session.getAttribute(parameterType.getCanonicalName()) });
										}
									} else {
										method.invoke(instance, new Object[] { session.getAttribute(annotation.attribute()) });
									}
								}
							}
						}
					} else if (entry.getKey() instanceof Cookie) {
						Cookie annotation = (Cookie) entry.getKey();
						String name = annotation.name();
						String value = null;
						if (name != null && !name.isEmpty()) {
							if (request.getCookies() != null) {
								for (javax.servlet.http.Cookie cookie : request.getCookies()) {
									if (name.equals(cookie.getName())) {
										// more than 1 value ?
										value = cookie.getValue();
									}
								}
							}
						}
						for (Method method : entry.getValue()) {
							Class[] parameterTypes = method.getParameterTypes();
							if (parameterTypes.length == 1) {
								if (parameterTypes[0] == String.class) {
									method.invoke(instance, new Object[] { value });
								}
							}
						}
					}

				} catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
					e.printStackTrace();
				}
			}

			Map<String, String[]> parameterMap = new HashMap<>();
			for (Map.Entry<String, String[]> entry : request.getParameterMap().entrySet()) {
				boolean valid = false;
				for (String v : entry.getValue()) {
					if (v.length() > 0) {
						valid = true;
						break;
					}
				}
				if (valid) {
					parameterMap.put(entry.getKey(), entry.getValue());
				}
			}

			parameterMap.putAll(handle.getParameterMap());

			String contentType = request.getContentType();

			if (contentType != null && contentType.toLowerCase().contains("application/json")) {
				Map<String, Object> map = (Map<String, Object>) gson.fromJson(request.getReader(), Map.class);
				for (Map.Entry<String, Object> entry : map.entrySet()) {
					try {
						Class propertyType = PropertyUtils.getPropertyType(instance, entry.getKey());
						if (propertyType != null) {
							Object property = gson.fromJson(gson.toJson(entry.getValue()), propertyType);
							BeanUtility.instance().setProperty(instance, entry.getKey(), property);
						}
					} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
						e.printStackTrace();
					}
				}
			}

			if (contentType != null && contentType.toLowerCase().contains("multipart")) {
				FileCleaningTracker fileCleaningTracker = FileCleanerCleanup.getFileCleaningTracker(request.getServletContext());
				DiskFileItemFactory factory = new DiskFileItemFactory();
				factory.setFileCleaningTracker(fileCleaningTracker);
				ServletFileUpload upload = new ServletFileUpload(factory);

				try {
					Map<String, List<String>> fileItemMap = new HashMap<>();
					List<FileItem> fileItemList = upload.parseRequest(request);
					Iterator<FileItem> fileItemIterator = fileItemList.iterator();
					while (fileItemIterator.hasNext()) {
						FileItem fileItem = fileItemIterator.next();
						if (fileItem.isFormField()) {
							String name = fileItem.getFieldName();
							String value = fileItem.getString();

							List<String> valueList = fileItemMap.get(name);
							if (valueList != null) {
								fileItemMap.get(name).add(value);
							} else {
								fileItemMap.put(name, valueList = new ArrayList<>());
								valueList.add(value);
							}
						} else {
							if (fileItem.getSize() != 0) {
								try {
									PropertyUtils.setProperty(instance, fileItem.getFieldName(), new Attachment(fileItem.getName(), fileItem.getContentType(), fileItem.getSize(), fileItem));
								} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {

								}
							}
						}
					}
					for (Map.Entry<String, List<String>> entry : fileItemMap.entrySet()) {
						parameterMap.put(entry.getKey(), entry.getValue().toArray(new String[0]));
					}

				} catch (FileUploadException e) {
					e.printStackTrace();
				}
			}

			try {
				BeanUtility.instance().populate(instance, parameterMap);
			} catch (IllegalAccessException | InvocationTargetException e) {
				e.printStackTrace();
			}

			request.setAttribute(Constant.model, instance);

			Throwable throwable = null;
			Method pathMethod = handle.getPathMethod();
			View view = null;
			if (pathMethod != null) {
				try {
					view = (View) pathMethod.invoke(instance, new Object[0]);
				} catch (Throwable t) {
					throwable = t;
				}
			} else {
				Logger.getLogger().warn("No path method for " + path);
			}

			if (throwable != null) {
				if (throwable instanceof InvocationTargetException) {
					throwable = ((InvocationTargetException) throwable).getTargetException();
				}
				throw new RuntimeException(throwable);
			}

			if (view != null) {
				if (view.getStatusCode() != null) {
					response.setStatus(view.getStatusCode());
				}
				if (View.Type.FORWARD == view.getType()) {
					response.setCharacterEncoding(characterEncoding);
					request.getRequestDispatcher((String) view.getValue()).forward(request, response);
				} else if (View.Type.REDIRECT == view.getType()) {
					response.sendRedirect(Constant.getContextPath() + (String) view.getValue());
				} else if (View.Type.JSON == view.getType()) {
					response.setCharacterEncoding(characterEncoding);
					response.setContentType("application/json");
					PrintWriter pw = response.getWriter();
					if (Boolean.TRUE.equals(view.getOptionAsBoolean("enumBean"))) {
						pw.println(gsonEnumBean.toJson(view.getValue()));
					} else {
						pw.println(gson.toJson(view.getValue()));
					}
				}
			} else {
				Logger.getLogger().warn("View is null for " + path);
			}
		}

	}

}
