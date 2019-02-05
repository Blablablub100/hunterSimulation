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

class InfoStage {

    private Stage infoStage;
    private BoardObject object;
    private StringProperty title;
    private StringProperty status = new SimpleStringProperty();
    private IntegerProperty x = new SimpleIntegerProperty();
    private IntegerProperty y = new SimpleIntegerProperty();
    private IntegerProperty energy = new SimpleIntegerProperty();
    private IntegerProperty pieces = new SimpleIntegerProperty();
    private IntegerProperty timesEaten = new SimpleIntegerProperty();
    private BooleanProperty groupMember = new SimpleBooleanProperty(false);
    private IntegerProperty groupStrength = new SimpleIntegerProperty();
    private StringProperty[] longTerm;

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

    void close() {
        infoStage.close();
    }

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

    private void updateTitle() {
        String type = "Obstacle ";
        if (object instanceof Hunter) type = "Hunter ";
        else if (object instanceof Prey) type = "Prey ";
        else if (object instanceof DeadCorpse) type = "Carrion ";
        title.set(type + "at Location " + x.getValue() + ","+y.getValue());
    }

    private void updateMem() {
        AI.Memory[] longTermMem = ((LivingCreature) object).getLongTermMemory();
        for (int i = 0; i < longTermMem.length; i++) {
            longTerm[i].set(getMemoryString(longTermMem[i]));
        }
    }

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

    private Label createHeaderLabel(String header) {
        Label label = new Label(header);
        label.setStyle("-fx-font-weight:700");
        return label;
    }

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

    private HBox createAttributeBox(String name, int value) {
        return createAttributeBox(name, Integer.toString(value));
    }

    private HBox createAttributeBox(String name, String value) {
        HBox attributeBox = new HBox();
        attributeBox.setSpacing(5);
        Label nameLabel = new Label(name);
        Label valueLabel = new Label(value);
        attributeBox.getChildren().addAll(nameLabel, valueLabel);
        return attributeBox;
    }
}
