import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Map;
import java.util.stream.Collectors;

public class Run {

    public enum RunMode {
        TEST("test"),
        DEV("dev");

        public final String value;

        private RunMode(String value) {
            this.value = value;
        }
    }

    private static final String PRIVATE_KEY_ENV_VAR = "SMALLRYE_JWT_SIGN_KEY";
    private static final String PUBLIC_KEY_ENV_VAR = "MP_JWT_VERIFY_PUBLICKEY";

    public static void main(String[] args) {
        if (args.length != 1) {
            System.err.println("Usage: java Run.java [" + usageOptions() + "]");
            System.exit(1);
        }
    
        RunMode mode;
        switch (args[0].toLowerCase()) {
            case "dev":
                mode = RunMode.DEV;
                break;
            case "test":
                mode = RunMode.TEST;
                break;
            default:
                System.err.println("Usage: java Run.java [" + usageOptions() + "]");
                System.exit(1);
                return;
        }

        try {
            KeyPair keyPair = generateKeys();
            runQuarkus(keyPair, mode);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static KeyPair generateKeys() throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(2048);
        KeyPair keyPair = keyGen.generateKeyPair();

        System.out.println("RSA key pair generated.");
        return keyPair;
    }

    private static void runQuarkus(KeyPair keyPair, RunMode mode) throws IOException, InterruptedException {
        final String RELATIVE_PROJECT_DIR = "../../rest-client-quickstart";
        String privateKeyPem = "-----BEGIN PRIVATE KEY-----\n" +
                new String(Base64.getMimeEncoder(64, "\n".getBytes()).encode(keyPair.getPrivate().getEncoded()), StandardCharsets.UTF_8) +
                "\n-----END PRIVATE KEY-----";

        String publicKeyPem = "-----BEGIN PUBLIC KEY-----\n" +
                new String(Base64.getMimeEncoder(64, "\n".getBytes()).encode(keyPair.getPublic().getEncoded()), StandardCharsets.UTF_8) +
                "\n-----END PUBLIC KEY-----";

        ProcessBuilder pb = new ProcessBuilder("./mvnw", "quarkus:" + mode.value);
        pb.directory(Paths.get(System.getProperty("user.dir")).resolve(RELATIVE_PROJECT_DIR).normalize().toFile());

        Map<String, String> env = pb.environment();
        env.put(PRIVATE_KEY_ENV_VAR, privateKeyPem);
        env.put(PUBLIC_KEY_ENV_VAR, publicKeyPem);

        Process process = pb.inheritIO().start();

        process.waitFor();

        System.out.println("Quarkus application successfully shut down.");
    }

    private static String usageOptions() {
        return Arrays.stream(RunMode.values())
                .map(mode -> mode.value)
                .collect(Collectors.joining("|"));
    }
}