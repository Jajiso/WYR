package com.campfiregames.WYRDate.repository.Entity;

public class Scenario
{
    private String id;

    private String category;

    private String optionA;

    private String optionB;

    public Scenario(){}

    public Scenario(String id, String category, String optionA, String optionB) {
            this.id = id;
            this.category = category;
            this.optionA = optionA;
            this.optionB = optionB;
    }

    public String getId() {
        return id;
    }

    public void setId( String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getOptionA() {
        return optionA;
    }

    public void setOptionA(String optionA) {
        this.optionA = optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public void setOptionB( String optionB) {
        this.optionB = optionB;
    }
}
