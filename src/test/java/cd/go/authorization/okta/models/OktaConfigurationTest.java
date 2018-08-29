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

package cd.go.authorization.generic.models;

import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;

import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasEntry;
import static org.junit.Assert.assertThat;

public class GenericConfigurationTest {

    @Test
    public void shouldDeserializeGenericConfiguration() throws Exception {
        final GenericConfiguration genericConfiguration = GenericConfiguration.fromJSON("{\n" +
                "  \"GenericEndpoint\": \"https://example.co.in\",\n" +
                "  \"ClientId\": \"client-id\",\n" +
                "  \"ClientSecret\": \"client-secret\"\n" +
                "}");

        assertThat(genericConfiguration.genericEndpoint(), is("https://example.co.in"));
        assertThat(genericConfiguration.clientId(), is("client-id"));
        assertThat(genericConfiguration.clientSecret(), is("client-secret"));
    }

    @Test
    public void shouldSerializeToJSON() throws Exception {
        GenericConfiguration genericConfiguration = new GenericConfiguration(
                "https://example.co.in", "client-id", "client-secret");

        String expectedJSON = "{\n" +
                "  \"GenericEndpoint\": \"https://example.co.in\",\n" +
                "  \"ClientId\": \"client-id\",\n" +
                "  \"ClientSecret\": \"client-secret\"\n" +
                "}";

        JSONAssert.assertEquals(expectedJSON, genericConfiguration.toJSON(), true);

    }

    @Test
    public void shouldConvertConfigurationToProperties() throws Exception {
        GenericConfiguration genericConfiguration = new GenericConfiguration(
                "https://example.co.in", "client-id", "client-secret");

        final Map<String, String> properties = genericConfiguration.toProperties();

        assertThat(properties, hasEntry("GenericEndpoint", "https://example.co.in"));
        assertThat(properties, hasEntry("ClientId", "client-id"));
        assertThat(properties, hasEntry("ClientSecret", "client-secret"));
    }
}
