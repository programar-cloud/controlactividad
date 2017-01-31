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
 */package cloud.programar.lms.controlactividad;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * Informacion sobre las interacciones que se han llevado a cabo
 * con una determinada unidad didactica durante un periodo de tiempo.
 * 
 * @author ciberado
 */
@Data @AllArgsConstructor @NoArgsConstructor
@JsonRootName("actividades")
public class ActividadUnidadDidactica {
    /**
     * El codigo de curso es legible para humanos. Inmutable.
     */
    @JsonProperty("codigo-curso")
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
    /**
     * Inicio del rango de fechas que se toma en cuenta a la hora de
     * contabilizar los datos proporcionados.
     */
    ZonedDateTime desde;
    /** Final de las fechas que se tomaron en cuenta a la hora de contabilizar
     * los datos proporcionados (inclusive).
     */
    ZonedDateTime hasta;
}
