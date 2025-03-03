import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

import com.EACH.integrationtests.DTO.PersonVO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

public class YamlSerializationTest {

    @Test
    public void testYamlSerialization() throws Exception {
        ObjectMapper yamlMapper = new ObjectMapper(new YAMLFactory());
        PersonVO person = new PersonVO();
        person.setFirstName("John");
        person.setLastName("Doe");
        person.setAddress("New York");
        person.setGender("Male");
        person.setEnabled(true);

        // Serialize to YAML
        String yaml = yamlMapper.writeValueAsString(person);
        System.out.println("Serialized YAML: " + yaml);

        // Ensure the YAML string is not null or empty
        assertNotNull(yaml);
    }
}