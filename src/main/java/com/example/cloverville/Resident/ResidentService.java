package com.example.cloverville.Resident;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.HashMap;
import java.util.Map;

public class ResidentService {

    // Mapa principal: id -> Resident
    private static final Map<Integer, Resident> residentMap = new HashMap<>();

    // Ruta al archivo JSON en tu proyecto
    // src/main/resources/com/example/cloverville/Data/Data.json
    private static final Path dataPath = Paths.get(
            "src", "main", "resources",
            "com", "example", "cloverville", "Data", "Data.json"
    );

    public ResidentService() {
        loadData();
    }

    public Map<Integer, Resident> getResidentMap() {
        return residentMap;
    }

    public Resident getResident(int id) {
        return residentMap.get(id);
    }

    // ========= NUEVOS MÉTODOS PÚBLICOS =========

    // Agregar un residente al mapa y guardar en el JSON
    public static void addResident(Resident r) {
        residentMap.put(r.getId(), r);
        saveData();
    }

    // Actualizar un residente existente (mismo id) y guardar
    public void updateResident(Resident r) {
        residentMap.put(r.getId(), r);
        saveData();
    }

    // ==========================
    // CARGA DESDE Data.json
    // ==========================
    private void loadData() {
        String json = readResourceAsString("/com/example/cloverville/Data/Data.json");
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

            Resident r = parseResident(obj);
            if (r != null) {
                residentMap.put(r.getId(), r);
            }
        }
    }

    // Parsea algo tipo: {"id":1,"name":"Alice","points":120}
    private Resident parseResident(String obj) {
        // sacamos llaves
        if (obj.startsWith("{")) obj = obj.substring(1);
        if (obj.endsWith("}")) obj = obj.substring(0, obj.length() - 1);

        String[] fields = obj.split(",");

        Integer id = null;
        String name = null;
        Integer points = null;

        for (String field : fields) {
            String[] kv = field.split(":", 2);
            if (kv.length != 2) continue;

            String key = kv[0].trim().replace("\"", "");
            String value = kv[1].trim();

            switch (key) {
                case "id" -> id = Integer.parseInt(value);
                case "name" -> {
                    // quitar comillas
                    if (value.startsWith("\"")) value = value.substring(1);
                    if (value.endsWith("\"")) value = value.substring(0, value.length() - 1);
                    name = value;
                }
                case "points" -> points = Integer.parseInt(value);
            }
        }

        if (id == null || name == null || points == null) {
            System.err.println("No se pudo parsear Resident de: " + obj);
            return null;
        }

        return new Resident(id, name, points);
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

    // ========= NUEVO: GUARDAR EL MAP EN Data.json =========
    private static void saveData() {
        StringBuilder sb = new StringBuilder();
        sb.append("[");

        boolean first = true;
        for (Resident r : residentMap.values()) {
            if (!first) sb.append(",");
            first = false;

            sb.append("{")
                    .append("\"id\":").append(r.getId()).append(",")
                    .append("\"name\":\"").append(escape(r.getName())).append("\",")
                    .append("\"points\":").append(r.getPoints())
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

    public Resident getById(int id) {
        return residentMap.get(id);
    }

    public static void removeById(int id) {
        residentMap.remove(id);
        saveData();
    }
}
