package app;

import infra.UserApiClient;
import infra.UserStorage;
import repo.UserRepository;
import server.UserHttpServer;

import java.net.URI;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        URI uri = URI.create("https://jsonplaceholder.typicode.com/users");
        Path file = Path.of("data", "users.csv");

        UserApiClient client = new UserApiClient(uri);
        UserStorage storage = new UserStorage(file);

        try (UserRepository repo = new UserRepository(client, storage);
             UserHttpServer server = new UserHttpServer(repo, 8080, 8)) {

            repo.refresh();

            server.start();
            System.out.println("Server started: http://localhost:8080");
            System.out.println("GET  /users");
            System.out.println("GET  /users/{id}");
            System.out.println("POST /refresh");
            System.out.println("GET  /stats");

            Thread.currentThread().join();
        } catch (Exception e) {
            System.out.println("Fatal error: " + e.getMessage());
        }
    }
}