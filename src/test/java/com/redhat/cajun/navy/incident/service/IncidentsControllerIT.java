package com.redhat.cajun.navy.incident.service;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import com.redhat.cajun.navy.incident.dao.IncidentDao;
import com.redhat.cajun.navy.incident.listener.UpdateIncidentCommandListener;
import com.redhat.cajun.navy.incident.message.IncidentReportedEvent;
import com.redhat.cajun.navy.incident.message.Message;
import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class IncidentsControllerIT {

    @Value("${local.server.port}")
    private int port;

    @MockBean
    private UpdateIncidentCommandListener updateIncidentCommandListener;

    @Autowired
    private KafkaTemplate<String, Message<?>> kafkaTemplate;

    @Autowired
    private IncidentDao incidentDao;

    @Captor
    private ArgumentCaptor<Message<IncidentReportedEvent>> messageCaptor;

    @Captor
    private ArgumentCaptor<String> destination;

    @Captor
    private ArgumentCaptor<String> key;

    @Before
    public void beforeTest() {
        RestAssured.baseURI = String.format("http://localhost:%d", port);
    }

    @Test
    public void testReportIncident() throws Exception {

        String reportedIncident = "{" +
                "\"lat\": \"34.14338\"," +
                "\"lon\": \"-77.86569\"," +
                "\"numberOfPeople\": 3," +
                "\"medicalNeeded\": true," +
                "\"victimName\": \"victim\"," +
                "\"victimPhoneNumber\": \"111-111-111\"" +
                "}";

        Response response = given().header(new Header("Content-type", "application/json"))
                .request().body(reportedIncident).post("/incidents");
        assertThat(response.statusCode(), equalTo(201));
    }
}
