/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package crudmongodbconsola;

import com.mongodb.ConnectionString;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import com.mongodb.client.MongoCursor;
import org.bson.types.ObjectId;

public class CrudMongoDBConsola {

    // Definir la colección de MongoDB
    private static MongoCollection<Document> collection;

    public static void main(String[] args) {

        // Conectar a MongoDB Atlas y obtener la colección
        MongoDatabase database = getDatabase();
        collection = database.getCollection("usuarios");

        // Mostrar opciones al usuario
        Scanner sc = new Scanner(System.in);
        String opcion = "";
        while (!opcion.equals("0")) {
            System.out.println("Elija una opción:");
            System.out.println("1. Obtener todos los datos");
            System.out.println("2. Insertar nuevo dato");
            System.out.println("3. Actualizar un dato");
            System.out.println("4. Eliminar un dato");
            System.out.println("0. Salir");
            opcion = sc.nextLine();

            switch (opcion) {
                case "1":
                    obtenerTodos();
                    break;
                case "2":
                    insertar();
                    break;
                case "3":
                    actualizar();
                    break;
                case "4":
                    eliminar();
                    break;
                case "0":
                    break;
                default:
                    System.out.println("Opción no válida");
                    break;
            }
        }

        // Cerrar la conexión
        ((MongoClient) MongoClients.create(new ConnectionString(getConnectionString()))).close();
    }

    private static void obtenerTodos() {
        System.out.println("Obteniendo todos los registros...");

        // Obtener todos los registros de la colección
        MongoCursor<Document> cursor = collection.find().iterator();
        while (cursor.hasNext()) {
            Document doc = cursor.next();
            System.out.println(doc.toJson());
        }
    }

    private static void insertar() {
        System.out.println("Insertando nuevo registro...");

        // Obtener datos del usuario
        Scanner sc = new Scanner(System.in);
        System.out.println("Nombre:");
        String nombre = sc.nextLine();
        System.out.println("Apellido:");
        String apellido = sc.nextLine();
        System.out.println("Email:");
        String email = sc.nextLine();
        System.out.println("Edad:");
        int edad = sc.nextInt();

        // Crear un nuevo documento y agregarlo a la colección
        Document doc = new Document("nombre", nombre)
                .append("apellido", apellido)
                .append("email", email)
                .append("edad", edad);
        collection.insertOne(doc);
    }

    private static void actualizar() {
        System.out.println("Actualizando un registro...");

        // Pedir el ID del registro a actualizar
        Scanner sc = new Scanner(System.in);
        System.out.println("Ingrese el ID del registro a actualizar:");
        String id = sc.nextLine();
        ObjectId objectId = new ObjectId(id);
        // Buscar el registro por ID
        Document doc = collection.find(Filters.eq("_id", objectId)).first();

        // Si el registro existe, actualizarlo
        if (doc != null) {
            System.out.println("Registro encontrado:");
            System.out.println(doc.toJson());

            // Pedir los nuevos datos del registro
            System.out.println("Ingrese el nuevo nombre (dejar en blanco si no desea actualizar):");
            String nombre = sc.nextLine();
            if (!nombre.isEmpty()) {
                collection.updateOne(Filters.eq("_id", objectId), Updates.set("nombre", nombre));
                System.out.println("Nombre actualizado");
                doc = collection.find(Filters.eq("_id", objectId)).first();
                System.out.println(doc.toJson());

                System.out.println("Ingrese el nuevo apellido (dejar en blanco si no desea actualizar):");
                String apellido = sc.nextLine();
                if (!apellido.isEmpty()) {
                    collection.updateOne(Filters.eq("_id", objectId), Updates.set("apellido", apellido));
                    System.out.println("Apellido actualizado");
                    doc = collection.find(Filters.eq("_id", objectId)).first();
                    System.out.println(doc.toJson());
                }

                System.out.println("Ingrese el nuevo email (dejar en blanco si no desea actualizar):");
                String email = sc.nextLine();
                if (!email.isEmpty()) {
                    collection.updateOne(Filters.eq("_id", objectId), Updates.set("email", email));
                    System.out.println("Email actualizado");
                    doc = collection.find(Filters.eq("_id", objectId)).first();
                    System.out.println(doc.toJson());
                }

                System.out.println("Ingrese la nueva edad (dejar en blanco si no desea actualizar):");
                String edadStr = sc.nextLine();
                if (!edadStr.isEmpty()) {
                    int edad = Integer.parseInt(edadStr);
                    collection.updateOne(Filters.eq("_id", objectId), Updates.set("edad", edad));
                    System.out.println("Edad actualizada");
                    doc = collection.find(Filters.eq("_id", objectId)).first();
                    System.out.println(doc.toJson());
                }

            } else {
                System.out.println("Registro no encontrado");
            }
        }
    }


    private static void eliminar() {
        System.out.println("Eliminando un registro...");

        // Pedir el ID del registro a eliminar
        Scanner sc = new Scanner(System.in);
        System.out.println("Ingrese el ID del registro a eliminar:");
        String id = sc.nextLine();
        ObjectId objectId = new ObjectId(id);
        Document doc = collection.find(Filters.eq("_id", objectId)).first();

        // Si el registro existe, eliminarlo
        if (doc != null) {
            collection.deleteOne(Filters.eq("_id", objectId));
            System.out.println("Registro eliminado");
        } else {
            System.out.println("Registro no encontrado");
        }
    }

// Método para conectar a MongoDB Atlas
    private static MongoDatabase getDatabase() {
        return MongoClients.create(new ConnectionString(getConnectionString()))
                .getDatabase("testing");
    }

// Método para obtener la cadena de conexión de MongoDB Atlas
    private static String getConnectionString() {
        return "mongodb+srv://chekho:elskill@cluster0.4zlheh7.mongodb.net/?retryWrites=true&w=majority";
    }
}
