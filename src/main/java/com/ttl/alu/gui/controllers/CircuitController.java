package com.ttl.alu.gui.controllers;

import com.ttl.alu.abc.TTL74181;
import com.ttl.alu.abc.chip.decoder.Result;
import com.ttl.alu.abc.chip.encoder.Operation;
import com.ttl.alu.abc.chip.utils.Carry;
import com.ttl.alu.abc.chip.utils.Mode;
import com.ttl.alu.abc.chip.utils.Pin;
import com.ttl.alu.abc.utils.table.component.Column;
import com.ttl.alu.abc.utils.table.component.Row;
import com.ttl.alu.abc.utils.values.Bit;
import com.ttl.alu.gui.utils.ApplicationState;
import com.ttl.alu.gui.utils.Diagram;
import com.ttl.alu.gui.validators.InputValidator;
import com.ttl.alu.gui.validators.ex.ValidationException;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.SpinnerValueFactory.ListSpinnerValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static com.ttl.alu.Launcher.ICONS;
import static com.ttl.alu.abc.TTL74181.WORD_SIZE;
import static com.ttl.alu.abc.utils.table.utils.Type.IN;
import static com.ttl.alu.gui.utils.ApplicationState.*;
import static javafx.fxml.FXMLLoader.load;
import static javafx.scene.control.Alert.AlertType;

public class CircuitController implements Initializable {

    private static final double MAX_COLUMN_ID_WIDTH = 250;
    private static final double MAX_COLUMN_IN_WIDTH = 500;
    private static final double MAX_COLUMN__LOGIC_OUT_WIDTH = 2500;

    private final TTL74181 alu;
    private final TableViewController functions;

    private final List<String> clogs = new ArrayList<>();

    @FXML
    private TableView<Row<Bit, String>> table;
    @FXML
    private Pane canvas_box;
    @FXML
    private Canvas canvas;
    @FXML
    private ChoiceBox<String> input_mode;

    private GraphicsContext gc;
    private ResourceBundle rs;

    private Diagram diagram;
    private ApplicationState state;

    // CONTAINERS

    @FXML
    private Accordion acc_function_table;
    @FXML
    private TitledPane function_table_view;
    @FXML
    private Accordion acc_cmd_table;
    @FXML
    private TitledPane cmd_table_view;
    @FXML
    private Accordion acc_output_table;
    @FXML
    private TitledPane output_view;
    @FXML
    private HBox bar_colors;
    @FXML
    private WebView web_view;

    // INPUT --- CONTROLS

    @FXML
    private TextField input_opcode;
    @FXML
    private Label input_label_a;
    @FXML
    private Label input_label_b;
    @FXML
    private TextField input_a;
    @FXML
    private TextField input_b;
    @FXML
    private Label decimal_input_a;
    @FXML
    private Label decimal_input_b;
    @FXML
    private Label decimal_input_opcode;
    @FXML
    private ChoiceBox<String> input_carry;
    @FXML
    private Spinner<Integer> input_bits;
    @FXML
    private Button exc;
    @FXML
    private Button btn_layers;

    // OUTPUT --- CONTROLS

    @FXML
    private Label output_f;
    @FXML
    private Label output_p;
    @FXML
    private Label output_g;
    @FXML
    private Label output_carry;
    @FXML
    private Label output_equality;
    @FXML
    private Label output_bits;
    @FXML
    private Label output_word_size;
    @FXML
    private TextArea logs;
    @FXML
    private Button output_file;

    public CircuitController() throws IOException {
        this.alu = new TTL74181();
        this.functions = new TableViewController();
        this.diagram = Diagram.LOGIC;
        this.state = ON_DIAGRAM_VIEW;
        // Initialise diagram images
        Diagram.Loader.init();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        __table();
        __cmd();
        __canvas();
        this.rs = resources;
    }

    public void draw(){
        __draw_background();
        __draw_diagram();
    }

    // -- INIT ------------------------------

