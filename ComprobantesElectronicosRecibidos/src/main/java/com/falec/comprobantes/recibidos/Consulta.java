package com.falec.comprobantes.recibidos;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Consulta extends Application
{
	@Override
	public void start(Stage primaryStage)
	{
		TabPane tabPane = new TabPane();
		
		Tab tabConfig = new Tab("Configuración");
		Label lblChrome = new Label("Ruta chromedriver * :");
		TextField txtChrome = new TextField("C:\\projects\\gitrepositorio\\FacturaElectronica-Doc\\chromedriver.exe");
		Label lblDescarga = new Label("Ruta descarga * :");
		TextField txtDescarga = new TextField("C:\\Reportes");
		Button btnEditar = new Button("Editar");
		GridPane gridPaneConfig = new GridPane();
		gridPaneConfig.setHgap(15);
		gridPaneConfig.setVgap(15);
		gridPaneConfig.add(lblChrome, 0, 0);
		gridPaneConfig.add(txtChrome, 1, 0);
		gridPaneConfig.add(lblDescarga, 0, 1);
		gridPaneConfig.add(txtDescarga, 1, 1);
		gridPaneConfig.add(btnEditar, 0, 2);
		gridPaneConfig.setPadding(new Insets(20, 20, 20, 20));
		gridPaneConfig.setAlignment(Pos.TOP_LEFT);
		tabConfig.setContent(gridPaneConfig);
		
		Tab tabConsulta = new Tab("Consulta");
		Label lblRuc = new Label("RUC / C.I. * :");
		TextField txtRuc = new TextField();
		Label lblCIAdicional = new Label("C.I. adicional :");
		TextField txtCIAdicional = new TextField();
		Label lblClave = new Label("Clave * :");
		TextField txtClave = new TextField();
		Label lblAnio = new Label("Año :");
		ChoiceBox cmbAnio = new ChoiceBox();
		cmbAnio.getItems().addAll("2020", "2019", "2018", "2017", "2016", "2015", "2014", "2013", "2012");
		Label lblMes = new Label("Mes :");
		ChoiceBox cmbMes = new ChoiceBox();
		cmbMes.getItems().addAll("Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio", "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre");
		Label lblDia = new Label("Día :");
		ChoiceBox cmbDia = new ChoiceBox();
		cmbDia.getItems().addAll("TODOS", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", 
				"16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31");
		Button btnConsultar = new Button("Consultar");
		GridPane gridPaneConsulta = new GridPane();
		gridPaneConsulta.setHgap(15);
		gridPaneConsulta.setVgap(15);
		gridPaneConsulta.add(lblRuc, 0, 0);
		gridPaneConsulta.add(txtRuc, 1, 0);
		gridPaneConsulta.add(lblCIAdicional, 0, 1);
		gridPaneConsulta.add(txtCIAdicional, 1, 1);
		gridPaneConsulta.add(lblClave, 0, 2);
		gridPaneConsulta.add(txtClave, 1, 2);
		gridPaneConsulta.add(lblAnio, 0, 3);
		gridPaneConsulta.add(cmbAnio, 1, 3);
		gridPaneConsulta.add(lblMes, 0, 4);
		gridPaneConsulta.add(cmbMes, 1, 4);
		gridPaneConsulta.add(lblDia, 0, 5);
		gridPaneConsulta.add(cmbDia, 1, 5);
		gridPaneConsulta.add(btnConsultar, 0, 6);
		gridPaneConsulta.setPadding(new Insets(20, 20, 20, 20));
		gridPaneConsulta.setAlignment(Pos.TOP_LEFT);
		tabConsulta.setContent(gridPaneConsulta);
		
		tabPane.getTabs().add(tabConfig);
		tabPane.getTabs().add(tabConsulta);
		VBox vbox = new VBox(tabPane);
        Scene scene = new Scene(vbox, 600, 500);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Comprobantes Electrónicos Recibidos");
        primaryStage.show();
	}

	public static void main(String[] args)
	{
		launch(args);
	}
}
