package Controller;

import Model.Aircraft;
import Model.Message;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Cursor;
import javafx.event.ActionEvent;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.paint.ImagePattern;
import javafx.util.Duration;
import javafx.scene.shape.Rectangle;
import sockets.ServerControl;


import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.LinkedList;
import java.util.ResourceBundle;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static javafx.scene.input.KeyCode.*;
import static javafx.scene.input.KeyCode.M;

public class FlightController  implements Initializable {
    public AnchorPane TopAP;
    private Aircraft receiverAircraft;
    private static Aircraft controlledAircraft;
    private AudioClip mediaPlayer;
    public ImageView background1;
    public ImageView background2;
    public ImageView headingIndicator;
    public ImageView headingIndicators;
    public ImageView horizonGroundAndSky;
    public ImageView pfdScale;
    public ImageView pfdScales;
    public ImageView pfdBarh;
    public ImageView pfdBarv;
    public VBox MainVBox;
    public SplitPane MainSP;
    public AnchorPane MainAP;
    public Label lab;
    public ImageView iv1;
    public ImageView iv2;
    public Rectangle rectangle;
    public Rectangle rectangles;
    public ImageView altitudeCursor;
    public ImageView SpeedCursor;
    public Label SpeedLabel;
    public Label AltLabel;
    public AnchorPane graphePane;
    private ScheduledExecutorService scheduledExecutorService;

    int intialpfdBarhPosition=0;
    int intialpfdBarvPosition=0;

    int initialAltitudePosition=0;
    int initialAltLab=3800;
    int initialSPDLab=250;
    int initialSpeedCursorPosition=0;
    int AnimationSpeed=10000;
    private ParallelTransition parallelTransition;
    final int BACKGROUND_WIDTH = 1000;
    public javafx.scene.control.TextField lat;
    public TextField speed;
    public StackPane stack;
    private static JavaSoundRecorder javaSoundRecorder;


    @FXML
    private VBox messagesVbox;

    @FXML
    private Button recordButton;


    @FXML
    private TextField Chosenspeed;
    @FXML
    private TextField Chosenaltitude;
    @FXML
    private Label n11Label;
    @FXML
    private ImageView n11NeedleImageView;


    private LinkedList<Message> messages= new LinkedList<>();

    public void automatic(ActionEvent actionEvent) {
        controlledAircraft.sethigher(-Integer.parseInt(Chosenspeed.getText().toString()));
        controlledAircraft.setSpeed(Integer.parseInt(Chosenaltitude.getText().toString()));
        controlledAircraft.onAutoControlProperty().setValue(true);
    }

    private void setUpBackgroundAnimation(int AS) {
        TranslateTransition translateTransition;
        TranslateTransition translateTransition2;

        translateTransition = new TranslateTransition(Duration.millis(AS), background1);
        translateTransition2 = new TranslateTransition(Duration.millis(AS), background2);

        translateTransition.setFromX(0);
        translateTransition.setToX(-1* BACKGROUND_WIDTH);
        translateTransition.setInterpolator(Interpolator.LINEAR);

        translateTransition2.setFromX(0);
        translateTransition2.setToX(-1 * BACKGROUND_WIDTH);
        translateTransition2.setInterpolator(Interpolator.LINEAR);

        parallelTransition =new ParallelTransition(translateTransition, translateTransition2);
        parallelTransition.setCycleCount(Timeline.INDEFINITE);

        parallelTransition.play();


    }
    private void playMusic() {
        String bip = "src/Resources/aircraftSoundEffect.mp3";
        Media hit = new Media(Paths.get(bip).toUri().toString());
        mediaPlayer = new AudioClip(hit.getSource());
        mediaPlayer.setVolume(0.1);
        mediaPlayer.play();
    }

