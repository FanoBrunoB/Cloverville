package com.example.cloverville.Task;

import com.example.cloverville.GreenAction.GreenAction;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class TaskService {

    private static final Map<Integer, Task> tasksMap = new HashMap<>();

    private static final Path dataPath = Paths.get(
            "src", "main", "resources",
            "com", "example", "cloverville", "Data", "Tasks.json"
    );

    public TaskService(){
        loadData();
    }

    public Map<Integer, Task> getTasksMap() {
        return tasksMap;
    }

    private String readResourceAsString(String path) {
        try (InputStream is = getClass().getResourceAsStream(path)) {
            if (is == null) {
                System.err.println("Recurso no encontrado: " + path);
                return null;
            }
            byte[] bytes = is.readAllBytes();
            return new String(bytes, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Error leyendo recurso: " + path, e);
        }
    }

    private void loadData() {
        String json = readResourceAsString("/com/example/cloverville/Data/Tasks.json");
        if (json == null || json.isBlank()) return;

        json = json.trim();

        // quitamos [ y ]
        if (json.startsWith("[")) {
            json = json.substring(1);
        }
        if (json.endsWith("]")) {
            json = json.substring(0, json.length() - 1);
        }

        // ahora json es algo como:
        // {"id":1,"name":"Alice","points":120},{"id":2,"name":"Bob","points":80}

        // separamos cada objeto
        if (json.isBlank()) return;

        String[] objects = json.split("\\},\\s*\\{");

        for (String obj : objects) {
            // normalizar: asegurar llaves
            obj = obj.trim();
            if (!obj.startsWith("{")) obj = "{" + obj;
            if (!obj.endsWith("}")) obj = obj + "}";

            Task t = parseTask(obj);
            if (t != null) {
                tasksMap.put(t.getId(), t);
            }
        }
    }

    private Task parseTask(String obj) {
        // sacamos llaves
        if (obj.startsWith("{")) obj = obj.substring(1);
        if (obj.endsWith("}")) obj = obj.substring(0, obj.length() - 1);

        String[] fields = obj.split(",");

        Integer id = null;
        String title = null;
        String status = null;
        String deadline = null;
        Integer greenActionAssigned = null;
        Integer residentAssigned = null;


        for (String field : fields) {
            String[] kv = field.split(":", 2);
            if (kv.length != 2) continue;

            String key = kv[0].trim().replace("\"", "");
            String value = kv[1].trim();

            switch (key) {
                case "id" -> id = Integer.parseInt(value);
                case "title" -> {
                    // quitar comillas
                    if (value.startsWith("\"")) value = value.substring(1);
                    if (value.endsWith("\"")) value = value.substring(0, value.length() - 1);
                    title = value;
                }
                case "status" -> {
                    // quitar comillas
                    if (value.startsWith("\"")) value = value.substring(1);
                    if (value.endsWith("\"")) value = value.substring(0, value.length() - 1);
                    status = value;
                }
                case "deadline" -> {
                    // quitar comillas
                    if (value.startsWith("\"")) value = value.substring(1);
                    if (value.endsWith("\"")) value = value.substring(0, value.length() - 1);
                    deadline = value;
                }
                case "greenActionAssigned" -> greenActionAssigned = Integer.parseInt(value);
                case "residentAssigned" -> residentAssigned = Integer.parseInt(value);
            }
        }

        if (id == null || title == null || status == null || deadline == null || greenActionAssigned == null || residentAssigned == null) {
            System.err.println("No se pudo parsear Resident de: " + obj);
            return null;
        }

        return new Task(id, title, status, deadline, greenActionAssigned,residentAssigned);
    }

}
