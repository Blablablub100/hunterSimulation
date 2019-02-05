package UI;

import Simulation.AI.GroupAI;
import Simulation.SimulationController;
import Simulation.SimulationObjects.*;
import UI.RangeSlider.RangeSlider;
import UI.Grid.Cell;
import UI.Grid.GridModel;
import UI.Grid.GridView;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class GUI extends Application{


    /**
     * The states that a Grid cell can have in our example
     */
    public enum States {
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
    private Thread simulationThread;
    private boolean run = false;
    private boolean pause = false;
    private BorderPane root;
    private HashMap<GroupAI, String> colors = new HashMap<>();

    private List<InfoStage> infos = new ArrayList<>();

    private GridView<States> gridView;
    private GridModel<States> gridModel;
    private IntegerProperty numberOfRows = new SimpleIntegerProperty(30);
    private IntegerProperty numberOfColumns = new SimpleIntegerProperty(30);
    private IntegerProperty numberOfHunter = new SimpleIntegerProperty(10);
    private IntegerProperty numberOfPrey = new SimpleIntegerProperty(10);
    private IntegerProperty numberOfObstacles = new SimpleIntegerProperty(10);

    private SimpleDoubleProperty hpRatio = new SimpleDoubleProperty(0.0);
    private SimpleDoubleProperty avgFoodGainH = new SimpleDoubleProperty(0.0);
    private SimpleDoubleProperty avgFoodGainP = new SimpleDoubleProperty(0.0);
    private SimpleDoubleProperty avgPkilledByH = new SimpleDoubleProperty(0.0);
    private SimpleDoubleProperty avgHkilledByP = new SimpleDoubleProperty(0.0);

    private IntegerProperty deadHunter = new SimpleIntegerProperty(0);
    private IntegerProperty deadPrey = new SimpleIntegerProperty(0);
    private IntegerProperty amtHunterStarved = new SimpleIntegerProperty(0);
    private IntegerProperty amtPreyStarved = new SimpleIntegerProperty(0);
    private IntegerProperty amtHunterKilled = new SimpleIntegerProperty(0);
    private IntegerProperty amtPreyKilled = new SimpleIntegerProperty(0);
    private IntegerProperty amtCarrion = new SimpleIntegerProperty(0);

    private IntegerProperty cellBorderWidth = new SimpleIntegerProperty(1);

    private IntegerProperty hunterMinSpeed = new SimpleIntegerProperty(2);
    private IntegerProperty hunterMaxSpeed = new SimpleIntegerProperty(9);
    private IntegerProperty hunterMinStrength = new SimpleIntegerProperty(2);
    private IntegerProperty hunterMaxStrength = new SimpleIntegerProperty(9);
    private IntegerProperty hunterMinSight = new SimpleIntegerProperty(2);
    private IntegerProperty hunterMaxSight = new SimpleIntegerProperty(9);
    private IntegerProperty hunterMinEnergy = new SimpleIntegerProperty(2);
    private IntegerProperty hunterMaxEnergy = new SimpleIntegerProperty(99);

    private IntegerProperty preyMinSpeed = new SimpleIntegerProperty(2);
    private IntegerProperty preyMaxSpeed = new SimpleIntegerProperty(9);
    private IntegerProperty preyMinStrength = new SimpleIntegerProperty(2);
    private IntegerProperty preyMaxStrength = new SimpleIntegerProperty(9);
    private IntegerProperty preyMinSight = new SimpleIntegerProperty(2);
    private IntegerProperty preyMaxSight = new SimpleIntegerProperty(9);
    private IntegerProperty preyMinEnergy = new SimpleIntegerProperty(2);
    private IntegerProperty preyMaxEnergy = new SimpleIntegerProperty(99);

    private IntegerProperty simSpeed = new SimpleIntegerProperty(100);


    private void printSim(SimulationController sim) {
        clear();
        List<BoardObject> things = sim.getBoard().getBoardObjects();
        for (BoardObject thing: things) {
            if (thing instanceof Hunter) {
                Cell c = gridModel.getCell(thing.getLocation().getX(), thing.getLocation().getY());
                c.changeState(States.HUNTER);
                if (((Hunter) thing).isGroupMember()) paintGroup(c, (Hunter) thing);
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

    private void paintGroup(Cell c, Hunter h) {
        checkGroupAliveness();
        Pane gridPane = gridView.getCellPane(c);
        if (colors.containsKey(h.getGroup())) {
            gridPane.setStyle(colors.get(h.getGroup()));
            return;
        }
        String prefix = "-fx-background-color: #";
        for (int i = 60; i < 98; i = i+3) { // max 1000 hunter
            for (int j = 30; j < 60; j = j+3) {
                String curr = Integer.toString(i) + Integer.toString(j) + "01";
                if (!colors.containsValue(prefix + curr)) {
                    colors.put(h.getGroup(), prefix + curr);
                    gridPane.setStyle(prefix+curr);
                    break;
                }
            }
        }
    }

    private void checkGroupAliveness() {
        GroupAI[] groups = colors.keySet().toArray(new GroupAI[colors.keySet().size()]);
        for (int i = 0; i < groups.length; i++) {
            if (!groups[i].isAlive()) colors.remove(groups[i]);
        }
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Hunter Simulation");
        root = new BorderPane();
        root.setMinSize(0,0);

        gridModel = new GridModel<>();

        // define the state that will be used as default.
        gridModel.setDefaultState(States.EMPTY);


        // create the Grid view and shunter colored the Grid model
        gridView = new GridView<>();
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


        // bind the number of rows/columns in the Grid model
        gridModel.numberOfColumns().bind(numberOfColumns);
        gridModel.numberOfRows().bind(numberOfRows);


        gridView.cellBorderWidthProperty().bind(cellBorderWidth);

        VBox left = createGridControls();
        VBox right = createQuickStatistics();
        right.getChildren().addAll(createSimulationControls().getChildren());
        right.setPrefWidth(200);

        root.setRight(right);
        root.setLeft(left);

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                System.exit(0);
            }
        });

        stage.setScene(new Scene(root, 800,600));
        stage.show();
    }

    private void startSim() {
        root.getLeft().setMouseTransparent(true);
        if (simulationThread != null) return;
        try {
            sim = new SimulationController(new FullUserInput(
                    numberOfColumns.getValue().intValue()
                    , numberOfRows.getValue().intValue()
                    , numberOfHunter.getValue().intValue()
                    , numberOfPrey.getValue().intValue()
                    , numberOfObstacles.getValue().intValue()
                    , hunterMinSpeed.getValue().intValue()
                    , hunterMaxSpeed.getValue().intValue()
                    , hunterMinStrength.getValue().intValue()
                    , hunterMaxStrength.getValue().intValue()
                    , hunterMinSight.getValue().intValue()
                    , hunterMaxSight.getValue().intValue()
                    , hunterMinEnergy.getValue().intValue()
                    , hunterMaxEnergy.getValue().intValue()
                    , preyMinSpeed.getValue().intValue()
                    , preyMaxSpeed.getValue().intValue()
                    , preyMinStrength.getValue().intValue()
                    , preyMaxStrength.getValue().intValue()
                    , preyMinSight.getValue().intValue()
                    , preyMaxSight.getValue().intValue()
                    , preyMinEnergy.getValue().intValue()
                    , preyMaxEnergy.getValue().intValue()
            ));
            simulationThread = new executeStepsThread();
            run = true;
            printSim(sim);
            simulationThread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cancelSim() {
        run = false;
        root.getLeft().setMouseTransparent(false);
        sim = null;
        simulationThread = null;
        for (InfoStage info: infos) {
            info.close();
        }
        resetStats();
        clear();
    }

    private void clear() {
        for (Cell cell: gridModel.getCells()) {
            Pane p = gridView.getCellPane(cell);
            cell.changeState(States.EMPTY);
            p.setStyle("");
        }
    }


    private void switchStates(Cell<States> cell) {
        if (sim == null) return;
        BoardObject clickedObject = sim.getBoard().getObjectAtLocation(new BoardObject.Location(cell.getColumn(), cell.getRow()));
        if (clickedObject == null) return;
        infos.add(new InfoStage(clickedObject));

    }

    /**
     * Create a controls panel so that we can control the Grid properties.
     */
    private VBox createGridControls(){
        VBox controlsBox = new VBox();
        controlsBox.setSpacing(5);
        controlsBox.setPadding(new Insets(5));

        HBox headerBox = createHeader("Simulation Arguments");
        HBox numberOfRowsBox = createNumberControl("Rows\t\n", numberOfRows, 3, 99);
        HBox numberOfColumnsBox = createNumberControl("Columns\t\n", numberOfColumns, 3, 99);
        //HBox cellBorderWidthBox = createNumberControl("Cell Border Width:", cellBorderWidth, 0, 5);
        HBox numberOfHunterBox = createNumberControl("Hunters\t\n", numberOfHunter, 0, 500);
        HBox numberOfPreyBox = createNumberControl("Preys\t\n", numberOfPrey, 0, 500);
        HBox numberOfObstacleBox = createNumberControl("Obstacle\t\n", numberOfObstacles, 0, 500);

        HBox hunterHeaderBox = createHeader("Hunter Control");
        HBox hunterSpeedBox = createRangeControl("Speed\t", hunterMinSpeed, hunterMaxSpeed, 1, 10);
        HBox hunterStrengthBox = createRangeControl("Strength\t", hunterMinStrength, hunterMaxStrength, 1, 10);
        HBox hunterSightBox = createRangeControl("Sight\t", hunterMinSight, hunterMaxSight, 1, 10);
        HBox hunterEnergyBox = createRangeControl("Energy\t", hunterMinEnergy, hunterMaxEnergy, 1, 100);

        HBox preyHeaderBox = createHeader("Prey Control");
        HBox preySpeedBox = createRangeControl("Speed\t", preyMinSpeed, preyMaxSpeed, 1, 10);
        HBox preyStrengthBox = createRangeControl("Strength\t", preyMinStrength, preyMaxStrength, 1, 10);
        HBox preySightBox = createRangeControl("Sight\t", preyMinSight, preyMaxSight, 1, 10);
        HBox preyEnergyBox = createRangeControl("Energy\t", preyMinEnergy, preyMaxEnergy, 1, 100);

        controlsBox.getChildren().addAll(headerBox
                , numberOfRowsBox
                , numberOfColumnsBox
                , numberOfHunterBox
                , numberOfPreyBox
                , numberOfObstacleBox
                , hunterHeaderBox
                , hunterSpeedBox
                , hunterStrengthBox
                , hunterSightBox
                , hunterEnergyBox
                , preyHeaderBox
                , preySpeedBox
                , preyStrengthBox
                , preySightBox
                , preyEnergyBox);

        return controlsBox;
    }

    private VBox createSimulationControls() {
        VBox controlsBox = new VBox();
        controlsBox.setSpacing(5);
        controlsBox.setPadding(new Insets(5));

        HBox headerBox = createHeader("\nSimulation Controls");
        HBox buttonsBox = createSimButtons();
        VBox speedBox = new VBox();
        speedBox.setSpacing(5);

        Label label = new Label();
        Slider slider = new Slider();
        speedBox.getChildren().addAll(label, slider);

        label.textProperty().bind(Bindings.concat("Simulation Speed\t\t", simSpeed, " ms"));
        slider.valueProperty().bindBidirectional(simSpeed);

        slider.setMin(50);
        slider.setMax(2500);

        controlsBox.getChildren().addAll(headerBox, speedBox, buttonsBox);
        return controlsBox;
    }

    private VBox createQuickStatistics() {
        VBox controlsBox = new VBox();
        controlsBox.setSpacing(5);
        controlsBox.setPadding(new Insets(5));

        HBox headerBox = createHeader("Quick Statistics");
        HBox hpRatioBox = createStatisticsBox("H/P Ratio\t\t\t", hpRatio);
        HBox avgFoodGainHBox = createStatisticsBox("Avg Food Gain H\t\t", avgFoodGainH);
        HBox avgFoodGainPBox = createStatisticsBox("Avg Food Gain P\t\t", avgFoodGainP);
        HBox avgPkilledByHBox = createStatisticsBox("Avg P killed by H\t\t", avgPkilledByH);
        HBox avgHkilledByPBox = createStatisticsBox("Avg H killed by P\t\t", avgHkilledByP);

        HBox hunterDeadBox = createStatisticsBox("Hunter died\t\t\t", deadHunter);
        HBox preyDeadBox = createStatisticsBox("Prey died\t\t\t", deadPrey);
        HBox hunterStarvedBox = createStatisticsBox("Hunter starved\t\t", amtHunterStarved);
        HBox preyStarvedBox = createStatisticsBox("Prey starved\t\t\t", amtPreyStarved);
        HBox hunterKilledBox = createStatisticsBox("Amount Hunter killed\t", amtHunterKilled);
        HBox preyKilledBox = createStatisticsBox("Amount Prey killed\t", amtPreyKilled);
        HBox amtCarrionBox = createStatisticsBox("Amount Carrion\t\t", amtCarrion);

        controlsBox.getChildren().addAll(headerBox
                , hpRatioBox
                , avgFoodGainHBox
                , avgFoodGainPBox
                , avgHkilledByPBox
                , avgPkilledByHBox
                , hunterDeadBox
                , preyDeadBox
                , hunterStarvedBox
                , preyStarvedBox
                , hunterKilledBox
                , preyKilledBox
                , amtCarrionBox);
        return controlsBox;
    }

    private HBox createStatisticsBox(String name, Property<Number> val) {
        return gethBox(name, val);
    }

    static HBox gethBox(String name, Property<?> val) {
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

    private HBox createSimButtons() {
        Button startButton = new Button("Start");
        startButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                startSim();
            }
        });
        Button stopButton = new Button("Stop");
        stopButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                cancelSim();
            }
        });
        Button pauseButton = new Button("Pause");
        pauseButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                if (!pause) {
                    pauseSim();
                    pauseButton.setText("Resume");
                } else {
                    resumeSim();
                    pauseButton.setText("Pause");
                }
            }
        });
        HBox controls = new HBox();
        controls.setSpacing(5);
        controls.getChildren().addAll(startButton, stopButton, pauseButton);
        return controls;
    }

    private HBox createRangeControl(String labelString, Property<Number> lo, Property<Number> hi, int min, int max) {
        labelString = labelString+"\n";
        HBox control = new HBox();
        control.setSpacing(5);
        RangeSlider multiRange = new RangeSlider(min, max);
        multiRange.setMajorTickUnit(1);
        multiRange.setMinorTickCount(10);
        multiRange.setSnapToTicks(true);
        multiRange.lowValueProperty().bindBidirectional(lo);
        multiRange.highValueProperty().bindBidirectional(hi);
        multiRange.setLowRangeValue(lo.getValue().intValue());
        multiRange.setHighRangeValue(hi.getValue().intValue());

        Label label = new Label();
        String spacer = " - ";
        label.textProperty().bind(Bindings.concat(labelString, lo, spacer, hi));
        control.getChildren().addAll(label, multiRange);


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

    private void resetStats() {
        hpRatio.set(0);
        avgFoodGainH.set(0);
        avgFoodGainP.set(0);
        avgPkilledByH.set(0);
        avgHkilledByP.set(0);

        deadHunter.set(0);
        deadPrey.set(0);
        amtHunterStarved.set(0);
        amtPreyStarved.set(0);
        amtHunterKilled.set(0);
        amtPreyKilled.set(0);
        amtCarrion.set(0);
    }

    private void pauseSim() {
        pause = true;
    }


    private void resumeSim() {
        pause = false;
    }

    class executeStepsThread extends Thread {

        public void run() {
            while (run) {
                while (pause) {
                    sleep(500);
                    if (!run) return;
                }
                if (sim == null) {
                    run = false;
                    return;
                }
                if (simulationThread != this) return;
                sleep(simSpeed.get());
                if (!pause) new NextStepThread().start();
            }
        }

        private void sleep(int ms) {
            try {
                TimeUnit.MILLISECONDS.sleep(ms);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class NextStepThread extends Thread {

        public void run() {
            Platform.runLater(() -> {
                if (sim == null) {
                    run = false;
                    return;
                }
                sim.simulateNextStep();
                printSim(sim);
                updateStats();
            });
        }

        private void updateStats() {
            hpRatio.set(roundTo2(sim.getStats().getHunterPreyRatio()));
            avgFoodGainH.set(roundTo2(sim.getStats().getAvgFoodGainPerIterationHunter()));
            avgFoodGainP.set(roundTo2(sim.getStats().getAvgFoodGainPerIterationPrey()));
            avgPkilledByH.set(roundTo2(sim.getStats().getAvgPreyKilledByHunter()));
            avgHkilledByP.set(roundTo2(sim.getStats().getAvgHunterKilledByPrey()));

            deadHunter.set(sim.getStats().getAmtHunterDead());
            deadPrey.set(sim.getStats().getAmtPreyDead());
            amtHunterStarved.set(sim.getStats().getAmtHunterStarved());
            amtPreyStarved.set(sim.getStats().getAmtPreyStarved());
            amtHunterKilled.set(sim.getStats().getAmountHunterKilledByPrey());
            amtPreyKilled.set(sim.getStats().getAmountPreyKilledByHunter());
            amtCarrion.set(sim.getStats().getAmtDeadCorpse());

            for (InfoStage info: infos) info.update();
        }

        // https://stackoverflow.com/questions/2808535/round-a-double-to-2-decimal-places
        double roundTo2(double value) {
            BigDecimal bd = new BigDecimal(value);
            bd = bd.setScale(2, RoundingMode.HALF_UP);
            return bd.doubleValue();
        }
    }
}