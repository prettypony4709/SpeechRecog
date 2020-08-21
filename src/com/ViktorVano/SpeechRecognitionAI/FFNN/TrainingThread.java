package com.ViktorVano.SpeechRecognitionAI.FFNN;

import com.ViktorVano.SpeechRecognitionAI.Audio.RecordedAudio;
import com.ViktorVano.SpeechRecognitionAI.Miscellaneous.Classifier;
import javafx.collections.ObservableList;

import java.util.ArrayList;
import java.util.LinkedList;

import static com.ViktorVano.SpeechRecognitionAI.FFNN.GeneralFunctions.showVectorValues;
import static com.ViktorVano.SpeechRecognitionAI.FFNN.Variables.*;
import static com.ViktorVano.SpeechRecognitionAI.FFNN.Variables.input;
import static com.ViktorVano.SpeechRecognitionAI.Miscellaneous.General.normalizeInputs;

public class TrainingThread extends Thread {
    private TrainingData trainingData;
    private NeuralNetwork neuralNetwork;
    private ObservableList<RecordedAudio> trainingDatabase;
    private ArrayList<Classifier> trainingClassifier;
    private int minimumTrainingCycles;

    public TrainingThread(ObservableList< RecordedAudio > database, ArrayList<Classifier> classifier)
    {
        this.trainingData = new TrainingData();
        this.neuralNetwork = new NeuralNetwork(topology);
        this.trainingDatabase = database;
        this.trainingClassifier = classifier;
        this.minimumTrainingCycles = trainingDatabase.size() * 10;
        input = new LinkedList<>();
        target = new LinkedList<>();
        result = new LinkedList<>();
        inputNodes = topology.get(0);
        outputNodes = topology.get(topology.size() - 1);
        this.trainingDatabase = database;
        patternCount = this.trainingDatabase.size();
        generateTrainingData();
    }

    private void generateTrainingData()
    {
        LinkedList<Float> inputLine = new LinkedList<>();
        LinkedList<Float> outputLine = new LinkedList<>();
        for(int i=0; i<trainingDatabase.size(); i++)
        {
            inputLine.clear();
            outputLine.clear();

            normalizeInputs(inputLine, trainingDatabase.get(i));

            for(int output=0; output<outputNodes; output++)
            {
                if(trainingDatabase.get(i).name.equals(trainingClassifier.get(output).getName()))
                    outputLine.add(1.0f);
                else
                    outputLine.add(0.0f);
            }
            learningInputs.add(new LinkedList<>(inputLine));
            learningOutputs.add(new LinkedList<>(outputLine));
        }
    }

    @Override
    public void run() {
        super.run();
        System.out.println("Training started\n");
        while (true)
        {
            trainingPass++;
            System.out.println("Pass: " + trainingPass);

            //Get new input data and feed it forward:
            trainingData.getNextInputs(input);
            //showVectorValues("Inputs:", input);
            neuralNetwork.feedForward(input);

            // Train the net what the outputs should have been:
            trainingData.getTargetOutputs(target);
            showVectorValues("Targets: ", target);
            assert(target.size() == topology.peekLast());
            neuralNetwork.backProp(target);//This function alters neurons

            // Collect the net's actual results:
            neuralNetwork.getResults(result);
            showVectorValues("Outputs: ", result);


            // Report how well the training is working, averaged over recent samples:
            System.out.println("Net recent average error: " + neuralNetwork.getRecentAverageError() + "\n\n");

            if (neuralNetwork.getRecentAverageError() < 0.003 && trainingPass>minimumTrainingCycles)
            {
                System.out.println("Exit due to low error :D\n\n");
                neuralNetwork.saveNeuronWeights();
                break;
            }
        }
        System.out.println("Training done.\n");
    }
}