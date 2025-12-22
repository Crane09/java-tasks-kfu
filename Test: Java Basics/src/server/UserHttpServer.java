package server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import domain.User;
import repo.UserRepository;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Executors;

public class UserHttpServer implements AutoCloseable {
    private final HttpServer server;
    private final UserRepository repo;

    public UserHttpServer(UserRepository repo, int port, int threads) throws IOException {
        this.repo = repo;
        this.server = HttpServer.create(new InetSocketAddress(port), 0);

        server.setExecutor(Executors.newFixedThreadPool(threads));

        server.createContext("/users", this::handleUsers);
        server.createContext("/refresh", this::handleRefresh);
        server.createContext("/stats", this::handleStats);
    }

    public void start() {
        server.start();
    }

    private void handleUsers(HttpExchange ex) throws IOException {
        try {
            if (!"GET".equalsIgnoreCase(ex.getRequestMethod())) {
                sendJson(ex, 405, "{\"error\":\"Method Not Allowed\"}");
                return;
            }

            String path = ex.getRequestURI().getPath();
            if ("/users".equals(path) || "/users/".equals(path)) {
                List<User> users = repo.getAll();
                users.sort(Comparator.comparingInt(User::getId));
                sendJson(ex, 200, usersToJson(users));
                return;
            }

            String prefix = "/users/";
            if (path.startsWith(prefix)) {
                String tail = path.substring(prefix.length());
                int id;
                try {
                    id = Integer.parseInt(tail);
                } catch (NumberFormatException nfe) {
                    sendJson(ex, 400, "{\"error\":\"Bad id\"}");
                    return;
                }

                User u = repo.getById(id);
                if (u == null) {
                    sendJson(ex, 404, "{\"error\":\"Not Found\"}");
                } else {
                    sendJson(ex, 200, userToJson(u));
                }
                return;
            }

            sendJson(ex, 404, "{\"error\":\"Not Found\"}");
        } finally {
            ex.close();
        }
    }

    private void handleRefresh(HttpExchange ex) throws IOException {
        try {
            if (!"POST".equalsIgnoreCase(ex.getRequestMethod())) {
                sendJson(ex, 405, "{\"error\":\"Method Not Allowed\"}");
                return;
            }

            try (InputStream in = ex.getRequestBody()) {
                drain(in);
            }

            boolean started = repo.refresh();
            if (started) {
                sendJson(ex, 202, "{\"status\":\"refresh started\"}");
            } else {
                sendJson(ex, 409, "{\"status\":\"refresh already running\"}");
            }
        } finally {
            ex.close();
        }
    }

    private void handleStats(HttpExchange ex) throws IOException {
        try {
            if (!"GET".equalsIgnoreCase(ex.getRequestMethod())) {
                sendJson(ex, 405, "{\"error\":\"Method Not Allowed\"}");
                return;
            }
            sendJson(ex, 200, repo.getStats().toJson());
        } finally {
            ex.close();
        }
    }

    private static void sendJson(HttpExchange ex, int status, String json) throws IOException {
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        ex.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
        ex.sendResponseHeaders(status, bytes.length);
        try (OutputStream os = ex.getResponseBody()) {
            os.write(bytes);
        }
    }

    private static void drain(InputStream in) throws IOException {
        byte[] buf = new byte[4096];
        while (in.read(buf) != -1) { /* просто вычитываем */ }
    }

    private static String usersToJson(List<User> users) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < users.size(); i++) {
            if (i > 0) sb.append(",");
            sb.append(userToJson(users.get(i)));
        }
        sb.append("]");
        return sb.toString();
    }

    private static String userToJson(User u) {
        return "{"
                + "\"id\":" + u.getId() + ","
                + "\"name\":\"" + jsonEscape(u.getName()) + "\","
                + "\"username\":\"" + jsonEscape(u.getUsername()) + "\","
                + "\"email\":\"" + jsonEscape(u.getEmail()) + "\""
                + "}";
    }

    private static String jsonEscape(String s) {
        if (s == null) return "";
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    @Override
    public void close() {
        server.stop(0);
    }
}