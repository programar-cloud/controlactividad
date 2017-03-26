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

import cloud.programar.hateoas.Page;
import cloud.programar.hateoas.Resource;
import cloud.programar.hateoas.QSBuilder;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
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
 * Controlador para gestionar los cursos, con soporte HAL.
 * 
 * @author ciberado
 */
@RestController
public class CursosCtrl {
    public static final String CURSOS_URL = "/cursos";
    public static final String DETALLES_CURSO_URL = "/cursos/{codigo}";
    public static final String DETALLES_UNIDAD_DIDACTICA_URL = DETALLES_CURSO_URL + "/unidades/{numero}";
    
    /* Simula el máximo número de cursos existentes. */
    private static final int NUMERO_CURSOS = 25;
    /* Simula un curso que normalmente se recuperaría del repositorio. */
    private static final Curso CURSO_EXISTENTE = new Curso(
         "cultura", "Cultura DevOps", 2000, Arrays.asList(new UnidadDidactica[]{
            new UnidadDidactica("1000", "El nacimiento de los web services", 1000, null),
            new UnidadDidactica("1010", "Desde monolíticas a microservicios", 500, null),
            new UnidadDidactica("1020", "Devops, no, en serio: devops", 500, null)}));

    private final String apiRoot;
    
    @Autowired
    public CursosCtrl(@Value("${application.apiRoot}") String apiRoot) {
        this.apiRoot = apiRoot;
    }

    /**
     * Lista los cursos disponibles. Ejemplo rápido (y sucio) sobre cómo implementar
     * la paginación a nivel de controlador con HAL. 
     * 
     * @param pagina número de página, 0 based. 10 resultados por página.
     * @param desde fecha opcional de inicio en formato yyyy-MM-dd.
     * @param hasta fecha opcional de final de rango (inclusive) en formato yyyy-MM-dd.
     * @return la lista de actividades como un array json de objetos o un arbol xml.
     */
    @GetMapping(value=CURSOS_URL, produces = {
            MediaType.APPLICATION_JSON_UTF8_VALUE, "application/hal+json"})
    public ResponseEntity<Resource<Page>> recuperarTodosLosCursosPorPagina(
            @RequestParam(required = false, defaultValue = "0") int pagina,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) ZonedDateTime desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) ZonedDateTime hasta) {
        if (pagina > NUMERO_CURSOS / 10) {
            throw new ResourceNotFoundException(String.format("La página %s no existe.", pagina));
        }
        
        // Simula la recuperación de los cursos
        List<Curso> cursos = new ArrayList<>();
        for (int i=0; i < 10; i++) {
            int numeroItem = pagina * 10 + i;
            if (numeroItem > NUMERO_CURSOS) break;
            cursos.add(new Curso("cod-"+numeroItem, "Curso número " + numeroItem, 2000, null));
        }
        if (pagina == 0) {
            cursos.set(0, CURSO_EXISTENTE);
        }
        
        Page page = new Page(pagina, NUMERO_CURSOS, cursos.size());
        String desdeAsString = desde == null ? null : DateTimeFormatter.ISO_DATE.format(desde);
        String hastaAsString = hasta == null ? null : DateTimeFormatter.ISO_DATE.format(hasta);

        //Construcción de los links next, previous y self.
        String self = new QSBuilder(CURSOS_URL)
                .add("pagina", pagina)
                .add("desde", desdeAsString)
                .add("hasta", hastaAsString)
                .build();
        String previous = null;
        if (pagina > 0) {
            previous = new QSBuilder(CURSOS_URL)
                .add("pagina", pagina-1)
                .add("desde", desdeAsString)
                .add("hasta", hastaAsString)
                .build();            
        }
        String next = null;
        if ((pagina+1) * 10 < NUMERO_CURSOS) {
            next = new QSBuilder(CURSOS_URL)
                .add("pagina", pagina+1)
                .add("desde", desdeAsString)
                .add("hasta", hastaAsString)
                .build();            
        }
        
        // Creación del recurso de paginación
        Resource<Page> pageResource = new Resource<>(page, apiRoot)
                .addAdditionalPropertyIfNotNull("desde", desdeAsString)
                .addAdditionalPropertyIfNotNull("hasta", hastaAsString)
                .addLink("self", self)
                .addLink("next", next)
                .addLink("previous", previous);
        for (Curso curso : cursos) {
            pageResource.addEmbedded("items", crearCursoResource(curso, null, null));
        }
        
        ResponseEntity<Resource<Page>> response = 
                new ResponseEntity<>(pageResource, HttpStatus.OK);
        
        return response;
    }
    
    
    /**
     * Retorna la información asociada a un curso incluyendo un resumen de sus
     * unidades didácticas.
     * 
     * @param codigo del curso, como por ejemplo *cultura*.
     * @param desde fecha opcional de inicio en formato yyyy-MM-dd.
     * @param hasta fecha opcional de final de rango (inclusive) en formato yyyy-MM-dd.
     * @return la lista de actividades como un array json de objetos o un arbol xml.
     */
    @GetMapping(value=DETALLES_CURSO_URL, produces = {
            MediaType.APPLICATION_JSON_UTF8_VALUE, "application/hal+json"})
    public ResponseEntity<Resource<Curso>> recuperarCurso(
            @PathVariable @Size(min = 4) String codigo,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) ZonedDateTime desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) ZonedDateTime hasta) {
        if (codigo.equals(CURSO_EXISTENTE.getCodigo()) == false) {
            throw new ResourceNotFoundException(String.format("El curso %s no existe.", codigo));
        }
        // Simula la recuperación del curso a partir de su código
        Curso curso = CURSO_EXISTENTE; 
        Resource<Curso> resourceCurso = crearCursoResource(curso, desde, hasta);
        
        for (UnidadDidactica ud : curso.getUnidadesDidacticas()) {
            String self = DETALLES_UNIDAD_DIDACTICA_URL
                    .replace("{codigo}", curso.getCodigo())
                    .replace("{numero}", ud.getNumero());
            Resource<UnidadDidactica> resourceUD = new Resource<>(ud, apiRoot)
                    .addLink("self", self);
            resourceCurso.addEmbedded("unidades_didacticas", resourceUD);
        }
        
        ResponseEntity<Resource<Curso>> response = 
                new ResponseEntity<>(resourceCurso, HttpStatus.OK);
        
        return response;
    }

    private Resource<Curso> crearCursoResource(Curso curso, ZonedDateTime desde, ZonedDateTime hasta) {
        String self = DETALLES_CURSO_URL.replace("{codigo}", curso.getCodigo());
        Resource<Curso> resource = new Resource<>(curso, apiRoot)
                .addAdditionalPropertyIfNotNull("desde", desde)
                .addAdditionalPropertyIfNotNull("hasta", hasta)
                .addLink("self", self);
        return resource;
    }



}
