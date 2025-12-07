package com.example.cloverville.GreenAction;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;

public class GreenActionService {

    private static final Map<Integer, GreenAction> greenActionsMap = new HashMap<>();

    private static final Path dataPath = Paths.get(
            "src", "main", "resources",
            "com", "example", "cloverville", "Data", "GreenActivities.json"
    );

    public GreenActionService(){
        loadData();
    }

    public Map<Integer, GreenAction> getGreenActionsMap() {
        return greenActionsMap;
    }

    // Lee un recurso de resources como String
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
        String json = readResourceAsString("/com/example/cloverville/Data/GreenActivities.json");
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

            GreenAction g = parseGreenAction(obj);
            if (g != null) {
                greenActionsMap.put(g.getId(), g);
            }
        }
    }

    private GreenAction parseGreenAction(String obj) {
        // sacamos llaves
        if (obj.startsWith("{")) obj = obj.substring(1);
        if (obj.endsWith("}")) obj = obj.substring(0, obj.length() - 1);

        String[] fields = obj.split(",");

        Integer id = null;
        String title = null;
        String description = null;
        Integer points = null;

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
                case "description" -> {
                    // quitar comillas
                    if (value.startsWith("\"")) value = value.substring(1);
                    if (value.endsWith("\"")) value = value.substring(0, value.length() - 1);
                    description = value;
                }
                case "points" -> points = Integer.parseInt(value);
            }
        }

        if (id == null || title == null || description == null || points == null) {
            System.err.println("No se pudo parsear Resident de: " + obj);
            return null;
        }

        return new GreenAction(id, title, description, points);
    }

    public static void addGreenAction(GreenAction g) {
        greenActionsMap.put(g.getId(), g);
        saveData();
    }

    public void updateGreenAction(GreenAction g) {
        greenActionsMap.put(g.getId(), g);
        saveData();
    }

    public void removeGreenAction(GreenAction g){
        greenActionsMap.put(g.getId(), g);
        saveData();
    }

    private static void saveData() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");

        boolean first = true;
        for (GreenAction g : greenActionsMap.values()) {
            if (!first) sb.append(",");
            first = false;

            sb.append("{")
                    .append("\"id\":").append(g.getId()).append(",")
                    .append("\"title\":\"").append(escape(g.getTitle())).append("\",")
                    .append("\"description\":\"").append(escape(g.getDescription())).append("\",")
                    .append("\"points\":").append(g.getPoints())
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
}
