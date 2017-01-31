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

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import javax.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;


/**
 *
 * Controlador para obtener la actividad de un curso concreto.
 * Cada metodo implemente uno o mas internet types. Actualmente se soportan
 * text/html, text/csv, appplication/json, application/xml, image/png, image/jpg.
 * 
 * ej: /cursos/actividad?desde=2000-10-31T01:30:00.000+02:00&hasta=2000-10-31T01:30:00.000+02:00
 * 
 * @author ciberado
 */
@RestController
@RequestMapping("/cursos/{codigo}/unidades-didacticas/actividad")
public class ActividadUnicoCursoCtrl {
    private final String CODIGO_CURSO_EXISTENTE = "cultura";
    
    private final ActividadUnidadDidactica[] actividadUnidades = {
            new ActividadUnidadDidactica(CODIGO_CURSO_EXISTENTE, "1000", "El nacimiento de los web services", 1080, null, null),
            new ActividadUnidadDidactica(CODIGO_CURSO_EXISTENTE, "1010", "Desde monolíticas a microservicios", 1000, null, null),
            new ActividadUnidadDidactica(CODIGO_CURSO_EXISTENTE, "1020", "Devops, no, en serio: devops", 500, null, null)};

    @GetMapping(produces = {MediaType.TEXT_HTML_VALUE})
    public ModelAndView actividadCursoHTML(
            @PathVariable @Size(min = 4) String codigo,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime hasta) {
        if (codigo.equals(CODIGO_CURSO_EXISTENTE) == false) {
            throw new ResourceNotFoundException(String.format("El curso %s no existe.", codigo));
        }
        ModelAndView mav = new ModelAndView("cursos/unidades-didacticas/actividad");
        if (actividadUnidades[0].getCodigoCurso().equals(codigo) == true) {
            for (ActividadUnidadDidactica actividad : actividadUnidades) {
                actividad.setDesde(desde);
                actividad.setHasta(hasta);
            }
            mav.addObject("actividadUnidades", Arrays.asList(actividadUnidades));
        }
        return mav;
    }

    /**
     * Retorna las unidades didacticas de un determinado curso opcionalmente restringiendo
     * la busqueda a un periodo de tiempo. Puede utilizarse para evaluar la popularidad de
     * cada una de las secciones del mismo.
     * 
     * @param codigo del curso, como por ejemplo <pre>cultura</pre>.
     * @param desde fecha opcional de inicio en formato yyyy-MM-dd.
     * @param hasta fecha opcional de final de rango (inclusive) en formato yyyy-MM-dd.
     * @return la lista de actividades como un array json de objetos o un arbol xml.
     */
    @GetMapping(produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE})
    public List<ActividadUnidadDidactica> actividadCurso(
            @PathVariable @Size(min = 4) String codigo,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") ZonedDateTime desde,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") ZonedDateTime hasta) {
        if (codigo.equals(CODIGO_CURSO_EXISTENTE) == false) {
            throw new ResourceNotFoundException(String.format("El curso %s no existe.", codigo));
        }
        for (ActividadUnidadDidactica actividad : actividadUnidades) {
            actividad.setDesde(desde);
            actividad.setHasta(hasta);
        }
        return Arrays.asList(actividadUnidades);
    }

    @GetMapping(produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    public List<ActividadCurso> actividadCursoImage(
            @PathVariable @Size(min = 4) String codigo,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") ZonedDateTime desde,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") ZonedDateTime hasta) {
        throw new UnsupportedOperationException("TODO");
    }

    @GetMapping(produces = {"text/csv"})
    public List<ActividadCurso> actividadCursoCSV(
            @PathVariable @Size(min = 4) String codigo,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") ZonedDateTime desde,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") ZonedDateTime hasta) {
        throw new UnsupportedOperationException("TODO");
    }


}
