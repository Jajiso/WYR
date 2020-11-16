package com.androidgames.WYRDate.repository;

import com.androidgames.WYRDate.repository.Entity.Scenario;

import java.util.ArrayList;

public class Situations {

    ArrayList<Scenario> situation;
    ArrayList<Scenario> situationRemoved;

    public Situations()
    {
        situation = new ArrayList<>();
        situationRemoved = new ArrayList<>();
    }

    public void add(Scenario scenario)
    {
        situation.add(scenario);
    }

    public void addAll(ArrayList<Scenario> scenarios)
    {
        situation.addAll(scenarios);
    }

    public void remove(Scenario scenario)
    {
        situation.remove(scenario);
    }

    public void removeAll(ArrayList<Scenario> scenarios)
    {
        situation.removeAll(scenarios);
        situationRemoved.removeAll(scenarios);
    }

    public ArrayList<Scenario> getSituation() {
        return situation;
    }

    public Scenario getScenario(int index) {
        return situation.get(index);
    }

    public int getSize(){
        return situation.size();
    }

    public Scenario nextScenario()
    {
        if (situation.size() == 0)
        {
            this.situation = (ArrayList<Scenario>) this.situationRemoved.clone();
            situationRemoved.removeAll(situationRemoved);
        }
        int randomScenario = UtilsTools.generateRandomNumber(situation.size());
        Scenario result = situation.get(randomScenario);
        situationRemoved.add(result);
        situation.remove(randomScenario);
        return result;
    }
}
