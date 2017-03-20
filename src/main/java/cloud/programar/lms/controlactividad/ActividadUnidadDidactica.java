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

import com.fasterxml.jackson.annotation.JsonRootName;
import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.core.Relation;

/**
 *
 * Informacion sobre las interacciones que se han llevado a cabo
 * con una determinada unidad didactica durante un periodo de tiempo.
 * 
 * @author ciberado
 */
@Data @AllArgsConstructor @NoArgsConstructor
@JsonRootName("actividades")
@Relation(value="actividad_unidad_didactica", 
          collectionRelation = "actividades_unidades_didacticas")
public class ActividadUnidadDidactica {
    /**
     * El codigo de curso es legible para humanos. Inmutable.
     */
    String codigoCurso;
    /**
     * Facilita la ordenacion de las unidades didacticas.
     */
    String numeroUnidadDidactica;
    /** Legible para humanos. Actualmente no soporta i18n.
     */
    String tituloUnidadDidactica;
    /**
     * Basado en la informacion proporcionada por las pantallas que
     * muestran la Unidad Didactica.
     */
    int lecturasCompletadas;

    
}
