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

import cloud.programar.hateoas.Resource;
import java.time.ZonedDateTime;
import java.util.Arrays;
import javax.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 *
 * Controlador para obtener la actividad de un curso concreto, con soporte HAL.
 * 
 * ej: /cursos/cultura?desde=2000-10-31&hasta=2000-10-31
 * 
 * @author ciberado
 */
@RestController
public class ActividadUnicoCursoCtrl {
    @Value("${application.apiRoot}")
    private String apiRoot;
    
    public static final String CURSO_URL = "/cursos/{codigo}";
    public static final String UNIDAD_DIDACTICA_URL = CURSO_URL + "/unidades/{numero}";
    
    
    /* Simula un curso que normalmente se recuperaría del repositorio. */
    private static final Curso CURSO_EXISTENTE = new Curso(
         "cultura", "Cultura DevOps", 2000, Arrays.asList(new UnidadDidactica[]{
            new UnidadDidactica("1000", "El nacimiento de los web services", 1000, null),
            new UnidadDidactica("1010", "Desde monolíticas a microservicios", 500, null),
            new UnidadDidactica("1020", "Devops, no, en serio: devops", 500, null)}));
    
    /**
     * Retorna la actividad de las unidades didacticas de un determinado curso opcionalmente restringiendo
     * la busqueda a un periodo de tiempo. Puede utilizarse para evaluar la popularidad de
     * cada una de las secciones del mismo.
     * 
     * @param codigo del curso, como por ejemplo *cultura*.
     * @param desde fecha opcional de inicio en formato yyyy-MM-dd.
     * @param hasta fecha opcional de final de rango (inclusive) en formato yyyy-MM-dd.
     * @return la lista de actividades como un array json de objetos o un arbol xml.
     */
    @GetMapping(value=CURSO_URL, produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
    public ResponseEntity<Resource<Curso>> actividadCurso(
            @PathVariable @Size(min = 4) String codigo,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) ZonedDateTime desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) ZonedDateTime hasta) {
        if (codigo.equals(CURSO_EXISTENTE.getCodigo()) == false) {
            throw new ResourceNotFoundException(String.format("El curso %s no existe.", codigo));
        }
        // Simula la recuperación del curso a partir de su código
        Curso curso = CURSO_EXISTENTE; 
        Resource<Curso> resourceCurso = createResource(curso, desde, hasta);
        
        for (UnidadDidactica ud : curso.getUnidadesDidacticas()) {
            String _self = UNIDAD_DIDACTICA_URL
                    .replace("{codigo}", curso.getCodigo())
                    .replace("{numero}", ud.getNumero());
            Resource<UnidadDidactica> resourceUD = new Resource<>(ud, apiRoot)
                    .addLink("_self", _self);
            resourceCurso.addEmbedded("unidades_didacticas", resourceUD);
        }
        
        ResponseEntity<Resource<Curso>> response = 
                new ResponseEntity<>(resourceCurso, HttpStatus.OK);
        
        return response;
    }

    private Resource<Curso> createResource(Curso curso, ZonedDateTime desde, ZonedDateTime hasta) {
        String _self = CURSO_URL.replace("{codigo}", curso.getCodigo());
        Resource<Curso> resource = new Resource<>(curso, apiRoot)
                .addAdditionalPropertyIfNotNull("desde", desde)
                .addAdditionalPropertyIfNotNull("hasta", hasta)
                .addLink("_self", _self);
        return resource;
    }



}