    private void playWarning(int bool) {

        String bip = "src/Resources/alarmwarning.mp3";
        Media hit = new Media(Paths.get(bip).toUri().toString());
        mediaPlayer = new AudioClip(hit.getSource());
            if(bool==0){
                mediaPlayer.setVolume(0.1);
        mediaPlayer.play();}
        else{
            mediaPlayer.stop();
        }
    }
    private void handleKeyPressed(KeyEvent event, Aircraft aircraft, Aircraft receiver) {
        final KeyCode code = event.getCode();

        if (code == RIGHT) {
            aircraft.rollRight();
            aircraft.headingRight();
        } else if (code == LEFT) {
            aircraft.rollLeft();
            aircraft.headingLeft();
        }
        else if (code == U) {aircraft.pitchUp();
            rectangle.setTranslateY(rectangle.getY()+20);
            rectangle.setY(rectangle.getY()+20);}
        else if (code == J) {aircraft.pitchDown();
            rectangle.setTranslateY(rectangle.getY()-20);
            rectangle.setY(rectangle.getY()-20);}
        else if (code == Z){
            aircraft.goHigher();
            initialAltitudePosition=initialAltitudePosition-5;
            altitudeCursor.setY(initialAltitudePosition);
            AltLabel.setText("");
             initialAltLab=initialAltLab+250;
             AltLabel.setText(String.valueOf(initialAltLab)+" "+"Feet");
        }
        else if (code == S) {
            aircraft.goLower();
            initialAltitudePosition=initialAltitudePosition+5;
            altitudeCursor.setY(initialAltitudePosition);
            AltLabel.setText("");
            initialAltLab=initialAltLab-250;
            AltLabel.setText(String.valueOf(initialAltLab)+" "+"Feet");
        }
        else if (code == D){

            initialSpeedCursorPosition=initialSpeedCursorPosition-20;

            SpeedCursor.setY(initialSpeedCursorPosition);
            aircraft.speedUp();
        }
        else if (code == Q){
            initialSpeedCursorPosition=initialSpeedCursorPosition+20;

            SpeedCursor.setY(initialSpeedCursorPosition);
            aircraft.speedDown();
        }

        else if (code == A) {
            aircraft.onAutoControlProperty().setValue(true);
        }
        else if (code == M)
        {aircraft.onAutoControlProperty().setValue(false);}
        else if (code.toString().startsWith("F") & code.toString().length() == 2)
            aircraft.headToLevel(Integer.parseInt(code.toString().substring(1)));

        else if (code == N) {
            receiver.rollRight();
            receiver.headingRight();
        } else if (code == V) {
            receiver.rollLeft();
            receiver.headingLeft();
        } else if (code == H) receiver.speedUp();
        else if (code == F) receiver.speedDown();

        else if (code == B) {
            receiver.pitchDown();
            rectangles.setTranslateY(rectangles.getY()-20);
            rectangles.setY(rectangles.getY()-20);
        }

        else if (code == G){
            receiver.pitchUp();
            rectangles.setTranslateY(rectangles.getY()+20);
            rectangles.setY(rectangles.getY()+20);
        }
        else if (code == O){
            receiver.goHigher();
        }
        else if (code == L) {
            receiver.goLower();
        }

    }
    private void receiverKeyPressed(KeyEvent event, Aircraft aircraft) {
        final KeyCode code = event.getCode();

        if (code == N) {
            aircraft.rollRight();
            aircraft.headingRight();
        } else if (code == V) {
            aircraft.rollLeft();
            aircraft.headingLeft();
        } else if (code == PAGE_UP) aircraft.speedUp();
        else if (code == PAGE_DOWN) aircraft.speedDown();

        else if (code == B) {
            receiverAircraft.pitchDown();
            rectangles.setTranslateY(rectangles.getY()-20);
            rectangles.setY(rectangles.getY()-20);

        }
        else if (code == X){
            aircraft.pitchUp();
            rectangles.setTranslateY(rectangles.getY()+20);
            rectangles.setY(rectangles.getY()+20);
        }
    }




    private void bindAircraftToUI(Aircraft aircraft,String url) {
        ImageView aircraftImageView = new ImageView();
        aircraftImageView.setImage(new Image(url));
        aircraftImageView.setFitHeight(100);
        aircraftImageView.setFitWidth(200);
        aircraftImageView.xProperty()
                .bind(aircraft.positionXProperty()
                        .add(controlledAircraft.speedProperty()
                                .multiply(-1)
                                .add(aircraft.speedProperty())
                                .multiply(10)
                        ));

        aircraftImageView.yProperty().bind(aircraft.positionYProperty());
        aircraftImageView.rotateProperty().bind(aircraft.pitchAngleProperty().multiply(-1));


        /**    if (!aircraft.getAircraftType().equals(AircraftType.RECEIVER)) {
         aircraftImageView.setFitWidth(200);
         aircraftImageView.setFitHeight(100);
         } else {
         aircraftImageView.setFitWidth(60);
         aircraftImageView.setFitHeight(30);
         }*/

        TopAP.getChildren().add(aircraftImageView);

    }

