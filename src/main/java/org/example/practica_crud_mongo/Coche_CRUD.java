package org.example.practica_crud_mongo;

import com.mongodb.ErrorCategory;
import com.mongodb.MongoClient;
import com.mongodb.MongoWriteException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import org.bson.Document;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class Coche_CRUD {

    private MongoClient con = ConnectionDB.conectar();
    MongoDatabase database = con.getDatabase("concesionario");
    MongoCollection<Document> collection = database.getCollection("coches");
    Gson gson = new Gson();

    public void guardarCoche(Coche coche) {
        Document nuevocoche = new Document();
        nuevocoche.append("_id", coche.getMatricula())
                .append("matricula", coche.getMatricula())
                .append("marca", coche.getMarca())
                .append("modelo", coche.getModelo())
                .append("tipo", coche.getTipo());
        try {
            collection.insertOne(nuevocoche);
        } catch (MongoWriteException mwe) {
            if (mwe.getError().getCategory().equals(ErrorCategory.DUPLICATE_KEY)) {
                System.out.println("El documento con esa identificación ya existe");
            }
        }
    }

    public void eliminarCoche(Coche coche) {
        collection.deleteOne(Filters.gte("matricula", coche.getMatricula()));
    }

    public void modificarCoche (Coche cocheAntiguo, Coche cocheNuevo) {
        collection.updateOne(new Document("matricula", cocheAntiguo.getMatricula()),
                new Document("$set", new Document("marca", cocheNuevo.getMarca())
                        .append("modelo", cocheNuevo.getModelo())
                        .append("tipo", cocheNuevo.getTipo()))
        );
    }

    public ObservableList<Coche> obtenerCoches() {
        ObservableList<Coche> coches = FXCollections.observableArrayList();
        MongoCursor<Document> cursor = collection.find().iterator();
        try {
            while (cursor.hasNext()) {
                Coche coche = gson.fromJson(cursor.next().toJson(), Coche.class);
                coches.add(coche);
            }
        } finally {
            cursor.close();
        }
        return coches;
    }

    public boolean existeCoche(String matricula) {
        Document coche = collection.find(Filters.eq("matricula", matricula)).first();

        boolean existe = true;
        if (coche == null) {
            existe = false;
        }

        return existe;
    }
}
