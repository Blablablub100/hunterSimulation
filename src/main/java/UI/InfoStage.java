package UI;

import Simulation.AI.AI;
import Simulation.SimulationObjects.*;
import javafx.beans.property.*;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * JavaFX stage used for displaying additional information.
 */
class InfoStage {

    /**
     * Stage that is going to be displayed.
     */
    private Stage infoStage;
    /**
     * BoardObject whose additional information is going to be showed.
     */
    private BoardObject object;
    /**
     * Property for the windows title.
     */
    private StringProperty title;
    /**
     * Property containing the LivingCreatures status.
     */
    private StringProperty status = new SimpleStringProperty();
    /**
     * Property containing the objects x value.
     */
    private IntegerProperty x = new SimpleIntegerProperty();
    /**
     * Property containing the objects y value.
     */
    private IntegerProperty y = new SimpleIntegerProperty();
    /**
     * Property containing the LivingCreatures energy value.
     */
    private IntegerProperty energy = new SimpleIntegerProperty();
    /**
     * Property containing the DeadCorpses pieces.
     */
    private IntegerProperty pieces = new SimpleIntegerProperty();
    /**
     * Property containing the DeadCorpses counter for how many pieces are already eaten.
     */
    private IntegerProperty timesEaten = new SimpleIntegerProperty();
    /**
     * Property containing the information whether the hunter is a group member or not.
     */
    private BooleanProperty groupMember = new SimpleBooleanProperty(false);
    /**
     * Property containing the groupStrength of the hunter. If he is not a group member the group strength
     * will be equal its normal strength.
     */
    private IntegerProperty groupStrength = new SimpleIntegerProperty();
    /**
     * List of Properties containing a String representation of the LivingCreatures long term memory.
     */
    private StringProperty[] longTerm;

    /**
     * crete a new InfoStage for an object. This will immediately show the window.
     * @param object
     */
    InfoStage(BoardObject object) {
        if (object == null) return;
        this.object = object;
        if (object instanceof LivingCreature) {
            longTerm = new StringProperty[((LivingCreature) object).getLongTermMemory().length];
            for (int i = 0; i < longTerm.length; i++) longTerm[i] = new SimpleStringProperty("");
        }
        title = new SimpleStringProperty("creating window");
        x.set(object.getLocation().getX());
        y.set(object.getLocation().getY());
        updateTitle();
        update();
        launch();
    }

    /**
     * launches the stage (showing the window).
     */
    private void launch() {
        infoStage = new Stage();
        infoStage.titleProperty().bind(title);
        BorderPane infoRoot = new BorderPane();

        VBox memoryBox = new VBox();

        if (object instanceof LivingCreature) {
            LivingCreature lc = (LivingCreature) object;
            Label longTermHeader = createHeaderLabel(" Long Term Memory");
            memoryBox.getChildren().addAll(longTermHeader, createMemoryBox(lc.getLongTermMemory()));
        }

        infoRoot.setLeft(createAttributeBox());
        infoRoot.setCenter(memoryBox);
        infoStage.setScene(new Scene(infoRoot));
        infoStage.show();
    }

    /**
     * closes the infoStage window.
     */
    void close() {
        infoStage.close();
    }

    /**
     * Updates the information showed in the infoStage. Gets called every iteration.
     */
    void update() {
        x.set(object.getLocation().getX());
        y.set(object.getLocation().getY());
        if (object instanceof LivingCreature) {
            status.set(((LivingCreature) object).getStatus());
            energy.set(((LivingCreature) object).getEnergy());
            updateMem();
            updateTitle();
        }
        if (object instanceof Hunter) {
            groupMember.set(((Hunter) object).isGroupMember());
            groupStrength.set(((Hunter) object).getStrength());
        }
        if (object instanceof DeadCorpse) {
            pieces.set(((DeadCorpse) object).getPieces());
            timesEaten.set(((DeadCorpse) object).getTimesEaten());
        }
    }

    /**
     * Updates the windows title.
     */
    private void updateTitle() {
        String type = "Obstacle ";
        if (object instanceof Hunter) type = "Hunter ";
        else if (object instanceof Prey) type = "Prey ";
        else if (object instanceof DeadCorpse) type = "Carrion ";
        title.set(type + "at Location " + x.getValue() + ","+y.getValue());
    }

    /**
     * Updates the showed memory information.
     */
    private void updateMem() {
        AI.Memory[] longTermMem = ((LivingCreature) object).getLongTermMemory();
        for (int i = 0; i < longTermMem.length; i++) {
            longTerm[i].set(getMemoryString(longTermMem[i]));
        }
    }

