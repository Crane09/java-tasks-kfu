package infra;

import domain.User;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class UserStorage {
    private final Path file;

    public UserStorage(Path file) {
        this.file = file;
    }

    public void save(List<User> users) throws IOException {
        Files.createDirectories(file.getParent() == null ? Path.of(".") : file.getParent());

        try (OutputStream os = Files.newOutputStream(file);
             BufferedWriter w = new BufferedWriter(new OutputStreamWriter(os, StandardCharsets.UTF_8))) {

            w.write("id,name,username,email");
            w.newLine();

            for (User u : users) {
                w.write(u.getId() + "," + csv(u.getName()) + "," + csv(u.getUsername()) + "," + csv(u.getEmail()));
                w.newLine();
            }
        }
    }

    public List<User> load() throws IOException {
        if (!Files.exists(file)) return List.of();

        List<User> out = new ArrayList<>();
        try (InputStream is = Files.newInputStream(file);
             BufferedReader r = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {

            String header = r.readLine(); 
            if (header == null) return List.of();

            String line;
            while ((line = r.readLine()) != null) {
                String[] cols = splitCsvLine(line);
                if (cols.length < 4) continue;

                int id = Integer.parseInt(cols[0].trim());
                String name = uncsv(cols[1]);
                String username = uncsv(cols[2]);
                String email = uncsv(cols[3]);
                out.add(new User(id, name, username, email));
            }
        }
        return out;
    }

    private static String csv(String s) {
        if (s == null) return "\"\"";
        String escaped = s.replace("\"", "\"\"");
        return "\"" + escaped + "\"";
    }

    private static String uncsv(String s) {
        String t = s.trim();
        if (t.startsWith("\"") && t.endsWith("\"") && t.length() >= 2) {
            t = t.substring(1, t.length() - 1).replace("\"\"", "\"");
        }
        return t;
    }

    private static String[] splitCsvLine(String line) {
        List<String> cols = new ArrayList<>();
        StringBuilder cur = new StringBuilder();
        boolean inQuotes = false;

        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '"') {
                    cur.append('"');
                    i++;
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                cols.add(cur.toString());
                cur.setLength(0);
            } else {
                cur.append(c);
            }
        }
        cols.add(cur.toString());
        return cols.toArray(new String[0]);
    }
}