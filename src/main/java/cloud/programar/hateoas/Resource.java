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

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author ciberado
 */
@NoArgsConstructor @Data
public class Resource<T> {
    @JsonIgnore(true)
    private String rootPath;
    @JsonUnwrapped
    private T content;
    private Map<String, Object> additionalProperties = new HashMap<>();
    /* Links can be both single objects (Link instances) or arrays
       of links... so we support them all using <i>Object</i>.
    */
    @JsonProperty("_links") @JsonUnwrapped 
    private Map<String, Object> links = new HashMap<>();
    @JsonProperty("_embedded") @JsonUnwrapped 
    private Map<String, List<Resource>> embeddeds = new HashMap<>();
    
    public Resource(T content) {
        this(content, null);
    }
    public Resource(T content, String rootPath) {
        this.rootPath = rootPath;
        this.content = content;
    }
    
    //http://stackoverflow.com/questions/18043587/why-im-not-able-to-unwrap-and-serialize-a-java-map-using-the-jackson-java-libra
    @JsonAnySetter
    public Resource<T> addAdditionalProperty(String name, Object value) {
        additionalProperties.put(name, value);
        return this;
    }
    
    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return additionalProperties;
    }
    
    public Resource<T> addLink(String id, String href) {
        links.put(id, new Link(null, rootPath+href, null, null, null));
        return this;
    }
    
    public Resource<T> addLink (String id, String href, String type) {
        links.put(id, new Link(null, rootPath + href, type, null, null));
        return this;
    }
    
    public Resource<T> addCurie(String name, String href) {
        List<Link> curies = (List<Link>) links.get("curies");
        if (curies == null) {
            curies = new ArrayList<>();
            links.put("curies", curies);
        }
        curies.add(new Link(name, href, null, null, true));
        return this;
    }
    
    public Resource<T> addEmbedded(String collection, Resource embedded) {
        if (embeddeds.containsKey(collection) == false) {
            embeddeds.put(collection, new ArrayList<>());
        }
        embeddeds.get(collection).add(embedded);
        return this;
    }
}
