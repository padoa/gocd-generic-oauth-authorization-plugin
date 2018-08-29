/*
 * Copyright 2017 ThoughtWorks, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cd.go.authorization.generic.executors;

import cd.go.authorization.generic.GenericApiClient;
import cd.go.authorization.generic.GenericAuthorizer;
import cd.go.authorization.generic.GenericUser;
import cd.go.authorization.generic.exceptions.NoAuthorizationConfigurationException;
import cd.go.authorization.generic.models.AuthConfig;
import cd.go.authorization.generic.models.GenericConfiguration;
import cd.go.authorization.generic.models.User;
import cd.go.authorization.generic.requests.UserAuthenticationRequest;
import com.google.gson.Gson;
import com.thoughtworks.go.plugin.api.response.DefaultGoPluginApiResponse;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;

import java.util.HashMap;
import java.util.Map;

public class UserAuthenticationRequestExecutor implements RequestExecutor {
    private static final Gson GSON = new Gson();
    private final UserAuthenticationRequest request;
    private final GenericAuthorizer genericAuthorizer;

    public UserAuthenticationRequestExecutor(UserAuthenticationRequest request) {
        this(request, new GenericAuthorizer());
    }

    UserAuthenticationRequestExecutor(UserAuthenticationRequest request, GenericAuthorizer genericAuthorizer) {
        this.request = request;
        this.genericAuthorizer = genericAuthorizer;
    }

    @Override
    public GoPluginApiResponse execute() throws Exception {
        if (request.authConfigs() == null || request.authConfigs().isEmpty()) {
            throw new NoAuthorizationConfigurationException("[Authenticate] No authorization configuration found.");
        }

        final AuthConfig authConfig = request.authConfigs().get(0);
        final GenericConfiguration configuration = request.authConfigs().get(0).getConfiguration();
        final GenericApiClient genericApiClient = configuration.genericApiClient();
        final GenericUser genericUser = genericApiClient.userProfile(request.tokenInfo());

        Map<String, Object> userMap = new HashMap<>();
        userMap.put("user", new User(genericUser));
        userMap.put("roles", genericAuthorizer.authorize(genericUser, authConfig, request.roles()));

        return DefaultGoPluginApiResponse.success(GSON.toJson(userMap));
    }
}
