package ch.heigvd.gen.mpms;

import ch.heigvd.gen.mpms.model.GameComponent.Square;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Vector;

/**
 * This class provides utility methods to convert Plain Old Java Objects (POJOs)
 * into their json representation, and vice-versa. It relies on the jackson
 * library.
 *
 * @author Olivier Liechti
 */
public class JsonObjectMapper {

  private static final ObjectMapper objectMapper = new ObjectMapper();

  /**
   * Converts a json string into a POJO of the specified class
   *
   * @param <T> the class that we want to instantiate
   * @param json the json representation of the object
   * @param type the class to instantiate
   * @return an instance of T, which value corresponds to the json string
   * @throws IOException
   */
  public static <T> T parseJson(String json, TypeReference<Vector<Square>> type) throws IOException {
	  return objectMapper.readValue(json, type);
  }

  /**
   * Converts a POJO into its json representation
   *
   * @param o the object to serialize
   * @return the json representation of o
   * @throws JsonProcessingException
   */
  public static String toJson(Object o) throws JsonProcessingException {
    return objectMapper.writeValueAsString(o);
  }

}
