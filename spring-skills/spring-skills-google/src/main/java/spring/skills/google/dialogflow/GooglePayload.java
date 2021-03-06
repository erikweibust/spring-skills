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
package spring.skills.google.dialogflow;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown=true)
@JsonInclude(value=Include.NON_NULL)
public class GooglePayload implements Payload {

	private boolean expectUserResponse;
	
	private String userStorage;
	
	private RichResponse richResponse;

	public boolean isExpectUserResponse() {
		return expectUserResponse;
	}

	public void setExpectUserResponse(boolean expectUserResponse) {
		this.expectUserResponse = expectUserResponse;
	}

	public String getUserStorage() {
		return userStorage;
	}

	public void setUserStorage(String userStorage) {
		this.userStorage = userStorage;
	}

	public RichResponse getRichResponse() {
		return richResponse;
	}

	public void setRichResponse(RichResponse richResponse) {
		this.richResponse = richResponse;
	}
	
}
