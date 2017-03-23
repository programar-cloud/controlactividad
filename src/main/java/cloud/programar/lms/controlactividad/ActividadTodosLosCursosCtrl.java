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

import java.io.InputStream;
import java.text.MessageFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.core.io.InputStreamResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 *
 * Controlador para obtener la actividad de todos los cursos existentes. Cada
 * método implemente uno o más internet types. Actualmente se soportan
 * text/html, text/csv, appplication/json, application/xml, image/png,
 * image/jpg.
 *
 * ej:
 * /cursos/actividad?desde=2000-10-31T01:30:00.000+02:00&hasta=2000-10-31T01:30:00.000+02:00
 *
 * @author ciberado
 */
@RestController
@RequestMapping("/cursos/actividad")
public class ActividadTodosLosCursosCtrl {
    /*
    private final ActividadCurso[] actividadCursos = {
            new ActividadCurso("cultura", "Cultura DevOps", 2580, null, null),
            new ActividadCurso("apirest", "Diseño de APIs", 2200, null, null),
            new ActividadCurso("spoiler", "No way", 0, null, null)};

    @GetMapping(produces = {MediaType.TEXT_HTML_VALUE})
    public ModelAndView actividadTodosLosCursosHTML(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime hasta) {
        for (ActividadCurso actividad : actividadCursos) {
            actividad.setDesde(desde);
            actividad.setHasta(hasta);
        }
        ModelAndView mav = new ModelAndView("cursos/actividad");
        mav.addObject("actividadCursos", Arrays.asList(actividadCursos));
        mav.addObject("fecha", actividadCursos[0].getDesde());
        mav.addObject("act", actividadCursos[0]);
        return mav;
    }

    @GetMapping(produces = {
        MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_XML_VALUE})
    public List<ActividadCurso> actividadTodosLosCursos(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime desde,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) ZonedDateTime hasta) {
        for (ActividadCurso actividad : actividadCursos) {
            actividad.setDesde(desde);
            actividad.setHasta(hasta);
        }
        return Arrays.asList(actividadCursos);
    }

    @GetMapping(produces = {MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE})
    public ResponseEntity<InputStreamResource> actividadTodosLosCursosImage(
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") ZonedDateTime desde,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") ZonedDateTime hasta,
            @RequestHeader(value = "Accept") String contentType) {
        String filename = "rex." + (contentType.contains("png") ? "png" : "jpg");
        InputStream in = ControlActividadApplication.class.getClassLoader()
                .getResourceAsStream(filename);
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .body(new InputStreamResource(in));
    }

    @GetMapping(produces = {"text/csv"})
    public String actividadTodosLosCursosCSV(
            HttpServletResponse response,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") ZonedDateTime desde,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") ZonedDateTime hasta) {
        if (desde == null) {
            desde = ZonedDateTime.of(2016, 6, 1, 0, 0, 0, 0, ZoneId.of("UTC+0"));
        }
        if (hasta == null) {
            hasta = ZonedDateTime.of(2042, 12, 31, 0, 0, 0, 0, ZoneId.of("UTC+0"));
        }
        response.setContentType("text/plain; charset=utf-8");

        String csvLine = "{0}, {1}, {2,number,integer}, {3}, {4}\n";
        String desdeStr = desde.format(DateTimeFormatter.ISO_DATE);
        String hastaStr = hasta.format(DateTimeFormatter.ISO_DATE);
        String data
                = MessageFormat.format(csvLine, "cultura", "Cultura DevOps", 2580, desdeStr, hastaStr)
                + MessageFormat.format(csvLine, "apirest", "Diseño de APIs", 2200, desdeStr, hastaStr)
                + MessageFormat.format(csvLine, "spoiler", "No way", 0, desdeStr, hastaStr);

        return data;
    }
*/
}
