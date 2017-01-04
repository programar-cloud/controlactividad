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

import com.jayway.jsonpath.JsonPath;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import static org.junit.Assert.assertEquals;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment=SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ActividadUnicoCursoCtrlIT {
    
    private static final String CODIGO_CURSO_EXISTENTE = "cultura";
    private static final String CODIGO_CURSO_INEXISTENTE = "deathcabforcutie";
    
    private static final String PATH = "/cursos/%s/unidades-didacticas/actividad";

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void actividadCursoInexistente() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        HttpEntity<String> entity = new HttpEntity<>(headers);
        String url = String.format(PATH, CODIGO_CURSO_INEXISTENTE);
        ResponseEntity<String> response = 
                restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        assertEquals("Curso no encontrado.", HttpStatus.NOT_FOUND,
                response.getStatusCode());
    }
    
    @Test
    public void actividadCursoExistenteJSON() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        HttpEntity<String> entity = new HttpEntity<>(headers);
        String url = String.format(PATH, CODIGO_CURSO_EXISTENTE);
        ResponseEntity<String> response = 
                restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        
        assertEquals("Invocación existosa", HttpStatus.OK,
                response.getStatusCode());

        String responseBody = response.getBody();        
        List<String> codigosCurso = JsonPath.read(responseBody, "[*].codigo-curso");

        Assert.assertTrue("Encontradas actividades.", codigosCurso.size() > 0);
        Assert.assertTrue("Actividad pertenciente al curso \"cultura\".", 
                          codigosCurso.stream().allMatch(codigo -> codigo.equals("cultura")));
    }

}
