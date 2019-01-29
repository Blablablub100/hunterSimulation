package UI;

import Simulation.SimulationController;
import Simulation.SimulationObjects.*;
import UI.grid.Cell;
import UI.grid.GridModel;
import UI.grid.GridView;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import static jdk.nashorn.internal.objects.NativeMath.round;

public class GUI extends Application{


    /**
     * The states that a grid cell can have in our example
     */
    public static enum States {
        EMPTY,
        B,
        HUNTER,
        PREY,
        OBSTACLE,
        CARRION
    }


    public static void main(String...args){
        Application.launch(args);
    }

    private SimulationController sim;

    private GridModel<States> gridModel;
    private IntegerProperty numberOfRows = new SimpleIntegerProperty(30);
    private IntegerProperty numberOfColumns = new SimpleIntegerProperty(30);
    private IntegerProperty numberOfHunter = new SimpleIntegerProperty(10);
    private IntegerProperty numberOfPrey = new SimpleIntegerProperty(10);
    private IntegerProperty numberOfObstacles = new SimpleIntegerProperty(10);

    private SimpleDoubleProperty hpRatio = new SimpleDoubleProperty(0.0);
    private SimpleDoubleProperty avgFoodGainH = new SimpleDoubleProperty(0.0);
    private SimpleDoubleProperty avgFoodGainP = new SimpleDoubleProperty(0.0);
    private IntegerProperty deadPrey = new SimpleIntegerProperty(0);
    private IntegerProperty deadHunter = new SimpleIntegerProperty(0);


    private IntegerProperty cellBorderWidth = new SimpleIntegerProperty(1);

    public void printSim(SimulationController sim) {
        clear();
        List<BoardObject> things = sim.getBoard().getBoardObjects();
        for (BoardObject thing: things) {
            if (thing instanceof Hunter) {
                Cell c = gridModel.getCell(thing.getLocation().getX(), thing.getLocation().getY());
                c.changeState(States.HUNTER);
            } else if (thing instanceof Prey) {
                Cell c = gridModel.getCell(thing.getLocation().getX(), thing.getLocation().getY());
                c.changeState(States.PREY);
            } else if (thing instanceof Obstacle) {
                Cell c = gridModel.getCell(thing.getLocation().getX(), thing.getLocation().getY());
                c.changeState(States.OBSTACLE);
            } else if (thing instanceof DeadCorpse) {
                Cell c = gridModel.getCell(thing.getLocation().getX(), thing.getLocation().getY());
                c.changeState(States.CARRION);
            }
        }

    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Hunter Simulation");
        BorderPane root = new BorderPane();
        root.setMinSize(0,0);

        gridModel = new GridModel<>();

        // define the state that will be used as default.
        gridModel.setDefaultState(States.EMPTY);


        // create the grid view and shunter coloret the grid model
        GridView<States> gridView = new GridView<>();
        gridView.setGridModel(gridModel);

        gridView.setMinSize(0,0);

        // define which color is shown when a cell has a specific state
        gridView.addColorMapping(States.EMPTY, Color.YELLOWGREEN);
        gridView.addColorMapping(States.B, Color.ORANGE);
        gridView.addColorMapping(States.HUNTER, Color.RED);
        gridView.addColorMapping(States.PREY, Color.BLUE);
        gridView.addColorMapping(States.OBSTACLE, Color.GREY);
        gridView.addColorMapping(States.CARRION, Color.VIOLET);


        // additionally we add labels that are placed inside the cells
        gridView.addNodeMapping(States.EMPTY, (cell) -> new Label(""));
        gridView.addNodeMapping(States.B, (cell) -> new Label("B"));
        gridView.addNodeMapping(States.HUNTER, (cell) -> new Label("H"));
        gridView.addNodeMapping(States.PREY, (cell) -> new Label("P"));
        gridView.addNodeMapping(States.OBSTACLE, (cell) -> new Label("O"));
        gridView.addNodeMapping(States.CARRION, (cell) -> new Label("C"));


        // styling of the strokes between the cells
        gridView.cellBorderColorProperty().set(Color.BLACK);
        gridView.cellBorderWidthProperty().set(1);

        gridView.gridBorderColorProperty().set(Color.BLUE);
        gridView.gridBorderWidthProperty().set(1);

        // setzt einfach nur merkwürdige fette Linien in das Spielfeld
        /*gridView.horizontalGuidelineUnitProperty().set(3);
        gridView.verticalGuidelineUnitProperty().set(5);
        gridView.guidelineColorProperty().set(Color.BLACK);
        gridView.guidelineStrokeWidth().set(4);*/

        root.setCenter(gridView);

        // every time a new cell is added, we add an click listener to it.
        gridModel.setOnCellAddedHandler((cell)->{
            // the click handler switches the state of the cells
            cell.setOnClick(event -> switchStates(cell));

            // move over cells with pressed mouse button will switch states
            cell.setOnMouseOver(event -> {
                if(event.isPrimaryButtonDown()){
                    switchStates(cell);
                }
            });
        });


        // bind the number of rows/columns in the grid model
        gridModel.numberOfColumns().bind(numberOfColumns);
        gridModel.numberOfRows().bind(numberOfRows);


        gridView.cellBorderWidthProperty().bind(cellBorderWidth);

        VBox right = createGridControls();
        right.getChildren().addAll(createQuickStatistics().getChildren());

        root.setRight(right);

        stage.setScene(new Scene(root, 800,600));
        stage.show();
        startSim();
    }

