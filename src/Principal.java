
import java.util.ArrayList;
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
import javafx.scene.canvas.GraphicsContext;
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
 * Curva de Bézier
 */

public class Principal extends Application {
    
    private Canvas canvas;
    private PixelWriter pixelWriter;
    private boolean informouQuantidade = false;
    private ArrayList<Integer> pontosXControle;
    private ArrayList<Integer> pontosYControle;
    private int quantidadePontosDeControle;
    private int contadorPontosDeControle = 0;
    
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
        pontosXControle = new ArrayList<>();
        pontosYControle = new ArrayList<>();
        canvas = new Canvas(1920,1080);
        pixelWriter = canvas.getGraphicsContext2D().getPixelWriter();
        canvas.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {
                if (informouQuantidade)
                {
                    if (contadorPontosDeControle < quantidadePontosDeControle)
                    {
                        pontosXControle.add((int)event.getX());
                        pontosYControle.add((int)event.getY());
                        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
                        graphicsContext.setFill(Color.BLUE);
                        graphicsContext.fillRect(event.getX()-5,event.getY()-5,10,10);
                        contadorPontosDeControle++;
                    }
                    else
                    {
                        System.out.println("Entrou");
                        CurvaBezier(10000);
                        System.out.println("Saiu");
                        informouQuantidade = false;
                        contadorPontosDeControle = 0;
                        pontosXControle.clear();
                        pontosYControle.clear();
                    }
                }
            }
        });
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
                            quantidadePontosDeControle = Integer.parseInt(quantidadePontosDeControleAux);
                            if (quantidadePontosDeControle > 0)
                            {
                                informouQuantidade = true;
                            }
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
    
    /**
     * Retorna o valor do fatorial de determinando número.
     * @param numero int - número a ser calculado
     * @return resultado int - resultado do fatorial
     */
    
    public static int fatorial(int numero)
    {
        int resultado = 1;
        for (int x = 2; x <= numero; x++)
        {
            resultado *= x;
        }
        return (resultado);
    }
    
    /**
     * Coeficiente Bionimial de Newtown utilizado no Polinômio de Bernstein.
     * @param coeficiente int - nésimo coeficiente da interpolação
     * @param quantidadePontos int - quantidade de pontos de controle
     * @return int - resultado do coeficiente
     */
    
    public static int CoeficienteBinomialNewton(int coeficiente, int quantidadePontos)
    {
        return (fatorial(quantidadePontos)/ fatorial(coeficiente) *fatorial(quantidadePontos-coeficiente));
    }
    
    /**
     * Polinômio de Bernstein
     * @param curvaParametrico int - valor paramétrico da curva
     * @param curvaAtual int - ponto atual da curva
     * @param quantidadePontos int - quantidade de pontos de controle
     * @return int - resultado do polinômio
     */
    
    public static int PolinomioBernstein (int curvaParametrico, int curvaAtual, int quantidadePontos)
    {
        return CoeficienteBinomialNewton(curvaAtual,quantidadePontos)*((int)Math.pow(curvaParametrico,curvaAtual))*((int)Math.pow((1-curvaParametrico),(quantidadePontos-curvaAtual)));
    }
    
    /**
     * Curva de Bézier.
     * @param quantidadePontos int - quantidade de pontos a serem criados
     */
    
    public void CurvaBezier(int quantidadePontos)
    {
        for (int x = 0; x < quantidadePontos;x++)
        {
            int curvaParametrico = x / (quantidadePontos-1); // valor entre 0 e 1, intervalo entre os pontos a serem criados
            int novoX = 0;
            int novoY = 0;
            int j = 0;
            for (j = 0;j < quantidadePontosDeControle;j++)
            {
                int resultadoPolinomio = PolinomioBernstein(curvaParametrico,j,quantidadePontosDeControle-1);
                novoX += pontosXControle.get(j) * resultadoPolinomio;
                novoY += pontosYControle.get(j) * resultadoPolinomio;
                GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
                graphicsContext.setFill(Color.BLUE);
                graphicsContext.fillRect(novoX,novoY,10,10);
            }
            //pixelWriter.setColor(novoX,novoY,Color.BLACK);
        }
    }
    
}
