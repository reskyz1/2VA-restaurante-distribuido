package org.sistema;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.sistema.dto.ItemDTO;
import org.sistema.dto.PedidoDTO;
import org.sistema.http.PedidosClient;

import java.util.List;

public class AppInterface extends Application {

    private final PedidosClient pedidosClient = new PedidosClient();
    private final ObservableList<PedidoDTO> pedidosData =
            FXCollections.observableArrayList();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Restaurante Distribuído");

        // Campos do formulário
        TextField clienteField = new TextField();
        TextField produtoIdField = new TextField();
        TextField quantidadeField = new TextField();

        clienteField.setPromptText("Nome do cliente");
        produtoIdField.setPromptText("ID do produto");
        quantidadeField.setPromptText("Quantidade");

        Button enviarButton = new Button("Enviar pedido");
        Button atualizarButton = new Button("Atualizar lista");

        Label mensagemLabel = new Label();

        // Tabela de pedidos
        TableView<PedidoDTO> tabela = new TableView<>();
        tabela.setItems(pedidosData);

        TableColumn<PedidoDTO, Long> colId = new TableColumn<>("ID");
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<PedidoDTO, String> colCliente = new TableColumn<>("Cliente");
        colCliente.setCellValueFactory(new PropertyValueFactory<>("cliente"));

        TableColumn<PedidoDTO, String> colStatus = new TableColumn<>("Status");
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));

        tabela.getColumns().addAll(colId, colCliente, colStatus);
        tabela.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Layout do formulário
        GridPane formGrid = new GridPane();
        formGrid.setHgap(10);
        formGrid.setVgap(10);
        formGrid.setPadding(new Insets(10));

        formGrid.add(new Label("Cliente:"), 0, 0);
        formGrid.add(clienteField, 1, 0);

        formGrid.add(new Label("ID do Produto:"), 0, 1);
        formGrid.add(produtoIdField, 1, 1);

        formGrid.add(new Label("Quantidade:"), 0, 2);
        formGrid.add(quantidadeField, 1, 2);

        HBox botoesBox = new HBox(10, enviarButton, atualizarButton);
        formGrid.add(botoesBox, 1, 3);

        VBox root = new VBox(10, formGrid, mensagemLabel, tabela);
        root.setPadding(new Insets(10));

        Scene scene = new Scene(root, 700, 500);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Ação botão "Enviar pedido"
        enviarButton.setOnAction(e -> {
            mensagemLabel.setText("");

            String cliente = clienteField.getText().trim();
            String prodStr = produtoIdField.getText().trim();
            String qtdStr = quantidadeField.getText().trim();

            if (cliente.isEmpty() || prodStr.isEmpty() || qtdStr.isEmpty()) {
                mensagemLabel.setText("Preencha todos os campos.");
                return;
            }

            long produtoId;
            int quantidade;
            try {
                produtoId = Long.parseLong(prodStr);
                quantidade = Integer.parseInt(qtdStr);
            } catch (NumberFormatException ex) {
                mensagemLabel.setText("ID do produto e quantidade devem ser numéricos.");
                return;
            }

            PedidoDTO pedido = new PedidoDTO();
            pedido.setCliente(cliente);

            ItemDTO item = new ItemDTO();
            item.setProdutoId(produtoId);
            item.setQuantidade(quantidade);
            pedido.setItens(List.of(item));

            // Chama o serviço REST em outra thread para não travar a UI
            new Thread(() -> {
                try {
                    PedidoDTO criado = pedidosClient.criarPedido(pedido);
                    Platform.runLater(() -> {
                        mensagemLabel.setText("Pedido criado! ID: " + criado.getId()
                                + " | Status: " + (criado.getStatus() != null ? criado.getStatus() : "N/A"));
                        clienteField.clear();
                        produtoIdField.clear();
                        quantidadeField.clear();
                        carregarPedidos();
                    });
                } catch (Exception ex) {
                    ex.printStackTrace();
                    Platform.runLater(() ->
                            mensagemLabel.setText("Erro ao criar pedido: " + ex.getMessage()));
                }
            }).start();
        });

        // Botão "Atualizar lista"
        atualizarButton.setOnAction(e -> carregarPedidos());

        // Carrega pedidos ao abrir
        carregarPedidos();
    }

    private void carregarPedidos() {
        new Thread(() -> {
            try {
                List<PedidoDTO> lista = pedidosClient.listarPedidos();
                Platform.runLater(() -> pedidosData.setAll(lista));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
