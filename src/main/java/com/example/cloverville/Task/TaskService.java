package com.example.cloverville.Task;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
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

    public static void addTask(Task t){
        tasksMap.put(t.getId(), t);
        saveData();
    }

    private static void saveData() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");

        boolean first = true;
        for (Task t : tasksMap.values()) {
            if (!first) sb.append(",");
            first = false;

            sb.append("{")
                    .append("\"id\":").append(t.getId()).append(",")
                    .append("\"title\":\"").append(escape(t.getTitle())).append("\",")
                    .append("\"status\":\"").append(escape(t.getStatus())).append("\",")
                    .append("\"deadline\":\"").append(escape(t.getDeadline())).append("\",")
                    .append("\"greenActionAssigned\":").append(t.getGreenActionAssigned()).append(",")
                    .append("\"residentAssigned\":").append(t.getResidentAssigned())
                    .append("}");
        }

        sb.append("]");

        try {
            Files.createDirectories(dataPath.getParent());
            Files.writeString(
                    dataPath,
                    sb.toString(),
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );
        } catch (IOException e) {
            throw new RuntimeException("Error escribiendo " + dataPath, e);
        }
    }


    private static String escape(String s) {
        return s.replace("\"", "\\\"");
    }

    public static void removeById(int id) {
        tasksMap.remove(id);
        saveData();
    }

    public void updateTask(Task t) {
        tasksMap.put(t.getId(), t);
        saveData();
    }
}
