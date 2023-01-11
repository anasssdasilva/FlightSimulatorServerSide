package Model;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.io.Serializable;

public class Aircraft implements Serializable {
    private static final long serialVersionUID = 6529685098267757690L;

    String aircraftName;
    String aircraftType;

    public String getAircraftName() {
        return aircraftName;

    }

    public String getAircraftType() {
        return aircraftType;
    }

    // Time
    private final SimpleIntegerProperty timeProperty = new SimpleIntegerProperty();
    // Position
    public final SimpleDoubleProperty positionX = new SimpleDoubleProperty();
    private final SimpleDoubleProperty positionY = new SimpleDoubleProperty();
    private final SimpleDoubleProperty positionZ = new SimpleDoubleProperty();
    // On Auto Control
    private final SimpleBooleanProperty onAutoControl = new SimpleBooleanProperty();
    // Air Speed Indicator Instrument Control
    private final SimpleIntegerProperty speed = new SimpleIntegerProperty();
    // Vertical SpeedIndicator Instrument Control
    private final SimpleIntegerProperty verticalSpeed = new SimpleIntegerProperty();
    // AltimeterInstrumentControl
    private final SimpleIntegerProperty altitude = new SimpleIntegerProperty();
    // Attitude Indicator Instrument Control
    private final SimpleDoubleProperty pitchAngle = new SimpleDoubleProperty();
    private final SimpleDoubleProperty rollAngle = new SimpleDoubleProperty();
    // Heading Indicator Instrument Control
    private final SimpleIntegerProperty heading = new SimpleIntegerProperty();
    // Turn Coordinator Instrument Control
    private final SimpleDoubleProperty turnRate = new SimpleDoubleProperty();
    private final SimpleDoubleProperty turnQuality = new SimpleDoubleProperty();

    //
    private final SimpleIntegerProperty engine1Temperature = new SimpleIntegerProperty();
    private final SimpleIntegerProperty engine2Temperature = new SimpleIntegerProperty();
    private final SimpleIntegerProperty engine1N1 = new SimpleIntegerProperty();
    private final SimpleIntegerProperty engine2N1 = new SimpleIntegerProperty();

    public int getEngine1Temperature() {
        return engine1Temperature.get();
    }

    public SimpleIntegerProperty engine1TemperatureProperty() {
        return engine1Temperature;
    }

    public int getEngine2Temperature() {
        return engine2Temperature.get();
    }

    public SimpleIntegerProperty engine2TemperatureProperty() {
        return engine2Temperature;
    }

    public int getEngine1N1() {
        return engine1N1.get();
    }

    public SimpleIntegerProperty engine1N1Property() {
        return engine1N1;
    }

    public int getEngine2N1() {
        return engine2N1.get();
    }

    public SimpleIntegerProperty engine2N1Property() {
        return engine2N1;
    }

    public Aircraft() {

        speed.setValue(1);
        altitude.bind(positionY.multiply(-1).add(400).multiply(50));

        engine1Temperature.bind(speed.multiply(2).add(300));
        engine2Temperature.bind(speed.multiply(2).add(300));

        engine1N1.bind(altitude.divide(166));
        engine2N1.bind(altitude.divide(166));
        onAutoControl.setValue(false);
    }

    public boolean isOnAutoControl() {
        return !onAutoControl.get();
    }

    public SimpleDoubleProperty positionXProperty() {
        return positionX;
    }

    public SimpleDoubleProperty positionYProperty() {
        return positionY;
    }

    public SimpleBooleanProperty onAutoControlProperty() {

        return onAutoControl;
    }

    public SimpleIntegerProperty speedProperty() {
        return speed;
    }

    public SimpleIntegerProperty altitudeProperty() {
        return altitude;
    }

    public SimpleDoubleProperty positionZProperty() {
        return positionZ;
    }

    public SimpleDoubleProperty pitchAngleProperty() {
        return pitchAngle;
    }

    public SimpleDoubleProperty rollAngleProperty() {
        return rollAngle;
    }

    public SimpleDoubleProperty turnRateProperty() {
        return turnRate;
    }

    public SimpleIntegerProperty headingProperty() {
        return heading;
    }

    public SimpleDoubleProperty turnQualityProperty() {
        return turnQuality;
    }

    public SimpleIntegerProperty verticalSpeedProperty() {
        return verticalSpeed;
    }


    /**
     *
     */

    public int getSpeed() {
        return speed.get();
    }

    public double getPitchAngle() {
        return pitchAngle.get();
    }

    public void headingRight() {
        if (!onAutoControl.getValue())
            heading.setValue(heading.getValue() + 3);
    }

    public void headingLeft() {
        if (!onAutoControl.getValue())
            heading.setValue(heading.getValue() - 3);
    }

    public void headToLevel(int level) {
        if (!onAutoControl.getValue())
            this.positionY.setValue((8 - level) * 50);
    }

    public void rollRight() {
        if (!onAutoControl.getValue())
            rollAngle.setValue(rollAngle.getValue() + 3);
    }

    public void rollLeft() {
        if (!onAutoControl.getValue())
            rollAngle.setValue(rollAngle.getValue() - 3);
    }

    public void pitchUp() {
        if (!onAutoControl.getValue())
            pitchAngle.setValue(pitchAngle.getValue() + 3);
    }

    public void pitchDown() {
        if (!onAutoControl.getValue())
            pitchAngle.setValue(pitchAngle.getValue() - 3);
    }

    public void speedUp() {
        if (!onAutoControl.getValue())
            this.speed.setValue(getSpeed() + 1);
    }

    public void speedDown() {
        if (!onAutoControl.getValue())
            if (this.getSpeed() > 1)
                this.speed.setValue(getSpeed() - 1);
    }

    public void goHigher() {
        if (!onAutoControl.getValue())
            this.positionY.setValue(positionY.getValue() - 5);
    }

    public void goLower() {
        if (!onAutoControl.getValue())
            this.positionY.setValue(positionY.getValue() + 5);
    }

    public void goForward() {
        if (!onAutoControl.getValue())
            this.positionX.setValue(positionX.getValue() + 20);
    }

    public void goBackward() {
        if (!onAutoControl.getValue())
            this.positionX.setValue(positionX.getValue() - 10);
    }

    public void goRight() {
        if (!onAutoControl.getValue())
            this.positionX.setValue(positionZ.getValue() + 5);
    }

    public void goLeft() {
        if (!onAutoControl.getValue())
            this.positionX.setValue(positionZ.getValue() - 5);
    }
public void followXMouse(double x){
    if (!onAutoControl.getValue())
        this.positionX.setValue(x);
}
    public void followYMouse(double y){
        if (!onAutoControl.getValue())
            this.positionY.setValue(y);
    }

    public void sethigher(int alt){
        this.positionY.setValue(alt);

    }
    public void setSpeed(int speed){
        this.speed.setValue(speed);
    }

    public static class Builder {

        public Aircraft build() {
            return new Aircraft();
        }
    }
}











