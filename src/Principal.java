
import java.util.Optional;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.image.PixelWriter;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

/*
 * Gabriel de Lima Corrêa Ferreira - 598883
 * Curva de Bezier
 */

public class Principal extends Application {
    
    private Canvas canvas;
    private PixelWriter pixelWriter;
    
    /**
     * Inicializar programa.
     */
    
    public static void main(String [] args)
    {
        launch();
    }
    
    /**
     * Configura tela, canvas e listeners de click.
     */
    
    @Override
    public void start(Stage stage)
    {
        String suporte;
        canvas = new Canvas(1920,1080);
        pixelWriter = canvas.getGraphicsContext2D().getPixelWriter();
        Label bezier = new Label("Curva de Bézier");
        bezier.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {
                Dialog <String> informarQuantidadePontosDeControle = new Dialog<>();
                informarQuantidadePontosDeControle.setTitle("Curva de Bézier");
                informarQuantidadePontosDeControle.setHeaderText(null);
                ButtonType ok = new ButtonType("OK",ButtonData.OK_DONE);
                informarQuantidadePontosDeControle.getDialogPane().getButtonTypes().addAll(ok,ButtonType.CANCEL);
                GridPane grid = new GridPane();
                grid.setHgap(10);
                grid.setVgap(10);
                grid.setPadding(new Insets(20,150,10,10));
                TextField quantidadePontosDeControleCampo = new TextField();
                grid.add(new Label("Quantidade Pontos de Controle:"),0,0);
                grid.add(quantidadePontosDeControleCampo,1,0);
                Node node = informarQuantidadePontosDeControle.getDialogPane().lookupButton(ok);
                quantidadePontosDeControleCampo.textProperty().addListener((observable,oldValue,newValue) ->
                {
                   node.setDisable(newValue.trim().isEmpty()); 
                });
                informarQuantidadePontosDeControle.getDialogPane().setContent(grid);
                informarQuantidadePontosDeControle.setResultConverter(dialogButton ->
                {
                    if (dialogButton == ok)
                    {
                        return (quantidadePontosDeControleCampo.getText());
                    }
                    return null;
                });
                Optional<String> optional = informarQuantidadePontosDeControle.showAndWait();
                optional.ifPresent(quantidadePontosDeControleAux ->
                {
                    if (!quantidadePontosDeControleAux.isEmpty())
                    {
                        try
                        {
                            int quantidadePontosDeControle = Integer.parseInt(quantidadePontosDeControleAux);
                        }
                        catch (Exception e)
                        {
                            Alert alertaErro = new Alert(AlertType.ERROR);
                            alertaErro.setTitle("ERRO");
                            alertaErro.setHeaderText("Quantidade Invalida!");
                            alertaErro.setHeaderText("Insira valores inteiros!");
                            alertaErro.showAndWait();
                        }
                    }
                });
            }
        });
        Label sobre = new Label("Sobre");
        sobre.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {
                Alert alertaSobre = new Alert(AlertType.INFORMATION);
                alertaSobre.setTitle("Sobre");
                alertaSobre.setHeaderText("Trabalho Computação Gráfica");
                alertaSobre.setContentText("Feito por: Gabriel de Lima Corrêa Ferreira");
                alertaSobre.showAndWait();
            }
        });
        Label ajuda = new Label("Ajuda");
        ajuda.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {
                Alert alertaAjuda = new Alert(AlertType.INFORMATION);
                alertaAjuda.setTitle("Ajuda");
                alertaAjuda.setHeaderText(null);
                alertaAjuda.setContentText("Após clicar em Curva de Bézier informe a quantidade de pontos de controle, após isso clique no canvas para definir a posição deste pontos para curva ser plotada.");
                alertaAjuda.showAndWait();
            }
        });
        Menu menuBezier = new Menu();
        Menu menuSobre = new Menu();
        Menu menuAjuda = new Menu();
        menuBezier.setGraphic(bezier);
        menuSobre.setGraphic(sobre);
        menuAjuda.setGraphic(ajuda);
        MenuBar menuBar = new MenuBar(menuBezier,menuSobre,menuAjuda);
        BorderPane pane = new BorderPane();
        pane.setCenter(canvas);
        pane.setTop(menuBar);
        Scene scene = new Scene(pane,1920,1080,Color.WHITE);
        scene.getStylesheets().add("Style.css");
        stage.setScene(scene);
        stage.setMaximized(true);
        stage.setTitle("Trabalho CG - Curva de Bézier");
        stage.show();
    }
    
}