    private void __table() {

        // Creating index
        TableColumn<Row<Bit, String>, Integer> id = new TableColumn<>("#");
        id.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().id()));
        id.setMaxWidth(MAX_COLUMN_ID_WIDTH);
        id.setStyle("-fx-alignment: CENTER;");

        TableColumn<Row<Bit, String>, Integer> op_code = new TableColumn<>("OP CODE");
        TableColumn<Row<Bit, String>, Integer> modes = new TableColumn<>("MODE");

        table.getColumns().add(id);
        table.getColumns().add(op_code);
        table.getColumns().add(modes);

        // Creating headers
        for(Column c : functions.header()){

            TableColumn<Row<Bit, String>, String> column = new TableColumn<>(c.name());

            column.setStyle("-fx-alignment: CENTER;");

            if(c.type() == IN) {
                // Setting values
                column.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().in(c).toString()));
                column.setMaxWidth(MAX_COLUMN_IN_WIDTH);
                // Nesting bit columns
                op_code.getColumns().add(column);
            }
            else {
                column.setCellValueFactory(param -> new SimpleStringProperty(param.getValue().out(c)));
                // Only for that guy here...
                if(c.name().equals("LOGIC")){
                    column.setMaxWidth(MAX_COLUMN__LOGIC_OUT_WIDTH);
                }

                modes.getColumns().add(column);
            }
        }

        // Creating rows
        ObservableList<Row<Bit, String>> rows = FXCollections.observableArrayList(functions.rows());

        // Updating
        table.setItems(rows);
    }
    private void __canvas(){
        // Setting 2D handler
        gc = canvas.getGraphicsContext2D();

        // Binding on wrapper pane
        canvas.widthProperty().bind(canvas_box.widthProperty());
        canvas.heightProperty().bind(canvas_box.heightProperty());

        // Creating canvas listeners
        canvas.widthProperty().addListener(event -> draw());
        canvas.heightProperty().addListener(event -> draw());
    }
    private void __cmd(){

        input_bits.setValueFactory(new ListSpinnerValueFactory<>(FXCollections.observableArrayList(1, 2, 4, 8)));
        input_bits.valueProperty().addListener((obs, old, current) -> {

            input_label_a.setText("A " + "(" + InputValidator.bits(current) + "-BIT)");
            input_label_b.setText("B " + "(" + InputValidator.bits(current) + "-BIT)");

            output_bits.setText("(" + current +" x 8 bit) = " + InputValidator.bits(current) * 2 + " bits");
            output_word_size.setText("(" + current +" x 4 bit) = " + InputValidator.bits(current) + " bits");
        });

        input_mode.setItems(FXCollections.observableArrayList(Mode.list()));
        input_carry.setItems(FXCollections.observableArrayList(Carry.list()));

        input_mode.getSelectionModel().select(0);
        input_carry.getSelectionModel().select(1);

        input_a.addEventFilter(KeyEvent.KEY_TYPED, event -> {

            if(!event.getCharacter().equals("0") &&
                    !event.getCharacter().equals("1") &&
                    event.getCode() != KeyCode.BACK_SPACE &&
                    event.getCode() != KeyCode.CANCEL)

                event.consume();

        });
        input_a.addEventFilter(KeyEvent.KEY_RELEASED, event -> {
            try {
                decimal_input_a.setText("(" + Long.parseUnsignedLong(input_a.getText(), 2) + ")");
            }catch (Exception e){
                decimal_input_a.setText("(?)");
            }
        });

        input_b.addEventFilter(KeyEvent.KEY_TYPED, event -> {

            if(!event.getCharacter().equals("0") &&
               !event.getCharacter().equals("1") &&
                event.getCode() != KeyCode.BACK_SPACE &&
                event.getCode() != KeyCode.CANCEL)

                event.consume();
        });
        input_b.addEventFilter(KeyEvent.KEY_RELEASED, event -> {
            try {
                decimal_input_b.setText("(" + Long.parseUnsignedLong(input_b.getText(), 2) + ")");
            }catch (Exception e){
                decimal_input_b.setText("(?)");
            }
        });

        input_opcode.addEventFilter(KeyEvent.KEY_TYPED, event -> {

            if(!event.getCharacter().equals("0") &&
                    !event.getCharacter().equals("1") &&
                    event.getCode() != KeyCode.BACK_SPACE &&
                    event.getCode() != KeyCode.CANCEL)

                event.consume();
        });
        input_opcode.addEventFilter(KeyEvent.KEY_RELEASED, event -> {
            try {
                decimal_input_opcode.setText("(" + Long.parseUnsignedLong(input_opcode.getText(), 2) + ")");
            }catch (Exception e){
                decimal_input_opcode.setText("(?)");
            }
        });

        __show();

    }

    // -- DRAWING ----------------------------

    private void __draw_background(){
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        gc.setFill(Color.WHITE);
        gc.fillRect(0, 0, canvas.getWidth(), canvas.getHeight());
    }
    private void __draw_diagram(){

        double width = canvas.getWidth();
        double height = canvas.getWidth() / Diagram.SCALE_RATIO;

        // Acquiring new size
        double cw = (width / 100) * 70;
        double ch = (height / 100) * 70;

        double dx = (canvas.getWidth() / 2) - (cw / 2);
        double dy = (canvas.getHeight() / 2) - (ch / 2);

        gc.drawImage(diagram.getIMG(), dx, dy, cw, ch);
    }

    // -- UTILITY --

    private void __show(){
        acc_function_table.setExpandedPane(function_table_view);
        acc_cmd_table.setExpandedPane(cmd_table_view);
        acc_output_table.setExpandedPane(output_view);
    }
    private void __set_outputs(Result rs){
        __set__out_f(rs);
        output_p.setText(Bit.valueOf(rs.getP().state()));
        output_g.setText(Bit.valueOf(rs.getG().state()));
        output_carry.setText(rs.getCarry().name() + " - " + "(" + rs.getCarry().value() + ")");
        output_equality.setText(Bit.valueOf(rs.getEquality().state()));
        __set_log();
    }
    private void __set__out_f(Result rs) {

        StringBuilder b0 = new StringBuilder(Pin.valueOf(rs.getF()));

        int size = b0.length();

        // Adding dotted notation
        for (int i = 1, j = 0; i < size / WORD_SIZE; ++i, ++j) {
            b0.insert(i * WORD_SIZE + j, ".");
        }

        String binary = b0.toString();

        String decimal = "(" + Long.parseUnsignedLong(Pin.valueOf(rs.getF()), 2) + ")";

        output_f.setText(binary + System.lineSeparator() + decimal);
    }
    private void __set_log(){
        // Clean previous log values
        this.clogs.clear();
        // Obtaining new values
        this.clogs.addAll(alu.logs());
        // Setting on log area
        logs.setText(String.join(System.lineSeparator(), this.clogs));
    }

    private void __enable_webview(){
        // Enabling web view
        web_view.setDisable(false);
        // Showing web view
        web_view.setVisible(true);
        // Loading the pdf viewer
        web_view.getEngine().load(getClass().getResource("/pdf/viewer/manual/index.html").toString());
    }
    private void __disable_webview(){
        // Hiding web view
        web_view.setVisible(false);
        // Disabling web view
        web_view.setDisable(true);
    }

    private void __enable_canvas(){
        // Enabling canvas
        canvas.setDisable(false);
        // Showing canvas
        canvas.setVisible(true);
        // Enabling colors bar
        bar_colors.setDisable(false);
        // Showing colors bar
        boolean state = diagram != Diagram.LOGIC;
        bar_colors.setVisible(state);
        // Enabling layer button
        btn_layers.setDisable(false);
    }
    private void __disable_canvas(){
        // Disabling layer button
        btn_layers.setDisable(true);
        // Disabling colors bar
        bar_colors.setDisable(true);
        // Hiding colors bar
        bar_colors.setVisible(false);
        // Hiding canvas
        canvas.setVisible(false);
        // Disabling canvas
        canvas.setDisable(true);
    }

    // -- BUTTONS ----------------------------

    public void execute(){

        InputValidator validator =
                new InputValidator(
                        input_opcode.getText(),
                        input_a.getText(),
                        input_b.getText(),
                        input_carry.getValue(),
                        input_mode.getValue(),
                        input_bits.getValue());

        try{

            Operation v = validator.validate();
            Result rs = alu.compute(v);

            __set_outputs(rs);

        }catch (ValidationException e){
            // Creating error dialog
            Alert error = new Alert(AlertType.ERROR);
            // Some stuff
            error.initOwner(exc.getScene().getWindow());
            error.setTitle(rs.getString("ALERT_TITLE_TEXT"));
            error.setHeaderText(rs.getString("ALERT_HEADER_TEXT"));
            error.setContentText(rs.getString("ALERT_CONTENT_TEXT") + System.lineSeparator() + e.getMessage());
            // Showing
            error.show();
        }catch (RuntimeException e){
            // Creating error dialog
            Alert error = new Alert(AlertType.ERROR);
            // Some stuff
            error.initOwner(exc.getScene().getWindow());
            error.setTitle(rs.getString("ALERT_TITLE_TEXT"));
            error.setHeaderText(rs.getString("ALERT_HEADER_TEXT"));
            error.setContentText(rs.getString("ALERT_CONTENT_TEXT") + System.lineSeparator() + e);
            // Showing
            error.show();
        }

    }
    public void export(){

        FileChooser fc = new FileChooser();

        fc.setTitle("Save .log file");

        fc.setInitialFileName("output");
        fc.getExtensionFilters().addAll(
                 new FileChooser.ExtensionFilter("log", "*.log")
        );

        File e = fc.showSaveDialog(output_file.getScene().getWindow());

        if(e != null){

            try(BufferedWriter bw = new BufferedWriter(new FileWriter(e))){
                for (String log : this.clogs) {
                    bw.write(log);
                    bw.newLine();
                }
                bw.flush();
            } catch (IOException ex) {
                this.logs.appendText(ex.toString());
            }

        }
    }
    public void layers(){
        // Change state
       diagram = (diagram == Diagram.LOGIC) ? Diagram.LAYERS : Diagram.LOGIC;
        // Change toolbar colors state
        boolean state = diagram != Diagram.LOGIC;
        // Setting visible
        bar_colors.setVisible(state);
        // Now, drawing to apply effect
        draw();
    }
    public void about(){
        try {
            // Opening new stage
            Stage stage = new Stage();
            // Importing view content
            stage.setScene(new Scene(load(getClass().getResource("/views/AboutView.fxml"))));
            // Disable resizable status
            stage.setResizable(false);
            // Setting title
            stage.setTitle(rs.getString("ABOUT_TITLE_TEXT"));
            // Setting icon
            stage.getIcons().addAll(ICONS);
            // Show
            stage.show();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    public void help(){

        switch (state) {
            case ON_DIAGRAM_VIEW:
                __disable_canvas();
                __enable_webview();
                state = ON_READER_VIEW;
                break;
            case ON_READER_VIEW:
                __enable_canvas();
                __disable_webview();
                state = ON_DIAGRAM_VIEW;
                break;
        }

    }

}