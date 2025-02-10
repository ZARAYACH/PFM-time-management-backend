package com.multiplatform.time_management_backend.timetable.service.geneticalgorithm;

import com.multiplatform.time_management_backend.exeption.BadArgumentException;
import com.multiplatform.time_management_backend.group.model.Group;
import com.multiplatform.time_management_backend.user.model.Teacher;
import lombok.Getter;

import java.util.*;

@Getter
public class Chromosome {

    private final Data data;
    // Time-space slots, one entry represent specific duration in one classroom
    private final int chromosomeLength;
    private final int[] slots;
    private HashMap<Integer, Integer> classesMap = new HashMap<>(); //classID -> assignedSlotIdx
    public double fitnessScore = 0;

    Chromosome(boolean atRandom, Data data) {
        this.data = data;
        this.chromosomeLength = data.getDaysPerWeek() * data.getPeriodsPerDay() * data.getRooms().length;
        this.slots = new int[chromosomeLength];
        Arrays.fill(slots, -1);
        fitnessScore = 0;
        if (atRandom) {
            Random rand = new Random();
            for (int i = 0; i < data.getClasses().length; i++) {
                int slotIdx = rand.nextInt(chromosomeLength);
                while (slots[slotIdx] != -1) {
                    slotIdx = rand.nextInt(chromosomeLength);
                }
                int classID = Math.toIntExact(data.getClasses()[i].getId());
                slots[slotIdx] = classID;
                classesMap.put(classID, slotIdx);
            }
        }
    }

    public void calcFitness() throws BadArgumentException {
        int qualityScore = 0;
        Iterator<?> it = classesMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            int classID = (int) pair.getKey();
            int slotIdx = (int) pair.getValue();
            qualityScore += calculateGeneScore(classID, slotIdx);
        }
        fitnessScore = qualityScore / (3.0 * classesMap.size());
    }

    public int calculateGeneScore(int classID, int slotIdx) throws BadArgumentException {
        int geneScore = 0;
        int roomID = Math.toIntExact(data.getRooms()[(slotIdx * data.getRooms().length * data.getDaysPerWeek() / slots.length) % data.getRooms().length].getId());
        if (data.getClassByID(classID).isAllowedRoom(roomID)) {
            geneScore++;
        }
        if (instructorIsAvailable(slotIdx)) {
            geneScore++;
        }
        if (groupStudentIsAvailable(slotIdx)) {
            geneScore++;
        }
        return geneScore;
    }

    private boolean instructorIsAvailable(int slotIdx) throws BadArgumentException {
        Teacher ins = data.getClassByID(slots[slotIdx]).getTeacher();
        int instructorClassesCount = 0;
        int k = slotIdx % data.getPeriodsPerDay();
        int dayID = (slotIdx * data.getDaysPerWeek() / chromosomeLength) % data.getDaysPerWeek();
        k += dayID * (chromosomeLength / data.getDaysPerWeek());
        int roomsCount = data.getRooms().length;
        for (int i = 0; i < roomsCount; i++, k += data.getPeriodsPerDay()) {
            if (slots[k] == -1) {
                continue;
            } else {
                int finalK = k;
                if (data.getClassByID(slots[k]).getTeacher() == ins) {
                    instructorClassesCount++;
                }
            }
        }
        return instructorClassesCount == 1;
    }


    private boolean groupStudentIsAvailable(int slotIdx) throws BadArgumentException {
        Group stdGrp = data.getClassByID(slots[slotIdx]).getGroup();
        int stdGrpClassesCount = 0;
        int k = slotIdx % data.getPeriodsPerDay();
        int dayID = (slotIdx * data.getDaysPerWeek() / chromosomeLength) % data.getDaysPerWeek();
        k += dayID * (chromosomeLength / data.getDaysPerWeek());
        int roomsCount = data.getRooms().length;
        for (int i = 0; i < roomsCount; i++, k += data.getPeriodsPerDay()) {
            if (slots[k] == -1) {
                continue;
            } else {
                int finalK = k;
                if (data.getClassByID(slots[k]).getGroup() == stdGrp) {
                    stdGrpClassesCount++;
                }
            }
        }
        if (stdGrpClassesCount == 1) {
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder("");
        for (int slot : slots) {
            str.append(slot + " ");
        }
        Iterator it = classesMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            int classID = (int) pair.getKey();
            int slotIdx = (int) pair.getValue();
            str.append(" | " + classID + " " + slotIdx + " | ");
        }
        return str.toString();
    }


    public int getGene(int slodIdx) {
        return slots[slodIdx];
    }

    public void setGene(int slotIdx, int classID) {
        slots[slotIdx] = classID;
        if (classID != -1) {
            classesMap.put(classID, slotIdx);
        }
    }

    public boolean classHasBeenSet(int classID) {
        return classesMap.containsKey(classID);
    }

    public int getSlotIdx(int classID) {
        return classesMap.get(classID);
    }
}
