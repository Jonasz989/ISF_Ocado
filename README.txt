/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//Starting program
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

java -jar application.jar absoluteStorePath.json absoluteOrdersPath.json

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//Solution
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

We are sorting our Orders by most important factors. Then we are checking which sort was the best. In the end we print the
biggest/most valuable list of orders and it's pickers.

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//Used imports:
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

Imports used to read from .json file and some modules that allows us to parse it into Duration and BigDecimal:
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.type.CollectionType;

Annotations to class and fields:
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

Import to create file and file stream from *.json file:
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

Imports to work with collections:
import java.util.ArrayList;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;

Imports to divide BigDecimal and work with streams:
import java.math.RoundingMode;
import java.util.stream.Collectors;

Imports needed for classes Store and Order:
import java.time.Duration;
import java.math.BigDecimal;
import java.time.LocalTime;

Imports for tests:
import org.junit.Before;
import org.junit.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//What I could do better
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
1. I could create some better methods that needs as an argument a list of orders and they would return a final list
2. More unit tests
3. Clearer code, fewer duplicates

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//Classes
/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

 ISF - main implementation of algorithms
 Order - class that represents order from our json file
 Store - class that represents store from our json file
 Parser - class that parses json file into list of orders and store
