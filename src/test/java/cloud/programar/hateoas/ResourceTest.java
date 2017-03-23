/*
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

package cloud.programar.hateoas;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.jayway.jsonpath.JsonPath;
import org.junit.Assert;
import lombok.Data;
import org.junit.Test;

/**
 *
 * @author ciberado
 */
public class ResourceTest {
    
    private String ROOT_PATH = "http://api.demo/v1/resources";
    
    public ResourceTest() {
    }

    @Test
    public void testResource() throws Exception {        
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.setSerializationInclusion(Include.NON_NULL);

        
        Resource<Pojo> resource = new Resource<>(new Pojo(), ROOT_PATH)
                .addAdditionalProperty("c", "gamma")
                .addCurie("pc", "http://docs.api.demo/v1/{rel}")
                .addLink("_self", "/a")
                .addLink("_next", "/b")
                .addLink("pc:refresh", "/refresh")
                .addEmbedded("pojos", new Resource<>(new Pojo()));

        String json = mapper.writeValueAsString(resource);
        System.out.println(json);
        
        String a = JsonPath.read(json, "a");
        Assert.assertEquals("a is alpha", "alpha", a);
        int b = JsonPath.read(json, "b");
        Assert.assertEquals("b is 1", 1, b);
        String c = JsonPath.read(json, "c");
        Assert.assertEquals("c is gamma", "gamma", c);
        String _self = JsonPath.read(json, "_links._self.href");
        Assert.assertEquals("_self is ok", "http://api.demo/v1/resources/a", _self);
        String embeddedA = JsonPath.read(json, "_embedded.pojos[0].a");
        Assert.assertEquals("_embedded is ok", "alpha", embeddedA);
    }
    
    @Data
    class Pojo {
        String a = "alpha";
        int b = 1;
    }
    
}
