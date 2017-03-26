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
package cloud.programar.lms.controlactividad;

import cloud.programar.hateoas.Link;
import cloud.programar.hateoas.Page;
import cloud.programar.hateoas.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * URIs de los recursos considerados "Raíz", en este caso "curso".
 * 
 * @author ciberado
 */
@RestController
public class RootCtrl {
    private final String apiRoot;
    
    @Autowired
    public RootCtrl(@Value("${application.apiRoot}") String apiRoot) {
        this.apiRoot = apiRoot;
    }

    @GetMapping(value="/", produces = {
            MediaType.APPLICATION_JSON_UTF8_VALUE, "application/hal+json"})
    public ResponseEntity<Resource<Object>> recuperarRecursosRaiz() {
        Link cursoResourceSelf = new Link(
            null, CursosCtrl.CURSOS_URL, 
            "application/vnd.cloud.programar.hateoas.Page<Curso>",
            "Cursos existentes (paginados)", false
        );
        Resource<Page> cursoResource = new Resource<>(new Page(), apiRoot)
                .addLink("self", cursoResourceSelf);
        Resource<Object> rootResource = new Resource<>(null, apiRoot)
                .addEmbedded("resources", cursoResource);
                
        ResponseEntity<Resource<Object>> response = 
                new ResponseEntity<>(rootResource, HttpStatus.OK);        
        return response;
    }
    
    
}
