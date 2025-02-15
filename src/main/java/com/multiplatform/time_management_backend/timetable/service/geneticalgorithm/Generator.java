package com.multiplatform.time_management_backend.timetable.service.geneticalgorithm;

import com.multiplatform.time_management_backend.exeption.BadArgumentException;
import lombok.Getter;

import java.util.Random;

@Getter
public class Generator {
    Population population;
    Chromosome fittestChromosome;
    Chromosome secondFittestChromosome;
    Chromosome offSpringChromosome;
    int populationSize;

    private final Data data;

    public Generator(Data data) {
        this.data = data;
    }

    public void initializePopulation(int populationSize) {
        this.populationSize = populationSize;
        population = new Population(populationSize, this.data);
    }

    public void selection() {
        //Select the most fittest individual
        fittestChromosome = population.getFittestChromosome();
        //Select the second most fittest individual
        secondFittestChromosome = population.getSecondFittestChromosome();
    }

    public void crossover() throws BadArgumentException {
        Chromosome offSpring = new Chromosome(false, this.data);
        Chromosome father = fittestChromosome;
        Chromosome mother = secondFittestChromosome;
        for (int i = 0; i < fittestChromosome.getChromosomeLength(); i++) {
            int fatherGene = father.getGene(i);
            int motherGene = mother.getGene(i);
            if (fatherGene == motherGene) {
                if (fatherGene != -1)
                    offSpring.setGene(i, fatherGene);
            } else if (fatherGene == -1 && !offSpring.classHasBeenSet(motherGene)) {
                offSpring.setGene(i, motherGene);
            } else if (motherGene == -1 && !offSpring.classHasBeenSet(fatherGene)) {
                offSpring.setGene(i, fatherGene);
            } else {
                if (offSpring.classHasBeenSet(motherGene) && !offSpring.classHasBeenSet(fatherGene)) {
                    offSpring.setGene(i, fatherGene);
                } else if (offSpring.classHasBeenSet(fatherGene) && !offSpring.classHasBeenSet(motherGene)) {
                    offSpring.setGene(i, motherGene);
                } else if (offSpring.classHasBeenSet(fatherGene) && offSpring.classHasBeenSet(motherGene)) {
                    offSpring.setGene(i, -1);
                } else {
                    int motherGeneScore = mother.calculateGeneScore(motherGene, i);
                    int fatherGeneScore = father.calculateGeneScore(fatherGene, i);
                    if (motherGeneScore > fatherGeneScore)
                        offSpring.setGene(i, motherGene);
                    else
                        offSpring.setGene(i, fatherGene);
                }
            }
        }
        for (int i = 0; i < data.getClasses().length; i++) {
            if (!offSpring.classHasBeenSet(Math.toIntExact(data.getClasses()[i].getId()))) {
                Random rn = new Random();
                int slotID = rn.nextInt(mother.getChromosomeLength());
                while (offSpring.getGene(slotID) != -1) {
                    slotID = rn.nextInt(mother.getChromosomeLength());
                }
                offSpring.setGene(slotID, Math.toIntExact(data.getClasses()[i].getId()));
            }
        }
        offSpringChromosome = offSpring;
    }

    public void addOffSpring() {

        //Get index of least fit individual
        int leastFittestIndex = population.getLeastFittestIndex();
        //Replace least fittest individual from most fittest offspring
        population.getChromosomes()[leastFittestIndex] = offSpringChromosome;
    }

    public void mutation(double mutationRate) {
        Random rn = new Random();
        for (int i = 0; i < populationSize; i++) {
            if (rn.nextInt(10) + 1 < mutationRate * 10) {
                int slotID = rn.nextInt(population.getChromosomes()[0].getChromosomeLength());
                while (population.getChromosomes()[i].getGene(slotID) != -1) {
                    slotID = rn.nextInt(population.getChromosomes()[0].getChromosomeLength());
                }
                int classIdx = rn.nextInt(data.getClasses().length);
                int classID = Math.toIntExact(data.getClasses()[classIdx].getId());
                int oldSlotId = population.getChromosomes()[i].getSlotIdx(classID);
                population.getChromosomes()[i].setGene(oldSlotId, -1);
                population.getChromosomes()[i].setGene(slotID, classID);
            }
        }
    }


}
