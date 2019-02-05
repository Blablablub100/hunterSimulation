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
import javafx.scene.Parent;
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

/**
 * Provides the main entry point of the program. Also shows the GUI.
 */
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

    /**
     * Starting point of the program when using the GUI
     * @param args not used
     */
    public static void main(String...args){
        Application.launch(args);
    }

    /**
     * Simulation controller for the current simulation.
     */
    private SimulationController sim;
    /**
     * Thread executing the simulation steps with specific delay.
     */
    private Thread simulationThread;
    /**
     * Used for controlling the thread. If run is set to false the thread will cancel.
     */
    private boolean run = false;
    /**
     * Used for controlling the thread. If pause is true the simulation will pause.
     */
    private boolean pause = false;
    /**
     * The root-pane for the main windows.
     */
    private BorderPane root;
    /**
     * The HashMap Containing the hunter groups and their corresponding colors.
     */
    private HashMap<GroupAI, String> colors = new HashMap<>();
    /**
     * ArrayList saving all the infoWindows. InfoWindows are the small windows that open when you click
     * on a cell that is not empty.
     */
    private List<InfoStage> infos = new ArrayList<>();
    /**
     * Used for printing the grid on the main menu.
     */
    private GridView<States> gridView;
    /**
     * Used for printing the grid on the main menu.
     */
    private GridModel<States> gridModel;
    /**
     * Property for saving the number of rows or board height. It is bound to the height slider.
     */
    private IntegerProperty numberOfRows = new SimpleIntegerProperty(30);
    /**
     * Property for saving the number of columns or board width. It is bound to the width slider.
     */
    private IntegerProperty numberOfColumns = new SimpleIntegerProperty(30);
    /**
     * Property for saving the initial amount of hunters. It is bound to the hunter slider.
     */
    private IntegerProperty numberOfHunter = new SimpleIntegerProperty(10);
    /**
     * Property for saving the initial amount of preys. It is bound to prey slider.
     */
    private IntegerProperty numberOfPrey = new SimpleIntegerProperty(10);
    /**
     * Property for saving the initial amount of obstacles. It is bound to the obstacles slider.
     */
    private IntegerProperty numberOfObstacles = new SimpleIntegerProperty(10);

    /**
     * Property for showing the hunter/ prey radio. It is bound to statistics field.
     */
    private SimpleDoubleProperty hpRatio = new SimpleDoubleProperty(0.0);
    /**
     * Property for showing the average food gain per iteration by hunter. Is is bound to the statistics
     * field.
     */
    private SimpleDoubleProperty avgFoodGainH = new SimpleDoubleProperty(0.0);
    /**
     * Property for showing the average food gain per iteration by prey. Is is bound to the statistics
     * field.
     */
    private SimpleDoubleProperty avgFoodGainP = new SimpleDoubleProperty(0.0);
    /**
     * Property for showing the average Prey killed by Hunter. It is bound to the statistics field.
     */
    private SimpleDoubleProperty avgPkilledByH = new SimpleDoubleProperty(0.0);
    /**
     * Property for showing the average Hunter killed by Prey. It is bound to the statistics field.
     */
    private SimpleDoubleProperty avgHkilledByP = new SimpleDoubleProperty(0.0);
    /**
     * Property for showing the amount of dead hunter. It is bound to the statistics field.
     */
    private IntegerProperty deadHunter = new SimpleIntegerProperty(0);
    /**
     * Property for showing the amount of dead prey. It is bound to the statistics field.
     */
    private IntegerProperty deadPrey = new SimpleIntegerProperty(0);
    /**
     * Property for showing the amount of Hunter starved. It is bound to the statistics field.
     */
    private IntegerProperty amtHunterStarved = new SimpleIntegerProperty(0);
    /**
     * Property for showing the amount of Prey starved. It is bound to the statistics field.
     */
    private IntegerProperty amtPreyStarved = new SimpleIntegerProperty(0);
    /**
     * Property for showing the amount of Hunter killed. It is bound to the statistics field.
     */
    private IntegerProperty amtHunterKilled = new SimpleIntegerProperty(0);
    /**
     * Property for showing the amount of Prey killed. It is bound to the statistics field.
     */
    private IntegerProperty amtPreyKilled = new SimpleIntegerProperty(0);
    /**
     * Property for showing the amount of Carrion currently on the board. It is bound to the statistics
     * field.
     */
    private IntegerProperty amtCarrion = new SimpleIntegerProperty(0);

    /**
     * This Property is used to control the border width of the grid.
     */
    private IntegerProperty cellBorderWidth = new SimpleIntegerProperty(1);

    /**
     * Property bound to the lower value of the hunter speed RangeSlider.
     */
    private IntegerProperty hunterMinSpeed = new SimpleIntegerProperty(2);
    /**
     * Property bound to the higher value of the hunter speed RangeSlider.
     */
    private IntegerProperty hunterMaxSpeed = new SimpleIntegerProperty(9);
    /**
     * Property bound to the lower value of the hunter strength RangeSlider.
     */
    private IntegerProperty hunterMinStrength = new SimpleIntegerProperty(2);
    /**
     * Property bound to the higher value of the hunter strength RangeSlider.
     */
    private IntegerProperty hunterMaxStrength = new SimpleIntegerProperty(9);
    /**
     * Property bound to the lower value of the hunter view distance RangerSlider.
     */
    private IntegerProperty hunterMinSight = new SimpleIntegerProperty(2);
    /**
     * Property bound to the higher value of the hunter view distance RangerSlider.
     */
    private IntegerProperty hunterMaxSight = new SimpleIntegerProperty(9);
    /**
     * Property bound to the lower value of the hunters initial energy.
     */
    private IntegerProperty hunterMinEnergy = new SimpleIntegerProperty(2);
    /**
     * Property bound to the higher value of the hunters initial energy.
     */
    private IntegerProperty hunterMaxEnergy = new SimpleIntegerProperty(99);

    /**
     * Property bound to the lower value of the prey speed RangeSlider.
     */
    private IntegerProperty preyMinSpeed = new SimpleIntegerProperty(2);
    /**
     * Property bound to the higher value of the hunter speed RangeSlider.
     */
    private IntegerProperty preyMaxSpeed = new SimpleIntegerProperty(9);
    /**
     * Property bound to the lower value of the hunter strength RangeSlider.
     */
    private IntegerProperty preyMinStrength = new SimpleIntegerProperty(2);
    /**
     * Property bound to the higher value of the hunter strength RangeSlider.
     */
    private IntegerProperty preyMaxStrength = new SimpleIntegerProperty(9);
    /**
     * Property bound to the lower value of the hunter view distance RangerSlider.
     */
    private IntegerProperty preyMinSight = new SimpleIntegerProperty(2);
    /**
     * Property bound to the higher value of the hunter view distance RangerSlider.
     */
    private IntegerProperty preyMaxSight = new SimpleIntegerProperty(9);
    /**
     * Property bound to the lower value of the hunters initial energy.
     */
    private IntegerProperty preyMinEnergy = new SimpleIntegerProperty(2);
    /**
     * Property bound to the higher value of the hunters initial energy.
     */
    private IntegerProperty preyMaxEnergy = new SimpleIntegerProperty(99);

    /**
     * Property bound to the simulation speed Slider.
     */
    private IntegerProperty simSpeed = new SimpleIntegerProperty(100);

    /**
     * This method is used for printing the current simulation on th board.
     * @param sim current simulation controller.
     */
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

    /**
     * paints a group of hunters in their corresponding colors.
     * @param c cell to paint.
     * @param h hunter on that cell.
     */
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

    /**
     * Check whether group is alive.
     */
    private void checkGroupAliveness() {
        GroupAI[] groups = colors.keySet().toArray(new GroupAI[colors.keySet().size()]);
        for (int i = 0; i < groups.length; i++) {
            if (!groups[i].isAlive()) colors.remove(groups[i]);
        }
    }

    /**
     * starts the main window.
     * @param stage stage that the window is going to be in.
     */
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
            cell.setOnClick(event -> showInfo(cell));

            // move over cells with pressed mouse button will switch states
            cell.setOnMouseOver(event -> {
                if(event.isPrimaryButtonDown()){
                    showInfo(cell);
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

    /**
     * starts the simulation.
     */
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
        } catch (WrongUserInputException e) {
            Stage stage = new Stage();
            Parent errorRoot = new Pane();
            stage.setTitle("ERROR wrong user input");
            ((Pane) errorRoot).getChildren().addAll(new Label(e.toString()));
            stage.setScene(new Scene(errorRoot, 400, 100));
            stage.show();
        }
    }

    /**
     * Cancels the ongoing simulation.
     */
    private void cancelSim() {
        run = false;
        root.getLeft().setMouseTransparent(false);
        sim = null;
        simulationThread = null;
        for (InfoStage info: infos) {
            info.close();
        }
        infos.clear();
        resetStats();
        clear();
    }

    /**
     * Clears the Screen, turns every cell empty.
     */
    private void clear() {
        for (Cell cell: gridModel.getCells()) {
            Pane p = gridView.getCellPane(cell);
            cell.changeState(States.EMPTY);
            p.setStyle("");
        }
    }

    /**
     * Launches an infoStage.
     * @param cell Cell the infoStage is for.
     */
    private void showInfo(Cell<States> cell) {
        if (sim == null) return;
        BoardObject clickedObject = sim.getBoard().getObjectAtLocation(new BoardObject.Location(cell.getColumn(), cell.getRow()));
        if (clickedObject == null) return;
        infos.add(new InfoStage(clickedObject));
    }

    /**
     * Create a controls panel so that we can control the Grid properties.
     * @return ready to print VBox containing controls.
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

    /**
     * Creates VBox containing simulation controls.
     * @return ready to show VBox containing simulation controls.
     */
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

    /**
     * Creates VBox for showing Statistics on the right side of the stage.
     * @return ready to print VBox containing all Statistics.
     */
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

    /**
     * Creates an HBox containing one Statistic and binds it to a Property.
     * @param name name of the statistic.
     * @param val property it is bound to.
     * @return ready to print finished HBox.
     */
    private HBox createStatisticsBox(String name, Property<Number> val) {
        return gethBox(name, val);
    }

    /**
     * Creates an HBox containing a name and a Property.
     * @param name name of the HBox.
     * @param val value of the HBox.
     * @return ready to print finished HBox.
     */
    static HBox gethBox(String name, Property<?> val) {
        HBox stat = new HBox();
        stat.setSpacing(5);
        Label labelName = new Label (name);
        Label labelVal = new Label ();
        stat.getChildren().addAll(labelName, labelVal);

        labelVal.textProperty().bind(Bindings.concat(val));

        return stat;
    }

    /**
     * Creates an HBox containing a header label in bold.
     * @param headerString String that is going to be on the header.
     * @return finished ready to print HBox.
     */
    private HBox createHeader(String headerString) {
        HBox header = new HBox();
        header.setSpacing(5);
        Label label = new Label(headerString);
        label.setStyle("-fx-font-weight:700");
        header.getChildren().addAll(label);
        return header;
    }

    /**
     * Creates an HBox containing all Buttons needed for controlling the simulation.
     * @return ready to print finished HBox.
     */
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

    /**
     * Creates an HBox containing a RangeSlider.
     * @param labelString Label at the side of the RangeSlider.
     * @param lo Property for low value of the RangeSlider.
     * @param hi Property for high value of the RangeSlier.
     * @param min min value for the RangeSlider.
     * @param max max value for the RangeSlider.
     * @return Ready to print finished HBox containing a RangeSlider.
     */
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

    /**
     * Creates an HBox containing a Slider.
     * @param labelString String that is going to be displayed near the Slider.
     * @param numberValue Property that is going to be bound to the Sliders value.
     * @param min The minimum value of the RangeSlider.
     * @param max The maximum value of the RangerSlider.
     * @return finished ready to display HBox.
     */
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

    /**
     * Resets the statistics. Only used before starting a new simulation.
     */
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

    /**
     * Pauses the simulation.
     */
    private void pauseSim() {
        pause = true;
    }

    /**
     * Resumes the simulation.
     */
    private void resumeSim() {
        pause = false;
    }

    /**
     * Thread that is used for starting the thread printing the simulation and simulating the next step.
     * This thread is also pausable by the pause boolean.
     */
    class executeStepsThread extends Thread {

        /**
         * Gets called directly when the Thread is started.
         * it starts the Thread for simulating and displaying new Threads after some waiting time.
         */
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

        /**
         * Lets the Thread sleep for a given amount of milliseconds.
         * @param ms amount of milliseconds for delay.
         */
        private void sleep(int ms) {
            try {
                TimeUnit.MILLISECONDS.sleep(ms);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * This thread is used for simulating new steps and updating the main window.
     */
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

        /**
         * Updates Stats displayed on the main window.
         */
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

        /**
         * Used to round double to 2 digits after point.
         * https://stackoverflow.com/questions/2808535/round-a-double-to-2-decimal-places
         * @param value value that is going to be rounded.
         * @return rounded value.
         */
        double roundTo2(double value) {
            BigDecimal bd = new BigDecimal(value);
            bd = bd.setScale(2, RoundingMode.HALF_UP);
            return bd.doubleValue();
        }
    }
}