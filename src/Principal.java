
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
                        pontosXControle.add((int)(event.getX()-(canvas.getWidth()/2)));
                        pontosYControle.add((int)((canvas.getHeight()/2)-event.getY()));
                        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
                        graphicsContext.setFill(Color.RED);
                        graphicsContext.fillRect((canvas.getWidth()/2)+pontosXControle.get(contadorPontosDeControle),(canvas.getHeight()/2)-pontosYControle.get(contadorPontosDeControle),10,10);
                        contadorPontosDeControle++;
                    }
                    if (contadorPontosDeControle == quantidadePontosDeControle)
                    {
                        CurvaBezier();
                        LigarPontosDeControle();
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
        Label limparCanvas = new Label("Limpar Canvas");
        limparCanvas.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {
                LimparCanvas();
            }
        });
        Menu menuBezier = new Menu();
        Menu menuSobre = new Menu();
        Menu menuAjuda = new Menu();
        Menu menuLimparCanvas = new Menu();
        menuBezier.setGraphic(bezier);
        menuSobre.setGraphic(sobre);
        menuAjuda.setGraphic(ajuda);
        menuLimparCanvas.setGraphic(limparCanvas);
        MenuBar menuBar = new MenuBar(menuBezier,menuLimparCanvas,menuSobre,menuAjuda);
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
    
    public static long fatorial(int numero)
    {
        long resultado = 1;
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
    
    public static double CoeficienteBinomialNewton(int coeficiente, int quantidadePontos)
    {
        return (fatorial(quantidadePontos)/ fatorial(coeficiente) *fatorial(quantidadePontos-coeficiente));
    }
    
    /**
     * Curva de Bézier.
     */
    
    public void CurvaBezier()
    {
        int novoPontoX=0;
        int novoPontoY=0;
        double t = 0;
        int tamanho = pontosXControle.size()-1;
        double u = 0.0005; // valor intermediário entre 0 e 1
        System.out.println(u);
        for (int i = 1; t <= 1;i++)
        {
            novoPontoX=0;
            novoPontoY=0;
            for (int j = 0; j <= tamanho;j++)
            {
                novoPontoX += (int)(CoeficienteBinomialNewton(j, tamanho) * Math.pow((double)t, (double)j) * Math.pow((double)1-t,(double)tamanho-j) * (pontosXControle.get(j)));
                novoPontoY += (int)(CoeficienteBinomialNewton(j, tamanho) * Math.pow((double)t, (double)j) * Math.pow((double)1-t,(double)tamanho-j) * (pontosYControle.get(j)));
            }
            GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
            graphicsContext.setFill(Color.BLACK);
            graphicsContext.fillOval(novoPontoX+(canvas.getWidth()/2),(canvas.getHeight()/2)-novoPontoY,10,10);
            if (t == 1)
            {
                t = 1.1;
            }
            else if(u*i > 1)
            {
                t = 1;
            }
            else
            {
                t = u * i;
            }
        }
    }
    
    /**
     * Limpa completamente a região do Canvas.
     */
    
    public void LimparCanvas()
    {
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        graphicsContext.clearRect(0, 0, 1920, 1080);
        informouQuantidade = false;
        contadorPontosDeControle = 0;
        pontosXControle.clear();
        pontosYControle.clear();
    }
    
    /**
     * Metodo para ligar os pontos de controle.
     */
    
    public void LigarPontosDeControle()
    {
        int conversaoX = (int)(canvas.getWidth()/2);
        int conversaoY = (int)(canvas.getHeight()/2);
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        graphicsContext.setFill(Color.DARKTURQUOISE);
        for (int x = 0; x < pontosXControle.size()-1;x++)
        {
            graphicsContext.strokeLine(conversaoX + (pontosXControle.get(x)), conversaoY - (pontosYControle.get(x)),conversaoX + (pontosXControle.get(x+1)),conversaoY - (pontosYControle.get(x+1)));
        }
    }
}
