
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

/*
 * Gabriel de Lima Corrêa Ferreira - 598883
 * Curva de Bézier
 */

public class Principal extends Application {
    
    private Canvas canvas;
    private PixelWriter pixelWriter;
    private char comando = ' ';
    private int contadorPontosDeControle = 0;
    private int [ ] pontosXControle;
    private int [ ] pontosYControle;
    private boolean desenhou = false;
    private int posicaoPontoEditar;
    
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
        pontosXControle = new int[4];
        pontosYControle = new int[4];
        canvas = new Canvas(1920,1080);
        pixelWriter = canvas.getGraphicsContext2D().getPixelWriter();
        canvas.setOnMouseClicked(new EventHandler<MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {
                if (comando == 'D' && !desenhou)
                {
                    if (contadorPontosDeControle < 4)
                    {
                        pontosXControle[contadorPontosDeControle] = (int)(event.getX());
                        pontosYControle[contadorPontosDeControle] = (int)(event.getY());
                        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
                        graphicsContext.setFill(Color.RED);
                        graphicsContext.fillRect(pontosXControle[contadorPontosDeControle]-5,pontosYControle[contadorPontosDeControle]-5,10,10);
                        contadorPontosDeControle++;
                    }
                    if (contadorPontosDeControle == 4)
                    {
                        CurvaBezier();
                        desenhou = true;
                        contadorPontosDeControle = 0;
                    }
                }
                else if(comando == 'E')
                {
                    EditarCurva((int)(event.getX()),(int)(event.getY()));
                    GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
                    graphicsContext.setFill(Color.RED);
                    for (int x = 0; x < 4;x++)
                    {
                       graphicsContext.fillRect(pontosXControle[x]-5,pontosYControle[x]-5,10,10); 
                    }
                }
            }
        });
        MenuItem menuItemDesenhar = new MenuItem("Desenhar");
        menuItemDesenhar.setOnAction(new EventHandler<ActionEvent>()
        {
           @Override
           public void handle(ActionEvent event)
           {
               comando = 'D';
           }
        });
        MenuItem menuItemEditar = new MenuItem("Editar");
        menuItemEditar.setOnAction(new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                if (desenhou)
                {
                    ChoiceDialog choiceDialog = new ChoiceDialog("01","01","02","03","04");
                    choiceDialog.setTitle("Editar");
                    choiceDialog.setHeaderText("Após confirmar qual ponto deseja editar, clique na região do canvas que deseja para atualizar posição do ponto e com isso atualizar a curva.");
                    choiceDialog.setContentText("Ponto:");
                    choiceDialog.showAndWait().ifPresent(r -> 
                    {
                        String ponto = r.toString();
                        if (ponto.equals("01"))
                        {
                           posicaoPontoEditar = 0; 
                        }
                        else if(ponto.equals("02"))
                        {
                            posicaoPontoEditar = 1;
                        }
                        else if(ponto.equals("03"))
                        {
                            posicaoPontoEditar = 2;
                        }
                        else
                        {
                            posicaoPontoEditar = 3;
                        }
                        comando = 'E';
                    });
                }
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
                alertaAjuda.setContentText("Pode ser plotado no máximo uma curva no canvas, para desenhar uma nova clique em Limpar Canvas.\n\n" +
                        "Desenhar: Após clicar em Curva de Bézier -> Desenhar, clique no canvas para definir a posição dos 4 pontos para curva ser plotada.\n\n"
                        + "Editar: Após clicar em Curva de Bézier -> Editar, selecione o ponto que deseja editar, após confirmar clique na região do canvas que deseja para atualizar a posição do ponto e com isso atualizar a curva.");
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
        Menu menuBezier = new Menu("Curva de Bézier");
        SeparatorMenuItem separator = new SeparatorMenuItem();
        menuBezier.getItems().add(menuItemDesenhar);
        menuBezier.getItems().add(separator);
        menuBezier.getItems().add(menuItemEditar);
        Menu menuSobre = new Menu();
        Menu menuAjuda = new Menu();
        Menu menuLimparCanvas = new Menu();
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
     * Curva de Bézier.
     */
    
    public void CurvaBezier()
    {
        double u = 0.0001; // valor de controle u entre 0 e 1
        for (double t = 0; t < 1; t+=u)
        {
            plotarPontoBezier(t);
        }
    }
    
    /**
     * Define as coordenadas do novo ponto para ser formado a Curva.
     * @param tAux double - variavel de controle de atualização dos novos pontos
     */
    
    public void plotarPontoBezier(double tAux)
    {
        int x1 = pontosXControle[0];
        int x2 = pontosXControle[1];
        int x3 = pontosXControle[2];
        int x4 = pontosXControle[3];
        int y1 = pontosYControle[0];
        int y2 = pontosYControle[1];
        int y3 = pontosYControle[2];
        int y4 = pontosYControle[3];
        double novoX = Math.pow(1-tAux,3) * x1 + 3 * tAux * Math.pow(1-tAux,2) * x2 + 3 * Math.pow(tAux,2) * (1-tAux) * x3 + Math.pow(tAux,3) * x4;
        double novoY = Math.pow(1-tAux,3) * y1 + 3 * tAux * Math.pow(1-tAux,2) * y2 + 3 * Math.pow(tAux,2) * (1-tAux) * y3 + Math.pow(tAux,3) * y4;
        pixelWriter.setColor((int)Math.round(novoX),(int)Math.round(novoY),Color.BLACK);
    }
    
    /**
     * Editar curva.
     * @param xNovo - int nova coordenada x do ponto que vai ser editado
     * @param yNovo - int nova coordenada y do ponto que vai ser editado
     */
    
    public void EditarCurva(int xNovo, int yNovo)
    {
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        graphicsContext.clearRect(0, 0, 1920, 1080);
        comando = ' ';
        pontosXControle[posicaoPontoEditar] = xNovo;
        pontosYControle[posicaoPontoEditar] = yNovo;
        CurvaBezier();
    }
    
    /**
     * Limpa completamente a região do Canvas.
     */
    
    public void LimparCanvas()
    {
        GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
        graphicsContext.clearRect(0, 0, 1920, 1080);
        comando = ' ';
        desenhou = false;
        contadorPontosDeControle = 0;
    }
}
