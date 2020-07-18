package de.home_skrobanek.schwingungen;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class MenuController {

    @FXML
    LineChart<String, Number> chart;

    //displays the actual function of the amplitude, speed or acceleration
    private XYChart.Series<String, Number> series;

    //Displays the envolepe function above and below.
    private XYChart.Series<String, Number> dampData;
    private XYChart.Series<String, Number> dampDataDown;

    @FXML
    TextField kresifrequenz;

    @FXML
    TextField MaxLength;

    @FXML
    TextField time;

    @FXML
    TextField damping;

    @FXML
    TextField count;

    @FXML
    TextField mass;

    @FXML
    TextField constant;

    @FXML
    TextField period;

    @FXML
    ChoiceBox calcType;

    @FXML
    Label error;

    @FXML
    CheckBox schwingungstyp;

    @FXML
    CheckBox dampingFunc;

    public void initialize(){
        calcType.getItems().add("Amplitude");
        calcType.getItems().add("Geschwindigkeit");
        calcType.getItems().add("Beschleunigung");

        //TODO add in future version
//        calcType.getItems().add("Gesamtenergie");
//        calcType.getItems().add("Kinetische Energie");
 //       calcType.getItems().add("Potenzielle Energie");
        calcType.getSelectionModel().select(0);

        if(schwingungstyp.isSelected()) {
            damping.setDisable(true);
            dampingFunc.setDisable(true);
        }

        series = new XYChart.Series();
        series.setName("Deflection");
        dampData = new XYChart.Series();
        dampData.setName("Einhüllende");
        dampDataDown = new XYChart.Series();
        dampDataDown.setName("Einhüllende");
        chart.getData().add(series);
        chart.getData().add(dampData);
        chart.getData().add(dampDataDown);
    }

    /*
        Calculate a vibration in diagram with the user input.
     */
    @FXML
    protected void calculate(){
        //Clearing the data storage
        series.getData().clear();
        dampData.getData().clear();
        dampDataDown.getData().clear();

        //error message set invisible
        error.setVisible(false);

        //Time passed after calculation
        double pastTime = 0;
        try {
            for (int i = 0; i < Integer.parseInt(count.getText()); i++) {

                //Get the user input.
                double smax = Double.parseDouble(MaxLength.getText());
                double omega = Double.parseDouble(kresifrequenz.getText());
                double timeValue = Double.parseDouble(time.getText());
                double massValue = Double.parseDouble(mass.getText());
                double constantValue = Double.parseDouble(constant.getText());

                //check time already passed
                pastTime += timeValue;

                double value = 0;

                //check if a harmonic vibration exists
                if (schwingungstyp.isSelected()) {

                    if (calcType.getSelectionModel().getSelectedItem().toString().equals("Amplitude")) {
                        value = calculateAmplitude(smax, omega, pastTime);
                    } else if (calcType.getSelectionModel().getSelectedItem().toString().equals("Geschwindigkeit")) {
                        value = calculateSpeed(smax, omega, pastTime);
                    } else if (calcType.getSelectionModel().getSelectedItem().toString().equals("Beschleunigung")) {
                        value = calculateAcceleration(smax, omega, pastTime);
                    } else if (calcType.getSelectionModel().getSelectedItem().toString().equals("Gesamtenergie")) {
                        value = calculateEnergy(smax, omega, timeValue, constantValue, massValue);
                    } else if (calcType.getSelectionModel().getSelectedItem().toString().equals("Kinetische Energie")) {
                        value = calculateKineticEnergy(smax, omega, timeValue, massValue);
                    } else if (calcType.getSelectionModel().getSelectedItem().toString().equals("Potenzielle Energie")) {
                        value = calculatePotentialEnergy(smax, omega, timeValue, constantValue);
                    } else {
                        error.setVisible(true);
                    }
                    period.setText("" + 2 * Math.PI * Math.sqrt(massValue / constantValue));
                } else {
                    double damp = Double.parseDouble(damping.getText());
                    if (calcType.getSelectionModel().getSelectedItem().toString().equals("Amplitude")) {
                        if (dampingFunc.isSelected()) {
                            dampData.getData().add(new XYChart.Data("" + pastTime, calculateDampingFunction(smax, damp, pastTime)));
                            dampDataDown.getData().add(new XYChart.Data("" + pastTime, -calculateDampingFunction(smax, damp, pastTime)));
                        }
                        value = calculateAmplitudeWithDamp(smax, omega, pastTime, damp);
                    } else if (calcType.getSelectionModel().getSelectedItem().toString().equals("Geschwindigkeit")) {
                        if (dampingFunc.isSelected()) {
                            dampData.getData().add(new XYChart.Data("" + pastTime, calculateDampingFunction(smax, damp, pastTime)));
                        }
                        value = calculateSpeedWithDamp(smax, omega, timeValue, damp);
                    } else if (calcType.getSelectionModel().getSelectedItem().toString().equals("Beschleunigung")) {
                        if (dampingFunc.isSelected()) {
                            dampData.getData().add(new XYChart.Data("" + pastTime, calculateDampingFunction(smax, damp, pastTime)));
                        }
                        value = calculateAccelerationWithDamp(smax, omega, pastTime, damp);
                    } else if (calcType.getSelectionModel().getSelectedItem().toString().equals("Gesamtenergie")) {
                        value = calculateEnergy(smax, omega, timeValue, constantValue, massValue);
                    } else if (calcType.getSelectionModel().getSelectedItem().toString().equals("Kinetische Energie")) {
                        value = calculateKineticEnergy(smax, omega, timeValue, massValue);
                    } else if (calcType.getSelectionModel().getSelectedItem().toString().equals("Potenzielle Energie")) {
                        value = calculatePotentialEnergy(smax, omega, timeValue, constantValue);
                    } else {
                        error.setVisible(true);
                    }
                    period.setText("Only with harmonic vibration possible!");
                }
                series.getData().add(new XYChart.Data("" + pastTime, value));
            }
        } catch (NumberFormatException e) {
                    error.setVisible(true);
  /*                  MaxLength.setText("");
                    kresifrequenz.setText("");
                    time.setText("");
                    mass.setText("0");
                    constant.setText("0");
                    count.setText("100");*/

        }

    }

    /*
        Select between harmonic and non harmonic vibration.
     */
    @FXML
    protected void onCheckBoxSelect(){
        damping.setDisable(schwingungstyp.isSelected());
        dampingFunc.setDisable(schwingungstyp.isSelected());
    }

    /**
     *
     * @param smax
     *      maximum deflection on the vibration.
     * @param omega
     *      represents the frequency of the vibration
     * @param time
     *      includes the delay in a calculation
     * @return
     *      returns the result of the function
     */
    public double calculateAmplitude(double smax, double omega, double time){
        return smax * Math.sin(Math.toRadians(omega*time));
    }

    /**
     *
     * @param smax
     *      maximum deflection on the vibration.
     * @param omega
     *      represents the frequency of the vibration
     * @param time
     *      includes the delay in a calculation
     * @param damp
     *      includes damping to the calcualtion
     * @return
     *      returns a amplitude of a non harmonic vibration
     */
    public double calculateAmplitudeWithDamp(double smax, double omega, double time, double damp){
        return smax * Math.pow(Math.E, -damp*time)*Math.sin(Math.toRadians(omega*time));
    }

    /**
     *
     * @param smax
     *      maximum deflection on the vibration.
     * @param omega
     *      represents the frequency of the vibration
     * @param time
     *      includes the delay in a calculation
     * @param damp
     *      includes damping to the calcualtion
     * @return
     *      returns the speed of a non harmonic vibration.
     *      V(t) --> v
     */
    public double calculateSpeedWithDamp(double smax, double omega, double time, double damp){
        return smax * (-damp)*Math.pow(Math.E, -damp*time)*omega*Math.cos(Math.toRadians(omega*time));
    }

    /**
     *
     * @param smax
     *      maximum deflection on the vibration.
     * @param omega
     *      represents the frequency of the vibration
     * @param time
     *      includes the delay in a calculation
     * @param damp
     *      includes damping to the calcualtion
     * @return
     *      returns the acceleration of a non harmonic vibration.
     *      V(t) --> v
     */
    public double calculateAccelerationWithDamp(double smax, double omega, double time, double damp){
        return smax * Math.pow(Math.E, -damp*time)*Math.sin(Math.toRadians(omega*time))*((damp*damp)-(omega*omega))-
                2*damp*smax*Math.pow(Math.E,-damp*time)*omega*Math.cos(Math.toRadians(omega*time));
    }

    /**
     *
     * @param smax
     *      maximum deflection on the vibration.
     * @param omega
     *      represents the frequency of the vibration
     * @param time
     *      includes the delay in a calculation
     * @return
     *      returns the speed for a harmonic vibration.
     */
    public double calculateSpeed(double smax, double omega, double time){
        return smax * omega * Math.cos(Math.toRadians(omega*time));
    }

    /**
     *
     * @param smax
     *      maximum deflection on the vibration.
     * @param omega
     *      represents the frequency of the vibration
     * @param time
     *      includes the delay in a calculation
     * @return
     *      returns the acceleration of a harmonic vibration.
     */
    public double calculateAcceleration(double smax, double omega, double time){
        return -smax * omega*omega * Math.sin(Math.toRadians(omega*time));
    }

    public double dampedOmega(double omega, double damp){
        return Math.sqrt((omega*omega)-(damp*damp));
    }

    //Functions for future version...

    //TODO
    public double calculateEnergy(double smax, double omega, double time, double constant, double mass){
        return calculatePotentialEnergy(smax,omega,time, constant)+calculateKineticEnergy(smax,omega,time,mass);
    }

    //TODO
    public double calculatePotentialEnergy(double smax, double omega, double time, double constant){
        return 0.5*constant*(calculateAmplitude(smax,omega,time)*calculateAmplitude(smax,omega,time));
    }

    //TODO
    public double calculateKineticEnergy(double smax, double omega, double time, double mass){
        return 0.5*mass*(calculateSpeed(smax,omega,time)*calculateSpeed(smax,omega,time));
    }

    //TODO
    public double calculateDampingFunction(double smax, double damp, double time){
        return smax * Math.pow(Math.E, -damp*time);
    }
}