    /**
     * Converts a memory to a String.
     * @param mem memory that gets converted.
     * @return memory in printable String form.
     */
    private String getMemoryString(AI.Memory mem) {
        String memoryString = "";
        if (mem == null) {
            memoryString = memoryString + "empty";
        } else {
            if (mem.getThingMemorized() instanceof Prey) {
                memoryString = memoryString + "Prey ";
            } else if (mem.getThingMemorized() instanceof Hunter) {
                memoryString = memoryString + "Hunter ";
            } else if (mem.getThingMemorized() instanceof DeadCorpse) {
                memoryString = memoryString + "Carrion ";
            } else {
                memoryString = memoryString + "Obstacle ";
            }
            memoryString = memoryString + "at "
                    + mem.getThingMemorized().getLocation().getX() + ","
                    + mem.getThingMemorized().getLocation().getY();
            if (mem.getThingMemorized() instanceof LivingCreature) {
                memoryString = memoryString + " , strength of " +
                        ((LivingCreature) mem.getThingMemorized()).getStrength();
            }
        }
        return memoryString;
    }

    /**
     * Creates a Label for showing a header in the infoStage.
     * @param header String value showed in the header.
     * @return Label ready for display.
     */
    private Label createHeaderLabel(String header) {
        Label label = new Label(header);
        label.setStyle("-fx-font-weight:700");
        return label;
    }

    /**
     * Creates a memoryBox for showing all the memory saved into the long term memory.
     * @param memory all memory values in an array.
     * @return finished VBox ready for display.
     */
    private VBox createMemoryBox(AI.Memory[] memory) {
        VBox memoryBox = new VBox();
        memoryBox.setSpacing(5);
        for (int i = 0; i < memory.length; i++) {
            HBox singleMemBox = GUI.gethBox(" "+Integer.toString(i+1)+".", longTerm[i]);
            memoryBox.getChildren().add(singleMemBox);
        }
        memoryBox.setPrefWidth(200);
        return memoryBox;
    }

    /**
     * Creates a VBox for showing the objects attribute.
     * @return ready to print VBox.
     */
    private VBox createAttributeBox() {
        VBox attributesBox = new VBox();
        Label attributesHeader = createHeaderLabel("Attributes");

        if (object instanceof LivingCreature) {
            LivingCreature lc = (LivingCreature) object;
            attributesBox.setPrefWidth(260);

            HBox statusBox = GUI.gethBox("Status\t\t\t", status);
            HBox speedBox = createAttributeBox("Speed\t\t\t", lc.getMaxMovementSpeed());
            HBox sightBox = createAttributeBox("Sight\t\t\t", lc.getSightDistance());
            HBox energyBox = GUI.gethBox("Energy\t\t\t", energy);
            if (lc instanceof Hunter) {
                HBox strengthBox = createAttributeBox("Strength\t\t\t", ((Hunter) lc).getIndividualStrength());
                HBox groupMemberBox = GUI.gethBox("Group Member\t", groupMember);
                HBox groupStrengthBox = GUI.gethBox("Group Strength\t", groupStrength);
                attributesBox.getChildren().addAll(
                        attributesHeader, statusBox, speedBox, strengthBox, sightBox, energyBox
                        , groupMemberBox, groupStrengthBox
                );
            } else if (lc instanceof Prey) {
                HBox strengthBox = createAttributeBox("Strength\t\t\t", lc.getStrength());
                attributesBox.getChildren().addAll(
                        attributesHeader, statusBox, speedBox, strengthBox, sightBox, energyBox
                );
            }
        } else if (object instanceof DeadCorpse) {
            HBox piecesBox = GUI.gethBox("Pieces\t\t\t", pieces);
            HBox timesEatenBox = GUI.gethBox("Times Eaten \t\t", timesEaten);
            HBox shareSizeBox = createAttributeBox("Share size\t\t", ((DeadCorpse) object).getShare());
            attributesBox.getChildren().addAll(piecesBox, timesEatenBox, shareSizeBox);
            attributesBox.setPrefWidth(300);
        }
        return attributesBox;
    }

    /**
     * Creates an HBox for showing one attribute.
     * @param name name of the attribute.
     * @param value value of the attribute.
     * @return finished HBox ready for printing.
     */
    private HBox createAttributeBox(String name, int value) {
        return createAttributeBox(name, Integer.toString(value));
    }

    /**
     * Creates an HBox for showing one attribute.
     * @param name name of the attribute.
     * @param value value of the attribute.
     * @return finished HBox ready for printing.
     */
    private HBox createAttributeBox(String name, String value) {
        HBox attributeBox = new HBox();
        attributeBox.setSpacing(5);
        Label nameLabel = new Label(name);
        Label valueLabel = new Label(value);
        attributeBox.getChildren().addAll(nameLabel, valueLabel);
        return attributeBox;
    }
}
