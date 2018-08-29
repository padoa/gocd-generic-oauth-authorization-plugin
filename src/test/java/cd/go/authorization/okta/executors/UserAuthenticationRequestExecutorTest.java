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
import cd.go.authorization.generic.GenericUser;
import cd.go.authorization.generic.exceptions.NoAuthorizationConfigurationException;
import cd.go.authorization.generic.models.AuthConfig;
import cd.go.authorization.generic.models.GenericConfiguration;
import cd.go.authorization.generic.models.TokenInfo;
import cd.go.authorization.generic.requests.UserAuthenticationRequest;
import com.thoughtworks.go.plugin.api.response.GoPluginApiResponse;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.Mock;
import org.skyscreamer.jsonassert.JSONAssert;

import java.util.Collections;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

public class UserAuthenticationRequestExecutorTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    @Mock
    private UserAuthenticationRequest request;
    @Mock
    private AuthConfig authConfig;
    @Mock
    private GenericConfiguration genericConfiguration;
    @Mock
    private GenericApiClient genericApiClient;
    private UserAuthenticationRequestExecutor executor;

    @Before
    public void setUp() throws Exception {
        initMocks(this);

        when(authConfig.getConfiguration()).thenReturn(genericConfiguration);
        when(genericConfiguration.genericApiClient()).thenReturn(genericApiClient);

        executor = new UserAuthenticationRequestExecutor(request);
    }

    @Test
    public void shouldErrorOutIfAuthConfigIsNotProvided() throws Exception {
        when(request.authConfigs()).thenReturn(Collections.emptyList());

        thrown.expect(NoAuthorizationConfigurationException.class);
        thrown.expectMessage("[Authenticate] No authorization configuration found.");

        executor.execute();
    }

    @Test
    public void shouldAuthenticate() throws Exception {
        final TokenInfo tokenInfo = new TokenInfo("31239032-xycs.xddasdasdasda", 7200, "foo-type", "refresh-xysaddasdjlascdas");

        when(request.authConfigs()).thenReturn(Collections.singletonList(authConfig));
        when(request.tokenInfo()).thenReturn(tokenInfo);
        when(genericApiClient.userProfile(tokenInfo)).thenReturn(new GenericUser("foo@bar.com", "Foo Bar"));

        final GoPluginApiResponse response = executor.execute();

        String expectedJSON = "{\n" +
                "  \"roles\": [],\n" +
                "  \"user\": {\n" +
                "    \"username\": \"foo@bar.com\",\n" +
                "    \"display_name\": \"Foo Bar\",\n" +
                "    \"email\": \"foo@bar.com\"\n" +
                "  }\n" +
                "}";

        assertThat(response.responseCode(), is(200));
        JSONAssert.assertEquals(expectedJSON, response.responseBody(), true);
    }
}
