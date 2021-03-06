/*
 * Copyright 2017-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package spring.skills.core;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Implementation of {@link SpeechRequestDispatcher} that delegates to a bean whose ID is the same
 * as the the {@link SpeechRequest}'s intent name. Supports flexible naming such that a bean named
 * "hello" can handle requests where the intent name is "hello", "helloIntent" or "Hello". 
 * 
 * @author Craig Walls
 */
public class BeanNameSpeechRequestDispatcher implements SpeechRequestDispatcher, ApplicationContextAware {

	private static Logger logger = Logger.getLogger(BeanNameSpeechRequestDispatcher.class.getName());

	private ApplicationContext context;
	private SpeechRequestHandler unknownIntentHandler;

	private SessionEndedSpeechRequestHandler sessionEndedHandler;

	private SpeechRequestHandler launchRequestHandler;
	
	public BeanNameSpeechRequestDispatcher(SpeechRequestHandler unknownIntentHandler,
			SpeechRequestHandler launchRequestHandler, // TODO: Figure out a better way and what to do if not specified
			SessionEndedSpeechRequestHandler sessionEndedHandler) {
		this.unknownIntentHandler = unknownIntentHandler;
		this.launchRequestHandler = launchRequestHandler;
		this.sessionEndedHandler = sessionEndedHandler;
	}

	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		this.context = context;
	}
	
	@Override
	public SpeechResponse dispatchRequest(SpeechRequest request) {
		if (request instanceof IntentSpeechRequest) {
			IntentSpeechRequest intentRequest = (IntentSpeechRequest) request;
			String intentName = intentRequest.getIntentName();
			String beanName = IntentToBeanNameUtils.beanifyIntentName(intentName);
			try {
				SpeechRequestHandler handler = context.getBean(beanName, SpeechRequestHandler.class);
				return handler.handle(request);
			} catch (NoSuchBeanDefinitionException nsbe) {
				String errorMessage = "No bean " + beanName + " found for intent " + intentName + ".";
				logger.log(Level.WARNING, errorMessage);
				return unknownIntentHandler.handle(request);
			}	
		} else if (request instanceof LaunchSpeechRequest) {
			return launchRequestHandler.handle(request);
		} else if (request instanceof SessionEndedSpeechRequest) {
			if (sessionEndedHandler != null) {
				sessionEndedHandler.handle(request);
			}
			logger.log(Level.INFO, "Dispatching session ended request.");
			return new SessionEndedSpeechResponse();
		}
		
		logger.info("Unable to handle request: " + request.getClass().getName());
		
		return unknownIntentHandler.handle(request);
	}

}
