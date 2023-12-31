package com.ViktorVano.SpeechRecognitionAI.Miscellaneous;

import javafx.scene.chart.XYChart;

import java.util.ArrayList;

public class Variables {

    public static int versionNumber = 20230621;

    public static int recorderThreshold = 500;
    public static int wordThreshold = 500;
    public static int preWordSamples = 1200;
    public static int wordInertiaThreshold = 300;
    public static int wordInertiaSamples = 250;

    public static boolean displayNeuralChart = false;
    public static boolean neuralChartBusy = false;
    public static String chartClassifierName = "";
    public static String chartClassifierMatch = "";
    public static ArrayList<XYChart.Series<Number, Number>> neuralChartSeries = new ArrayList<>();

    public static int patternCount;
    public static int inputNodes;
    public static int outputNodes;
    public static float velocity = 0.1f; // overall net learning rate [0.0..1.0]
    public static float momentum = 0.5f; // momentum multiplier of last deltaWeight [0.0..n]


    public static ArrayList<Integer> topology = new ArrayList<>();
    public static final ArrayList<ArrayList<Float>> learningInputs = new ArrayList<>();
    public static final ArrayList<ArrayList<Float>> learningOutputs = new ArrayList<>();
    public static float[] weights = null;
    public static int neuronIndex = 0;
    public static int trainingLine = 0;// Has to be initialized 0
    public static ArrayList<Float> input, target, result;
    public static int trainingPass = 0;
    public static XYChart.Series<Number, Number> currentLossSeries, averageLossSeries;

    public static final int minWordLength = 6000, maxWordLength = 131072;//max length is binary (2^16)*2 (because of 16 bit)
    public static final int minimumLayerSize = 10;
    public static final int maximumTopologySize = 12;
    public static boolean trainingIsRunning = false, updateTrainingLabel = false, savingWeightsPopUp = false;
    public static float averageLoss, exitTrainingLoss = 0.01f;
    public static int minimumTrainingCycles;
    public static int trainingPassLabel, trainingLineLabel;
    public static float currentTrainingLossLabel;
    public static float classifierThreshold = 0.85f;
    public static boolean weightsLoaded = false;
    public static int loadingStep = 0;

    public static boolean printNetworkValues = false;
    public static boolean plotNeuralCharts = false;
    public static boolean keepLongWords = false;
    public static boolean useIpMic = false;
    public static String token = "defaultToken123";
    public static int audioServerPort = 7777;

    public static boolean useIpMicOnly = false;

    public static boolean updateBackground = false;
    public static int background_red = 244;
    public static int background_green = 244;
    public static int background_blue = 244;

}
