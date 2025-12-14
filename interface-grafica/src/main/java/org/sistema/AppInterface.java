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
import org.sistema.dto.FuncionarioDTO;
import org.sistema.http.PedidosClient;
import org.sistema.http.FuncionarioClient;

import java.util.List;

public class AppInterface extends Application {

    private final PedidosClient pedidosClient = new PedidosClient();
    private final FuncionarioClient funcionarioClient = new FuncionarioClient(); // <- NOVO

    private final ObservableList<PedidoDTO> pedidosData =
            FXCollections.observableArrayList();

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Restaurante Distribuído - Interface");

        TextField clienteField = new TextField();
        TextField produtoIdField = new TextField();
        TextField quantidadeField = new TextField();
        TextField funcionarioIdField = new TextField();  // <- NOVO

        clienteField.setPromptText("Nome do cliente");
        produtoIdField.setPromptText("ID do produto");
        quantidadeField.setPromptText("Quantidade");
        funcionarioIdField.setPromptText("ID do funcionário"); // <- NOVO

        Button enviarButton = new Button("Enviar pedido");
        Button atualizarButton = new Button("Atualizar lista");

        Label mensagemLabel = new Label();

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

        GridPane formGrid = new GridPane();
        formGrid.setHgap(10);
        formGrid.setVgap(10);
        formGrid.setPadding(new Insets(10));

        int linha = 0;

        formGrid.add(new Label("ID do Funcionário:"), 0, linha);
        formGrid.add(funcionarioIdField, 1, linha++);
        formGrid.add(new Label("Cliente:"), 0, linha);
        formGrid.add(clienteField, 1, linha++);
        formGrid.add(new Label("ID do Produto:"), 0, linha);
        formGrid.add(produtoIdField, 1, linha++);
        formGrid.add(new Label("Quantidade:"), 0, linha);
        formGrid.add(quantidadeField, 1, linha++);

        HBox botoesBox = new HBox(10, enviarButton, atualizarButton);
        formGrid.add(botoesBox, 1, linha);

        VBox root = new VBox(10, formGrid, mensagemLabel, tabela);
        root.setPadding(new Insets(10));

        Scene scene = new Scene(root, 700, 500);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Clique do botão "Enviar pedido"
        enviarButton.setOnAction(e -> {
            mensagemLabel.setText("");

            String funcStr = funcionarioIdField.getText().trim();
            String cliente = clienteField.getText().trim();
            String prodStr = produtoIdField.getText().trim();
            String qtdStr = quantidadeField.getText().trim();

            if (funcStr.isEmpty() || cliente.isEmpty() || prodStr.isEmpty() || qtdStr.isEmpty()) {
                mensagemLabel.setText("Preencha todos os campos, incluindo o ID do funcionário.");
                return;
            }

            long funcionarioId;
            long produtoId;
            int quantidade;

            try {
                funcionarioId = Long.parseLong(funcStr);
                produtoId = Long.parseLong(prodStr);
                quantidade = Integer.parseInt(qtdStr);
            } catch (NumberFormatException ex) {
                mensagemLabel.setText("ID do funcionário, ID do produto e quantidade devem ser numéricos.");
                return;
            }

            // valida funcionario em outra thread
            new Thread(() -> {
                try {
                    FuncionarioDTO funcionario = funcionarioClient.buscarPorId(funcionarioId);
                    if (funcionario == null) {
                        Platform.runLater(() ->
                                mensagemLabel.setText("Funcionário não encontrado (ID " + funcionarioId + ")."));
                        return;
                    }

                    if (!funcionario.isAtivo()) {
                        Platform.runLater(() ->
                                mensagemLabel.setText("Funcionário " + funcionario.getNome()
                                        + " (ID " + funcionario.getId() + ") não está ativo."));
                        return;
                    }

                    // se chegou aqui, funcionario é válido e ativo -> cria pedido
                    PedidoDTO pedido = new PedidoDTO();
                    pedido.setCliente(cliente);

                    ItemDTO item = new ItemDTO();
                    item.setProdutoId(produtoId);
                    item.setQuantidade(quantidade);
                    pedido.setItens(List.of(item));

                    PedidoDTO criado = pedidosClient.criarPedido(pedido);
                    Platform.runLater(() -> {
                        mensagemLabel.setText("Pedido criado! ID: " + criado.getId()
                                + " | Status: " + (criado.getStatus() != null ? criado.getStatus() : "N/A"));
                        clienteField.clear();
                        produtoIdField.clear();
                        quantidadeField.clear();
                        // funcionário poderia permanecer na tela
                        carregarPedidos();
                    });

                } catch (Exception ex) {
                    ex.printStackTrace();
                    Platform.runLater(() ->
                            mensagemLabel.setText("Erro ao validar funcionário ou criar pedido: " + ex.getMessage()));
                }
            }).start();
        });

        atualizarButton.setOnAction(e -> carregarPedidos());

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
