package monami.lms.rest.exceptions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import io.sentry.spring.SentryExceptionResolver;
import io.sentry.spring.SentryServletContextInitializer;

@Configuration
public class BrokenPipeExceptionResolver {


	@Bean
	public HandlerExceptionResolver sentryExceptionResolver() {
		return new SentryExceptionResolver() {
			@Override
			public ModelAndView resolveException(HttpServletRequest request,
					HttpServletResponse response,
					Object handler,
					Exception ex) {
				Throwable rootCause = ex;

				while (rootCause .getCause() != null && rootCause.getCause() != rootCause) {
					rootCause = rootCause.getCause();
				}

				if (!rootCause.getMessage().contains("Broken pipe")) {
					//super.resolveException(request, response, handler, ex);
				}
				return null;
			}   

		};
	}

	@Bean
	public ServletContextInitializer sentryServletContextInitializer() {
		return new SentryServletContextInitializer();
	}
}