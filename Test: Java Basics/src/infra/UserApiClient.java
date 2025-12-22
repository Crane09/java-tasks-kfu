package infra;

import domain.User;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class UserApiClient {
    private final HttpClient httpClient;
    private final URI endpoint;

    public UserApiClient(URI endpoint) {
        this.endpoint = endpoint;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    public List<User> fetchUsers() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(endpoint)
                .timeout(Duration.ofSeconds(15))
                .GET()
                .build();

        HttpResponse<InputStream> resp = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());
        int code = resp.statusCode();
        if (code < 200 || code >= 300) {
            throw new IOException("HTTP error: " + code);
        }

        String body;
        try (InputStream in = resp.body()) {
            body = readAllToString(in);
        }

        return parseUsersFromJson(body);
    }

    private static String readAllToString(InputStream in) throws IOException {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            byte[] buf = new byte[8192];
            int r;
            while ((r = in.read(buf)) != -1) {
                baos.write(buf, 0, r);
            }
            return baos.toString(StandardCharsets.UTF_8);
        }
    }

    private static List<User> parseUsersFromJson(String json) throws IOException {
        List<String> objects = splitTopLevelObjects(json);
        List<User> users = new ArrayList<>(objects.size());

        for (String obj : objects) {
            int id = getIntField(obj, "id");
            String name = getStringField(obj, "name");
            String username = getStringField(obj, "username");
            String email = getStringField(obj, "email");
            users.add(new User(id, name, username, email));
        }
        return users;
    }

    private static List<String> splitTopLevelObjects(String json) throws IOException {
        int start = json.indexOf('[');
        int end = json.lastIndexOf(']');
        if (start == -1 || end == -1 || end <= start) {
            throw new IOException("Unexpected JSON format (not an array)");
        }

        String arr = json.substring(start + 1, end);
        List<String> out = new ArrayList<>();

        int depth = 0;
        int objStart = -1;
        for (int i = 0; i < arr.length(); i++) {
            char c = arr.charAt(i);
            if (c == '{') {
                if (depth == 0) objStart = i;
                depth++;
            } else if (c == '}') {
                depth--;
                if (depth == 0 && objStart != -1) {
                    out.add(arr.substring(objStart, i + 1));
                    objStart = -1;
                }
            }
        }
        return out;
    }

    private static int getIntField(String obj, String field) throws IOException {
        String key = "\"" + field + "\"";
        int k = obj.indexOf(key);
        if (k == -1) throw new IOException("Missing int field: " + field);
        int colon = obj.indexOf(':', k + key.length());
        if (colon == -1) throw new IOException("Bad JSON near field: " + field);

        int i = colon + 1;
        while (i < obj.length() && Character.isWhitespace(obj.charAt(i))) i++;

        int sign = 1;
        if (i < obj.length() && obj.charAt(i) == '-') { sign = -1; i++; }

        int val = 0;
        boolean any = false;
        while (i < obj.length() && Character.isDigit(obj.charAt(i))) {
            any = true;
            val = val * 10 + (obj.charAt(i) - '0');
            i++;
        }
        if (!any) throw new IOException("Bad int field: " + field);
        return val * sign;
    }

    private static String getStringField(String obj, String field) throws IOException {
        String key = "\"" + field + "\"";
        int k = obj.indexOf(key);
        if (k == -1) throw new IOException("Missing string field: " + field);
        int colon = obj.indexOf(':', k + key.length());
        if (colon == -1) throw new IOException("Bad JSON near field: " + field);

        int i = colon + 1;
        while (i < obj.length() && Character.isWhitespace(obj.charAt(i))) i++;

        if (i >= obj.length() || obj.charAt(i) != '"') {
            throw new IOException("Expected string for field: " + field);
        }
        i++;

        StringBuilder sb = new StringBuilder();
        while (i < obj.length()) {
            char c = obj.charAt(i);
            if (c == '\\') {
                if (i + 1 < obj.length()) {
                    char n = obj.charAt(i + 1);
                    sb.append(n);
                    i += 2;
                    continue;
                }
                throw new IOException("Bad escape in field: " + field);
            }
            if (c == '"') break;
            sb.append(c);
            i++;
        }
        return sb.toString();
    }
}