    private void bindAircraftToIndicators(Aircraft aircraft) {
        //System.out.println(" bindAircraftToIndicators " + controlledAircraftId);

        horizonGroundAndSky.rotateProperty().bind(aircraft.rollAngleProperty());
        pfdScale.rotateProperty().bind(aircraft.rollAngleProperty());
        pfdScales.rotateProperty().bind(aircraft.rollAngleProperty());
        headingIndicator.rotateProperty().bind(aircraft.headingProperty());
        parallelTransition.rateProperty().bind(aircraft.speedProperty());

        n11Label.textProperty().bind(aircraft.engine1N1Property().asString());
        n11NeedleImageView.rotateProperty().bind(aircraft.engine1N1Property().multiply(2.3).add(-10));

    }
    private void bindAircraftToIndicator(Aircraft aircraft) {
        pfdScales.rotateProperty().bind(aircraft.rollAngleProperty());
        headingIndicators.rotateProperty().bind(aircraft.headingProperty());

    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Image img = new Image("./resources/pfd4.jpg");
        rectangle.setFill(new ImagePattern(img));
        rectangles.setFill(new ImagePattern(img));
        Platform.runLater(() -> {
            controlledAircraft=new Aircraft();
            receiverAircraft=new Aircraft();
            setUpBackgroundAnimation(10000);
            bindAircraftToUI(controlledAircraft,"./resources/a220.png");
            bindAircraftToUI(receiverAircraft,"./resources/f16.png");
            bindAircraftToIndicators(controlledAircraft);
            bindAircraftToIndicator(receiverAircraft);
            AltLabel.setText("3800 Feet");
            SpeedLabel.setText("250 Knot");
            TopAP.requestFocus();
            TopAP.setCursor(Cursor.NONE);
            TopAP.setOnKeyPressed(event -> handleKeyPressed(event,controlledAircraft, receiverAircraft));
            TopAP.requestFocus();

            controlledAircraft.headToLevel(7);
            receiverAircraft.headToLevel(5);

            ServerControl serverControl = new ServerControl(receiverAircraft);
            Thread thread = new Thread(serverControl);
            thread.start();

        });
        playMusic();
        new Thread(() -> {
            System.out.println("Started Thread");
            while (true) {
                if (messages != null)
                    updateMessages();

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
        recordButton.setOnAction(event -> {
            if (recordButton.getText().equals("Record message")) {
                javaSoundRecorder = new JavaSoundRecorder();
                Thread thread = new Thread(javaSoundRecorder);
                thread.start();

                recordButton.setText("Stop");
            } else if(recordButton.getText().equals("Stop")){
                String recordFilePath = javaSoundRecorder.finish();
                System.out.println(recordFilePath);
                Message a=new Message(recordFilePath, "Message from A220");
                messages.add(a);
                javaSoundRecorder.cancel();
                recordButton.setText("Record message");
            }

        });



        final CategoryAxis xAxis = new CategoryAxis(); // we are gonna plot against time
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Time/s");
        xAxis.setAnimated(false);
        yAxis.setLabel("Altitude");
        yAxis.setAnimated(false);

        final LineChart<String, Number> lineChart = new LineChart<>(xAxis, yAxis);
        lineChart.setAnimated(false);
        lineChart.setTitle("Vertical Display");
        lineChart.setLegendVisible(false);


        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.setName("altitude/distance");

        lineChart.getData().add(series);

        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");

        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

        scheduledExecutorService.scheduleAtFixedRate(() -> {
            Platform.runLater(() -> {

                Date now = new Date();

                series.getData().add(new XYChart.Data<>(simpleDateFormat.format(now), controlledAircraft.altitudeProperty().getValue().intValue()));
            });
        }, 0, 1, TimeUnit.SECONDS);

        lineChart.setMaxWidth(400.0);
        lineChart.setMaxHeight(180.0);
        lineChart.setCreateSymbols(false);
        graphePane.getChildren().addAll(lineChart);

    }
    private void updateMessages() {
        System.out.println("messages : " + messages.size());
        Platform.runLater(() -> {
            messagesVbox.getChildren().clear();

            messages.forEach(message -> {

                final Button playbutton = new Button();
                playbutton.setText(message.getTitle());
                playbutton.setOnAction(event1 -> {

                    String bip = message.getFileName();
                    Media hit = new Media(Paths.get(bip).toUri().toString());
                    mediaPlayer = new AudioClip(hit.getSource());
                    mediaPlayer.play();
                });
                messagesVbox.getChildren().add(playbutton);
            });
        });

    }
    public static class JavaSoundRecorder extends Task<Void> {

        static final long RECORD_TIME = 60000;  // 1 minute

        private final String fileName = System.currentTimeMillis() + ".wav";

        private final File wavFile = new File(fileName);

        private final AudioFileFormat.Type fileType = AudioFileFormat.Type.WAVE;

        TargetDataLine line;

        @Override
        protected Void call() throws Exception {
            try {
                AudioFormat format = getAudioFormat();
                DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

                // checks if system supports the data line
                if (!AudioSystem.isLineSupported(info)) {
                    System.out.println("Line not supported");
                    System.exit(0);
                }
                line = (TargetDataLine) AudioSystem.getLine(info);
                line.open(format);
                line.start();   // start capturing

                System.out.println("Start capturing...");

                AudioInputStream ais = new AudioInputStream(line);

                System.out.println("Start recording...");

                AudioSystem.write(ais, fileType, wavFile);

            } catch (LineUnavailableException | IOException ex) {
                ex.printStackTrace();
            }

            return null;
        }


        AudioFormat getAudioFormat() {
            float sampleRate = 16000;
            int sampleSizeInBits = 8;
            int channels = 2;
            boolean signed = true;
            boolean bigEndian = true;
            AudioFormat format = new AudioFormat(sampleRate, sampleSizeInBits,
                    channels, signed, bigEndian);
            return format;
        }

        String finish() {
            line.stop();
            line.close();
            System.out.println("Finished");
            return fileName;
        }

    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        mediaPlayer.stop();
    }
    public void piloteManuel(ActionEvent actionEvent) {
        actionEvent.consume();
        controlledAircraft.onAutoControlProperty().setValue(false);
    }

}
