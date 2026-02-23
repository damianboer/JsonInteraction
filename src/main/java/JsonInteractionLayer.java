import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.File;
import java.io.IOException;

public class JsonInteractionLayer {

    private final ObjectMapper mapper;
    private final File inputDir;
    private final File outputDir;

    public JsonInteractionLayer(String inputDirPath, String outputDirPath) {
        this.mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        this.inputDir = new File(inputDirPath);
        this.outputDir = new File(outputDirPath);
        this.inputDir.mkdirs();
        this.outputDir.mkdirs();
    }

    /** Deserialize a JSON file into a Java object */
    public <T> T readFromFile(String filename, Class<T> clazz) throws IOException {
        File file = new File(inputDir, filename);
        return mapper.readValue(file, clazz);
    }

    /** Serialize a Java object and write it to a JSON file */
    public void writeToFile(String filename, Object obj) throws IOException {
        File file = new File(outputDir, filename);
        mapper.writeValue(file, obj);
        System.out.println("Written to: " + file.getAbsolutePath());
    }

    /** Read raw JSON string from a file */
    public String readRaw(String filename) throws IOException {
        File file = new File(inputDir, filename);
        return mapper.readTree(file).toPrettyString();
    }

    public File getInputDir()  { return inputDir; }
    public File getOutputDir() { return outputDir; }
}