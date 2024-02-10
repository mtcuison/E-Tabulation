/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package org.rmj.etabulation.view;

import javafx.animation.Animation;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.control.TextField;
import javafx.util.Duration;

/**
 *
 * @author User
 */
public class TextFieldAnimationUtil {

    /**
     * This function is used to use the shake animation to animate the text
     * field.
     *
     * @param textField the name of the textfield.
     * @return Timeline
     */
    public Timeline shakeTextField(TextField textField) {
        Timeline timeline = new Timeline();
        double originalX = textField.getTranslateX();

        // Set initial translation to 0
        textField.setTranslateX(0);

        // Add keyframes for the animation with EASE_BOTH interpolator
        KeyFrame keyFrame1 = new KeyFrame(Duration.millis(0), new KeyValue(textField.translateXProperty(), 0, Interpolator.EASE_BOTH));
        KeyFrame keyFrame2 = new KeyFrame(Duration.millis(100), new KeyValue(textField.translateXProperty(), -1, Interpolator.EASE_BOTH));
        KeyFrame keyFrame3 = new KeyFrame(Duration.millis(200), new KeyValue(textField.translateXProperty(), 1, Interpolator.EASE_BOTH));
        KeyFrame keyFrame4 = new KeyFrame(Duration.millis(300), new KeyValue(textField.translateXProperty(), -1, Interpolator.EASE_BOTH));
        KeyFrame keyFrame5 = new KeyFrame(Duration.millis(400), new KeyValue(textField.translateXProperty(), 1, Interpolator.EASE_BOTH));
        KeyFrame keyFrame6 = new KeyFrame(Duration.millis(500), new KeyValue(textField.translateXProperty(), -1, Interpolator.EASE_BOTH));
        KeyFrame keyFrame7 = new KeyFrame(Duration.millis(600), new KeyValue(textField.translateXProperty(), 1, Interpolator.EASE_BOTH));
        KeyFrame keyFrame8 = new KeyFrame(Duration.millis(700), new KeyValue(textField.translateXProperty(), originalX, Interpolator.EASE_BOTH));

        // Add keyframes to the timeline
        timeline.getKeyFrames().addAll(
                keyFrame1, keyFrame2, keyFrame3, keyFrame4, keyFrame5, keyFrame6, keyFrame7, keyFrame8
        );

        // Play the animation
        timeline.play();

        // Return the timeline
        return timeline;
    }

    /**
     * This function is to remove the shake animation.
     *
     * @param textField the name of the textfield.
     * @param timeline call the shakeTextField method and enter the name of the
     * text field.
     * @param className the name of the CSS class.
     */
    public void removeShakeAnimation(TextField textField, Timeline timeline, String className) {
        if (timeline.getStatus() == Animation.Status.RUNNING) {
            timeline.stop();
        }
        textField.setTranslateX(0); // Reset to the original position
        textField.getStyleClass().remove(className);
    }

    /**
     * This function will activate the shake animation in the text field.
     *
     * @param textField the name of the textfield.
     */
    public void addRequiredFieldListener(TextField textField) {
        textField.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue && textField.getText().isEmpty() || !newValue && textField.getText().equals(Double.valueOf("0.00")) || !newValue && textField.getText().equals("0")) {
                shakeTextField(textField);
                textField.getStyleClass().add("required-field");
            } else {
                textField.getStyleClass().remove("required-field");
            }
        });
    }

}
