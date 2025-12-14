package com.example.cloverville.Product;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class ProductService {

    private static final Map<Integer, Product> productsMap = new HashMap<>();

    public ProductService(){
        loadData();
    }

    public Map<Integer, Product> getProductsMap() {
        return productsMap;
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
        String json = readResourceAsString("/com/example/cloverville/Data/Products.json");
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

            Product p = parseProducts(obj);
            if (p != null) {
                productsMap.put(p.getId(), p);
            }
        }
    }

    private Product parseProducts(String obj) {
        // sacamos llaves
        if (obj.startsWith("{")) obj = obj.substring(1);
        if (obj.endsWith("}")) obj = obj.substring(0, obj.length() - 1);

        String[] fields = obj.split(",");

        Integer id = null;
        String name = null;
        String description = null;
        Integer price = null;
        Integer stock = null;

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
                case "description" -> {
                    // quitar comillas
                    if (value.startsWith("\"")) value = value.substring(1);
                    if (value.endsWith("\"")) value = value.substring(0, value.length() - 1);
                    description = value;
                }
                case "price" -> price = Integer.parseInt(value);
                case "stock" -> stock = Integer.parseInt(value);
            }
        }

        if (id == null || name == null || description == null || price == null ||  stock == null) {
            System.err.println("No se pudo parsear Resident de: " + obj);
            return null;
        }

        return new Product(id, name, description, price, stock);
    }

}
