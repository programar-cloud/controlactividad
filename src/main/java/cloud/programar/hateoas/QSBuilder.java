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

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import lombok.SneakyThrows;

/**
 * Helper created to quickly define the query string of an URL.
 * 
 * @author ciberado
 */
@Data
public class QSBuilder {
    private Map<String, String> queryParams = new HashMap<>();
    
    private String path;

    public QSBuilder(String path) {
        this.path = path;
    }
    
    /** Adds a new param to the query string ONLY if it is present (not null
     * and not empty).
     */
    public QSBuilder add(String name, Object value) {
        if (value == null) return this;
        
        String valueAsString = String.valueOf(value);
        if (valueAsString.isEmpty()) return this;
        
        queryParams.put(name, valueAsString);
        return this;
    }
    
    @SneakyThrows
    public String build() {
        StringBuilder uri = new StringBuilder(path);
        if (queryParams.isEmpty() == false) {
            uri.append("?");
            for (String paramName : queryParams.keySet()) {
                uri.append(URLEncoder.encode(paramName, "UTF-8"))
                   .append("=")
                   .append(URLEncoder.encode(queryParams.get(paramName), "UTF-8"));
            }
        }
        return uri.toString();
    }
    
}
