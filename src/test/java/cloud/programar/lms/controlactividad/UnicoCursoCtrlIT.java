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

import capital.scalable.restdocs.AutoDocumentation;
import capital.scalable.restdocs.jackson.JacksonResultHandlers;
import capital.scalable.restdocs.response.ResponseModifyingPreprocessors;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.junit.Before;
import org.junit.Rule;
import org.springframework.restdocs.RestDocumentation;
import org.springframework.restdocs.cli.CliDocumentation;
import org.springframework.restdocs.http.HttpDocumentation;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import org.springframework.restdocs.operation.preprocess.Preprocessors;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class UnicoCursoCtrlIT {

    private static final String CODIGO_CURSO_EXISTENTE = "cultura";
    private static final String CODIGO_CURSO_INEXISTENTE = "deathcabforcutie";

    private static final String PATH = "/cursos/%s/unidades-didacticas/actividad";

    @Rule
    public final RestDocumentation restDocumentation = new RestDocumentation("target/generated-snippets");

    @Autowired
    private WebApplicationContext context;

    @Autowired
    protected ObjectMapper objectMapper;

    private MockMvc mockMvc;

    private RestDocumentationResultHandler document;

    @Before
    public void setUp() {
        String classNameForDoc = getClass().getSimpleName();
        classNameForDoc = classNameForDoc.substring(0, classNameForDoc.length() - "CtrlIt".length());
        classNameForDoc = classNameForDoc
                           .replaceAll("([a-z])([A-Z]+)", "$1-$2")
                           .toLowerCase();
        this.document = document("{method-name}",
                preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()));
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(context)
                // .addFilters(springSecurityFilterChain)
                .alwaysDo(JacksonResultHandlers.prepareJackson(objectMapper))
                .alwaysDo(MockMvcRestDocumentation.document(classNameForDoc + "/{method-name}",
                        Preprocessors.preprocessRequest(),
                        Preprocessors.preprocessResponse(
                                ResponseModifyingPreprocessors.replaceBinaryContent(),
                                ResponseModifyingPreprocessors.limitJsonArrayLength(objectMapper),
                                Preprocessors.prettyPrint())))
                .apply(MockMvcRestDocumentation.documentationConfiguration(restDocumentation)
                        .uris()
                        .withScheme("http")
                        .withHost("localhost")
                        .withPort(8080)
                        .and().snippets()
                        .withDefaults(CliDocumentation.curlRequest(),
                                HttpDocumentation.httpRequest(),
                                HttpDocumentation.httpResponse(),
                                AutoDocumentation.requestFields(),
                                AutoDocumentation.responseFields(),
                                AutoDocumentation.pathParameters(),
                                AutoDocumentation.requestParameters(),
                                AutoDocumentation.description(),
                                AutoDocumentation.methodAndPath(),
                                AutoDocumentation.section()))
                .build();
    }

    @Test
    public void getJsonInexistente() throws Exception {
        String url = String.format(PATH, CODIGO_CURSO_INEXISTENTE);
        this.mockMvc.perform(
                get(url).accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isNotFound());
    }

    @Test
    public void getJson() throws Exception {
        String url = String.format(PATH, CODIGO_CURSO_EXISTENTE);
        this.mockMvc.perform(
                get(url).accept(MediaType.APPLICATION_JSON)
        ).andExpect(status().isOk())
        // More about jsonpath: https://github.com/jayway/JsonPath
         .andExpect(MockMvcResultMatchers.jsonPath("[*].codigoCurso", 
            containsInAnyOrder(CODIGO_CURSO_EXISTENTE,CODIGO_CURSO_EXISTENTE,CODIGO_CURSO_EXISTENTE)));
    }

    @Test
    public void getHtml() throws Exception {
        String url = String.format(PATH, CODIGO_CURSO_EXISTENTE);
        MvcResult result = this.mockMvc
         .perform(get(url).accept(MediaType.TEXT_HTML))
         .andExpect(status().isOk())
         .andReturn();
        
        String responseBody = result.getResponse().getContentAsString();
        Document doc = Jsoup.parse(responseBody);
        Elements codigosCursoElem = doc.select("table tr td:first-child");
        Assert.assertTrue("Encontradas actividades.", codigosCursoElem.size() > 0);
        Assert.assertTrue("Actividad pertenciente al curso \"cultura\".",
        codigosCursoElem.stream().allMatch(elem -> elem.text().equals("cultura")));
         
    }
}
