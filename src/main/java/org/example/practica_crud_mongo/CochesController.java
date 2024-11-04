package org.example.practica_crud_mongo;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

public class CochesController implements Initializable {

    public TextField txtMatricula, txtMarca, txtModelo;
    public ComboBox<String> cbTipo;
    public TableView<Coche> tblCoches;
    public TableColumn<Coche, String> colMatricula, colMarca, colModelo, colTipo;
    public Button btnNuevo, btnGuardar, btnCancelar, btnModificar, btnEliminar;
    public Label lblEstado;

    private enum Accion {
        NUEVO, MODIFICAR
    }

    private Accion accion;

    private final Coche_CRUD cocheCRUD;
    private final ConnectionDB conexion;
    private Coche cocheSeleccionado;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.colMatricula.setCellValueFactory(new PropertyValueFactory<Coche, String>("matricula"));
        this.colMarca.setCellValueFactory(new PropertyValueFactory<Coche, String>("marca"));
        this.colModelo.setCellValueFactory(new PropertyValueFactory<Coche, String>("modelo"));
        this.colTipo.setCellValueFactory(new PropertyValueFactory<Coche, String>("tipo"));

        ObservableList<Coche> lista = cocheCRUD.obtenerCoches();
        this.tblCoches.setItems(lista);
    }

    public CochesController() {
        cocheCRUD = new Coche_CRUD();
        conexion = new ConnectionDB();
        try {
            conexion.conectar();
        } catch (Exception exception) {
            System.err.println(exception.getClass().getName() + ": " + exception.getMessage());
        }
    }

    public void cargarDatos() {
        modoEdicion(false);

        tblCoches.getItems().clear();

        List<Coche> coches = cocheCRUD.obtenerCoches();
        tblCoches.setItems(FXCollections.observableList(coches));

        String[] tipos = new String[]{"<Selecciona tipo>", "Familiar", "Monovolumen", "Deportivo", "SUV"};
        cbTipo.setItems(FXCollections.observableArrayList(tipos));
    }

    @FXML
    public void nuevoCoche(Event event) {
        limpiarCajas();
        modoEdicion(true);
        accion = Accion.NUEVO;
    }

    @FXML
    public void modificarCoche(Event event) {
        modoEdicion(true);
        accion = Accion.MODIFICAR;
    }

    @FXML
    public void eliminarCoche(Event event) {
        Coche coche = (Coche) tblCoches.getSelectionModel().getSelectedItem();
        if (coche == null) {
            lblEstado.setText("ERROR: No se ha seleccionado ningún coche");
            return;
        }

        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Eliminar coche");
        confirmacion.setContentText("¿Estás seguro?");
        Optional<ButtonType> respuesta = confirmacion.showAndWait();
        if (respuesta.get().getButtonData() == ButtonBar.ButtonData.CANCEL_CLOSE)
            return;

        cocheCRUD.eliminarCoche(coche);
        lblEstado.setText("Coche eliminado con éxito");

        cargarDatos();
    }

    @FXML
    public void guardarCoche(Event event) {
        String matricula = txtMatricula.getText();
        if (matricula.equals("")) {
            AlertUtils.mostrarError("La matricula es un campo obligatorio");
            return;
        }
        String marca = txtMarca.getText();
        String modelo = txtModelo.getText();
        String tipo = cbTipo.getSelectionModel().getSelectedItem();
        Coche coche = new Coche(matricula, marca, modelo, tipo);

        switch (accion) {
            case NUEVO:
                cocheCRUD.guardarCoche(coche);
                break;
            case MODIFICAR:
                cocheCRUD.modificarCoche(cocheSeleccionado, coche);
                break;
        }

        lblEstado.setText("Coche guardado con éxito");
        cargarDatos();

        modoEdicion(false);
    }

    @FXML
    public void cancelar() {
        Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacion.setTitle("Edición");
        confirmacion.setContentText("¿Estás seguro?");
        Optional<ButtonType> respuesta = confirmacion.showAndWait();
        if (respuesta.get().getButtonData() == ButtonBar.ButtonData.CANCEL_CLOSE)
            return;

        modoEdicion(false);
        cargarCoche(cocheSeleccionado);
    }

    @FXML
    public void cargarCoche(Coche coche) {
        txtMatricula.setText(coche.getMatricula());
        txtMarca.setText(coche.getMarca());
        txtModelo.setText(coche.getModelo());
        cbTipo.setValue(coche.getTipo());
    }

    @FXML
    public void seleccionarCoche(Event event) {
        /*tblCoches.getItems().clear();

        List<Coche> coches = cocheCRUD.obtenerCoches();
        tblCoches.setItems(FXCollections.observableList(coches));*/
        cocheSeleccionado = (Coche) tblCoches.getSelectionModel().getSelectedItem();
        System.out.println(cocheSeleccionado);
        cargarCoche(cocheSeleccionado);
    }

    @FXML
    public void importar(ActionEvent event) {

    }

    public void mostrarDialogo() {
        Dialog dialog = new Dialog();
        dialog.setTitle("hola Aitor");
        dialog.setContentText("hola a todos");
        dialog.show();
    }

    private void limpiarCajas() {
        txtMatricula.setText("");
        txtModelo.setText("");
        txtMarca.setText("");
        cbTipo.setValue("<Selecciona tipo>");
        txtMatricula.requestFocus();
    }

    private void modoEdicion(boolean activar) {
        btnNuevo.setDisable(activar);
        btnGuardar.setDisable(!activar);
        btnModificar.setDisable(activar);
        btnEliminar.setDisable(activar);

        txtMatricula.setEditable(activar);
        txtMarca.setEditable(activar);
        txtModelo.setEditable(activar);
        cbTipo.setDisable(!activar);

        tblCoches.setDisable(activar);
    }
}