    private void startSim() {
        try {
            sim = new SimulationController(new UserInput(
                    30
                    , 30
                    , 60
                    , 30
                    , 0
            ));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Timer timer = new Timer();
        timer.schedule(new NextSimulationStep(sim), 0, 100);
    }


    private void clear() {
        for (Cell cell: gridModel.getCells()) {
            cell.changeState(States.EMPTY);
        }
    }


    private void switchStates(Cell<States> cell){
        final States stateBefore = cell.getState();
        if (stateBefore == States.EMPTY) {
            cell.changeState(States.B);
        } else {
            cell.changeState(States.EMPTY);
        }
    }

    /**
     * Create a controls panel so that we can control the grid properties.
     */
    private VBox createGridControls(){
        VBox controlsBox = new VBox();
        controlsBox.setSpacing(5);
        controlsBox.setPadding(new Insets(5));

        HBox headerBox = createHeader("Simulation Control");
        HBox numberOfRowsBox = createNumberControl("Rows:\t\t\n", numberOfRows, 3, 99);
        HBox numberOfColumnsBox = createNumberControl("Columns:\t\n", numberOfColumns, 3, 99);
        //HBox cellBorderWidthBox = createNumberControl("Cell Border Width:", cellBorderWidth, 0, 5);
        HBox numberOfHunterBox = createNumberControl("Hunters:\t\t\n", numberOfHunter, 0, 999);
        HBox numberOfPreyBox = createNumberControl("Preys:\t\t\n", numberOfPrey, 0, 999);
        HBox numberOfObstacleBox = createNumberControl("Obstacles:\t\n", numberOfObstacles, 0, 999);

        controlsBox.getChildren().addAll(headerBox
                , numberOfRowsBox
                , numberOfColumnsBox
                , numberOfHunterBox
                , numberOfPreyBox
                , numberOfObstacleBox);

        return controlsBox;
    }

    private VBox createQuickStatistics() {
        VBox controlsBox = new VBox();
        controlsBox.setSpacing(5);
        controlsBox.setPadding(new Insets(5));

        HBox headerBox = createHeader("Quick Statistics");
        HBox hpRatioBox = createStatistics("H/P Ratio\t\t", hpRatio);
        HBox avgFoodGainHBox = createStatistics("Avg Food Gain H\t", avgFoodGainH);
        HBox avgFoodGainPBox = createStatistics("Avg Food Gain P\t", avgFoodGainP);
        HBox hunterDeadBox = createStatistics("Hunter died\t\t", deadHunter);
        HBox preyDeadBox = createStatistics("Prey died\t\t", deadPrey);

        controlsBox.getChildren().addAll(headerBox
                , hpRatioBox
                , avgFoodGainHBox
                , avgFoodGainPBox
                , hunterDeadBox
                , preyDeadBox);
        return controlsBox;
    }

    private VBox createSimulationControls() {
        VBox controlsBox = new VBox();
        controlsBox.setSpacing(5);
        controlsBox.setPadding(new Insets(5));

        HBox headerBox = createHeader("Simulation Control");
        HBox widthBox = createNumberTextControl("width\t");
        HBox heightBox = createNumberTextControl("height\t");
        HBox hunterBox = createNumberTextControl("hunter\t");
        HBox preyBox = createNumberTextControl("prey\t\t");
        HBox obstacleBox = createNumberTextControl("obstacle\t");

        controlsBox.getChildren().addAll(headerBox, widthBox, heightBox, hunterBox, preyBox, obstacleBox);

        return controlsBox;
    }

    private HBox createStatistics(String name, Property<Number> val) {
        HBox stat = new HBox();
        stat.setSpacing(5);
        Label labelName = new Label (name);
        Label labelVal = new Label ();
        stat.getChildren().addAll(labelName, labelVal);

        labelVal.textProperty().bind(Bindings.concat(val));

        return stat;
    }

    private HBox createHeader(String headerString) {
        HBox header = new HBox();
        header.setSpacing(5);
        Label label = new Label(headerString);
        label.setStyle("-fx-font-weight:700");
        header.getChildren().addAll(label);
        return header;
    }

    private HBox createNumberTextControl(String labelString) {
        HBox control = new HBox();
        control.setSpacing(5);
        Label label = new Label(labelString);
        NumberTextField numberTextField = new NumberTextField();
        control.getChildren().addAll(label, numberTextField);
        return control;
    }

    private HBox createNumberControl(String labelString, Property<Number> numberValue, double min, double max){
        HBox control = new HBox();

        control.setSpacing(5);

        Label label = new Label();
        Slider slider = new Slider();
        control.getChildren().addAll(label, slider);


        label.textProperty().bind(Bindings.concat(labelString, numberValue));
        slider.valueProperty().bindBidirectional(numberValue);

        slider.setMin(min);
        slider.setMax(max);

        return control;
    }

    class NextSimulationStep extends TimerTask {

        SimulationController sim;


        public NextSimulationStep(SimulationController sim) {
            this.sim = sim;
        }

        public void run() {
            Platform.runLater(() -> {
                numberOfPrey.setValue(sim.getBoard().getPreys().size());
                numberOfHunter.setValue(sim.getBoard().getHunters().size());
                numberOfObstacles.setValue(sim.getStats().getObstacleCount());
                printSim(sim);
                sim.simulateNextStep();
                updateStats();
            });
        }

        private void updateStats() {
            hpRatio.set(sim.getStats().getHunterPreyRatio());
            avgFoodGainH.set(round(sim.getStats().getAvgFoodGainPerIterationHunter(), 2));
            avgFoodGainP.set(round(sim.getStats().getAvgFoodGainPerIterationPrey(), 2));
            deadHunter.set(sim.getStats().getAmtHunterDead());
            deadPrey.set(sim.getStats().getAmtPreyDead());
        }

        public double round(double value, int places) {
            if (places < 0) throw new IllegalArgumentException();

            BigDecimal bd = new BigDecimal(value);
            bd = bd.setScale(places, RoundingMode.HALF_UP);
            return bd.doubleValue();
        }    }
}


// https://stackoverflow.com/questions/7555564/what-is-the-recommended-way-to-make-a-numeric-textfield-in-javafx
class NumberTextField extends TextField {

    @Override
    public void replaceText(int start, int end, String text) {
        if (validate(text)) {
            super.replaceText(start, end, text);
        }
    }

    @Override
    public void replaceSelection(String text) {
        if (validate(text)) {
            super.replaceSelection(text);
        }
    }

    private boolean validate(String text) {
        return text.matches("[0-9]*");
    }
}
