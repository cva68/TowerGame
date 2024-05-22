package seng201.team25.gui;

import javafx.scene.control.Label;
import javafx.scene.image.Image;
import seng201.team25.models.Tower;
import seng201.team25.services.AvailableTowerManager;
import seng201.team25.services.GoldManager;
import seng201.team25.services.WindowManager;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TowerScreenController {
    WindowManager windowManager;
    @FXML ImageView imgTower0;
    @FXML ImageView imgTowerSelected;
    @FXML Label lblTowerName;

    @FXML ImageView imgLoadedTower0;
    @FXML ImageView imgLoadedTower1;
    @FXML ImageView imgLoadedTower2;

    @FXML Label lblLoadedTower0;
    @FXML Label lblLoadedTower1;
    @FXML Label lblLoadedTower2;

    @FXML Label lblStartGame;
    @FXML Label lblResources;
    @FXML Label lblReloadSpeed;


    private final Tower[] towersToBuy = AvailableTowerManager.getTowersToBuy();
    private Tower selectedTower = towersToBuy[0];


    private ImageView selectedTowerElement;
    private int selectedTowerResourceID;

    private List<ImageView> loadedTowerImages = new ArrayList<>();
    private List<Label> loadedTowerLabels = new ArrayList<>();
    Image referenceTowerImage;




    public void initialize() {
        selectedTowerElement = imgTower0;
        selectedTowerResourceID = Integer.parseInt(imgTower0.getId().substring(imgTower0.getId().length() - 1));
        loadedTowerImages.addAll(List.of(imgLoadedTower0, imgLoadedTower1, imgLoadedTower2));
        loadedTowerLabels.addAll(List.of(lblLoadedTower0, lblLoadedTower1, lblLoadedTower2));

        referenceTowerImage = imgLoadedTower0.getImage();
        AvailableTowerManager.clearAvailableTowers();
    }

    public TowerScreenController(WindowManager windowManager) {
        this.windowManager = windowManager;
    }

    public void continuePressed() {
        if ( AvailableTowerManager.getNextAvailableIndex() == -1 ) windowManager.toGameScreen();
    }
    public void backPressed() {
        windowManager.toSetupScreen();
    }

    public void towerSelected(Event event) {
        ImageView pressedTower = (ImageView) event.getSource();
        if ( pressedTower == selectedTowerElement ) { return; }

        // Get Resource ID then Resource String from selected tower
        this.selectedTowerResourceID = Integer.parseInt(pressedTower.getId().substring(pressedTower.getId().length() - 1));
        String resourceString = AvailableTowerManager.getResourceTypeString(selectedTowerResourceID);
        lblTowerName.setText(resourceString);

        pressedTower.setOpacity(1);

        imgTowerSelected.setImage(pressedTower.getImage());

        int selectedTowerResourceID = Integer.parseInt(pressedTower.getId().substring(pressedTower.getId().length() - 1));
        Tower selectedTower = Arrays.stream(towersToBuy)
                .filter(tower -> (tower.getResourceType() == selectedTowerResourceID))
                .findFirst()
                .orElse(null);

        lblTowerName.setText(resourceString);

        pressedTower.setOpacity(1);

        selectedTowerElement.setOpacity(0.4);
        selectedTowerElement = pressedTower;

        imgTowerSelected.setImage(selectedTowerElement.getImage());
        if ( selectedTower.getReloadSpeed() < 0 ) {
            lblResources.setText("N/A");
            lblReloadSpeed.setText("x" + String.valueOf(-1 * selectedTower.getReloadSpeed()));
        } else {
            lblResources.setText(String.valueOf(selectedTower.getResourceAmount()));
            lblReloadSpeed.setText(String.valueOf(selectedTower.getReloadSpeed()));
        }
    }

    public void addSelectedToLoadout() {
        int nextIndex = AvailableTowerManager.getNextAvailableIndex();
        if (nextIndex != -1) {
            AvailableTowerManager.addAvailableTower(new Tower(selectedTowerResourceID));
            loadedTowerImages.get(nextIndex).setImage(imgTowerSelected.getImage());
            loadedTowerImages.get(nextIndex).setOpacity(1);
            String resourceString = AvailableTowerManager.getResourceTypeString(selectedTowerResourceID);
            loadedTowerLabels.get(nextIndex).setText(resourceString);

            if (AvailableTowerManager.getNextAvailableIndex() == -1) lblStartGame.setOpacity(1);
        }
    }
    public void resetLoadout() {
        lblStartGame.setOpacity(0.4);
        AvailableTowerManager.clearAvailableTowers();
        for (ImageView towerImage : loadedTowerImages) {
            towerImage.setImage(referenceTowerImage);
            towerImage.setOpacity(0.4);
        };
        for (Label towerLabel : loadedTowerLabels) {
            towerLabel.setText("Not Selected");
        }

    }
}
