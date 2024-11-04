package org.example.practica_crud_mongo;

import javafx.scene.control.Alert;

public class AlertUtils {
    public static void mostrarError(String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.ERROR);
        alerta.setContentText(mensaje);
        alerta.show();
    }
}
